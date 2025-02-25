package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.helpers.MovieDisplayHelper;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXButton sortBtn;

    public List<Movie> allMovies = Movie.initializeMovies();

    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    private boolean isAscending = true; //track the current sorting order

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableMovies.addAll(allMovies);         // add dummy data to observable list

        // initialize UI stuff
        movieListView.setItems(observableMovies);   // set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data

        genreComboBox.setPromptText("Filter by Genre");
        genreComboBox.getItems().addAll(Genre.values());

        sortBtn.setOnAction(actionEvent -> {
            if (isAscending) {
                // Sort Movies alphabetically ascending
                List<Movie> sortedMovies = MovieDisplayHelper.sortMoviesAscending(observableMovies);
                observableMovies.clear();
                observableMovies.addAll(sortedMovies);
                sortBtn.setText("Sort (desc)");
            } else {
                // Sort Movies alphabetically descending
                List<Movie> sortedMovies = MovieDisplayHelper.sortMoviesDescending(observableMovies);
                observableMovies.clear();
                observableMovies.addAll(sortedMovies);
                sortBtn.setText("Sort (asc)");
            }

            isAscending = !isAscending; // Toggle sorting order
        });

        searchBtn.setOnAction(actionEvent -> {
            String query = searchField.getText(); // Get search query from searchField
            Genre genre = (Genre) genreComboBox.getValue(); // Get genre from genreComboBox

            List<Movie> filteredMovies = MovieDisplayHelper.filterMovies(allMovies, query, genre);

            observableMovies.setAll(filteredMovies);
        });


    }
}