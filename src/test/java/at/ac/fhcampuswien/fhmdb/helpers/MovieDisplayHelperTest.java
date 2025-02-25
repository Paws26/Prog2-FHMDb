package at.ac.fhcampuswien.fhmdb.helpers;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieDisplayHelperTest {

    private List<Movie> movies;

    @BeforeEach
    void movies() {
        movies = new ArrayList<>();
        movies.add(new Movie("Inception", "A thief enters people's dreams to steal secrets.",
                List.of(Genre.ACTION, Genre.SCIENCE_FICTION, Genre.THRILLER)));

        movies.add(new Movie("The Godfather", "The aging patriarch of an organized crime dynasty transfers control to his reluctant son.",
                List.of(Genre.CRIME, Genre.DRAMA, Genre.THRILLER)));

        movies.add(new Movie("Forrest Gump", "The life story of a slow-witted but kind-hearted man.",
                List.of(Genre.DRAMA, Genre.ROMANCE, Genre.COMEDY)));

        movies.add(new Movie("Interstellar", "A team of explorers travels through a wormhole in space to ensure humanity's survival.",
                List.of(Genre.SCIENCE_FICTION, Genre.DRAMA, Genre.ADVENTURE)));

        movies.add(new Movie("The Dark Knight", "Batman faces his greatest challenge yet in the form of the Joker.",
                List.of(Genre.ACTION, Genre.CRIME, Genre.THRILLER)));

        movies.add(new Movie("Titanic", "A love story set aboard the ill-fated RMS Titanic.",
                List.of(Genre.ROMANCE, Genre.DRAMA, Genre.HISTORY)));
    }

    @Test
    void search_field_empty_returns_unmodified_list() {
        List<Movie> actual = MovieDisplayHelper.filterSearch(movies, "");
        assertEquals(movies, actual, "An empty search query should return an unmodified list.");
    }

    @Test
    void search_field_null_returns_unmodified_list() {
        List<Movie> actual = MovieDisplayHelper.filterSearch(movies, null);
        assertEquals(movies, actual, "A null search query should return an unmodified list.");
    }

    @Test
    void search_query_finds_no_match_returns_empty_list() {
        List<Movie> actual = MovieDisplayHelper.filterSearch(movies, "A Query that should never Match!!!_?§$%&/)");
        assertTrue(actual.isEmpty(), "Non-matching query should return an empty list.");
    }

    @Test
    void search_query_finds_1_match_in_title_returns_modified_list() {
        List<Movie> actual = MovieDisplayHelper.filterSearch(movies, "Dark");
        assertEquals(1, actual.size(), "Single word search \"Dark\" should have exactly one match.");
        assertEquals("The Dark Knight", actual.get(0).getTitle(), "The filtered List should contain the movie \"The Dark Knight\".");
    }

    @Test
    void search_query_finds_2_matches_in_description_returns_modified_list() {
        List<Movie> actual = MovieDisplayHelper.filterSearch(movies, "story");
        assertEquals(2, actual.size(), "Single word search \"story\" should have exactly two matches.");
        assertEquals("The life story of a slow-witted but kind-hearted man.", actual.get(0).getDescription(), "The filtered List should contain the movie \"Forrest Gump\".");
        assertEquals("A love story set aboard the ill-fated RMS Titanic.", actual.get(1).getDescription(), "The filtered List should contain the movie \"Titanic\".");
    }

    @Test
    void multiple_word_search_query_finds_1_match_returns_modified_list() {
        List<Movie> actual = MovieDisplayHelper.filterSearch(movies, "Dark Knight");
        assertEquals(1, actual.size(), "Multi-word search \"Dark Knight\" should have exactly one match.");
        assertEquals("The Dark Knight", actual.get(0).getTitle(), "The filtered List should contain the movie \"The Dark Knight\".");
    }

    @Test
    void case_sensitive_search_query_finds_1_match_returns_modified_list() {
        List<Movie> actual = MovieDisplayHelper.filterSearch(movies, "the godfather");
        assertEquals(1, actual.size(), "Case-insensitive search \"the godfather\" should still find the match.");
    }

    @Test
    void partial_search_query_finds_1_match_returns_modified_list() {
        List<Movie> actual = MovieDisplayHelper.filterSearch(movies, "terstellar");
        assertEquals(1, actual.size(), "Partial search query should match \"Interstellar\".");
    }

    @Test
    void trailing_and_leading_whitespace_search_query_finds_1_match_returns_modified_list() {
        List<Movie> actual = MovieDisplayHelper.filterSearch(movies, "  inception  ");
        assertEquals(1, actual.size(), "Search should ignore trailing/leading whitespace.");
    }

    @Test
    void empty_movie_list_returns_empty_list() {
        List<Movie> emptyMovies = new ArrayList<>();
        List<Movie> actual = MovieDisplayHelper.filterSearch(emptyMovies, "Inception");
        assertTrue(actual.isEmpty(), "Searching in an empty list should return an empty list.");
    }


    @Test
    void genre_selected_is_null_returns_unmodified_list(){
        List<Movie> actual = MovieDisplayHelper.filterMoviesByGenre(movies, null);
        assertEquals(actual, movies, "filtering by a genre that is null should return an unmodified list.");
    }

    @Test
    void genre_filter_finds_no_matching_movies_returns_empty_list(){
        movies = new ArrayList<>();
        movies.add(new Movie("Inception", "A thief enters people's dreams to steal secrets.",
                List.of(Genre.ACTION, Genre.SCIENCE_FICTION, Genre.THRILLER)));
        movies.add(new Movie("The Godfather", "The aging patriarch of an organized crime dynasty transfers control to his reluctant son.",
                List.of(Genre.CRIME, Genre.DRAMA, Genre.THRILLER)));
        movies.add(new Movie("Forrest Gump", "The life story of a slow-witted but kind-hearted man.",
                List.of(Genre.DRAMA, Genre.ROMANCE, Genre.COMEDY)));
        movies.add(new Movie("Interstellar", "A team of explorers travels through a wormhole in space to ensure humanity's survival.",
                List.of(Genre.SCIENCE_FICTION, Genre.DRAMA, Genre.ADVENTURE)));
        movies.add(new Movie("The Dark Knight", "Batman faces his greatest challenge yet in the form of the Joker.",
                List.of(Genre.ACTION, Genre.CRIME, Genre.THRILLER)));
        movies.add(new Movie("Titanic", "A love story set aboard the ill-fated RMS Titanic.",
                List.of(Genre.ROMANCE, Genre.DRAMA, Genre.HISTORY)));

        List<Movie> actual = MovieDisplayHelper.filterMoviesByGenre(movies, Genre.DOCUMENTARY);
        assertTrue(actual.isEmpty(), "Filtering by genre not present in movies-list should return an empty list.");
    }

    @Test
    void genre_filter_excludes_non_matching_movies() {
        List<Movie> movies = List.of(
                new Movie("Inception", "...", List.of(Genre.ACTION, Genre.SCIENCE_FICTION, Genre.THRILLER)),
                new Movie("The Godfather", "...", List.of(Genre.CRIME, Genre.DRAMA, Genre.THRILLER)),
                new Movie("Forrest Gump", "...", List.of(Genre.DRAMA, Genre.ROMANCE, Genre.COMEDY))
        );

        List<Movie> actual = MovieDisplayHelper.filterMoviesByGenre(movies, Genre.SCIENCE_FICTION);

        assertEquals(1, actual.size(), "Filtered list should contain exactly 1 movie");
        assertFalse(actual.contains(new Movie("The Godfather", "...", List.of(Genre.CRIME, Genre.DRAMA, Genre.THRILLER))),
                "Filtered list should NOT contain 'The Godfather'");
        assertFalse(actual.contains(new Movie("Forrest Gump", "...", List.of(Genre.DRAMA, Genre.ROMANCE, Genre.COMEDY))),
                "Filtered list should NOT contain 'Forrest Gump'");
    }

    @Test
    void genre_filter_includes_all_matching_movies() {
        Movie inception = new Movie("Inception", "...", List.of(Genre.ACTION, Genre.SCIENCE_FICTION, Genre.THRILLER));
        Movie theGodfather = new Movie("The Godfather", "...", List.of(Genre.CRIME, Genre.DRAMA, Genre.THRILLER));
        Movie interstellar = new Movie("Interstellar", "...", List.of(Genre.SCIENCE_FICTION, Genre.DRAMA, Genre.ADVENTURE));
        List<Movie> movies = List.of(
                inception,
                theGodfather,
                interstellar
        );

        List<Movie> actual = MovieDisplayHelper.filterMoviesByGenre(movies, Genre.SCIENCE_FICTION);

        assertEquals(2, actual.size(), "Filtered list should contain exactly 2 movies");
        assertTrue(actual.contains(inception));
        assertTrue(actual.contains(interstellar));
    }

    //test sorts observableMovies alphabetically ascending
    @Test
    void sort_movies_ascending_returns_sorted_list() {
        List<Movie> unsorted = new ArrayList<>();
        unsorted.add(new Movie("Titanic", "A love story set aboard the ill-fated RMS Titanic.",
                List.of(Genre.ROMANCE, Genre.DRAMA, Genre.HISTORY)));
        unsorted.add(new Movie("Inception", "A thief enters people's dreams to steal secrets.",
                List.of(Genre.ACTION, Genre.SCIENCE_FICTION, Genre.THRILLER)));
        unsorted.add(new Movie("Forrest Gump", "The life story of a slow-witted but kind-hearted man.",
                List.of(Genre.DRAMA, Genre.ROMANCE, Genre.COMEDY)));

        MovieDisplayHelper helper = new MovieDisplayHelper(unsorted);
        helper.sortMoviesAscending();
        List<Movie> sorted = helper.getObservableMovies();

        // expected: "Forrest Gump", "Inception", "Titanic"
        assertEquals("Forrest Gump", sorted.get(0).getTitle(), "Expected first movie: 'Forrest Gump'");
        assertEquals("Inception", sorted.get(1).getTitle(), "Expected second movie: 'Inception'");
        assertEquals("Titanic", sorted.get(2).getTitle(), "Expected third movie: 'Titanic'");
    }

    //test sorts observableMovies alphabetically descending
    @Test
    void sort_movies_descending_returns_sorted_list() {
        List<Movie> unsorted = new ArrayList<>();
        unsorted.add(new Movie("Titanic", "A love story set aboard the ill-fated RMS Titanic.",
                List.of(Genre.ROMANCE, Genre.DRAMA, Genre.HISTORY)));
        unsorted.add(new Movie("Inception", "A thief enters people's dreams to steal secrets.",
                List.of(Genre.ACTION, Genre.SCIENCE_FICTION, Genre.THRILLER)));
        unsorted.add(new Movie("Forrest Gump", "The life story of a slow-witted but kind-hearted man.",
                List.of(Genre.DRAMA, Genre.ROMANCE, Genre.COMEDY)));

        List.of(Genre.DRAMA, Genre.ROMANCE, Genre.COMEDY);

        MovieDisplayHelper helper = new MovieDisplayHelper(unsorted);
        helper.sortMoviesDescending();
        List<Movie> sorted = helper.getObservableMovies();

        assertEquals("Titanic", sorted.get(0).getTitle(), "Expected first movie: 'Titanic'");
        assertEquals("Inception", sorted.get(1).getTitle(), "Expected second movie: 'Inception'");
        assertEquals("Forrest Gump", sorted.get(2).getTitle(), "Expected third movie: 'Forrest Gump'");
    }
}