package at.ac.fhcampuswien.fhmdb.repositories;
import at.ac.fhcampuswien.fhmdb.database.DatabaseManager;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.MovieEntity;
import at.ac.fhcampuswien.fhmdb.models.WatchlistMovieEntity;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;
import java.util.List;


//we will use generic interface DAO to access data from our h2 db
public class MovieRepository {

    private final Dao<MovieEntity, String> movieDao;

    public MovieRepository() throws SQLException {
        this.movieDao = DaoManager.createDao(DatabaseManager.getConnectionSource(), MovieEntity.class);
    };

    //only used when saving movie list from api in db
    public void saveAll(List<Movie> movies) throws SQLException {
        for (Movie m : movies) {
            movieDao.createOrUpdate(new MovieEntity(m));
        };
    };

    //get all movies
    public List<MovieEntity> findAll() throws SQLException {
        return movieDao.queryForAll();
    };

    //find a movie by its API ID
    public MovieEntity findByApiId(String apiId) throws SQLException {
        QueryBuilder<MovieEntity, String> queryBuilder = movieDao.queryBuilder();
        queryBuilder.where().eq("id", apiId); // Assuming 'id' in MovieEntity corresponds to API ID
        return queryBuilder.queryForFirst();
    };

    //delete movie, we create tables using entities so no ON DELETE CASCADE is supported out of the box
    //unless if we were to create the tables via raw sql statements
    public void deleteMovieAndWatchlist(String movieId) throws SQLException {
        Dao<WatchlistMovieEntity, String> watchlistDao =
                DaoManager.createDao(DatabaseManager.getConnectionSource(), WatchlistMovieEntity.class);

        // Delete all watchlist entries for this movie
        List<WatchlistMovieEntity> entries = watchlistDao.queryBuilder()
                .where().eq("api_id", movieId)
                .query();

        watchlistDao.delete(entries);

        // Then delete the movie
        movieDao.deleteById(movieId);
    };

    //delete all movies
    public void deleteAll() throws SQLException {
        // First delete all entries from watchlist
        TableUtils.clearTable(DatabaseManager.getConnectionSource(), WatchlistMovieEntity.class);

        // Then delete all movies
        TableUtils.clearTable(DatabaseManager.getConnectionSource(), MovieEntity.class);
    };

};
