package at.ac.fhcampuswien.fhmdb.models;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import lombok.NoArgsConstructor;


//map entity to watchlist_movies table
@Data
@NoArgsConstructor
@DatabaseTable(tableName = "watchlist_movies")
public class WatchlistMovieEntity {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(canBeNull = false, unique = true, columnName = "apiId")
    private String apiId;  // This references MovieEntity's id


    public WatchlistMovieEntity(MovieEntity movie) {
        if (movie == null || movie.getId() == null) {
            throw new IllegalArgumentException("MovieEntity and its ID must not be null");
        }
        this.apiId = movie.getId();
    };
};