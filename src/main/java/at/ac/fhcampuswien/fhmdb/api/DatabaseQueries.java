package at.ac.fhcampuswien.fhmdb.api;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;



//Queries against db
public class DatabaseQueries {

    // Inserts or updates each movie in the database
    public static void saveMovies(List<Movie> movies, Connection conn) throws SQLException {
        if (movies == null || movies.isEmpty()) return;

        String mergeSql = "MERGE INTO movies (id, title, description, release_year, " +
                "length_in_minutes, rating, img_url, directors, writers, main_cast, genres) "
                + "KEY(title) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(mergeSql)) {
            conn.setAutoCommit(false);
            for (Movie m : movies) {
                ps.setString(1, m.getId());
                ps.setString(2, m.getTitle());
                ps.setString(3, m.getDescription());
                ps.setInt(4, m.getReleaseYear());
                ps.setInt(5, m.getLengthInMinutes());
                ps.setDouble(6, m.getRating());
                ps.setString(7, m.getImgUrl());
                ps.setString(8, String.join(",", m.getDirectors()));
                ps.setString(9, String.join(",", m.getWriters()));
                ps.setString(10, String.join(",", m.getMainCast()));
                ps.setString(11, m.getGenres().stream().map(Enum::name).collect(Collectors.joining(",")));
                ps.addBatch();
            };
            ps.executeBatch();
            conn.commit(); //Commit transaction
            System.out.println("Movie saved");
        } catch (SQLException e) {
            conn.rollback(); // Rollback on error
            System.out.println(e.getMessage());
            throw e;
        }
    };


    // Create SQL table if not exists with camelCase column names
    public static void createTableIfNotExists(Connection conn) {
        String sql =
                "CREATE TABLE IF NOT EXISTS movies (" +
                        "  id VARCHAR(36) PRIMARY KEY, " +
                        "  title VARCHAR(255) NOT NULL, " +
                        "  description CLOB, " +
                        "  release_year INT, " +
                        "  length_in_minutes INT, " +
                        "  rating DOUBLE, " +
                        "  img_url VARCHAR(512), " +
                        "  directors CLOB, " +
                        "  writers CLOB, " +
                        "  main_cast CLOB, " +
                        "  genres CLOB" +
                        ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error when creating table: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Read all movie list from database
    public static List<Movie> fetchAllMovies(Connection conn) throws SQLException {
        String sql = "SELECT id, title, description, release_year, length_in_minutes, rating, img_url, " +
                "directors, writers, main_cast, genres FROM movies";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            List<Movie> result = new ArrayList<>();
            while (rs.next()) {
                Movie m = new Movie(
                        rs.getString("title"),
                        rs.getString("description"),
                        Arrays.stream(rs.getString("genres").split(","))
                                .map(Genre::valueOf)
                                .collect(Collectors.toList())
                );
                m.setId(rs.getString("id"));
                m.setReleaseYear(rs.getInt("release_year"));
                m.setLengthInMinutes(rs.getInt("length_in_minutes"));
                m.setRating(rs.getDouble("rating"));
                m.setImgUrl(rs.getString("img_url"));
                m.setDirectors(Arrays.asList(rs.getString("directors").split(",")));
                m.setWriters(Arrays.asList(rs.getString("writers").split(",")));
                m.setMainCast(Arrays.asList(rs.getString("main_cast").split(",")));
                result.add(m);
            }
            return result;
        }
    };
}