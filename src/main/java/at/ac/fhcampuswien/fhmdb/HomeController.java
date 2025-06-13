package at.ac.fhcampuswien.fhmdb;
import at.ac.fhcampuswien.fhmdb.database.MovieDataInitializer;
import at.ac.fhcampuswien.fhmdb.factory.ControllerFactory;
import at.ac.fhcampuswien.fhmdb.helpers.MovieDisplayHelper;
import at.ac.fhcampuswien.fhmdb.models.MovieEntity;
import at.ac.fhcampuswien.fhmdb.states.MovieSortingContext;
import at.ac.fhcampuswien.fhmdb.repositories.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.Getter;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


//home controller
public class HomeController implements Initializable {

    @FXML
    private BorderPane borderPane;
    @FXML
    public JFXButton filterBtn;
    @FXML
    public TextField searchField;
    @FXML
    public JFXListView<Movie> movieListView;
    @FXML
    public JFXComboBox<Genre> genreComboBox;
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

    @Getter
    private static HomeController instance;

//
//    private HomeController() {
//        if (instance != null) throw new IllegalStateException("Only one instance allowed!");
//        instance = this;
//    }
//
//    public static HomeController getInstanceOrCreate() {
//        if (instance == null) {
//            instance = new HomeController();
//        }
//        return instance;
//    }

    // automatically updates corresponding UI elements when underlying data changes
    private final ObservableList<Movie> observableMovies =
            FXCollections.observableArrayList();
    MovieSortingContext sortingContext;
    Comparator<Movie> comparatorTitle;

    public static final int NO_YEAR_FILTER = -1;
    private Image hamburgerIcon;
    private Image closeIcon;

    private final ClickEventHandler<Movie> onAddToWatchlistClicked = (clickedItem) ->
    {
        try {
            // Convert Movie to MovieEntity if needed
            MovieEntity movieEntity = new MovieEntity(clickedItem);

            // Add to watchlist repository
            boolean added = new WatchlistRepository().addToWatchlist(movieEntity);

            if (added) {
                showAlert("Success", "Movie added to watchlist!");
            } else {
                System.out.println("Movie was already in watchlist");
            }

        } catch (Exception e) {
            showAlert("Error", "Failed to add movie to watchlist: " + e.getMessage());

        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Initialize movie data
            List<Movie> movies = MovieDataInitializer.loadAndCacheMovies();
            observableMovies.addAll(movies);
            comparatorTitle = Comparator.comparing(Movie::getTitle);
            sortingContext = new MovieSortingContext(observableMovies, comparatorTitle);

            // Set up list view
            movieListView.setItems(observableMovies);
            //use custom cell factory to display data, init with the watchlist click handler
            movieListView.setCellFactory(movieListView
                    -> new MovieCell(onAddToWatchlistClicked));

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

            // -- Genre ComboBox --
            genreComboBox.setPromptText("Filter by Genre");
            genreComboBox.getItems().addAll(Genre.values());

            // - Release Year ComboBox -
            updateYearComboBox();
            yearComboBox.getSelectionModel().selectFirst();

            // - Rating Spinner -
            SpinnerValueFactory<Double> ratingValueFactory =
                    new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 10.0, 0.0, 0.1);
            ratingSpinner.setValueFactory(ratingValueFactory);
            ratingSpinner.getEditor().setText("");
            ratingSpinner.getEditor().setPromptText("Rating");

            // Set up button actions
            setupButtonActions();
        } catch (Exception e) {
            showAlert("Initialization Error", e.getMessage());
        }
    };

    //button actions
    private void setupButtonActions() {
        //- Menu Button -
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

        //sort button
        sortBtn.setOnAction(actionEvent -> {
            sortingContext.toggleSorting();
            sortBtn.setText(sortingContext.isAscending() ? "Sort (desc)" : "Sort (asc)");
        });

        // Filter button
        filterBtn.setOnAction(evt -> applyFilters());

        //- Rating Spinner -
        ratingSpinner.valueProperty().addListener(
                (observable,
                 oldValue, newValue) -> {
                    if (newValue == null) {
                        ratingSpinner.getValueFactory().setValue(oldValue);
                    }
                });
    }

    ;

    //update combo year box
    private void updateYearComboBox() {
        List<Integer> filteredYears = MovieDisplayHelper.getDistinctReleaseYears(observableMovies);
        if (!filteredYears.contains(NO_YEAR_FILTER)) {
            filteredYears.addFirst(NO_YEAR_FILTER);  // Add NO_YEAR_FILTER = -1 at the beginning
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

    ;

    //apply filters
    private void applyFilters() {
        try {
            // Get filter values
            String query = searchField.getText().trim();
            Genre genre = genreComboBox.getValue();
            Integer year = yearComboBox.getValue() == NO_YEAR_FILTER ? null : yearComboBox.getValue();
            Double rating = ratingSpinner.getValue() == 0.0 ? null : ratingSpinner.getValue();

            // Start with all movies
            List<Movie> filteredMovies = MovieDataInitializer.loadAndCacheMovies();

            // Apply filters
            if (!query.isEmpty()) {
                filteredMovies = MovieDisplayHelper.filterMoviesBySearch(filteredMovies, query);
            }

            if (genre != null && genre != Genre.ANY) {
                filteredMovies = MovieDisplayHelper.filterMoviesByGenre(filteredMovies, genre);
            }

            if (year != null) {
                filteredMovies = filteredMovies.stream()
                        .filter(movie -> movie.getReleaseYear() == year)
                        .collect(Collectors.toList());
            };

            if (rating != null) {
                filteredMovies = filteredMovies.stream()
                        .filter(movie -> movie.getRating() >= rating)
                        .collect(Collectors.toList());
            };

            // Update UI
            observableMovies.setAll(filteredMovies);
            sortingContext.applyCurrentSorting(); // Apply sorting
            movieListView.setItems(observableMovies);
            //use custom cell factory to display data, init with the watchlist click handler
            movieListView.setCellFactory(movieListView
                    -> new MovieCell(onAddToWatchlistClicked));
            updateYearComboBox();

        } catch (Exception e) {
            showAlert("Filter Error", "Failed to apply filters: " + e.getMessage());
        };
    };

    //alert prompt
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    };

    //to home
    public void goHome(ActionEvent actionEvent) throws IOException {
//        Parent watchlistRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home-view.fxml")));
//
//        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
//        stage.setScene(new Scene(watchlistRoot));
//        stage.show();
    };

    //Go to watchlist view
    public void goWatchlist(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("watchlist-view.fxml"));
        loader.setControllerFactory(new ControllerFactory());
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    };

    //Not sure for what right now ?
    public void goAbout(ActionEvent actionEvent) {
    };
};


