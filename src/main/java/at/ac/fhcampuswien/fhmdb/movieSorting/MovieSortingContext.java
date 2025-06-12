package at.ac.fhcampuswien.fhmdb.movieSorting;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;

public class MovieSortingContext {
    @Getter
    private final List<Movie> movies;
    private SortingState currentState;
    @Getter
    private final Comparator<Movie> comparator;

    public MovieSortingContext(List<Movie> movies, Comparator<Movie> comparator) {
        this.movies = movies;
        this.currentState = UnsortedState.getInstance();
        this.comparator = comparator;
    }

    void setCurrentState(SortingState newState) {
        this.currentState = newState;
    }

    public void toggleSorting() {
        currentState.sort(this);
    }

    public String getStateName() {
        return currentState.getClass().getSimpleName();
    }

    public boolean isAscending() {
        return currentState instanceof AscendingState;
    }

    public boolean isDescending() {
        return currentState instanceof DescendingState;
    }

    public void applyCurrentSorting() {
        if (isAscending()) {
            setCurrentState(UnsortedState.getInstance());
            toggleSorting();
        } else if (isDescending()) {
            setCurrentState(AscendingState.getInstance());
            toggleSorting();
        }
    }
}
