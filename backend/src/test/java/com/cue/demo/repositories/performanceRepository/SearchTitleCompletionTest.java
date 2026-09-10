package com.cue.demo.repositories.performanceRepository;

import com.cue.demo.dtos.search.SearchSuggestion;
import com.cue.demo.entities.Performance;
import com.cue.demo.repositories.PerformanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class SearchTitleCompletionTest {
    static final String CONTAINER_NAME = "postgres:15";
    static final String INIT_SCRIPT_NAME = "init-unaccent.sql";

    @Container
    @ServiceConnection
    @SuppressWarnings("resource")
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(CONTAINER_NAME)
            .withInitScript(INIT_SCRIPT_NAME);

    @Autowired
    PerformanceRepository performanceRepository;

    @Autowired
    TestEntityManager entityManager;

    Long livadaId;
    Long totiId;
    Long orlandoId;
    Long logodnaId;
    Long machinalId;

    /**
     * Sanity check that the PostgreSQL Testcontainer is created and running before any query runs.
     */
    @Test
    void connectionEstablished() {
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    /**
     *
     * Populates the database with Performance entities that contain diacritics, uppercase or lowercase names.
     */
    @BeforeEach
    void seedDatabase() {
        Performance livada = Performance.builder().title("LIVADA DE VIȘINI").director("Andrei Măjeri").location("SALA STUDIO").build();
        Performance toti = Performance.builder().title("TOȚI FIII MEI").director("Vlad Cristache").location("SALA MARE").build();
        Performance orlando = Performance.builder().title("ORLANDO").director("DIANA Caracala").location("SALA STUDIO").build();
        Performance logodna = Performance.builder().title("LOGODNĂ RELATIVĂ").director("Irina VĂLCESCU").location("SALA STUDIO").build();
        Performance machinal = Performance.builder().title("MACHINAL").director("Diana Csik-Mititelu").location("SALA MARE").build();

        entityManager.persist(livada);
        entityManager.persist(toti);
        entityManager.persist(orlando);
        entityManager.persist(logodna);
        entityManager.persist(machinal);
        entityManager.flush();

        livadaId = livada.getId();
        totiId = toti.getId();
        orlandoId = orlando.getId();
        logodnaId = logodna.getId();
        machinalId = machinal.getId();

        entityManager.clear();
    }

    /**
     * A lowercase keyword that is a prefix of a title (typical autocomplete: "orlan" -> "ORLANDO")
     * must match case-insensitively and return that performance.
     */
    @Test
    void whenSearchTitleCompletionSuggestionsTitleLowercaseIncomplete_thenReturnSuggestions() {
        String keyword = "orlan";
        String resulTitle = "ORLANDO";
        Long resultId = orlandoId;

        List<SearchSuggestion> suggestions = performanceRepository.searchTitleCompletionSuggestions(keyword);

        assertThat(suggestions).isNotEmpty();
        assertThat(suggestions.getFirst().getTitle()).isEqualTo(resulTitle);
        assertThat(suggestions.getFirst().getId()).isEqualTo(resultId);
    }

    /**
     * An uppercase keyword containing a diacritic that appears verbatim in the title
     * ("TOȚI" -> "TOȚI FIII MEI") must match on the title column.
     */
    @Test
    void whenSearchTitleSuggestionsTitleDiacriticsUppercase_thenReturnSuggestions() {
        String keyword = "TOȚI";
        String resultTitle = "TOȚI FIII MEI";
        Long resultId = totiId;

        List<SearchSuggestion> suggestions = performanceRepository.searchTitleCompletionSuggestions(keyword);

        assertThat(suggestions).isNotEmpty();
        assertThat(suggestions.getFirst().getTitle()).isEqualTo(resultTitle);
        assertThat(suggestions.getFirst().getId()).isEqualTo(resultId);
    }

    /**
     * A keyword matched against the director column, where the keyword itself carries a diacritic
     * ("VĂLCE" -> director "Irina VĂLCESCU"). Exercises accent-insensitive matching on both sides
     * of the comparison and confirms the search also covers the director, not only the title.
     */
    @Test
    void whenSearchTitleCompletionSuggestionsDirectorDiacriticsLowercase_thenReturnSuggestions() {
        String keyword = "VĂLCE";
        String resultTitle = "LOGODNĂ RELATIVĂ";
        Long resultId = logodnaId;

        List<SearchSuggestion> suggestions = performanceRepository.searchTitleCompletionSuggestions(keyword);

        assertThat(suggestions).isNotEmpty();
        assertThat(suggestions.getFirst().getTitle()).isEqualTo(resultTitle);
        assertThat(suggestions.getFirst().getId()).isEqualTo(resultId);
    }

    /**
     * With 7 performances whose director matches "diana" (2 from the seed + 5 added here),
     * the query's LIMIT must cap the result at 6 rows.
     */
    @Test
    void whenSearchTitleCompletionSuggestionsMoreThanLimitMatches_thenResultIsCappedAtSix() {
        entityManager.persist(Performance.builder().title("HEDDA GABLER").director("DIANA Nechit").location("SALA MICA").build());
        entityManager.persist(Performance.builder().title("RICHARD AL III-LEA").director("diana popescu").location("SALA MARE").build());
        entityManager.persist(Performance.builder().title("PESCARUSUL").director("Diana Ionescu").location("SALA STUDIO").build());
        entityManager.persist(Performance.builder().title("UNCHIUL VANEA").director("DiAnA Marin").location("SALA MICA").build());
        entityManager.persist(Performance.builder().title("FURTUNA").director("diana VLAD").location("SALA MARE").build());
        entityManager.flush();
        entityManager.clear();

        String keyword = "diana";

        List<SearchSuggestion> suggestions = performanceRepository.searchTitleCompletionSuggestions(keyword);

        assertThat(suggestions).hasSize(6);
    }

    /**
     * A keyword that matches the director of several performances ("diana" -> "ORLANDO" and "MACHINAL")
     * returns all of them. Order is not asserted, since the query has no ORDER BY.
     */
    @Test
    void whenSearchTitleCompletionSuggestionsDirectorMultipleResults_thenReturnSuggestions() {
        String keyword = "diana";
        String firstResultTitle = "ORLANDO";
        Long firstResultId = orlandoId;
        String secondResultTitle = "MACHINAL";
        Long secondResultId = machinalId;

        List<SearchSuggestion> suggestions = performanceRepository.searchTitleCompletionSuggestions(keyword);

        assertThat(suggestions).isNotEmpty();
        assertThat(suggestions.stream().anyMatch(suggestion -> suggestion.getTitle().equals(firstResultTitle))).isTrue();
        assertThat(suggestions.stream().anyMatch(suggestion -> suggestion.getTitle().equals(secondResultTitle))).isTrue();
        assertThat(suggestions.stream().anyMatch(suggestion -> suggestion.getTitle().equals(firstResultTitle))).isTrue();
        assertThat(suggestions.stream().anyMatch(suggestion -> suggestion.getId().equals(firstResultId))).isTrue();
        assertThat(suggestions.stream().anyMatch(suggestion -> suggestion.getId().equals(secondResultId))).isTrue();
    }

    /**
     * A keyword that is an inner/suffix substring of a title word and carries a diacritic
     * ("ATIVĂ" inside "RELATIVĂ"). Confirms matching is substring-based (not prefix-only)
     * and that accent normalization is applied to the keyword as well as the column.
     */
    @Test
    void whenKeywordIsDiacriticSuffixInsideTitle_thenReturnSuggestion() {
        String keyword = "ATIVĂ";

        List<SearchSuggestion> suggestions = performanceRepository.searchTitleCompletionSuggestions(keyword);

        assertThat(suggestions).isNotEmpty();
        assertThat(suggestions.getFirst().getTitle()).isEqualTo("LOGODNĂ RELATIVĂ");
        assertThat(suggestions.getFirst().getId()).isEqualTo(logodnaId);
    }

    /**
     * A keyword that matches no title and no director returns an empty list, never {@code null}.
     */
    @Test
    void whenKeywordMatchesNothing_thenReturnEmptyListNotNull() {
        String keyword = "qwertyxyz";

        List<SearchSuggestion> suggestions = performanceRepository.searchTitleCompletionSuggestions(keyword);

        assertThat(suggestions).isNotNull().isEmpty();
    }

}
