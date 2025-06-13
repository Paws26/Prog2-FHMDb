package at.ac.fhcampuswien.fhmdb;
import at.ac.fhcampuswien.fhmdb.database.DatabaseException;
import at.ac.fhcampuswien.fhmdb.database.DatabaseManager;
import at.ac.fhcampuswien.fhmdb.factory.ControllerFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;


public class FhmdbApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        // Initialize database first
        try {
            DatabaseManager.init();
        } catch (DatabaseException e) {
            e.printStackTrace();
            // Show error alert to user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Failed to initialize database");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return; // Exit if database fails
        };

        //FXMLLoader = new FXMLLoader(FhmdbApplication.class.getResource("home-view.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(FhmdbApplication.class.getResource("home-view.fxml"));
        fxmlLoader.setControllerFactory(new ControllerFactory());
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 1280, 800);
        scene.getStylesheets().add(Objects.requireNonNull(FhmdbApplication.class.getResource("styles.css")).toExternalForm());
        stage.setTitle("FHMDb");
        stage.setScene(scene);
        stage.show();
    };

    public static void main(String[] args) {
        launch();
    }
};