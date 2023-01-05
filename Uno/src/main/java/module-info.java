module com.example.unogui {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;


    opens com.example.unogui to javafx.fxml;
    exports com.example.unogui;
}