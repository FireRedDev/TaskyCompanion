package at.jku.timemanagerapp;

import java.io.IOException;
import javafx.fxml.FXML;

public class ActivityListController {

    
     @FXML
    private void addActivity() throws IOException {
        App.setRoot("Add_Activity");
    }
     @FXML
    private void editActivity() throws IOException {
        App.setRoot("Edit_Activity");
    }
    @FXML
    private void deleteActivity() throws IOException {
        App.setRoot("Edit_Activity");
    }
    @FXML
    private void backToMainScreen() throws IOException {
        App.setRoot("mainScreen");
    }
}
