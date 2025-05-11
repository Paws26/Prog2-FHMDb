package at.ac.fhcampuswien.fhmdb.repositories;

import at.ac.fhcampuswien.fhmdb.database.DatabaseException;
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
import java.util.Objects;
import java.util.stream.Collectors;


//same for watchlist repository
public class WatchlistRepository {

    private final Dao<WatchlistMovieEntity, Long> watchlistDao;

    public WatchlistRepository() throws DatabaseException {
        try {
            this.watchlistDao = DaoManager.createDao(
                    DatabaseManager.getConnectionSource(),
                    WatchlistMovieEntity.class
            );
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    ;

    //add a movie to watchlist
    public boolean addToWatchlist(MovieEntity movie) throws DatabaseException {
        try {
            if (movie == null || movie.getId() == null) {
                throw new IllegalArgumentException("MovieEntity and ID must not be null");
            }

            // Check if already exists
            if (getByApiId(movie.getId()) != null) {
                return false; // Already exists
            }

            // Create returns 1 if successful
            int created = watchlistDao.create(new WatchlistMovieEntity(movie));
            return created == 1;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    ;

    //if exists in watchlist
    public boolean existsInWatchlist(String apiId) throws DatabaseException {
        try {
            QueryBuilder<WatchlistMovieEntity, Long> qb = watchlistDao.queryBuilder();
            qb.setCountOf(true); // Add this line
            qb.where().eq("apiId", apiId);
            return watchlistDao.countOf(qb.prepare()) > 0;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    ;


    //remove from watchlist
    public int removeFromWatchlist(String apiId) throws DatabaseException {
        try {
            WatchlistMovieEntity item = getByApiId(apiId);
            if (item != null) {
                return watchlistDao.delete(item);
            }
            return 0;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    ;

    //get all movies from watchlist
    public List<Movie> getWatchlistMovies() throws DatabaseException {
        try {
            MovieRepository movieRepo = new MovieRepository();
            return watchlistDao.queryForAll().stream()
                    .map(watchlistItem -> {
                        try {
                            return movieRepo.findByApiId(watchlistItem.getApiId()).toMovie();
                        } catch (DatabaseException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    ;

    //clear watchlist
    public void clear() throws DatabaseException {
        try {
            TableUtils.clearTable(DatabaseManager.getConnectionSource(), WatchlistMovieEntity.class);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    ;

    // Check if movie exists in watchlist
    private WatchlistMovieEntity getByApiId(String apiId) throws DatabaseException {
        try {
            QueryBuilder<WatchlistMovieEntity, Long> qb = watchlistDao.queryBuilder();
            qb.where().eq("apiId", apiId);
            return qb.queryForFirst();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
};

