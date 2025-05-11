package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.helpers.MovieDisplayHelper;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class MovieFilterIntegrationTest {

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
    void search_null_and_genre_null_returns_unmodified_list() {
        List<Movie> actual = MovieDisplayHelper.filterMovies(movies, null, null);
        assertEquals(movies, actual, "A null search query and null genre should return the unmodified list.");
    }

    @Test
    void search_null_and_genre_valid_filters_by_genre() {
        List<Movie> actual = MovieDisplayHelper.filterMovies(movies, null, Genre.ACTION);
        List<Movie> expected = MovieDisplayHelper.filterMoviesByGenre(movies, Genre.ACTION);
        assertEquals(expected, actual, "Should return only Action movies.");
    }

    @Test
    void search_valid_and_genre_null_filters_by_search() {
        List<Movie> actual = MovieDisplayHelper.filterMovies(movies, "dark", null);
        List<Movie> expected = MovieDisplayHelper.filterMoviesBySearch(movies, "dark");
        assertEquals(expected, actual, "Should return only movies with 'dark' in title or description.");
    }

    @Test
    void search_valid_and_genre_valid_filters_by_both_genre_and_search() {
        List<Movie> actual = MovieDisplayHelper.filterMovies(movies, "dark", Genre.ACTION);
        String actualTitle = actual.get(0).getTitle();
        String expectedTitle = "The Dark Knight";

        assertEquals(expectedTitle, actualTitle, "Should return only 'The Dark Knight' since it matches both search and genre.");
    }

    @Test
    void search_query_matches_but_genre_does_not_returns_empty_list() {
        List<Movie> actual = MovieDisplayHelper.filterMovies(movies, "dark", Genre.ROMANCE);
        assertTrue(actual.isEmpty(), "Matching query 'dark' with Romance genre should return an empty list.");
    }

    @Test
    void genre_matches_but_search_does_not_returns_empty_list() {
        List<Movie> actual = MovieDisplayHelper.filterMovies(movies, "aQueryThatShouldNotMatch!", Genre.ACTION);
        assertTrue(actual.isEmpty(), "Non-matching query 'aQueryThatShouldNotMatch' with Action genre should return an empty list.");
    }

    @Test
    void both_filters_match_returns_subset_of_movies() {
        List<Movie> actual = MovieDisplayHelper.filterMovies(movies, "story", Genre.DRAMA);

        List<String> actualTitles = actual.stream().map(Movie::getTitle).toList();
        List<String> expectedTitles = List.of("Forrest Gump", "Titanic");

        assertEquals(expectedTitles, actualTitles, "Should return only 'Forrest Gump' and 'Titanic', since both match 'story' and the Drama genre.");
    }
}
