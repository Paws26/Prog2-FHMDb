package at.ac.fhcampuswien.fhmdb.api;

import at.ac.fhcampuswien.fhmdb.HomeController;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class MovieAPI {
    private final OkHttpClient client = new OkHttpClient();

    // Build API Url String for movieAPI Request
    public String buildApiURL(String initialUrl, String query, Genre genre, Integer year, Double rating) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(initialUrl).newBuilder();

        if (query != null && !query.trim().isEmpty()) {
            urlBuilder.addQueryParameter("query", query);
        }
        if (genre != null && genre != Genre.ANY) {
            urlBuilder.addQueryParameter("genre", genre.name());
        }
        if (year != HomeController.NO_YEAR_FILTER) {
            urlBuilder.addQueryParameter("releaseYear", String.valueOf(year));
        }
        if (rating != null) {
            urlBuilder.addQueryParameter("ratingFrom", String.valueOf(rating));
        }

        System.out.println(urlBuilder);
        return urlBuilder.build().toString();
    }

    public String getMoviesJson(String url) throws IOException {
        Request request = new Request.Builder().url(url).header("User-Agent", "http.agent").build(); // URL Request on API

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();  // return JSON String
        }
    }

    public List<Movie> parseJsonMovies(String jsonString) {
        Gson gson = new Gson();
        List<Movie> movies = null;

        try {
            // For lists, we need to tell Gson the specific generic type (List<Movie>) - use TypeToken for this
            Type movieListType = new TypeToken<List<Movie>>(){}.getType();

            movies = gson.fromJson(jsonString, movieListType);

            if (movies != null) {
                //TODO: remove (debug)
                System.out.println("Successfully parsed " + movies.size() + " movies.");
                // You can loop through them
                // for(Movie movie : movies) {
                //    System.out.println("- " + movie.getTitle());
                // }
            }
        } catch (JsonSyntaxException e) {
            //TODO: UI Ausgabe?
            System.err.println("Error parsing JSON array: " + e.getMessage());
            e.printStackTrace();
        }
        return movies; // Return the list (or null if error)
    }
}
