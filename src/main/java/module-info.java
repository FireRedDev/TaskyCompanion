module at.gusenbauer.taskycompanion {


    requires javafx.controls;
    requires java.xml.bind;


    requires javafx.fxml;
    exports at.gusenbauer.taskycompanion;

    exports at.gusenbauer.taskycompanion.controller;
    exports at.gusenbauer.taskycompanion.util;
    exports at.gusenbauer.taskycompanion.dataclasses;

    opens at.gusenbauer.taskycompanion;

    opens at.gusenbauer.taskycompanion.controller;
    opens at.gusenbauer.taskycompanion.util;
    opens at.gusenbauer.taskycompanion.dataclasses;

}