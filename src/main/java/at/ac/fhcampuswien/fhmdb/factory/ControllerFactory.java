package at.ac.fhcampuswien.fhmdb.factory;
import at.ac.fhcampuswien.fhmdb.HomeController;
import at.ac.fhcampuswien.fhmdb.WatchlistController;
import javafx.util.Callback;

public class ControllerFactory implements Callback<Class<?>, Object> {

    @Override
    public Object call(Class<?> clazz) {
        if (clazz == HomeController.class) {
            return HomeController.getInstanceOrCreate();
        }

        if (clazz == WatchlistController.class) {
            return WatchlistController.getInstanceOrCreate();
        }

        // Default fallback for other FXML elements
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
