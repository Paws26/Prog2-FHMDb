package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.helpers.MovieDisplayHelper;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
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
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
    public JFXListView movieListView;

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

    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    private Image hamburgerIcon;
    private Image closeIcon;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // *** Initialize UI ***
        
        // - Movie-list view -
        movieListView.setItems(observableMovies);   // set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data

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

    public void goHome(ActionEvent actionEvent) throws IOException {
        Parent watchlistRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home-view.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(watchlistRoot));
        stage.show();
    }

    public void goWatchlist(ActionEvent actionEvent) throws IOException {
        Parent watchlistRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("watchlist-view.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(watchlistRoot));
        stage.show();
    }

    public void goAbout(ActionEvent actionEvent) {
    }
}