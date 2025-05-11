package at.ac.fhcampuswien.fhmdb.ui;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.repositories.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.utils.ClickEventHandler;
import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.sql.SQLException;
import java.util.stream.Collectors;



public class MovieCell extends ListCell<Movie> {

    private final WatchlistRepository watchlistRepo;
    //private final ClickEventHandler<Movie> addToWatchlistClicked;

    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genre = new Label();
    private final Label releaseYear = new Label();
    private final Label rating = new Label();
    private final JFXButton showDetailsBtn = new JFXButton();
    private final JFXButton addToWatchlistBtn = new JFXButton();
    private final HBox titleLayout = new HBox(title, showDetailsBtn, addToWatchlistBtn);
    private final VBox layout = new VBox(titleLayout, detail, genre, releaseYear, rating);

    //click event will be attached on movie cell
    public MovieCell(ClickEventHandler<Movie> addToWatchlistClicked) {
        super();

        this.watchlistRepo = new WatchlistRepository();

        // Set up the button action
        addToWatchlistBtn.setOnAction(event -> {
            if (getItem() != null) {
                addToWatchlistClicked.onClick(getItem());
            }
        });
    };

    //dynamically change btn text
    private void updateButtonText() {
        try {
            if (getItem() != null) {
                boolean isInWatchlist = watchlistRepo.existsInWatchlist(getItem().getId());
                addToWatchlistBtn.setText(isInWatchlist ? "Remove from Watchlist" : "Add to Watchlist");
            }
        } catch (SQLException e) {
            addToWatchlistBtn.setText("Add to Watchlist");
        }
    };


    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setText(null);
            setGraphic(null);
        } else {
            this.getStyleClass().add("movie-cell");
            title.setText(movie.getTitle());
            showDetailsBtn.setText("Show Details");
            addToWatchlistBtn.setText("Add to Watchlist");
            detail.setText(
                    movie.getDescription() != null
                            ? movie.getDescription()
                            : "No description available"
            );
            genre.setText(movie.getGenres().stream().map(Enum::name).collect(Collectors.joining(", ")));
            releaseYear.setText("Released: " + movie.getReleaseYear());
            rating.setText("Rating: " + movie.getRating());


            // color scheme
            title.getStyleClass().add("text-yellow");
            showDetailsBtn.getStyleClass().add("text-black");
            showDetailsBtn.getStyleClass().add("background-yellow");
            addToWatchlistBtn.getStyleClass().add("text-black");
            addToWatchlistBtn.getStyleClass().add("background-yellow");
            detail.getStyleClass().add("text-white");
            genre.getStyleClass().add("text-white");
            releaseYear.getStyleClass().add("text-white");
            rating.getStyleClass().add("text-white");
            layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));

            // layout
            title.fontProperty().set(title.getFont().font(20));
            detail.maxWidthProperty().bind(
                    getListView().widthProperty()
                            .subtract( getListView().getInsets().getLeft()
                                    + getListView().getInsets().getRight()
                                    + 45 )
            );
            detail.setWrapText(true);
            layout.setPadding(new Insets(10));
            layout.spacingProperty().set(10);
            layout.alignmentProperty().set(Pos.CENTER_LEFT);
            setGraphic(layout);
        }

        if (!empty && movie != null) {
            updateButtonText(); // Update button text when cell is populated
        }
    };
};

