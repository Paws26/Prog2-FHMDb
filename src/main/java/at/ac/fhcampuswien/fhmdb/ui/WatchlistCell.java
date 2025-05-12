package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.utils.ClickEventHandler;
import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.stream.Collectors;


public class WatchlistCell extends ListCell<Movie> {

    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genre = new Label();
    private final Label releaseYear = new Label();
    private final Label rating = new Label();
    private final JFXButton showDetailsBtn = new JFXButton();
    private final JFXButton removeFromWatchlistBtn = new JFXButton();
    private final Region titleButtonSpacer = new Region();
    private final Region buttonSpacer = new Region();
    private final HBox titleLayout = new HBox(title, titleButtonSpacer, showDetailsBtn, buttonSpacer, removeFromWatchlistBtn);
    private final VBox layout = new VBox(titleLayout, detail, genre, releaseYear, rating);


    //attach click event handler to watchlist cell
    public WatchlistCell(ClickEventHandler<Movie> removeFromWatchlistClicked) {
        super();
        removeFromWatchlistBtn.setOnAction(event -> {
            if (getItem() != null) {
                removeFromWatchlistClicked.onClick(getItem());
            }
        });
    }

    ;


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
            removeFromWatchlistBtn.setText("Remove");
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
            removeFromWatchlistBtn.getStyleClass().add("text-black");
            removeFromWatchlistBtn.getStyleClass().add("background-yellow");
            detail.getStyleClass().add("text-white");
            genre.getStyleClass().add("text-white");
            releaseYear.getStyleClass().add("text-white");
            rating.getStyleClass().add("text-white");
            layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));

            // layout
            title.fontProperty().set(title.getFont().font(20));
            HBox.setHgrow(titleButtonSpacer, Priority.ALWAYS);
            buttonSpacer.setMinWidth(10);
            detail.maxWidthProperty().bind(
                    getListView().widthProperty()
                            .subtract(getListView().getInsets().getLeft()
                                    + getListView().getInsets().getRight()
                                    + 45)
            );
            detail.setWrapText(true);
            layout.setPadding(new Insets(10));
            layout.spacingProperty().set(10);
            layout.alignmentProperty().set(javafx.geometry.Pos.CENTER_LEFT);
            setGraphic(layout);
        }
    }
}

