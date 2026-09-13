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
 * Tests for {@link ReviewRepository#findPerformancesReviewedByUserId(Long, org.springframework.data.domain.Pageable)},
 * a {@code @Query} ("SELECT r.performance FROM Review r WHERE r.user.id = :userId") that projects
 * the Performance association instead of returning Review itself.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class FindPerformancesReviewedByUserIdTest {

    static final String CONTAINER_NAME = "postgres:15";
    static final String INIT_SCRIPT_NAME = "init-unaccent.sql";

    protected static final String TARGET_USERNAME = "targetUser";
    protected static final String OTHER_USERNAME = "otherUser";

    /**
     * How many performances {@value #TARGET_USERNAME} has reviewed. Performances reviewed by
     * another user exist on top of these.
     */
    protected static final int MATCHING_COUNT = 7;

    /**
     * Titles of the performances {@value #TARGET_USERNAME} reviewed, in ascending alphabetical
     * order. Used to assert sorting and page contents.
     */
    protected static final List<String> MATCHING_TITLES_ASC = List.of(
            "ANTIGONA",
            "AZILUL DE NOAPTE",
            "CARTEA JUNGLEI",
            "NUNTA INSANGERATA",
            "PADUREA SPANZURATILOR",
            "STEAUA FARA NUME",
            "VIZITA BATRANEI DOAMNE"
    );

    /**
     * Titles of performances reviewed by {@value #OTHER_USERNAME} only. Prove that the query
     * filters by user instead of returning every reviewed performance in the table.
     */
    protected static final List<String> NOISE_TITLES = List.of(
            "O SCRISOARE PIERDUTA",
            "TAKE, IANKE SI CADAR",
            "D-ALE CARNAVALULUI"
    );

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
     * Seeds {@value #TARGET_USERNAME} with one review per performance in {@link #MATCHING_TITLES_ASC}
     * (a user can only review a performance once, so this needs 7 distinct performances), plus
     * {@value #OTHER_USERNAME} reviewing the 3 {@link #NOISE_TITLES} performances.
     */
    @BeforeEach
    void seedDatabase() {
        User targetUser = User.builder().username(TARGET_USERNAME).build();
        User otherUser = User.builder().username(OTHER_USERNAME).build();
        entityManager.persist(targetUser);
        entityManager.persist(otherUser);

        MATCHING_TITLES_ASC.forEach(title -> {
            Performance performance = Performance.builder().title(title).build();
            entityManager.persist(performance);
            entityManager.persist(Review.builder().user(targetUser).performance(performance).stars(5).build());
        });

        NOISE_TITLES.forEach(title -> {
            Performance performance = Performance.builder().title(title).build();
            entityManager.persist(performance);
            entityManager.persist(Review.builder().user(otherUser).performance(performance).stars(5).build());
        });

        entityManager.flush();
        targetUserId = targetUser.getId();
        otherUserId = otherUser.getId();
        entityManager.clear();
    }

    /**
     * With a page size smaller than the number of reviewed performances (7, size 2), the first
     * page is full, is marked as first-but-not-last, and the totals report the 4 pages the
     * result spans.
     */
    @Test
    void whenPageSizeSmallerThanResultCount_thenFirstPageIsFullAndMorePagesReported() {
        Page<Performance> page = reviewRepository.findPerformancesReviewedByUserId(targetUserId, PageRequest.of(0, 2));

        assertThat(page.getContent()).hasSize(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.isLast()).isFalse();
        assertThat(page.hasNext()).isTrue();
        assertThat(page.getTotalElements()).isEqualTo(MATCHING_COUNT);
        assertThat(page.getTotalPages()).isEqualTo(4);
    }

    /**
     * The last page of a result set that does not divide evenly by page size is partially filled
     * (7 performances, size 3 -> page 2 holds 1 row) and is reported as the last page.
     */
    @Test
    void whenLastPageIsPartial_thenItHoldsTheRemainderAndIsMarkedLast() {
        Page<Performance> page = reviewRepository.findPerformancesReviewedByUserId(targetUserId, PageRequest.of(2, 3));

        assertThat(page.getNumber()).isEqualTo(2);
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.isLast()).isTrue();
        assertThat(page.hasNext()).isFalse();
        assertThat(page.getTotalElements()).isEqualTo(MATCHING_COUNT);
        assertThat(page.getTotalPages()).isEqualTo(3);
    }

    /**
     * Requesting a page index past the end returns an empty content list while the totals
     * (computed by Spring Data's automatically-derived count query for this JPQL @Query) stay
     * correct.
     */
    @Test
    void whenPageIndexIsPastTheEnd_thenContentIsEmptyButTotalsAreStillCorrect() {
        Page<Performance> page = reviewRepository.findPerformancesReviewedByUserId(targetUserId, PageRequest.of(5, 3));

        assertThat(page.getContent()).isEmpty();
        assertThat(page.getTotalElements()).isEqualTo(MATCHING_COUNT);
        assertThat(page.getTotalPages()).isEqualTo(3);
    }

    /**
     * The Sort carried by the Pageable is resolved against the query's FROM-clause root (Review,
     * aliased "r" in "SELECT r.performance FROM Review r ..."), not against the projected type
     * (Performance) it ultimately returns. Sort.by("title") fails at runtime with
     * UnknownPathException ("Could not resolve attribute 'title' of Review"), even though the
     * page content is made of Performance objects; the property path has to be
     * "performance.title" to reach it through the root alias. Ascending and descending requests
     * on that path return opposite orders.
     */
    @Test
    void whenSortByPerformanceTitle_thenContentIsOrderedAccordingly() {
        Page<Performance> ascending = reviewRepository.findPerformancesReviewedByUserId(
                targetUserId, PageRequest.of(0, MATCHING_COUNT, Sort.by("performance.title").ascending()));

        assertThat(ascending.getContent())
                .extracting(Performance::getTitle)
                .containsExactlyElementsOf(MATCHING_TITLES_ASC);

        Page<Performance> descending = reviewRepository.findPerformancesReviewedByUserId(
                targetUserId, PageRequest.of(0, MATCHING_COUNT, Sort.by("performance.title").descending()));

        assertThat(descending.getContent())
                .extracting(Performance::getTitle)
                .containsExactlyElementsOf(MATCHING_TITLES_ASC.reversed());
    }

    /**
     * The query filters by user: the 3 performances {@value #OTHER_USERNAME} reviewed never
     * appear in {@value #TARGET_USERNAME}'s results, and querying by {@value #OTHER_USERNAME}'s
     * id returns exactly their own reviewed performances.
     */
    @Test
    void whenOtherUserHasReviewedPerformances_thenTheyAreExcludedFromResults() {
        Page<Performance> targetPage = reviewRepository.findPerformancesReviewedByUserId(targetUserId, PageRequest.of(0, 100));

        assertThat(targetPage.getTotalElements()).isEqualTo(MATCHING_COUNT);
        assertThat(targetPage.getContent())
                .extracting(Performance::getTitle)
                .containsExactlyInAnyOrderElementsOf(MATCHING_TITLES_ASC);

        Page<Performance> otherPage = reviewRepository.findPerformancesReviewedByUserId(otherUserId, PageRequest.of(0, 100));

        assertThat(otherPage.getTotalElements()).isEqualTo(NOISE_TITLES.size());
        assertThat(otherPage.getContent())
                .extracting(Performance::getTitle)
                .containsExactlyInAnyOrderElementsOf(NOISE_TITLES);
    }

    /**
     * A user who has reviewed nothing gets an empty, zero-total page rather than an error or
     * another user's reviewed performances.
     */
    @Test
    void whenUserHasReviewedNothing_thenReturnEmptyPage() {
        User lonelyUser = User.builder().username("lonelyUser").build();
        entityManager.persist(lonelyUser);
        entityManager.flush();

        Page<Performance> page = reviewRepository.findPerformancesReviewedByUserId(lonelyUser.getId(), PageRequest.of(0, 10));

        assertThat(page.getContent()).isEmpty();
        assertThat(page.getTotalElements()).isZero();
        assertThat(page.getTotalPages()).isZero();
    }
}
