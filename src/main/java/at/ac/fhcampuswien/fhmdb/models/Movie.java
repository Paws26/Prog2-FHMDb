package at.ac.fhcampuswien.fhmdb.models;
import at.ac.fhcampuswien.fhmdb.utils.Genre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    private String id;
    private int releaseYear;
    private String imgUrl;
    private int lengthInMinutes;
    private double rating;
    private String title;
    private String description;
    private List<Genre> genres;
    private List<String> directors;
    private List<String> writers;
    private List<String> mainCast;


    public Movie(String id, int releaseYear, String imgUrl, int lengthInMinutes,
                 double rating, String title, String description, List<Genre> genres) {
        this.id = id;
        this.releaseYear = releaseYear;
        this.imgUrl = imgUrl;
        this.lengthInMinutes = lengthInMinutes;
        this.rating = rating;
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.directors = List.of("N/A");
        this.writers = List.of("N/A");
        this.mainCast = List.of("N/A");
    }
};
