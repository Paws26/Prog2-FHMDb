package at.ac.fhcampuswien.fhmdb;
import at.ac.fhcampuswien.fhmdb.database.DatabaseException;
import at.ac.fhcampuswien.fhmdb.factory.ControllerFactory;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.repositories.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.ui.WatchlistCell;
import at.ac.fhcampuswien.fhmdb.utils.ClickEventHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;


public class WatchlistController implements Initializable {

    @FXML
    private BorderPane borderPane;

    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView watchlistListView;

    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXComboBox<Integer> yearComboBox;

    @FXML
    public Spinner<Double> ratingSpinner;

    @FXML
    public JFXButton sortBtn;

    @FXML
    public JFXButton menuBtn;

    @FXML
    private ImageView menuIcon;

    @FXML
    private VBox sidebar;


    // automatically updates corresponding UI elements when underlying data changes
    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

    private Image hamburgerIcon;
    private Image closeIcon;


    private final ClickEventHandler<Movie> onRemoveFromWatchlistClicked = (clickedItem) -> {
        try {
            WatchlistRepository watchlistRepo = new WatchlistRepository();
            watchlistRepo.removeFromWatchlist(clickedItem.getId());
            loadWatchlist(); // Refresh the list
        } catch (DatabaseException e) {
            showAlert("Error", "Failed to remove from watchlist: " + e.getMessage());
        }
    };


    @Getter
    private static WatchlistController instance;


    private WatchlistController() {
        loadWatchlist(); // Only after instance is assigned
    }


    public static synchronized WatchlistController getInstance() {
        if (instance == null) {
            instance = new WatchlistController();
        }
        return instance;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // *** Initialize UI ***

        // - Movie-list view -
        watchlistListView.setItems(observableMovies);   // set data of observable list to list view
        watchlistListView.setCellFactory(movieListView -> new WatchlistCell(onRemoveFromWatchlistClicked)); // use custom cell factory to display data

        // - Menu Icons -
        hamburgerIcon = new Image(
                Objects.requireNonNull(getClass().getResource("/Icons/hamburger-menu.png"))
                        .toExternalForm()
        );
        closeIcon = new Image(
                Objects.requireNonNull(getClass().getResource("/Icons/close-menu.png"))
                        .toExternalForm()
        );

        // - Menu Button -
        menuBtn.setText("");
        menuIcon.setImage(hamburgerIcon);

        // - Sidebar -
        borderPane.setRight(null);
        sidebar.setManaged(false);
        sidebar.setVisible(false);

        // *** Actions ***

        // - Menu Button -
        menuBtn.setOnAction(actionEvent -> {
            boolean sidebarStatus = borderPane.getRight() == null;

            if (sidebarStatus) {
                borderPane.setRight(sidebar);
                sidebar.setVisible(true);
                sidebar.setManaged(true);
                menuIcon.setImage(closeIcon);
            } else {
                borderPane.setRight(null);
                sidebar.setVisible(false);
                sidebar.setManaged(false);
                menuIcon.setImage(hamburgerIcon);
            }
        });
    }

    private void loadWatchlist() {
        try {
            List<Movie> watchlistMovies = new WatchlistRepository().getWatchlistMovies();
            observableMovies.setAll(watchlistMovies);
        } catch (DatabaseException e) {
            showAlert("Error", "Failed to load watchlist: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    ;


    //navigate to home
    public void goHome(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
        loader.setControllerFactory(new ControllerFactory());
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    };

    //navigate to watchlist
    public void goWatchlist(ActionEvent actionEvent) throws IOException {

        // Get existing instance if available
//        WatchlistController controller = WatchlistController.getInstance();
//
//        //Parent watchlistRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("watchlist-view.fxml")));
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("watchlist-view.fxml"));
//        loader.setController(controller); // Set the existing instance as controller
//
//        Parent watchlistRoot = loader.load();
//        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
//        stage.setScene(new Scene(watchlistRoot));
//        stage.show();
    };

    //navigate to about
    public void goAbout(ActionEvent actionEvent) {
    };
};