package at.ac.fhcampuswien.fhmdb.api;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;


public class MovieAPIRequestBuilder {

    private final String baseUrl;
    private String query;
    private Genre genre;
    private String releaseYear;
    private String ratingFrom;

    public MovieAPIRequestBuilder(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public MovieAPIRequestBuilder query(String query) {
        this.query = query;
        return this;
    }

    public MovieAPIRequestBuilder genre(Genre genre) {
        this.genre = genre;
        return this;
    }

    public MovieAPIRequestBuilder releaseYear(int year) {
        this.releaseYear = Integer.toString(year);
        return this;
    }

    public MovieAPIRequestBuilder ratingFrom(double rating) {
        this.ratingFrom = String.format(Locale.ROOT, "%.1f", rating);
        return this;
    }

    public String build() {
        StringBuilder sb = new StringBuilder(baseUrl);
        StringBuilder queryPart = new StringBuilder();

        if (query != null && !query.isBlank()) {
            appendParam(queryPart, "query", query);
        }
        if (genre != null) {
            appendParam(queryPart, "genre", genre.name());
        }
        if (releaseYear != null && !releaseYear.isBlank()) {
            appendParam(queryPart, "releaseYear", releaseYear);
        }
        if (ratingFrom != null && !ratingFrom.isBlank()) {
            appendParam(queryPart, "ratingFrom", ratingFrom);
        }

        if (queryPart.length() > 0) {
            sb.append("?").append(queryPart);
        }

        return sb.toString();
    }

    private void appendParam(StringBuilder sb, String key, String value) {
        if (sb.length() > 0) {
            sb.append("&");
        }
        sb.append(URLEncoder.encode(key, StandardCharsets.UTF_8));
        sb.append("=");
        sb.append(URLEncoder.encode(value, StandardCharsets.UTF_8));
    }
}


