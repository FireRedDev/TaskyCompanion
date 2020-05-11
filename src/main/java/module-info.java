module at.jku.timemanagerapp {

    requires javafx.graphics;

    requires javafx.fxml;
    requires javafx.controls;
    requires java.xml.bind;

    exports at.jku.timemanagerapp;

    exports at.jku.timemanagerapp.testpackage.view;
    exports at.jku.timemanagerapp.testpackage.util;
    exports at.jku.timemanagerapp.testpackage.model;
  
    opens at.jku.timemanagerapp;

    opens at.jku.timemanagerapp.testpackage.view;
    opens at.jku.timemanagerapp.testpackage.util;
    opens at.jku.timemanagerapp.testpackage.model;
  
}