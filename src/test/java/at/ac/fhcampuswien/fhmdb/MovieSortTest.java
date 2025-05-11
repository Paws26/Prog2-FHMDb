package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.helpers.MovieDisplayHelper;
import at.ac.fhcampuswien.fhmdb.utils.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MovieSortTest {

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

        ObservableList<Movie> unsortedObservableList = FXCollections.observableList(unsorted);

        List<Movie> sorted = MovieDisplayHelper.sortMoviesAscending(unsortedObservableList);

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

        ObservableList<Movie> unsortedObservableList = FXCollections.observableList(unsorted);

        List<Movie> sorted = MovieDisplayHelper.sortMoviesDescending(unsortedObservableList);

        assertEquals("Titanic", sorted.get(0).getTitle(), "Expected first movie: 'Titanic'");
        assertEquals("Inception", sorted.get(1).getTitle(), "Expected second movie: 'Inception'");
        assertEquals("Forrest Gump", sorted.get(2).getTitle(), "Expected third movie: 'Forrest Gump'");
    }
    @Test
    void sort_movies_with_empty_list_returns_empty_list() {
        List<Movie> emptyList = new ArrayList<>();
        ObservableList<Movie> emptyObservableList = FXCollections.observableList(emptyList);

        List<Movie> sortedAscending = MovieDisplayHelper.sortMoviesAscending(emptyObservableList);
        List<Movie> sortedDescending = MovieDisplayHelper.sortMoviesDescending(emptyObservableList);

        assertTrue(sortedAscending.isEmpty(), "Expected empty list when sorting ascending");
        assertTrue(sortedDescending.isEmpty(), "Expected empty list when sorting descending");
    }
    @Test
    void sort_movies_with_single_element_returns_same_list() {
        List<Movie> singleMovieList = List.of(
                new Movie("Interstellar", "A sci-fi adventure through space and time.",
                        List.of(Genre.SCIENCE_FICTION, Genre.DRAMA))
        );

        ObservableList<Movie> singleObservableList = FXCollections.observableList(new ArrayList<>(singleMovieList));

        List<Movie> sortedAscending = MovieDisplayHelper.sortMoviesAscending(singleObservableList);
        List<Movie> sortedDescending = MovieDisplayHelper.sortMoviesDescending(singleObservableList);

        assertEquals("Interstellar", sortedAscending.get(0).getTitle(), "Expected same movie when sorting ascending");
        assertEquals("Interstellar", sortedDescending.get(0).getTitle(), "Expected same movie when sorting descending");
    }
}
