package at.ac.fhcampuswien.fhmdb.helpers;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieDisplayHelper {

    public static List<Movie> sort(List<Movie> observableMovies) {
        return null;
    }

    //Filter the incoming List of Movies by the search query
    public static List<Movie> filterSearch(List<Movie> moviesToFilter, String query) {
        if (query == null || query.isBlank()) {     //empty or null queries return unmodified list
            return new ArrayList<>(moviesToFilter);
        }
        List<Movie> filteredMovies = new ArrayList<Movie>();
        String sanitizedQuery = query.toLowerCase().trim().replaceAll("\\s+", " ");     //repair search query

        for (Movie movie : moviesToFilter) {
            if (movie.getTitle().toLowerCase().contains(sanitizedQuery) || movie.getDescription().toLowerCase().contains(sanitizedQuery)) {
                filteredMovies.add(movie);
            }
        }
        return filteredMovies;
    }

    // TODO Create Filter for Genre
    public static List<Movie> filterMoviesByGenre(List<Movie> moviesToFilter, Genre genre) {
        if (genre == null) {
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


    // TODO Create Method for Both Filters(?)
}
