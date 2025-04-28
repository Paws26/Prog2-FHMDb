package at.ac.fhcampuswien.fhmdb.api;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;



//fetch movies from h2 database
public class MovieRepo {

//    @Getter
    private List<Movie> movies;

    //get singleton instance and read movies from db
    Connection conn = DatabaseConf.getInstance().getConnection();

    public MovieRepo() {
        if (conn != null) {
            try {
                //read from database
                this.movies = DatabaseQueries.fetchAllMovies(conn);
                System.out.println("Movies fetched successfully");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                this.movies = Collections.emptyList();
            };
        };
    };

    public List<Movie> getMovies (){ return this.movies; }


    /* More methods here */
};


