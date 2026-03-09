module com.example.upr4 {
    requires javafx.fxml;
    requires javafx.controls;
    opens com.example.upr4 to javafx.fxml;
    exports com.example.upr4;
}