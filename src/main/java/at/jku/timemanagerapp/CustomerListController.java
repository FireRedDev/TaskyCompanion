package at.jku.timemanagerapp;

import java.io.IOException;
import javafx.fxml.FXML;

public class CustomerListController {

    
     @FXML
    private void addCustomer() throws IOException {
        App.setRoot("Add_Customers");
    }
     @FXML
    private void editCustomer() throws IOException {
        App.setRoot("Edit_Customers");
    }
    @FXML
    private void deleteCustomer() throws IOException {
        App.setRoot("Edit_Customers");
    }
     @FXML
    private void backToMainScreen() throws IOException {
        App.setRoot("mainScreen");
    }
}
