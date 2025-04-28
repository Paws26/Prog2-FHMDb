package at.ac.fhcampuswien.fhmdb.api;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import lombok.Getter;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


//config jdbc database
public class DatabaseConf {

    //expose port to use over tcp on localhost
    private static final String jdbcUrl = "jdbc:h2:file:./data/movies_db;AUTO_SERVER=TRUE;AUTO_SERVER_PORT=9092";
    private static final String db_user = "sa";
    private static final String password = "";

    // singleton instance
    private static DatabaseConf INSTANCE;

    @Getter
    private final Connection connection;

    //Private constructor so nobody else can new-up this class.
    //It opens (or creates) the H2 database and sets up the table.
    private DatabaseConf() {
        try {
            Class.forName("org.h2.Driver");
            this.connection = DriverManager.getConnection(jdbcUrl, db_user, password);;

            //ensure table exists
            DatabaseQueries.createTableIfNotExists(connection);

            //fetch movies from api
            List<Movie> apiMovies = new MovieAPI().fetMovieList();

            //save in db
            DatabaseQueries.saveMovies(apiMovies, connection);


            //TODO: close db connection if not used

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        };
    };

    //Thread-safe lazy-init accessor for the singleton.
    public static synchronized DatabaseConf getInstance() {
        if (INSTANCE == null) INSTANCE = new DatabaseConf();
        return INSTANCE;
    };

};
