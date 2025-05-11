package at.ac.fhcampuswien.fhmdb.models;
import at.ac.fhcampuswien.fhmdb.utils.Genre;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Arrays;
import java.util.stream.Collectors;


//entity to map to movies table
@DatabaseTable(tableName = "movies")
@Data
@NoArgsConstructor
public class MovieEntity {

    @DatabaseField(id = true)
    private String id;

    @DatabaseField(canBeNull = false)
    private String title;

    @DatabaseField
    private String description;

    @DatabaseField
    private int releaseYear;

    @DatabaseField
    private int lengthInMinutes;

    @DatabaseField
    private double rating;

    @DatabaseField
    private String imgUrl;

    @DatabaseField
    private String genres;

    @DatabaseField
    private String directors;

    @DatabaseField
    private String writers;

    @DatabaseField
    private String mainCast;

    public MovieEntity(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.description = movie.getDescription();
        this.releaseYear = movie.getReleaseYear();
        this.lengthInMinutes = movie.getLengthInMinutes();
        this.rating = movie.getRating();
        this.imgUrl = movie.getImgUrl();
        this.genres = movie.getGenres().stream().map(Enum::name).collect(Collectors.joining(","));
        this.directors = String.join(",", movie.getDirectors());
        this.writers = String.join(",", movie.getWriters());
        this.mainCast = String.join(",", movie.getMainCast());
    };

    //map movie entity to a movie object
    public Movie toMovie() {
        return new Movie(
                id,
                releaseYear,
                imgUrl,
                lengthInMinutes,
                rating,
                title,
                description,
                Arrays.stream(genres.split(","))
                        .map(Genre::valueOf)
                        .collect(Collectors.toList()),
                Arrays.asList(directors.split(",")),
                Arrays.asList(writers.split(",")),
                Arrays.asList(mainCast.split(","))
        );
    };
};
