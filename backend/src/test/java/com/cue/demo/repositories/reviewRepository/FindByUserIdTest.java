package com.cue.demo.repositories.reviewRepository;

import com.cue.demo.entities.Performance;
import com.cue.demo.entities.Review;
import com.cue.demo.entities.User;
import com.cue.demo.repositories.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ReviewRepository#findByUser_Id(Long, org.springframework.data.domain.Pageable)},
 * a derived (non-{@code @Query}) paginated finder.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class FindByUserIdTest {

    static final String CONTAINER_NAME = "postgres:15";
    static final String INIT_SCRIPT_NAME = "init-unaccent.sql";

    protected static final String TARGET_USERNAME = "targetUser";
    protected static final String OTHER_USERNAME = "otherUser";

    /**
     * How many reviews are seeded for {@value #TARGET_USERNAME}. Reviews by another user
     * exist on top of these.
     */
    protected static final int MATCHING_COUNT = 7;

    /**
     * Star rating {@value #TARGET_USERNAME} gave each of their reviews, in ascending order.
     * Each review has a distinct rating so sorting by "stars" is unambiguous.
     */
    protected static final List<Integer> MATCHING_STARS_ASC = List.of(1, 2, 3, 4, 5, 6, 7);

    @Container
    @ServiceConnection
    @SuppressWarnings("resource")
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(CONTAINER_NAME)
            .withInitScript(INIT_SCRIPT_NAME);

    @Autowired
    protected ReviewRepository reviewRepository;

    @Autowired
    protected TestEntityManager entityManager;

    Long targetUserId;
    Long otherUserId;

    /**
     * Seeds {@value #TARGET_USERNAME} with {@value #MATCHING_COUNT} reviews (one per distinct
     * performance, since (user_id, performance_id) is unique) rated according to
     * {@link #MATCHING_STARS_ASC}, plus {@value #OTHER_USERNAME} with 3 reviews of their own
     * that must never leak into {@value #TARGET_USERNAME}'s results.
     */
    @BeforeEach
    void seedDatabase() {
        User targetUser = User.builder().username(TARGET_USERNAME).build();
        User otherUser = User.builder().username(OTHER_USERNAME).build();
        entityManager.persist(targetUser);
        entityManager.persist(otherUser);

        for (int i = 0; i < MATCHING_STARS_ASC.size(); i++) {
            Performance performance = Performance.builder().title("Target performance " + i).build();
            entityManager.persist(performance);
            entityManager.persist(Review.builder()
                    .user(targetUser)
                    .performance(performance)
                    .stars(MATCHING_STARS_ASC.get(i))
                    .build());
        }

        for (int i = 0; i < 3; i++) {
            Performance performance = Performance.builder().title("Noise performance " + i).build();
            entityManager.persist(performance);
            entityManager.persist(Review.builder()
                    .user(otherUser)
                    .performance(performance)
                    .stars(1)
                    .build());
        }

        entityManager.flush();
        targetUserId = targetUser.getId();
        otherUserId = otherUser.getId();
        entityManager.clear();
    }

    /**
     * With a page size smaller than the number of reviews (7 reviews, size 2), the first page is
     * full, is marked as first-but-not-last, and the totals report the 4 pages the result spans.
     */
    @Test
    void whenPageSizeSmallerThanResultCount_thenFirstPageIsFullAndMorePagesReported() {
        Page<Review> page = reviewRepository.findByUser_Id(targetUserId, PageRequest.of(0, 2));

        assertThat(page.getContent()).hasSize(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.isLast()).isFalse();
        assertThat(page.hasNext()).isTrue();
        assertThat(page.getTotalElements()).isEqualTo(MATCHING_COUNT);
        assertThat(page.getTotalPages()).isEqualTo(4);
    }

    /**
     * The last page of a result set that does not divide evenly by page size is partially filled
     * (7 reviews, size 3 -> page 2 holds 1 row) and is reported as the last page.
     */
    @Test
    void whenLastPageIsPartial_thenItHoldsTheRemainderAndIsMarkedLast() {
        Page<Review> page = reviewRepository.findByUser_Id(targetUserId, PageRequest.of(2, 3));

        assertThat(page.getNumber()).isEqualTo(2);
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.isLast()).isTrue();
        assertThat(page.hasNext()).isFalse();
        assertThat(page.getTotalElements()).isEqualTo(MATCHING_COUNT);
        assertThat(page.getTotalPages()).isEqualTo(3);
    }

    /**
     * Requesting a page index past the end returns an empty content list while the totals
     * (computed by a separate count query) stay correct.
     */
    @Test
    void whenPageIndexIsPastTheEnd_thenContentIsEmptyButTotalsAreStillCorrect() {
        Page<Review> page = reviewRepository.findByUser_Id(targetUserId, PageRequest.of(5, 3));

        assertThat(page.getContent()).isEmpty();
        assertThat(page.getTotalElements()).isEqualTo(MATCHING_COUNT);
        assertThat(page.getTotalPages()).isEqualTo(3);
    }

    /**
     * The Sort carried by the Pageable is applied to the query: ascending and descending
     * requests on "stars" return opposite orders.
     */
    @Test
    void whenSortByStars_thenContentIsOrderedAccordingly() {
        Page<Review> ascending = reviewRepository.findByUser_Id(
                targetUserId, PageRequest.of(0, MATCHING_COUNT, Sort.by("stars").ascending()));

        assertThat(ascending.getContent())
                .extracting(Review::getStars)
                .containsExactlyElementsOf(MATCHING_STARS_ASC);

        Page<Review> descending = reviewRepository.findByUser_Id(
                targetUserId, PageRequest.of(0, MATCHING_COUNT, Sort.by("stars").descending()));

        assertThat(descending.getContent())
                .extracting(Review::getStars)
                .containsExactlyElementsOf(MATCHING_STARS_ASC.reversed());
    }

    /**
     * findByUser_Id filters by user: {@value #OTHER_USERNAME}'s 3 reviews never appear in
     * {@value #TARGET_USERNAME}'s results, and querying by {@value #OTHER_USERNAME}'s id returns
     * exactly their own rows.
     */
    @Test
    void whenOtherUserHasReviews_thenTheyAreExcludedFromResults() {
        Page<Review> targetPage = reviewRepository.findByUser_Id(targetUserId, PageRequest.of(0, 100));

        assertThat(targetPage.getTotalElements()).isEqualTo(MATCHING_COUNT);
        assertThat(targetPage.getContent())
                .allMatch(review -> review.getUser().getId().equals(targetUserId));

        Page<Review> otherPage = reviewRepository.findByUser_Id(otherUserId, PageRequest.of(0, 100));

        assertThat(otherPage.getTotalElements()).isEqualTo(3);
        assertThat(otherPage.getContent())
                .allMatch(review -> review.getUser().getId().equals(otherUserId));
    }

    /**
     * A user with no reviews gets an empty, zero-total page rather than an error or another
     * user's rows.
     */
    @Test
    void whenUserHasNoReviews_thenReturnEmptyPage() {
        User lonelyUser = User.builder().username("lonelyUser").build();
        entityManager.persist(lonelyUser);
        entityManager.flush();

        Page<Review> page = reviewRepository.findByUser_Id(lonelyUser.getId(), PageRequest.of(0, 10));

        assertThat(page.getContent()).isEmpty();
        assertThat(page.getTotalElements()).isZero();
        assertThat(page.getTotalPages()).isZero();
    }
}
