package at.gusenbauer.taskycompanion.controller;

import at.gusenbauer.taskycompanion.MainApp;
import at.gusenbauer.taskycompanion.dataclasses.Activity;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Dialog to edit details of a activity.
 */
public class ActivityEditDialogController {

    /**
     *
     */
    public ActivityEditDialogController() {
    }

    // Reference to the main application.
    private MainApp mainApp;
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
    private DatePicker dueDateField;
    @FXML
    private ColorPicker colorPic;
    @FXML
    private TextField tagList;

    private Stage dialogStage;
    private Activity activity;
    private boolean okClicked;
    private AnimationTimer timer;
    private boolean timerRunning;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }

    /**
     * @param mainApp
     */
    public void setMainApp(final MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(final Stage dialogStage) {
        this.dialogStage = dialogStage;

        // Set the dialog icon.
        this.dialogStage.getIcons().add(new Image(this.getClass().getResourceAsStream("32x32.png")));
    }

    /**
     * Sets the activity to be edited in the dialog.
     *
     * @param activity
     */
    public void setActivity(final Activity activity) {
        this.activity = activity;

        descriptionField.setText(activity.getDescription());
        customerField.setText(activity.getCustomer());
        projectField.setText(activity.getProject());
        durationField.setText(Integer.toString(activity.getDuration()));
        cityField.setText(activity.getCity());
        dueDateField.setValue(activity.getDueDate());
        dueDateField.setPromptText("dd.mm.yyyy");
        colorPic.setValue(activity.getColor());
        //Convert List into comma seperated List
        tagList.setText(activity.getTags().stream().collect(Collectors.joining(",")));
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
            activity.setDueDate(dueDateField.getValue());
            activity.setColor(colorPic.getValue());
            activity.setTags(FXCollections.observableList(
                    //Convert comma seperated String to List
                    new ArrayList<>(
                            Arrays.asList(
                                    tagList.getText().replaceAll("\\s+", "").split(",")))));
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
        StringBuilder errorMessage = new StringBuilder("");

        if (descriptionField.getText() == null || descriptionField.getText().length() == 0) {
            errorMessage.append("No valid Description!\n");
        }
        if (customerField.getText() == null || customerField.getText().length() == 0) {
            errorMessage.append("No valid Participant Description!\n");
        }
        if (projectField.getText() == null || projectField.getText().length() == 0) {
            errorMessage.append("No valid Project Name!\n");
        }

        if (durationField.getText() == null || durationField.getText().length() == 0) {
            errorMessage.append("No valid Duration!\n");
        } else {
            // try to parse the postal code into an int.
            try {
                Integer.parseInt(durationField.getText());
            } catch (NumberFormatException e) {
                errorMessage.append("No valid Duration (must be an integer)!\n");
            }
        }

        if (cityField.getText() == null || cityField.getText().length() == 0) {
            errorMessage.append("No valid Location Text!\n");
        }

        if (dueDateField.getValue() == null) {
            errorMessage.append("No valid Date!\n");
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            final Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage.toString());

            alert.showAndWait();

            return false;
        }
    }

    /**
     * Starts logging the Time for a specific Activity
     * If the Method gets called again, it stops tracking and saves the Duration
     */
    @FXML
    public void startTimeTracking() {
        if (timerRunning) {
            timer.stop();
            timerRunning = false;
        } else {
            final long startTime = System.currentTimeMillis();
            timer = new AnimationTimer() {
                @Override
                public void start() {
                    super.start();
                    timerRunning = true;
                }

                @Override
                public void handle(final long now) {
                    final long elapsedMillis = System.currentTimeMillis() - startTime;
                    durationField.setText(Long.toString(elapsedMillis / 1000));
                }
            };
            timer.start();
        }
    }
}