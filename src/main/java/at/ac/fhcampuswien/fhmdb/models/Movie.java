package at.ac.fhcampuswien.fhmdb.models;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


//@Data
public class Movie {

    private String id;
    private int releaseYear;
    private String imgUrl;
    private int lengthInMinutes;
    private List<String> directors;
    private List<String> writers;
    private List<String> mainCast;
    private double rating;

    private String title;
    private String description;
    private List<Genre> genres;

    public void setId(String id) {
        this.id = id;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setLengthInMinutes(int lengthInMinutes) {
        this.lengthInMinutes = lengthInMinutes;
    }

    public void setDirectors(List<String> directors) {
        this.directors = directors;
    }

    public void setWriters(List<String> writers) {
        this.writers = writers;
    }

    public void setMainCast(List<String> mainCast) {
        this.mainCast = mainCast;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
// TODO add more properties here

    public Movie(String title, String description, List<Genre> genres) {
        this.title = title;
        this.description = description;
        this.genres = new ArrayList<>(genres);
    }

    public Movie(String title, String description, List<Genre> genres, List<String> mainCast) {
        this.title = title;
        this.description = description;
        this.genres = new ArrayList<>(genres);
        this.mainCast = mainCast;
    }

    //TODO: remove
    public boolean containsDirector(Movie movie, String director) {
        List<String> movieDirectors = movie.getDirectors();
        // Check if the movie's director list is not null AND contains the target director
        return movieDirectors != null && movieDirectors.contains(director);
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Genre> getGenres() {
        return List.copyOf(genres);
    }

    public String getId() {
        return id;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getLengthInMinutes() {
        return lengthInMinutes;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public List<String> getWriters() {
        return writers;
    }

    public List<String> getMainCast() {
        return mainCast;
    }

    public double getRating() {
        return rating;
    }

//    public static List<Movie> initializeMovies() {
//        List<Movie> movies = new ArrayList<>();
//        //movies.add(new Movie("title", "description", List.of(Genre.ACTION, Genre.DRAMA, Genre.DOCUMENTARY)));
//        movies.add(new Movie("Inception", "A thief enters people's dreams to steal secrets.",
//                List.of(Genre.ACTION, Genre.SCIENCE_FICTION, Genre.THRILLER)));
//
//        movies.add(new Movie("The Godfather", "The aging patriarch of an organized crime dynasty transfers control to his reluctant son.",
//                List.of(Genre.CRIME, Genre.DRAMA, Genre.THRILLER)));
//
//        movies.add(new Movie("Forrest Gump", "The life story of a slow-witted but kind-hearted man.",
//                List.of(Genre.DRAMA, Genre.ROMANCE, Genre.COMEDY)));
//
//        movies.add(new Movie("Interstellar", "A team of explorers travels through a wormhole in space to ensure humanity's survival.",
//                List.of(Genre.SCIENCE_FICTION, Genre.DRAMA, Genre.ADVENTURE)));
//
//        movies.add(new Movie("The Dark Knight", "Batman faces his greatest challenge yet in the form of the Joker.",
//                List.of(Genre.ACTION, Genre.CRIME, Genre.THRILLER)));
//
//        movies.add(new Movie("Titanic", "A love story set aboard the ill-fated RMS Titanic.",
//                List.of(Genre.ROMANCE, Genre.DRAMA, Genre.HISTORY)));
//        // TODO add some dummy data here
//
//        return movies;
//    };


    //visual representation
    @Override
    public String toString(){
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", releaseYear=" + releaseYear +
                ", lengthInMinutes=" + lengthInMinutes +
                ", rating=" + rating +
                ", imgUrl='" + imgUrl + '\'' +
                ", directors=" + directors +
                ", writers=" + writers +
                ", mainCast=" + mainCast +
                ", genres=" + genres +
                '}';
    };

}
