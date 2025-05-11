package at.ac.fhcampuswien.fhmdb.database;
import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.api.MovieAPIException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.MovieEntity;
import at.ac.fhcampuswien.fhmdb.repositories.MovieRepository;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


//fetch and save the data from api in database
public class MovieDataInitializer {

    public static List<Movie> loadAndCacheMovies() throws DatabaseException {
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

        } catch(MovieAPIException movieAPIException){
            // 4. Fallback: load from DB
            try {
                MovieRepository repo = new MovieRepository();
                List<MovieEntity> cached = repo.findAll();

                System.out.println("✅ Fallback: loaded movies from DB.");
                return cached.stream().map(MovieEntity::toMovie).collect(Collectors.toList());

            } catch (DatabaseException dbError) {
                System.err.println("❌ DB fallback failed: " + dbError.getMessage());
                throw dbError;
            }

        }
        catch (DatabaseException ex) {
            System.err.println("⚠️ Failed to cache API movies: " + ex.getMessage());
            throw ex;
        }
    }
}

