package at.ac.fhcampuswien.fhmdb.helpers;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;
import java.util.stream.Collectors;

public class MovieDisplayHelper {

    // sorts observableMovies alphabetically ascending
    public static List<Movie> sortMoviesAscending(ObservableList<Movie> observableMovies) {
        List<Movie> sortedMovies = new ArrayList<>(observableMovies);
        sortedMovies.sort(Comparator.comparing(Movie::getTitle));
        return sortedMovies;
    }

    // sorts observableMovies alphabetically descending
    public static List<Movie> sortMoviesDescending(ObservableList<Movie> observableMovies) {
        List<Movie> sortedMovies = new ArrayList<>(observableMovies);
        sortedMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
        return sortedMovies;
    }

    // Filter the incoming List of Movies by the search query
    public static List<Movie> filterMoviesBySearch(List<Movie> moviesToFilter, String query) {
        if (query == null || query.isBlank()) { // empty or null queries return unmodified list
            return new ArrayList<>(moviesToFilter);
        }
        List<Movie> filteredMovies = new ArrayList<Movie>();
        String sanitizedQuery = query.toLowerCase().trim().replaceAll("\\s+", " "); // repair search query

        for (Movie movie : moviesToFilter) {
            if (movie.getTitle().toLowerCase().contains(sanitizedQuery) || movie.getDescription().toLowerCase().contains(sanitizedQuery)) {
                filteredMovies.add(movie);
            }
        }
        return filteredMovies;
    }

    // Filter the incoming List of Movies by the genre
    public static List<Movie> filterMoviesByGenre(List<Movie> moviesToFilter, Genre genre) {
        if (genre == Genre.NONE || genre == null) {
            return moviesToFilter;
        }
        List<Movie> filteredMovies = new ArrayList<>();

        for (Movie movie : moviesToFilter) {
            if (movie.getGenres().contains(genre)) {
                filteredMovies.add(movie);
            }
        }
        return filteredMovies;
    }

    // Filter the incoming List of Movies
    public static List<Movie> filterMovies(List<Movie> moviesToFilter, String query, Genre genre) {
        List<Movie> filteredMovies = filterMoviesByGenre(moviesToFilter, genre);

        return filterMoviesBySearch(filteredMovies, query);
    }

    // Return distinct years for filtered Movies
    public static Set<Integer> getFilteredReleaseYears(ObservableList<Movie> filteredMovies) {
        // Use a TreeSet to collect distinct years and keep them sorted
        return filteredMovies.stream()
                .map(Movie::getReleaseYear)
                .collect(Collectors.toCollection(TreeSet::new));
    }
}
