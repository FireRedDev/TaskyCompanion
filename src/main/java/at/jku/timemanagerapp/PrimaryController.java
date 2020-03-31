package at.jku.timemanagerapp;

import java.io.IOException;
import javafx.fxml.FXML;

public class PrimaryController {

    
     @FXML
    private void addCustomer() throws IOException {
        App.setRoot("Add_Customer");
    }
     @FXML
    private void editCustomer() throws IOException {
        App.setRoot("Edit_Customer");
    }
    @FXML
    private void viewCustomerList() throws IOException {
        App.setRoot("View_Customers");
    }
     @FXML
    private void addActivity() throws IOException {
        App.setRoot("Add_Activity");
    }
     @FXML
    private void editActivity() throws IOException {
        App.setRoot("Edit_Activity");
    }
    @FXML
    private void viewActivityList() throws IOException {
        App.setRoot("View_Activities");
    }
     @FXML
    private void addProject() throws IOException {
        App.setRoot("Add_Project");
    }
     @FXML
    private void editProject() throws IOException {
        App.setRoot("Edit_Project");
    }
    @FXML
    private void viewProjectList() throws IOException {
        App.setRoot("View_Projects");
    }
    @FXML
    private void viewDashboard() throws IOException {
        App.setRoot("dashboard");
    }
    
}
