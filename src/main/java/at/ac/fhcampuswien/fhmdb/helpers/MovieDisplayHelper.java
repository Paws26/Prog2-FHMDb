package at.ac.fhcampuswien.fhmdb.helpers;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.ObservableList;

import java.util.*;
import java.util.function.Function;
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

    //gibt jene Person zurück, die am öftesten im mainCast der übergebenen Filme vorkommt.
    public static String getMostPopularActor(List<Movie> movies){
        if (movies == null || movies.isEmpty()) {
            return null;
        }

        // Stream pipeline:
        return movies.stream() // 1. Start with a Stream<Movie>
                // Optional: Filter out null movies if they might exist in the list
                .filter(Objects::nonNull)
                // 2. Get the list of actors for each movie -> Stream<List<String>>
                .map(Movie::getMainCast)
                // Optional: Filter out null actor lists if a movie might have null instead of an empty list
                .filter(Objects::nonNull)
                // 3. Flatten the Stream<List<String>> into a single Stream<String>
                //    List::stream is a method reference for list -> list.stream()
                .flatMap(List::stream)
                // Optional: Filter out potential null actor names within the lists
                .filter(Objects::nonNull)
                // 4. Collect the actors into a Map<String, Long> counting occurrences
                //    Function.identity() means use the stream element (the actor string) as the key
                //    Collectors.counting() counts how many elements fall into each group
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                )) // Result: Map<ActorName, Count>
                // 5. Get the entries (key-value pairs) from the map -> Set<Map.Entry<String, Long>>
                .entrySet()
                // 6. Stream the entries -> Stream<Map.Entry<String, Long>>
                .stream()
                // 7. Find the entry with the maximum value (the count)
                //    Map.Entry.comparingByValue() tells max() to compare based on the map value
                .max(Map.Entry.comparingByValue()) // Result: Optional<Map.Entry<String, Long>>
                // 8. If an entry was found, get its key (the actor name)
                //    Map.Entry::getKey is a method reference for entry -> entry.getKey()
                .map(Map.Entry::getKey)
                .orElse(null);

//        Stream<String> actorsList = movies.stream().flatMap(movie -> movie.getMainCast().stream());
//
//        Map<String, Long> actorsCount = actorsList.collect(Collectors.groupingBy(actor -> actor, Collectors.counting()));
//
//        //TODO: remove (debug)
//        actorsCount.forEach((actor, count) -> System.out.println(actor + ": " + count));
//
//        var actorResult = actorsCount.entrySet().stream()
//                .max(Map.Entry.comparingByValue())
//                .map(Map.Entry::getKey)
//                .orElse(null);
//
//        return movies.stream()
//                .flatMap(movie -> movie.getMainCast().stream()) // Extract and flatten actor lists
//                .collect(Collectors.groupingBy(actor -> actor, Collectors.counting())) // Count occurrences
//                .entrySet().stream()
//                .max(Map.Entry.comparingByValue()) // Find the most common actor
//                .map(Map.Entry::getKey) // Extract the actor name
//                .orElse(null);
    }

    //filtert auf den längsten Titel der übergebenen Filme und gibt die Anzahl der Buchstaben des Titels zurück
    public static int getLongestMovieTitle(List<Movie> movies){
        if (movies == null || movies.isEmpty()) {
            return 0;
        }

        //TODO: remove (first approach - can't handle nulls ... kept around so far to have something simpler to compare to)
//        return movies.stream()
//                .map(movie -> movie.getTitle().length())
//                .max(Integer::compareTo)
//                .get();

        // 2. Use a stream to find the max title length.
        return movies.stream()                       // Stream<Movie>
                .filter(Objects::nonNull)           // Filter out null movies -> Stream<Movie>
                .map(Movie::getTitle)               // Get titles -> Stream<String>
                .filter(Objects::nonNull)           // Filter out null titles (safer) -> Stream<String>
                .mapToInt(String::length)           // Convert to lengths -> IntStream
                .max()                              // Find the maximum value -> OptionalInt
                .orElse(0);
    }

//    gibt die Anzahl der Filme eines bestimmten Regisseurs zurück.
    public static long countMoviesFrom(List<Movie> movies, String director) {

        return movies.stream() // 1. Create a stream from the list of movies
                .filter(Objects::nonNull) // 2. Filter out any null Movie objects in the list
                .filter(movie -> { // 3. Filter movies based on the director
                    List<String> movieDirectors = movie.getDirectors();
                    // Check if the movie's director list is not null AND contains the target director
                    return movieDirectors != null && movieDirectors.contains(director);
                })
                .count(); // 4. Count the number of movies remaining in the stream
    }

    //gibt jene Filme zurück, die zwischen zwei gegebenen Jahren veröffentlicht wurden
    public static List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear){
        return movies.stream()
                .filter(Objects::nonNull)
                .filter(movie -> movie.getReleaseYear() > startYear && movie.getReleaseYear() < endYear)
                .collect(Collectors.toList());
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

    //Filter the incoming List of Movies by the genre
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

    //Filter the incoming List of Movies
    public static List<Movie> filterMovies(List<Movie> moviesToFilter, String query, Genre genre) {
        List<Movie> filteredMovies = filterMoviesByGenre(moviesToFilter, genre);

        return filterMoviesBySearch(filteredMovies, query);
    }

    // Return distinct years for Movies
    public static List<Integer> getDistinctReleaseYears(ObservableList<Movie> filteredMovies) {
        // Collect distinct years and sort them using the TreeSet Set type
        Set<Integer> distinctyears = filteredMovies.stream()
                .map(Movie::getReleaseYear)
                .collect(Collectors.toCollection(TreeSet::new));

        List<Integer> filteredYears = new ArrayList<>(distinctyears);
        filteredYears.sort(Comparator.reverseOrder());

        return filteredYears;
    }
}
