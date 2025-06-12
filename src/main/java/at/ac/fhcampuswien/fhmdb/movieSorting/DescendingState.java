package at.ac.fhcampuswien.fhmdb.movieSorting;

public class DescendingState implements SortingState {

    private static final DescendingState INSTANCE = new DescendingState();
    private DescendingState() {}
    public static DescendingState getInstance() {
        return INSTANCE;
    }

    @Override
    public void sort(MovieSortingContext context) {
        System.out.println("STATE = DESCENDING : Sorting Ascending now.");
        context.getMovies().sort(context.getComparator());
        context.setCurrentState(AscendingState.getInstance());
    }
}
