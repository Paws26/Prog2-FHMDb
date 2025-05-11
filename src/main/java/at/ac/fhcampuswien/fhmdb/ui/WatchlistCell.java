package at.ac.fhcampuswien.fhmdb.ui;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.utils.ClickEventHandler;
import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.util.stream.Collectors;



public class WatchlistCell extends ListCell<Movie> {

    private final ClickEventHandler<Movie> removeFromWatchlistClicked;

    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genre = new Label();
    private final Label releaseYear = new Label();
    private final Label rating = new Label();
    private final JFXButton showDetails = new JFXButton();
    private final JFXButton removeFromWatchlist = new JFXButton();
    private final HBox titleLayout = new HBox(title, showDetails, removeFromWatchlist);
    private final VBox layout = new VBox(titleLayout, detail, genre, releaseYear, rating);


    //attach click event handler to watchlist cell
    public WatchlistCell(ClickEventHandler<Movie> removeFromWatchlistClicked) {
        this.removeFromWatchlistClicked = removeFromWatchlistClicked;
        removeFromWatchlist.setOnAction(event -> {
            if (getItem() != null) {
                removeFromWatchlistClicked.onClick(getItem());
            }
        });
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
            showDetails.setText("Show Details");
            removeFromWatchlist.setText("Remove");
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
            showDetails.getStyleClass().add("text-black");
            showDetails.getStyleClass().add("background-yellow");
            removeFromWatchlist.getStyleClass().add("text-black");
            removeFromWatchlist.getStyleClass().add("background-yellow");
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
            layout.alignmentProperty().set(javafx.geometry.Pos.CENTER_LEFT);
            setGraphic(layout);
        }
    }
}

