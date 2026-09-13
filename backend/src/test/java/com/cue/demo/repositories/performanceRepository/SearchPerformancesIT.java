package com.cue.demo.repositories.performanceRepository;

import com.cue.demo.entities.Performance;
import com.cue.demo.repositories.PerformanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class SearchPerformancesIT {

    static final String CONTAINER_NAME = "postgres:15";
    static final String INIT_SCRIPT_NAME = "init-unaccent.sql";

    /**
     * Keyword shared by every matching performance below (matched on the director column).
     */
    protected static final String SEARCH_KEYWORD = "afrim";

    /**
     * How many seeded performances match {@link #SEARCH_KEYWORD}. Non-matching rows exist on top of these.
     */
    protected static final int MATCHING_COUNT = 7;

    /**
     * Titles of the matching performances, in ascending alphabetical order.
     * Use this to assert sorting and page contents.
     */
    protected static final List<String> MATCHING_TITLES_ASC = List.of(
            "ANIMALE BOLNAVE DE PESTA",
            "CE ZILE FRUMOASE",
            "GHETUL",
            "NEVROZA",
            "SUFLETE MOARTE",
            "TRAGEDIA OMULUI",
            "ZBOR DEASUPRA UNUI CUIB DE CUCI"
    );

    @Container
    @ServiceConnection
    @SuppressWarnings("resource")
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(CONTAINER_NAME)
            .withInitScript(INIT_SCRIPT_NAME);

    @Autowired
    protected PerformanceRepository performanceRepository;

    @Autowired
    protected TestEntityManager entityManager;

    /**
     * Seeds {@value #MATCHING_COUNT} performances whose director matches "{@value #SEARCH_KEYWORD}"
     * plus 3 performances that must never show up in the results.
     */
    @BeforeEach
    void seedDatabase() {
        MATCHING_TITLES_ASC.forEach(title ->
                entityManager.persist(Performance.builder()
                        .title(title)
                        .director("Radu Afrim")
                        .location("SALA MARE")
                        .build()));

        entityManager.persist(Performance.builder().title("RICHARD AL III-LEA").director("Silviu Purcarete").location("SALA MARE").build());
        entityManager.persist(Performance.builder().title("FURTUNA").director("Silviu Purcarete").location("SALA STUDIO").build());
        entityManager.persist(Performance.builder().title("HAMLET").director("Vlad Cristache").location("SALA MICA").build());

        entityManager.flush();
        entityManager.clear();
    }

    /**
     * With a page size smaller than the number of matches (7 matches, size 2), the first page
     * is full, is marked as first-but-not-last, and the totals report the 4 pages the result spans.
     */
    @Test
    void whenPageSizeSmallerThanResultCount_thenFirstPageIsFullAndMorePagesReported() {

        Pageable page = PageRequest.of(0, 2, Sort.by("average_rating").descending());
        Page<Performance> result = performanceRepository.searchPerformances(SEARCH_KEYWORD, page);

        assertThat(result.isEmpty()).isFalse();

        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.getContent().size()).isEqualTo(2);

        assertThat(result.isFirst()).isTrue();
        assertThat(result.isLast()).isFalse();
        assertThat(result.hasNext()).isTrue();

        assertThat(result.getTotalElements()).isEqualTo(MATCHING_COUNT);
        assertThat(result.getTotalPages()).isEqualTo(4);


    }

    /**
     * The last page of a result set that does not divide evenly by page size is partially filled
     * (7 matches, size 3 -> page 2 holds 1 row) and is reported as the last page.
     */
    @Test
    void whenLastPageIsPartial_thenItHoldsTheRemainderAndIsMarkedLast() {
        Pageable pageable = PageRequest.of(2, 3);

        Page<Performance> result = performanceRepository.searchPerformances(SEARCH_KEYWORD, pageable);

        assertThat(result.getNumber()).isEqualTo(2);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.isLast()).isTrue();
        assertThat(result.hasNext()).isFalse();
        assertThat(result.hasPrevious()).isTrue();
        assertThat(result.getTotalElements()).isEqualTo(MATCHING_COUNT);
        assertThat(result.getTotalPages()).isEqualTo(3);
    }

    /**
     * Requesting a page index past the end returns an empty content list while the totals
     * (computed by a separate count query) stay correct.
     */
    @Test
    void whenPageIndexIsPastTheEnd_thenContentIsEmptyButTotalsAreStillCorrect() {
        Pageable pageable = PageRequest.of(5, 3);

        Page<Performance> result = performanceRepository.searchPerformances(SEARCH_KEYWORD, pageable);

        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(MATCHING_COUNT);
        assertThat(result.getTotalPages()).isEqualTo(3);
    }

    /**
     * The Sort carried by the Pageable is applied to the query: ascending and descending
     * requests on the title column return the matches in opposite orders.
     */
    @Test
    void whenSortByTitle_thenContentIsOrderedAccordingly() {
        Page<Performance> ascending = performanceRepository.searchPerformances(
                SEARCH_KEYWORD, PageRequest.of(0, MATCHING_COUNT, Sort.by("title").ascending()));

        assertThat(ascending.getContent())
                .extracting(Performance::getTitle)
                .containsExactlyElementsOf(MATCHING_TITLES_ASC);

        Page<Performance> descending = performanceRepository.searchPerformances(
                SEARCH_KEYWORD, PageRequest.of(0, MATCHING_COUNT, Sort.by("title").descending()));

        assertThat(descending.getContent())
                .extracting(Performance::getTitle)
                .containsExactlyElementsOf(MATCHING_TITLES_ASC.reversed());
    }

    /**
     * The keyword filter excludes rows that match neither title, director nor location:
     * the 3 non-Afrim performances never appear, and an unknown keyword yields an empty page.
     */
    @Test
    void whenKeywordFilters_thenOnlyMatchingRowsAreCounted() {
        Page<Performance> matching = performanceRepository.searchPerformances(
                SEARCH_KEYWORD, PageRequest.of(0, 100));

        assertThat(matching.getTotalElements()).isEqualTo(MATCHING_COUNT);

        Page<Performance> none = performanceRepository.searchPerformances(
                "keyword-that-matches-nothing", PageRequest.of(0, 100));

        assertThat(none.isEmpty()).isTrue();
        assertThat(none.getTotalElements()).isZero();
    }

    /**
     * The query matches on title and on location as well, not only on the director:
     * a keyword that only occurs in one title, and one that only occurs in a location,
     * each return exactly the row that contains it.
     */
    @Test
    void whenKeywordOccursInTitleOrLocationOnly_thenThatRowIsReturned() {
        Page<Performance> byTitle = performanceRepository.searchPerformances(
                "NEVROZA", PageRequest.of(0, 100));

        assertThat(byTitle.getContent())
                .extracting(Performance::getTitle)
                .containsExactly("NEVROZA");

        // "STUDIO" appears only in the location of "FURTUNA" (director Silviu Purcarete).
        Page<Performance> byLocation = performanceRepository.searchPerformances(
                "STUDIO", PageRequest.of(0, 100));

        assertThat(byLocation.getContent())
                .extracting(Performance::getTitle)
                .containsExactly("FURTUNA");
    }

    /**
     * The seed stores the director as "Silviu Purcarete" (no diacritics), but a user may type
     * "Purcărete" with the diacritic. The query must strip accents from the keyword as well as
     * from the column, so both RICHARD AL III-LEA and FURTUNA are found.
     * <p>
     * This fails while {@code unaccent(...)} is applied only to the columns and not to the keyword.
     */
    @Test
    void whenKeywordContainsDiacritics_thenItMatchesTheAccentStrippedColumn() {
        Page<Performance> result = performanceRepository.searchPerformances(
                "Purcărete", PageRequest.of(0, 100));

        assertThat(result.getContent())
                .extracting(Performance::getTitle)
                .containsExactlyInAnyOrder("RICHARD AL III-LEA", "FURTUNA");
    }
}
