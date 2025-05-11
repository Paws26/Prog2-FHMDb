package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.helpers.MovieDisplayHelper;
import at.ac.fhcampuswien.fhmdb.utils.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MovieFilterGenreTest {

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
}
