package com.cue.demo.repositories.reviewRepository;

import com.cue.demo.entities.Performance;
import com.cue.demo.entities.Review;
import com.cue.demo.entities.User;
import com.cue.demo.repositories.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Tests for {@link ReviewRepository#getAverageRatingByPerformance_Id(Long)}, an aggregate
 * {@code @Query} ("SELECT COALESCE(AVG(r.stars), 0.0) ...") with no Pageable involved. Each test
 * builds its own isolated performance/review fixture, since the method takes a single id and
 * there is no shared seed to reuse across cases here.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class GetAverageRatingByPerformanceIdTest {

    static final String CONTAINER_NAME = "postgres:15";
    static final String INIT_SCRIPT_NAME = "init-unaccent.sql";

    @Container
    @ServiceConnection
    @SuppressWarnings("resource")
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(CONTAINER_NAME)
            .withInitScript(INIT_SCRIPT_NAME);

    @Autowired
    protected ReviewRepository reviewRepository;

    @Autowired
    protected TestEntityManager entityManager;

    /**
     * Persists a performance with one review per given star rating, each by a distinct user
     * (the (user_id, performance_id) pair is unique), and returns the performance's id.
     */
    private Long seedPerformanceWithReviews(String title, Integer... starsPerReview) {
        Performance performance = Performance.builder().title(title).build();
        entityManager.persist(performance);

        for (int i = 0; i < starsPerReview.length; i++) {
            User reviewer = User.builder().username(title + "-reviewer-" + i).build();
            entityManager.persist(reviewer);

            Review review = Review.builder()
                    .user(reviewer)
                    .performance(performance)
                    .stars(starsPerReview[i])
                    .build();
            entityManager.persist(review);
        }

        entityManager.flush();
        Long performanceId = performance.getId();
        entityManager.clear();
        return performanceId;
    }

    /**
     * Two reviews (5 and 4 stars) average to a non-integer value (4.5), proving the query
     * computes a real floating-point average and not truncated/integer division.
     */
    @Test
    void whenPerformanceHasMultipleReviews_thenReturnsAverageOfStars() {
        Long performanceId = seedPerformanceWithReviews("HAMLET", 5, 4);

        Double average = reviewRepository.getAverageRatingByPerformance_Id(performanceId);

        assertThat(average).isCloseTo(4.5, within(0.0001));
    }

    /**
     * A single review's average is simply its own star rating.
     */
    @Test
    void whenPerformanceHasSingleReview_thenReturnsThatReviewsStars() {
        Long performanceId = seedPerformanceWithReviews("MACBETH", 3);

        Double average = reviewRepository.getAverageRatingByPerformance_Id(performanceId);

        assertThat(average).isCloseTo(3.0, within(0.0001));
    }

    /**
     * COALESCE in the query guards against SQL's AVG() returning NULL for zero rows: a
     * performance with no reviews returns 0.0, not null, so callers can unbox safely.
     */
    @Test
    void whenPerformanceHasNoReviews_thenReturnsZeroInsteadOfNull() {
        Long performanceId = seedPerformanceWithReviews("ORLANDO");

        Double average = reviewRepository.getAverageRatingByPerformance_Id(performanceId);

        assertThat(average).isNotNull();
        assertThat(average).isCloseTo(0.0, within(0.0001));
    }

    /**
     * The average is scoped to the requested performance: a heavily-reviewed other performance
     * (1-star reviews, which would drag a shared average down) does not influence the result.
     */
    @Test
    void whenOtherPerformanceHasDifferentReviews_thenTheyDoNotAffectTheAverage() {
        Long targetPerformanceId = seedPerformanceWithReviews("MACHINAL", 5, 5);
        seedPerformanceWithReviews("NOISE PERFORMANCE", 1, 1, 1);

        Double average = reviewRepository.getAverageRatingByPerformance_Id(targetPerformanceId);

        assertThat(average).isCloseTo(5.0, within(0.0001));
    }
}
