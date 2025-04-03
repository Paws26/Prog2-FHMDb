package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.helpers.MovieDisplayHelper;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MovieStreamsTest {

    private List<Movie> movies;

    @BeforeEach
    void movies() {
        movies = new ArrayList<>();
        movies.add(new Movie("Inception", "A thief enters people's dreams to steal secrets.",
                List.of(Genre.ACTION, Genre.SCIENCE_FICTION, Genre.THRILLER)));

        movies.add(new Movie("The Godfather", "The aging patriarch of an organized crime dynasty transfers control to his reluctant son.",
                List.of(Genre.CRIME, Genre.DRAMA, Genre.THRILLER)));

        movies.add(new Movie("Forrest Gump", "The life story of a slow-witted but kind-hearted man.",
                List.of(Genre.DRAMA, Genre.ROMANCE, Genre.COMEDY)));

        movies.add(new Movie("Interstellar", "A team of explorers travels through a wormhole in space to ensure humanity's survival.",
                List.of(Genre.SCIENCE_FICTION, Genre.DRAMA, Genre.ADVENTURE)));

        movies.add(new Movie("The Dark Knight", "Batman faces his greatest challenge yet in the form of the Joker.",
                List.of(Genre.ACTION, Genre.CRIME, Genre.THRILLER)));

        movies.add(new Movie("Titanic", "A love story set aboard the ill-fated RMS Titanic.",
                List.of(Genre.ROMANCE, Genre.DRAMA, Genre.HISTORY)));
    }

    @Test
    void most_popular_actor_returns_name_of_an_actor_with_highest_occurence(){
        movies = new ArrayList<>();
        String actor1 = "Bad Pritt";
        String actor2 = "Bat Man";
        String actor3 = "Weird Al Yankovic";
        String actor4 = "This is just unbelievable. I'm so honored, touched and relieved that the Academy and the members of the Academy that have supported us have seen past the trolls and the wizards and the hobbits and are recognizing fantasy this year. Fantasy is an F-word that hopefully the five second delay won't do anything with. I just want to say a very few quick words especially to the people of New Zealand and the government of New Zealand and the city councils and everybody who supported us the length and breadth of the country--Billy Crystal is welcome to come and make a film in New Zealand any time he wants. A special thanks to Peter Nelson and Ken Kamins, who were with me right from the days of \"Bad Taste\" and \"Meet the Feebles,\" which were wisely overlooked by the Academy at that time. And I especially want to pay tribute to our wonderful producer Barrie Osborne. And I'd please like him to say a few words.";
        movies.add(new Movie("Movie with actors 1", "Description", List.of(Genre.ACTION, Genre.SCIENCE_FICTION), List.of(actor1)));
        movies.add(new Movie("Movie with actors 1, 2", "Description", List.of(Genre.ACTION, Genre.SCIENCE_FICTION), List.of(actor1, actor2)));
        movies.add(new Movie("Movie with actors 1, 2, 3", "Description", List.of(Genre.ACTION, Genre.SCIENCE_FICTION), List.of(actor1, actor2, actor3)));
        movies.add(new Movie("Movie with actors 1, 4", "Description", List.of(Genre.ACTION, Genre.SCIENCE_FICTION), List.of(actor1, actor4)));
        movies.add(new Movie("Movie with actors 2, 3, 4", "Description", List.of(Genre.ACTION, Genre.SCIENCE_FICTION), List.of(actor2, actor3, actor4)));
        movies.add(new Movie("Movie with actors 4", "Description", List.of(Genre.ACTION, Genre.SCIENCE_FICTION), List.of(actor4)));
        movies.add(new Movie("Movie with actors 4", "Description", List.of(Genre.ACTION, Genre.SCIENCE_FICTION), List.of(actor4)));
        movies.add(new Movie("Movie with actors 4", "Description", List.of(Genre.ACTION, Genre.SCIENCE_FICTION), List.of(actor4)));

        String mostCommonActor = MovieDisplayHelper.getMostPopularActor(movies);

        assertEquals(actor4, mostCommonActor, "Expected actor: " + actor4);
    }

    @Test
    void most_popular_actor_returns_null_if_no_actors_are_found() {
        movies = new ArrayList<>();
        movies.add(new Movie("Movie with actors 1", "Description", List.of(Genre.ACTION, Genre.SCIENCE_FICTION)));
        movies.add(new Movie("Movie with actors 1, 2", "Description", List.of(Genre.ACTION, Genre.SCIENCE_FICTION)));
        movies.add(new Movie("Movie with actors 1, 2, 3", "Description", List.of(Genre.ACTION, Genre.SCIENCE_FICTION)));
        movies.add(new Movie("Movie with actors 1, 4", "Description", List.of(Genre.ACTION, Genre.SCIENCE_FICTION)));
        movies.add(new Movie("Movie with actors 2, 3, 4", "Description", List.of(Genre.ACTION, Genre.SCIENCE_FICTION)));

        String mostCommonActor = MovieDisplayHelper.getMostPopularActor(movies);

        assertEquals(null, mostCommonActor, "Expected null");
    }

    @Test
    void most_popular_actor_returns_null_if_list_is_empty() {
        movies = null;

        String mostCommonActor = MovieDisplayHelper.getMostPopularActor(movies);

        assertEquals(null, mostCommonActor, "Expected null");
    }

    @Test
    void most_popular_actor_returns_null_if_list_contains_null_movies() {
        movies = new ArrayList<>();

        movies.add(null);
        movies.add(null);
        movies.add(null);

        String mostCommonActor = MovieDisplayHelper.getMostPopularActor(movies);

        assertEquals(null, mostCommonActor, "Expected null");
    }

    @Test
    void most_popular_actor_returns_null_if_actor_list_is_null() {
        movies = new ArrayList<>();
        movies.add(new Movie("Movie with actors null", "Description", List.of(Genre.ACTION, Genre.SCIENCE_FICTION), null));
        movies.add(new Movie("Movie with actors null", "Description", List.of(Genre.ACTION, Genre.SCIENCE_FICTION), null));
        movies.add(new Movie("Movie with actors null", "Description", List.of(Genre.ACTION, Genre.SCIENCE_FICTION), null));

        String mostCommonActor = MovieDisplayHelper.getMostPopularActor(movies);

        assertEquals(null, mostCommonActor, "Expected null");
    }

    @Test
    void most_popular_actor_returns_null_if_actor_list_contains_null_actors() {
        movies = new ArrayList<>();

        List<String> actorslist1 = new ArrayList<>();
        actorslist1.add(null);
        List<String> actorslist2 = new ArrayList<>();
        actorslist2.add(null);
        actorslist2.add(null);
        List<String> actorslist3 = new ArrayList<>();
        actorslist3.add(null);
        actorslist3.add(null);
        actorslist3.add(null);

        movies.add(new Movie("Movie with actors null", "Description", List.of(Genre.ACTION, Genre.SCIENCE_FICTION), actorslist1));
        movies.add(new Movie("Movie with actors null, null", "Description", List.of(Genre.ACTION, Genre.SCIENCE_FICTION), actorslist2));
        movies.add(new Movie("Movie with actors null, null, null", "Description", List.of(Genre.ACTION, Genre.SCIENCE_FICTION), actorslist3));

        String mostCommonActor = MovieDisplayHelper.getMostPopularActor(movies);

        assertEquals(null, mostCommonActor, "Expected null");
    }

    @Test
    void most_popular_actor_returns_actor2_if_actor_list_contains_some_null_actors() {
        movies = new ArrayList<>();

        String actor1 = "Bad Pritt";
        String actor2 = "Bat Man";
        String actor3 = "Weird Al Yankovic";

        List<String> actorslist1 = new ArrayList<>();
        actorslist1.add(null);
        List<String> actorslist2 = new ArrayList<>();
        actorslist2.add(actor1);
        actorslist2.add(actor2);
        actorslist2.add(null);
        List<String> actorslist3 = new ArrayList<>();
        actorslist3.add(null);
        actorslist3.add(actor2);
        actorslist3.add(actor3);

        movies.add(new Movie("Movie with actors null", "Description", List.of(Genre.ACTION, Genre.SCIENCE_FICTION), actorslist1));
        movies.add(new Movie("Movie with actors actor1, actor2, null", "Description", List.of(Genre.ACTION, Genre.SCIENCE_FICTION), actorslist2));
        movies.add(new Movie("Movie with actors null, actor2, actor3", "Description", List.of(Genre.ACTION, Genre.SCIENCE_FICTION), actorslist3));

        String mostCommonActor = MovieDisplayHelper.getMostPopularActor(movies);

        assertEquals(actor2, mostCommonActor, "Expected actor: "+ actor2);
    }

    // ----

    @Test
    void get_longest_title_returns_length_of_longest_movie_title(){
        movies = new ArrayList<>();
        String title1 = "1";
        String title2 = "22";
        String title3 = "333";
        String title4 = "4444";
        String title5 = "55555";
        String title6 = "666666";

        movies.add(new Movie("title1", "Description", List.of(Genre.ACTION, Genre.SCIENCE_FICTION)));
        movies.add(new Movie("title2", "Description", List.of(Genre.ACTION, Genre.SCIENCE_FICTION)));
        movies.add(new Movie("title3", "Description", List.of(Genre.ACTION, Genre.SCIENCE_FICTION)));
        movies.add(new Movie("title4", "Description", List.of(Genre.ACTION, Genre.SCIENCE_FICTION)));
        movies.add(new Movie("title5", "Description", List.of(Genre.ACTION, Genre.SCIENCE_FICTION)));
        movies.add(new Movie("title6", "Description", List.of(Genre.ACTION, Genre.SCIENCE_FICTION)));

        int longestMovie =  MovieDisplayHelper.getLongestMovieTitle(movies);

        assertEquals(title6.length(), longestMovie, "Expected longest movie length: " + title6.length());
    }

    //TODO: longest movie title with list being null

    //TODO: longest movie title with list not in ascending order of title-length

    //TODO: longest movie title with list containing null movies

    //TODO: longest movie title with list containing movies with null titles
}
