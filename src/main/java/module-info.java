module at.ac.fhcampuswien.fhmdb {
    requires javafx.fxml;
    requires com.jfoenix;
    requires javafx.controls;
    requires okhttp3;
    requires com.google.gson;
    requires surefire.shared.utils;


    opens at.ac.fhcampuswien.fhmdb to javafx.fxml;
    opens at.ac.fhcampuswien.fhmdb.models to com.google.gson;
    exports at.ac.fhcampuswien.fhmdb;
}