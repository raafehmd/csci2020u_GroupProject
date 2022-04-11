module com.example.groupproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.groupproject to javafx.fxml;
    exports com.example.groupproject;
}