package at.ac.fhcampuswien.fhmdb.states;

public class UnsortedState implements SortingState {

    private static final UnsortedState INSTANCE = new UnsortedState();
    private UnsortedState() {}
    public static UnsortedState getInstance() {
        return INSTANCE;
    }

    @Override
    public void sort(MovieSortingContext context) {
        System.out.println("STATE = UNSORTED : Sorting Ascending now.");
        context.getMovies().sort(context.getComparator());
        context.setCurrentState(AscendingState.getInstance());
    }
}
