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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
    public JFXComboBox<Integer> yearComboBox;

    @FXML
    public Spinner<Double> ratingSpinner;

    @FXML
    public JFXButton sortBtn;

    public List<Movie> allMovies = new ArrayList<>() {
    };

    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    private boolean isAscending = true; //track the current sorting order

    public static final int NO_YEAR_FILTER = -1;

    private String initialUrl = "https://prog2.fh-campuswien.ac.at/movies"; // Initial URL

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MovieAPI movieAPI = new MovieAPI();

        // Get all movies from api and add them to ObservablesList observableMovies
        try {
            String json = movieAPI.getMoviesJson(initialUrl);
            allMovies = movieAPI.parseJsonMovies(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        observableMovies.addAll(allMovies);

        // initialize UI stuff
        movieListView.setItems(observableMovies);   // set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data

        // Genre UI
        genreComboBox.setPromptText("Filter by Genre");
        genreComboBox.getItems().addAll(Genre.values());

        // Release Year UI
        updateYearComboBox();
        yearComboBox.getSelectionModel().selectFirst();

        // Rating UI
        SpinnerValueFactory<Double> ratingValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 10.0, 0.0, 0.1);
        ratingSpinner.setValueFactory(ratingValueFactory);
        ratingSpinner.getEditor().setText("");
        ratingSpinner.getEditor().setPromptText("Rating");


        // Sorting Button
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

        // Filter/Search Button
        searchBtn.setOnAction(actionEvent -> {
            String query = searchField.getText(); // Get search query from searchField
            Genre genre = (Genre) genreComboBox.getValue(); // Get genre from genreComboBox
            Integer releaseYear = yearComboBox.getValue(); // Get release year from yearComboBox
            Double rating = ratingSpinner.getValue(); // Get rating from ratingSpinner

            // Build API Url using queried values
            String queriedUrl = movieAPI.buildApiURL(initialUrl, query, genre, releaseYear, rating);

            try {
                // Update observable list with filtered Movies from API response
                observableMovies.setAll(movieAPI.parseJsonMovies(movieAPI.getMoviesJson(queriedUrl)));

                updateYearComboBox();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Rating Spinner
        ratingSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                ratingSpinner.getValueFactory().setValue(oldValue);
            }
        });
    }

    private void updateYearComboBox() {
        List<Integer> filteredYears = MovieDisplayHelper.getDistinctReleaseYears(observableMovies);
        if (!filteredYears.contains(NO_YEAR_FILTER)) {
            filteredYears.add(0, NO_YEAR_FILTER);  // Add NO_YEAR_FILTER = -1 at the beginning
        }
        yearComboBox.setItems(FXCollections.observableArrayList(filteredYears));

        // Convert NO_YEAR_FILTER (-1) to display "Any Year" instead (I hate Integer and String incompatibility in Lists)
        yearComboBox.setConverter(new StringConverter<Integer>() {
            @Override
            public String toString(Integer year) {
                return (year == null || year == NO_YEAR_FILTER) ? "Any Year" : year.toString();
            }

            @Override
            public Integer fromString(String string) {
                if ("Any Year".equals(string)) {
                    return NO_YEAR_FILTER;
                }
                try {
                    return Integer.valueOf(string);
                } catch (NumberFormatException e) {
                    return NO_YEAR_FILTER;
                }
            }
        });

        if (yearComboBox.getValue() == null) {
            yearComboBox.setValue(NO_YEAR_FILTER);
        }
    }
}