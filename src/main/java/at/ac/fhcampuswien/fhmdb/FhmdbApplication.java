package at.ac.fhcampuswien.fhmdb;
import at.ac.fhcampuswien.fhmdb.database.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;


public class FhmdbApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        // Initialize database first
        try {
            DatabaseManager.init();
        } catch (SQLException e) {
            e.printStackTrace();
            // Show error alert to user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Failed to initialize database");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return; // Exit if database fails
        };

        FXMLLoader fxmlLoader = new FXMLLoader(FhmdbApplication.class.getResource("home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 800);
        scene.getStylesheets().add(Objects.requireNonNull(FhmdbApplication.class.getResource("styles.css")).toExternalForm());
        stage.setTitle("FHMDb");
        stage.setScene(scene);
        stage.show();
    };

    public static void main(String[] args) {
        launch();
    }
};