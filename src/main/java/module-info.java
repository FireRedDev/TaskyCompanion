module at.jku.timemanagerapp {
    requires java.xml.bind;
    requires javafx.graphics;

    requires javafx.fxml;
    requires javafx.controls;
    exports at.jku.timemanagerapp;
    exports at.jku.timemanagerapp.testpackage;
    exports at.jku.timemanagerapp.testpackage.view;
    exports at.jku.timemanagerapp.testpackage.util;
    exports at.jku.timemanagerapp.testpackage.model;
    exports at.jku.timemanagerapp.activity;
    exports at.jku.timemanagerapp.customer;
    exports at.jku.timemanagerapp.dashboard;

    exports at.jku.timemanagerapp.project;
    opens at.jku.timemanagerapp;
    opens at.jku.timemanagerapp.testpackage;
    opens at.jku.timemanagerapp.testpackage.view;
    opens at.jku.timemanagerapp.testpackage.util;
    opens at.jku.timemanagerapp.testpackage.model;
    opens at.jku.timemanagerapp.activity;
    opens at.jku.timemanagerapp.customer;
    opens at.jku.timemanagerapp.dashboard;

    opens at.jku.timemanagerapp.project;
}