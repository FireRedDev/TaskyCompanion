package at.jku.timemanagerapp.customer;

import at.jku.timemanagerapp.App;
import java.io.IOException;
import javafx.fxml.FXML;

public class EditCustomerController {

    
  
     @FXML
    private void backToMainScreen() throws IOException {
        App.setRoot("mainScreen");
    }
}
