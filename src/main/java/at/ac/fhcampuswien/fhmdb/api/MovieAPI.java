package at.ac.fhcampuswien.fhmdb.api;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class MovieAPI {
    private final OkHttpClient client = new OkHttpClient();

    public String getMoviesJson(String url) throws IOException {
        Request request = new Request.Builder().url(url).header("User-Agent", "http.agent").build(); // URL Request an API

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();  // JSON String
        }
    }

    public List<Movie> parseJsonMovies(String jsonString) {
        Gson gson = new Gson();
        List<Movie> movies = null;

        try {
            // For lists, you need to tell Gson the specific generic type (List<Movie>)
            // Use TypeToken for this
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
