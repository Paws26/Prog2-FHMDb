package at.ac.fhcampuswien.fhmdb.factory;
import at.ac.fhcampuswien.fhmdb.HomeController;
import at.ac.fhcampuswien.fhmdb.WatchlistController;
import javafx.util.Callback;

// ControllerFactory.java
public class ControllerFactory implements Callback<Class<?>, Object> {
    private static HomeController homeController;
    private static WatchlistController watchlistController;

    @Override
    public Object call(Class<?> clazz) {
        try {
            if (clazz == HomeController.class) {
                if (homeController == null) {
                    homeController = new HomeController();
                    //WatchlistRepository.getInstance().addObserver(homeController);
                }
                return homeController;
            }

            if (clazz == WatchlistController.class) {
                return WatchlistController.getInstance();
            }
            return clazz.getDeclaredConstructor().newInstance();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
