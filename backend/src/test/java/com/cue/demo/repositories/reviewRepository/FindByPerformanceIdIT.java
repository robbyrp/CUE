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
 * Tests for {@link ReviewRepository#findByPerformance_Id(Long, org.springframework.data.domain.Pageable)},
 * a derived (non-{@code @Query}) paginated finder.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class FindByPerformanceIdIT {

    static final String CONTAINER_NAME = "postgres:15";
    static final String INIT_SCRIPT_NAME = "init-unaccent.sql";

    /**
     * The number of reviews seeded for the target performance. Reviews on other performances
     * exist on top of these.
     */
    protected static final int MATCHING_COUNT = 7;

    /**
     * Star rating of each of the target performance's reviews, in ascending order. Each review
     * has a distinct rating so sorting by "stars" is unambiguous.
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

    Long targetPerformanceId;
    Long otherPerformanceId;

    /**
     * Seeds a target performance with {@value #MATCHING_COUNT} reviews (one per distinct user,
     * since (user_id, performance_id) is unique) rated according to {@link #MATCHING_STARS_ASC},
     * plus a second performance with 3 reviews that must never leak into the target's page.
     */
    @BeforeEach
    void seedDatabase() {
        Performance targetPerformance = Performance.builder().title("HAMLET").build();
        Performance otherPerformance = Performance.builder().title("MACBETH").build();
        entityManager.persist(targetPerformance);
        entityManager.persist(otherPerformance);

        for (int i = 0; i < MATCHING_STARS_ASC.size(); i++) {
            User reviewer = User.builder().username("targetReviewer" + i).build();
            entityManager.persist(reviewer);
            entityManager.persist(Review.builder()
                    .user(reviewer)
                    .performance(targetPerformance)
                    .stars(MATCHING_STARS_ASC.get(i))
                    .build());
        }

        for (int i = 0; i < 3; i++) {
            User reviewer = User.builder().username("otherReviewer" + i).build();
            entityManager.persist(reviewer);
            entityManager.persist(Review.builder()
                    .user(reviewer)
                    .performance(otherPerformance)
                    .stars(1)
                    .build());
        }

        entityManager.flush();
        targetPerformanceId = targetPerformance.getId();
        otherPerformanceId = otherPerformance.getId();
        entityManager.clear();
    }

    /**
     * With a page size smaller than the number of reviews (7 reviews, size 2), the first page is
     * full, is marked as first-but-not-last, and the totals report the 4 pages the result spans.
     */
    @Test
    void whenPageSizeSmallerThanResultCount_thenFirstPageIsFullAndMorePagesReported() {
        Page<Review> page = reviewRepository.findByPerformance_Id(targetPerformanceId, PageRequest.of(0, 2));

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
        Page<Review> page = reviewRepository.findByPerformance_Id(targetPerformanceId, PageRequest.of(2, 3));

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
        Page<Review> page = reviewRepository.findByPerformance_Id(targetPerformanceId, PageRequest.of(5, 3));

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
        Page<Review> ascending = reviewRepository.findByPerformance_Id(
                targetPerformanceId, PageRequest.of(0, MATCHING_COUNT, Sort.by("stars").ascending()));

        assertThat(ascending.getContent())
                .extracting(Review::getStars)
                .containsExactlyElementsOf(MATCHING_STARS_ASC);

        Page<Review> descending = reviewRepository.findByPerformance_Id(
                targetPerformanceId, PageRequest.of(0, MATCHING_COUNT, Sort.by("stars").descending()));

        assertThat(descending.getContent())
                .extracting(Review::getStars)
                .containsExactlyElementsOf(MATCHING_STARS_ASC.reversed());
    }

    /**
     * findByPerformance_Id filters by performance: the other performance's 3 reviews never
     * appear in the target's results, and querying by the other performance's id returns
     * exactly its own rows.
     */
    @Test
    void whenOtherPerformanceHasReviews_thenTheyAreExcludedFromResults() {
        Page<Review> targetPage = reviewRepository.findByPerformance_Id(targetPerformanceId, PageRequest.of(0, 100));

        assertThat(targetPage.getTotalElements()).isEqualTo(MATCHING_COUNT);
        assertThat(targetPage.getContent())
                .allMatch(review -> review.getPerformance().getId().equals(targetPerformanceId));

        Page<Review> otherPage = reviewRepository.findByPerformance_Id(otherPerformanceId, PageRequest.of(0, 100));

        assertThat(otherPage.getTotalElements()).isEqualTo(3);
        assertThat(otherPage.getContent())
                .allMatch(review -> review.getPerformance().getId().equals(otherPerformanceId));
    }

    /**
     * A performance with no reviews gets an empty, zero-total page rather than an error or
     * another performance's rows.
     */
    @Test
    void whenPerformanceHasNoReviews_thenReturnEmptyPage() {
        Performance unreviewedPerformance = Performance.builder().title("ORLANDO").build();
        entityManager.persist(unreviewedPerformance);
        entityManager.flush();

        Page<Review> page = reviewRepository.findByPerformance_Id(unreviewedPerformance.getId(), PageRequest.of(0, 10));

        assertThat(page.getContent()).isEmpty();
        assertThat(page.getTotalElements()).isZero();
        assertThat(page.getTotalPages()).isZero();
    }
}
