package at.jku.timemanagerapp.testpackage.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import at.jku.timemanagerapp.testpackage.model.Activity;
import at.jku.timemanagerapp.testpackage.util.DateUtil;

/**
 * Dialog to edit details of a activity.
 *
 */
public class ActivityEditDialogController {

    @FXML
    private TextField descriptionField;
    @FXML
    private TextField customerField;
    @FXML
    private TextField projectField;
    @FXML
    private TextField durationField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField dueDateField;


    private Stage dialogStage;
    private Activity activity;
    private boolean okClicked = false;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        
        // Set the dialog icon.
        this.dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));
    }

    /**
     * Sets the activity to be edited in the dialog.
     * 
     * @param activity
     */
    public void setActivity(Activity activity) {
        this.activity = activity;

        descriptionField.setText(activity.getDescription());
        customerField.setText(activity.getCustomer());
        projectField.setText(activity.getProject());
        durationField.setText(Integer.toString(activity.getDuration()));
        cityField.setText(activity.getCity());
        dueDateField.setText(DateUtil.format(activity.getDueDate()));
        dueDateField.setPromptText("dd.mm.yyyy");
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            activity.setDescription(descriptionField.getText());
            activity.setCustomer(customerField.getText());
            activity.setProject(projectField.getText());
            activity.setDuration(Integer.parseInt(durationField.getText()));
            activity.setCity(cityField.getText());
            activity.setDueDate(DateUtil.parse(dueDateField.getText()));

            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     * 
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (descriptionField.getText() == null || descriptionField.getText().length() == 0) {
            errorMessage += "No valid first name!\n"; 
        }
        if (customerField.getText() == null || customerField.getText().length() == 0) {
            errorMessage += "No valid last name!\n"; 
        }
        if (projectField.getText() == null || projectField.getText().length() == 0) {
            errorMessage += "No valid street!\n"; 
        }

        if (durationField.getText() == null || durationField.getText().length() == 0) {
            errorMessage += "No valid postal code!\n"; 
        } else {
            // try to parse the postal code into an int.
            try {
                Integer.parseInt(durationField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid postal code (must be an integer)!\n"; 
            }
        }

        if (cityField.getText() == null || cityField.getText().length() == 0) {
            errorMessage += "No valid city!\n"; 
        }

        if (dueDateField.getText() == null || dueDateField.getText().length() == 0) {
            errorMessage += "No valid birthday!\n";
        } else {
            if (!DateUtil.validDate(dueDateField.getText())) {
                errorMessage += "No valid birthday. Use the format dd.mm.yyyy!\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            
            alert.showAndWait();
            
            return false;
        }
    }
}