package at.ac.fhcampuswien.fhmdb.api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class MovieAPI {
    private final OkHttpClient client = new OkHttpClient();

    public String getMovies(String url) throws IOException {
        Request request = new Request.Builder().url(url).header("User-Agent", "http.agent").build(); // URL Request an API

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();  // JSON String
        }
    }
}
