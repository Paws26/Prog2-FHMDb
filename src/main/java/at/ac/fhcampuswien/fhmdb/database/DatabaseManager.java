package at.ac.fhcampuswien.fhmdb.database;
import at.ac.fhcampuswien.fhmdb.models.MovieEntity;
import at.ac.fhcampuswien.fhmdb.models.WatchlistMovieEntity;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import lombok.Getter;
import java.sql.SQLException;

//config jdbc database
public class DatabaseManager {

    //expose port to use over tcp on localhost
    private static final String jdbcUrl = "jdbc:h2:file:./data/fhmdbdb;AUTO_SERVER=TRUE;AUTO_SERVER_PORT=9092";
    private static final String db_user = "sa";
    private static final String password = "";

    @Getter
    private static ConnectionSource connectionSource;

    public static void init() throws SQLException {
        if(connectionSource == null) {
            connectionSource = new JdbcConnectionSource(jdbcUrl, db_user, password);

            //create the tables based on the entities if not exist
            TableUtils.createTableIfNotExists(connectionSource, MovieEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, WatchlistMovieEntity.class);
        };
    };
};
