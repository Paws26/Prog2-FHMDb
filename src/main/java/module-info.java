module at.ac.fhcampuswien.fhmdb {
    requires javafx.fxml;
    requires com.jfoenix;
    requires javafx.controls;
    requires okhttp3;


    opens at.ac.fhcampuswien.fhmdb to javafx.fxml;
    exports at.ac.fhcampuswien.fhmdb;
}