module at.jku.timemanagerapp {
    requires javafx.controls;
    requires javafx.fxml;

    opens at.jku.timemanagerapp to javafx.fxml;
    exports at.jku.timemanagerapp;
}