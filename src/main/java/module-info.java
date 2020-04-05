module at.jku.timemanagerapp {
    requires javafx.controls;
    requires javafx.fxml;

    opens at.jku.timemanagerapp to javafx.fxml;
    opens at.jku.entities to javafx.base;
    exports at.jku.timemanagerapp;
    exports at.jku.entities;
}