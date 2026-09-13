package com.cue.demo.repositories.watchItemRepository;

import com.cue.demo.entities.Performance;
import com.cue.demo.entities.User;
import com.cue.demo.entities.WatchLaterPerformanceItem;
import com.cue.demo.entities.WatchedPerformanceItem;
import com.cue.demo.repositories.WatchLaterPerformanceItemRepository;
import com.cue.demo.repositories.WatchedPerformanceItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class WatchItemRepositoryTest {

    static final String CONTAINER_NAME = "postgres:15";
    static final String INIT_SCRIPT_NAME = "init-unaccent.sql";

    protected static final String TARGET_USERNAME = "targetUser";
    protected static final String OTHER_USERNAME = "otherUser";

    /**
     * How many watch-later / watched performances are seeded for {@value #TARGET_USERNAME}.
     */
    protected static final int MATCHING_COUNT = 7;

    /**
     * Titles of {@value #TARGET_USERNAME}'s performances, in ascending alphabetical order.
     * Used to assert sorting and page contents for both repositories.
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
     * Titles of performances belonging to {@value #OTHER_USERNAME} only. Prove that
     * findByUserId filters by user instead of returning every row in the table.
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
    protected WatchLaterPerformanceItemRepository watchLaterPerformanceItemRepository;
    @Autowired
    protected WatchedPerformanceItemRepository watchedPerformanceItemRepository;

    @Autowired
    protected TestEntityManager entityManager;

    Long targetUserId;
    Long otherUserId;

    /**
     * Id of the first (MATCHING_TITLES_ASC.get(0)) performance seeded for {@value #TARGET_USERNAME},
     * reused by the unique-constraint test to build a duplicate pair.
     */
    Long firstMatchingPerformanceId;

    /**
     * Id of {@value #TARGET_USERNAME}'s first watch-later item, reused by the soft-delete test.
     */
    Long watchLaterItemToDeleteId;

    /**
     * Id of {@value #TARGET_USERNAME}'s first watched item, reused by the soft-delete test.
     */
    Long watchedItemToDeleteId;

    /**
     * Seeds {@value #TARGET_USERNAME} with {@value #MATCHING_COUNT} performances, each present in
     * both their watch-later and their watched list, plus {@value #OTHER_USERNAME} with 3 performances
     * of their own (in both lists too) that must never leak into {@value #TARGET_USERNAME}'s results.
     */
    @BeforeEach
    void seedDatabase() {
        User targetUser = User.builder().username(TARGET_USERNAME).build();
        User otherUser = User.builder().username(OTHER_USERNAME).build();
        entityManager.persist(targetUser);
        entityManager.persist(otherUser);

        List<Performance> matchingPerformances = MATCHING_TITLES_ASC.stream()
                .map(title -> Performance.builder().title(title).build())
                .toList();
        matchingPerformances.forEach(entityManager::persist);

        for (int i = 0; i < matchingPerformances.size(); i++) {
            Performance performance = matchingPerformances.get(i);

            WatchLaterPerformanceItem watchLaterItem = WatchLaterPerformanceItem.builder()
                    .user(targetUser)
                    .performance(performance)
                    .build();
            entityManager.persist(watchLaterItem);

            WatchedPerformanceItem watchedItem = WatchedPerformanceItem.builder()
                    .user(targetUser)
                    .performance(performance)
                    .build();
            entityManager.persist(watchedItem);

            if (i == 0) {
                watchLaterItemToDeleteId = watchLaterItem.getId();
                watchedItemToDeleteId = watchedItem.getId();
                firstMatchingPerformanceId = performance.getId();
            }
        }

        List<Performance> noisePerformances = NOISE_TITLES.stream()
                .map(title -> Performance.builder().title(title).build())
                .toList();
        noisePerformances.forEach(entityManager::persist);
        noisePerformances.forEach(performance -> {
            entityManager.persist(WatchLaterPerformanceItem.builder().user(otherUser).performance(performance).build());
            entityManager.persist(WatchedPerformanceItem.builder().user(otherUser).performance(performance).build());
        });

        entityManager.flush();

        targetUserId = targetUser.getId();
        otherUserId = otherUser.getId();

        entityManager.clear();
    }

    /**
     * Saving an item to one repository does not affect the other: a watch-later item and a
     * watched item, both freshly saved for the same user/performance pair, are each found by
     * their own id through their own repository.
     */
    @Test
    void whenSavingToBothRepositories_thenFindByIdReturnsEachSavedItem() {
        Performance performance = Performance.builder().title("O NOAPTE FURTUNOASA").build();
        entityManager.persist(performance);
        User user = entityManager.find(User.class, targetUserId);

        WatchLaterPerformanceItem watchLaterItem = WatchLaterPerformanceItem.builder()
                .user(user)
                .performance(performance)
                .build();
        WatchedPerformanceItem watchedItem = WatchedPerformanceItem.builder()
                .user(user)
                .performance(performance)
                .build();

        entityManager.persist(watchLaterItem);
        entityManager.persist(watchedItem);
        entityManager.flush();

        Long watchLaterId = watchLaterItem.getId();
        Long watchedId = watchedItem.getId();
        Long performanceId = performance.getId();
        entityManager.clear();

        Optional<WatchLaterPerformanceItem> foundWatchLater = watchLaterPerformanceItemRepository.findById(watchLaterId);
        Optional<WatchedPerformanceItem> foundWatched = watchedPerformanceItemRepository.findById(watchedId);

        assertThat(foundWatchLater).isPresent();
        assertThat(foundWatchLater.get().getUser().getId()).isEqualTo(targetUserId);
        assertThat(foundWatchLater.get().getPerformance().getId()).isEqualTo(performanceId);

        assertThat(foundWatched).isPresent();
        assertThat(foundWatched.get().getUser().getId()).isEqualTo(targetUserId);
        assertThat(foundWatched.get().getPerformance().getId()).isEqualTo(performanceId);
    }

    /**
     * Soft-deleting one item from each repository (@SQLDelete flips the "deleted" flag instead of
     * removing the row) makes both disappear from findById and from findByUserId in the same way,
     * while the remaining {@value #MATCHING_COUNT} - 1 items on each side are untouched.
     */
    @Test
    void whenItemIsSoftDeleted_thenItDisappearsFromBothFindByIdAndFindByUserId() {
        watchLaterPerformanceItemRepository.deleteById(watchLaterItemToDeleteId);
        watchedPerformanceItemRepository.deleteById(watchedItemToDeleteId);

        assertThat(watchLaterPerformanceItemRepository.findById(watchLaterItemToDeleteId)).isEmpty();
        assertThat(watchedPerformanceItemRepository.findById(watchedItemToDeleteId)).isEmpty();

        Page<WatchLaterPerformanceItem> watchLaterPage =
                watchLaterPerformanceItemRepository.findByUserId(targetUserId, PageRequest.of(0, 100));
        Page<WatchedPerformanceItem> watchedPage =
                watchedPerformanceItemRepository.findByUserId(targetUserId, PageRequest.of(0, 100));

        assertThat(watchLaterPage.getTotalElements()).isEqualTo(MATCHING_COUNT - 1);
        assertThat(watchedPage.getTotalElements()).isEqualTo(MATCHING_COUNT - 1);
    }

    //------------------------------- WATCH LATER -------------------------------

    /**
     * With a page size smaller than the number of items (7 items, size 2), the first page is
     * full, is marked as first-but-not-last, and the totals report the 4 pages the result spans.
     */
    @Test
    void whenWatchLaterPageSizeSmallerThanResultCount_thenFirstPageIsFullAndMorePagesReported() {
        Page<WatchLaterPerformanceItem> page =
                watchLaterPerformanceItemRepository.findByUserId(targetUserId, PageRequest.of(0, 2));

        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getNumber()).isZero();
        assertThat(page.isFirst()).isTrue();
        assertThat(page.isLast()).isFalse();
        assertThat(page.hasNext()).isTrue();
        assertThat(page.getTotalElements()).isEqualTo(MATCHING_COUNT);
        assertThat(page.getTotalPages()).isEqualTo(4);
    }

    /**
     * The last page of a result set that does not divide evenly by page size is partially filled
     * (7 items, size 3 -> page 2 holds 1 row) and is reported as the last page.
     */
    @Test
    void whenWatchLaterLastPageIsPartial_thenItHoldsTheRemainderAndIsMarkedLast() {
        Page<WatchLaterPerformanceItem> page =
                watchLaterPerformanceItemRepository.findByUserId(targetUserId, PageRequest.of(2, 3));

        assertThat(page.getNumber()).isEqualTo(2);
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.isLast()).isTrue();
        assertThat(page.hasNext()).isFalse();
        assertThat(page.hasPrevious()).isTrue();
        assertThat(page.getTotalElements()).isEqualTo(MATCHING_COUNT);
        assertThat(page.getTotalPages()).isEqualTo(3);
    }

    /**
     * Requesting a page index past the end returns an empty content list while the totals
     * (computed by a separate count query) stay correct.
     */
    @Test
    void whenWatchLaterPageIndexIsPastTheEnd_thenContentIsEmptyButTotalsAreStillCorrect() {
        Page<WatchLaterPerformanceItem> page =
                watchLaterPerformanceItemRepository.findByUserId(targetUserId, PageRequest.of(5, 3));

        assertThat(page.getContent()).isEmpty();
        assertThat(page.getTotalElements()).isEqualTo(MATCHING_COUNT);
        assertThat(page.getTotalPages()).isEqualTo(3);
    }

    /**
     * The Sort carried by the Pageable is applied through the performance association:
     * ascending and descending requests on performance.title return opposite orders.
     */
    @Test
    void whenWatchLaterSortByPerformanceTitle_thenContentIsOrderedAccordingly() {
        Page<WatchLaterPerformanceItem> ascending = watchLaterPerformanceItemRepository.findByUserId(
                targetUserId, PageRequest.of(0, MATCHING_COUNT, Sort.by("performance.title").ascending()));

        assertThat(ascending.getContent())
                .extracting(item -> item.getPerformance().getTitle())
                .containsExactlyElementsOf(MATCHING_TITLES_ASC);

        Page<WatchLaterPerformanceItem> descending = watchLaterPerformanceItemRepository.findByUserId(
                targetUserId, PageRequest.of(0, MATCHING_COUNT, Sort.by("performance.title").descending()));

        assertThat(descending.getContent())
                .extracting(item -> item.getPerformance().getTitle())
                .containsExactlyElementsOf(MATCHING_TITLES_ASC.reversed());
    }

    /**
     * A user with no watch-later items gets an empty, zero-total page rather than an error
     * or another user's rows.
     */
    @Test
    void whenUserHasNoWatchLaterItems_thenReturnEmptyPage() {
        User lonelyUser = User.builder().username("lonelyUser").build();
        entityManager.persist(lonelyUser);
        entityManager.flush();

        Page<WatchLaterPerformanceItem> page =
                watchLaterPerformanceItemRepository.findByUserId(lonelyUser.getId(), PageRequest.of(0, 10));

        assertThat(page.getContent()).isEmpty();
        assertThat(page.getTotalElements()).isZero();
        assertThat(page.getTotalPages()).isZero();
    }

    /**
     * findByUserId filters by user: {@value #OTHER_USERNAME}'s 3 watch-later items never appear
     * in {@value #TARGET_USERNAME}'s results, and querying by {@value #OTHER_USERNAME}'s id returns
     * exactly their own rows.
     */
    @Test
    void whenOtherUsersWatchLaterItemsExist_thenTheyAreExcludedFromResults() {
        Page<WatchLaterPerformanceItem> targetUserPage =
                watchLaterPerformanceItemRepository.findByUserId(targetUserId, PageRequest.of(0, 100));

        assertThat(targetUserPage.getTotalElements()).isEqualTo(MATCHING_COUNT);
        assertThat(targetUserPage.getContent())
                .allMatch(item -> item.getUser().getId().equals(targetUserId));

        Page<WatchLaterPerformanceItem> otherUserPage =
                watchLaterPerformanceItemRepository.findByUserId(otherUserId, PageRequest.of(0, 100));

        assertThat(otherUserPage.getTotalElements()).isEqualTo(NOISE_TITLES.size());
        assertThat(otherUserPage.getContent())
                .allMatch(item -> item.getUser().getId().equals(otherUserId));
    }

    /**
     * The table's unique constraint on (user_id, performance_id) is enforced at the DB level:
     * saving a second watch-later item for a pair that is already present is rejected.
     */
    @Test
    void whenSavingDuplicateWatchLaterUserAndPerformancePair_thenUniqueConstraintRejectsIt() {
        User user = entityManager.find(User.class, targetUserId);
        Performance performance = entityManager.find(Performance.class, firstMatchingPerformanceId);

        WatchLaterPerformanceItem duplicate = WatchLaterPerformanceItem.builder()
                .user(user)
                .performance(performance)
                .build();

        assertThatThrownBy(() -> watchLaterPerformanceItemRepository.saveAndFlush(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    //-------------------------------- WATCHED ----------------------------------

    /**
     * With a page size smaller than the number of items (7 items, size 2), the first page is
     * full, is marked as first-but-not-last, and the totals report the 4 pages the result spans.
     */
    @Test
    void whenWatchedPageSizeSmallerThanResultCount_thenFirstPageIsFullAndMorePagesReported() {
        Page<WatchedPerformanceItem> page =
                watchedPerformanceItemRepository.findByUserId(targetUserId, PageRequest.of(0, 2));

        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getNumber()).isZero();
        assertThat(page.isFirst()).isTrue();
        assertThat(page.isLast()).isFalse();
        assertThat(page.hasNext()).isTrue();
        assertThat(page.getTotalElements()).isEqualTo(MATCHING_COUNT);
        assertThat(page.getTotalPages()).isEqualTo(4);
    }

    /**
     * The last page of a result set that does not divide evenly by page size is partially filled
     * (7 items, size 3 -> page 2 holds 1 row) and is reported as the last page.
     */
    @Test
    void whenWatchedLastPageIsPartial_thenItHoldsTheRemainderAndIsMarkedLast() {
        Page<WatchedPerformanceItem> page =
                watchedPerformanceItemRepository.findByUserId(targetUserId, PageRequest.of(2, 3));

        assertThat(page.getNumber()).isEqualTo(2);
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.isLast()).isTrue();
        assertThat(page.hasNext()).isFalse();
        assertThat(page.hasPrevious()).isTrue();
        assertThat(page.getTotalElements()).isEqualTo(MATCHING_COUNT);
        assertThat(page.getTotalPages()).isEqualTo(3);
    }

    /**
     * Requesting a page index past the end returns an empty content list while the totals
     * (computed by a separate count query) stay correct.
     */
    @Test
    void whenWatchedPageIndexIsPastTheEnd_thenContentIsEmptyButTotalsAreStillCorrect() {
        Page<WatchedPerformanceItem> page =
                watchedPerformanceItemRepository.findByUserId(targetUserId, PageRequest.of(5, 3));

        assertThat(page.getContent()).isEmpty();
        assertThat(page.getTotalElements()).isEqualTo(MATCHING_COUNT);
        assertThat(page.getTotalPages()).isEqualTo(3);
    }

    /**
     * The Sort carried by the Pageable is applied through the performance association:
     * ascending and descending requests on performance.title return opposite orders.
     */
    @Test
    void whenWatchedSortByPerformanceTitle_thenContentIsOrderedAccordingly() {
        Page<WatchedPerformanceItem> ascending = watchedPerformanceItemRepository.findByUserId(
                targetUserId, PageRequest.of(0, MATCHING_COUNT, Sort.by("performance.title").ascending()));

        assertThat(ascending.getContent())
                .extracting(item -> item.getPerformance().getTitle())
                .containsExactlyElementsOf(MATCHING_TITLES_ASC);

        Page<WatchedPerformanceItem> descending = watchedPerformanceItemRepository.findByUserId(
                targetUserId, PageRequest.of(0, MATCHING_COUNT, Sort.by("performance.title").descending()));

        assertThat(descending.getContent())
                .extracting(item -> item.getPerformance().getTitle())
                .containsExactlyElementsOf(MATCHING_TITLES_ASC.reversed());
    }

    /**
     * A user with no watched items gets an empty, zero-total page rather than an error or
     * another user's rows.
     */
    @Test
    void whenUserHasNoWatchedItems_thenReturnEmptyPage() {
        User lonelyUser = User.builder().username("lonelyUser").build();
        entityManager.persist(lonelyUser);
        entityManager.flush();

        Page<WatchedPerformanceItem> page =
                watchedPerformanceItemRepository.findByUserId(lonelyUser.getId(), PageRequest.of(0, 10));

        assertThat(page.getContent()).isEmpty();
        assertThat(page.getTotalElements()).isZero();
        assertThat(page.getTotalPages()).isZero();
    }

    /**
     * findByUserId filters by user: {@value #OTHER_USERNAME}'s 3 watched items never appear in
     * {@value #TARGET_USERNAME}'s results, and querying by {@value #OTHER_USERNAME}'s id returns
     * exactly their own rows.
     */
    @Test
    void whenOtherUsersWatchedItemsExist_thenTheyAreExcludedFromResults() {
        Page<WatchedPerformanceItem> targetUserPage =
                watchedPerformanceItemRepository.findByUserId(targetUserId, PageRequest.of(0, 100));

        assertThat(targetUserPage.getTotalElements()).isEqualTo(MATCHING_COUNT);
        assertThat(targetUserPage.getContent())
                .allMatch(item -> item.getUser().getId().equals(targetUserId));

        Page<WatchedPerformanceItem> otherUserPage =
                watchedPerformanceItemRepository.findByUserId(otherUserId, PageRequest.of(0, 100));

        assertThat(otherUserPage.getTotalElements()).isEqualTo(NOISE_TITLES.size());
        assertThat(otherUserPage.getContent())
                .allMatch(item -> item.getUser().getId().equals(otherUserId));
    }
}
