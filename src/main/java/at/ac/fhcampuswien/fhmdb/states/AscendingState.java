package at.ac.fhcampuswien.fhmdb.states;

public class AscendingState implements SortingState {

    private static final AscendingState INSTANCE = new AscendingState();
    private AscendingState() {}
    public static AscendingState getInstance() {
        return INSTANCE;
    }

    @Override
    public void sort(MovieSortingContext context) {
        System.out.println("STATE = ASCENDING : Sorting Descending now.");
        context.getMovies().sort(context.getComparator().reversed());
        context.setCurrentState(DescendingState.getInstance());
    }
}
