package at.jku.timemanagerapp.project;

import at.jku.timemanagerapp.App;
import java.io.IOException;
import javafx.fxml.FXML;

public class AddProjectController {

    
   
     @FXML
    private void backToMainScreen() throws IOException {
        App.setRoot("mainScreen");
    }
}
