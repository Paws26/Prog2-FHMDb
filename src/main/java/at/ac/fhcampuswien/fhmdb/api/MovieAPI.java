package at.ac.fhcampuswien.fhmdb.api;
import at.ac.fhcampuswien.fhmdb.HomeController;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;



//Movie api to fetch data from url
public class MovieAPI {

    private final String endpointUrl = "https://prog2.fh-campuswien.ac.at/movies"; // Initial URL

    private final OkHttpClient client = new OkHttpClient();

    //TODO: rename to fetch movie
    // Build API Url String for movieAPI Request
    public String buildApiURL(
            //TODO: remove later, will be final here
            String url,
            //TODO: remove later, wont be used anymore
            String query,
            Genre genre,
            Integer year,
            Double rating
   )  {
        try {
            String json = getMoviesJson(endpointUrl);
            List<Movie> movies = parseJsonMovies(json);
            return json;
        }
        catch (IOException e) { System.out.println(e.getMessage()); }

        //for now return url
        return url;
    };
    /******************************************************************************************************************/
    //use this instead
    public List<Movie>  fetMovieList() {
        List<Movie> movies = new ArrayList<>();
        try {
            String json = getMoviesFromJSON();
            List<Movie> parsed = parseJsonMovies(json); //parse into movie list
            if(parsed != null) movies = parsed;
        }
        catch (IOException e) { System.out.println(e.getMessage()); }
        return movies;
    };
    /******************************************************************************************************************/
    //return movies form json
    public String getMoviesFromJSON() throws IOException {
        Request request = new Request.Builder().url(endpointUrl).header("User-Agent", "http.agent").build();
        try (Response res = client.newCall(request).execute()) {
            if (!res.isSuccessful()) {
                throw new IOException("Unexpected code " + res);
            }
            return res.body() != null ?
                    res.body().string() : null;  // return JSON String
        }
    };
    /******************************************************************************************************************/
    //parse json movies
    public List<Movie> parseJsonMovies(String jsonString) {
        Gson gson = new Gson();
        List<Movie> movies = null;
        try {
            // For lists, we need to tell Gson the specific generic type (List<Movie>) - use TypeToken for this
            Type movieListType = new TypeToken<List<Movie>>(){}.getType();
            movies = gson.fromJson(jsonString, movieListType);
        } catch (JsonSyntaxException e) {
            System.err.println("Error parsing JSON array: " + e.getMessage());
        }
        return movies; // Return the list (or null if error)
    };



    //TODO: remove later
    public String getMoviesJson(String url) throws IOException {
        Request request = new Request.Builder().url(url).header("User-Agent", "http.agent").build(); // URL Request on API
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();  // return JSON String
        }
    };


}
