package at.jku.timemanagerapp.activity;

import at.jku.timemanagerapp.App;
import java.io.IOException;
import javafx.fxml.FXML;

public class ActivityListController {

    
     
    
    @FXML
    private void backToMainScreen() throws IOException {
        App.setRoot("mainScreen");
    }
}