package at.ac.fhcampuswien.fhmdb.database;
import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.MovieEntity;
import at.ac.fhcampuswien.fhmdb.repositories.MovieRepository;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


//fetch and save the data from api in database
public class MovieDataInitializer {

    public static List<Movie> loadAndCacheMovies() {
        try {
            // 1. Try fetch from API
            MovieAPI api = new MovieAPI();
            List<Movie> moviesFromApi = api.fetMovieList();

            // 2. Cache to DB
            MovieRepository repo = new MovieRepository();
            repo.saveAll(moviesFromApi);
            System.out.println("✅ Movies fetched and cached from API.");

            // 3. Now return movies from DB
            return repo.findAll().stream()
                    .map(MovieEntity::toMovie)
                    .collect(Collectors.toList());

        } catch (Exception apiOrSaveError) {
            System.err.println("⚠️ Failed to fetch/cache API movies: " + apiOrSaveError.getMessage());

            // 4. Fallback: load from DB
            try {
                MovieRepository repo = new MovieRepository();
                List<MovieEntity> cached = repo.findAll();

                System.out.println("✅ Fallback: loaded movies from DB.");
                return cached.stream().map(MovieEntity::toMovie).collect(Collectors.toList());

            } catch (Exception dbError) {
                System.err.println("❌ DB fallback failed: " + dbError.getMessage());
                return Collections.emptyList();
            }
        }
    };
}

