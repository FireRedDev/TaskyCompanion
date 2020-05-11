package at.jku.timemanagerapp.testpackage.view;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import at.jku.timemanagerapp.MainApp;
import at.jku.timemanagerapp.testpackage.model.Activity;
import at.jku.timemanagerapp.testpackage.util.DateUtil;

public class ActivityOverviewController {
    @FXML
    public TableView<Activity> activityTable;
    @FXML
    private TableColumn<Activity, String> descriptionColumn;
    @FXML
    private TableColumn<Activity, String> projectColumn;
    @FXML
    public TextField filterField;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label customerLabel;
    @FXML
    private Label projectLabel;
    @FXML
    private Label durationLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label dueDateLabel;

    // Reference to the main application.
    private MainApp mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public ActivityOverviewController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the activity table with the two columns.
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().DescriptionProperty());
        projectColumn.setCellValueFactory(cellData -> cellData.getValue().projectProperty());
        

        // Listen for selection changes and show the activity details when changed.
        activityTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showActivityDetails(newValue));
    }

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        activityTable.setItems(mainApp.getActivityData());
    }
    
    /**
     * Fills all text fields to show details about the activity.
     * If the specified activity is null, all text fields are cleared.
     * 
     * @param activity the activity or null
     */
    private void showActivityDetails(Activity activity) {
        if (activity != null) {
            // Fill the labels with info from the activity object.
            descriptionLabel.setText(activity.getDescription());
            customerLabel.setText(activity.getCustomer());
            projectLabel.setText(activity.getProject());
            durationLabel.setText(Integer.toString(activity.getDuration()));
            cityLabel.setText(activity.getCity());
            dueDateLabel.setText(DateUtil.format(activity.getDueDate()));
        } else {
            // Activity is null, remove all the text.
            descriptionLabel.setText("");
            customerLabel.setText("");
            projectLabel.setText("");
            durationLabel.setText("");
            cityLabel.setText("");
            dueDateLabel.setText("");
        }
    }
    
    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleDeleteActivity() {
        int selectedIndex = activityTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            //activityTable.getItems().remove(selectedIndex);
            mainApp.activityData.remove(selectedIndex);
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Activity Selected");
            alert.setContentText("Please select a activity in the table.");
            
            alert.showAndWait();
        }
    }
    
    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new activity.
     */
    @FXML
    private void handleNewActivity() {
        Activity tempActivity = new Activity();
        boolean okClicked = mainApp.showActivityEditDialog(tempActivity);
        if (okClicked) {
            mainApp.getActivityData().add(tempActivity);
        }
    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected activity.
     */
    @FXML
    private void handleEditActivity() {
        Activity selectedActivity = activityTable.getSelectionModel().getSelectedItem();
        if (selectedActivity != null) {
            boolean okClicked = mainApp.showActivityEditDialog(selectedActivity);
            if (okClicked) {
                showActivityDetails(selectedActivity);
            }

        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Activity Selected");
            alert.setContentText("Please select a activity in the table.");
            
            alert.showAndWait();
        }
    }

}