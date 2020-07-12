package at.gusenbauer.taskycompanion.controller;

import at.gusenbauer.taskycompanion.MainApp;
import at.gusenbauer.taskycompanion.dataclasses.Activity;
import at.gusenbauer.taskycompanion.util.DateUtil;
import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Displays a List of Activites and provides filtering and sorting capabilities
 *
 * @author chris
 */
public class ActivityOverviewController {

    /**
     * Activity Table
     */
    @FXML
    public TableView<Activity> activityTable;

    /**
     * Input Text
     */
    @FXML
    public TextField filterField;

    /**
     *
     */
    @FXML
    public FlowPane filterArea;

    /**
     *
     */
    @FXML
    public DatePicker vonDate;

    /**
     *
     */
    @FXML
    public DatePicker bisDate;

    /**
     * Tag Dropdown
     */
    @FXML
    public ComboBox tagSelector;
    @FXML
    private TableColumn<Activity, String> descriptionColumn;
    @FXML
    private TableColumn<Activity, String> projectColumn;
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
    @FXML
    private Label tagLabel;
    @FXML
    private Label deleteArea;
    // Reference to the main application.
    private MainApp mainApp;

    /**
     * The constructor. The constructor is called before the initialize()
     * method.
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
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().DescriptionProperty()
        );
        projectColumn.setCellValueFactory(cellData -> cellData.getValue().projectProperty());
        setUpDragAndDrop();
        // Listen for selection changes and show the activity details when changed.
        activityTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showActivityDetails(newValue));
        //Provide the correct styling as specified in the activity.
        activityTable.setRowFactory(t -> new TableRow<>() {

            @Override
            protected void updateItem(final Activity activity, final boolean empty) {
                super.updateItem(activity, empty);
                styleProperty().unbind();
                if (empty) {
                    setStyle("");
                } else {
                    styleProperty().bind(Bindings.createStringBinding(()
                                    -> "-fx-background: " + formatColorString(activity.getColor()),
                            activity.colorProperty()
                    ));
                }
            }

            private String formatColorString(final Color color) {
                final int r = (int) (color.getRed() * 255);
                final int g = (int) (color.getGreen() * 255);
                final int b = (int) (color.getBlue() * 255);
                return String.format("#%02x%02x%02x", r, g, b);
            }
        });
    }

    /**
     * Sets Up Drag and Drop
     */
    private void setUpDragAndDrop() {
        //Start animation and show text to show DragandDrop
        activityTable.setOnDragDetected((final MouseEvent event) -> {
                    // drag was detected, start drag-and-drop gesture
                    final Activity selected = activityTable.getSelectionModel().getSelectedItem();
                    if (selected != null) {

                        final Dragboard db = activityTable.startDragAndDrop(TransferMode.ANY);
                        final ClipboardContent content = new ClipboardContent();
                        content.putString(selected.getDescription());
                        db.setContent(content);
                        event.consume();
                    }
                    deleteArea.setStyle("-fx-background-color:#34c3eb;");
                    final FadeTransition fadeIn = new FadeTransition(
                            Duration.millis(500)
                    );
                    fadeIn.setNode(deleteArea);
                    fadeIn.setFromValue(0.0);
                    fadeIn.setToValue(1.0);
                    fadeIn.setCycleCount(1);
                    fadeIn.setAutoReverse(false);
                    fadeIn.playFromStart();
                    deleteArea.setVisible(true);
                } //drag
        );
        deleteArea.setOnDragOver((final DragEvent event) -> {
            deleteArea.setVisible(true);
            // data is dragged over the target

            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
        deleteArea.setOnDragDropped((final DragEvent event) -> {
            boolean success = false;
            if (event.getDragboard().hasString()) {

                final Activity selected = activityTable.getSelectionModel().getSelectedItem();
                if (selected != null) {

                    mainApp.activityData.remove(selected);

                }
                success = true;
            }
            event.setDropCompleted(success);
            deleteArea.setVisible(false);
            event.consume();
        });
        activityTable.setOnDragExited((final DragEvent event) -> {
            /* mouse moved away, remove the graphical cues */

            event.consume();
            deleteArea.setVisible(false);
        });
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(final MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        activityTable.setItems(mainApp.getActivityData());

    }

    /**
     * Fills all text fields to show details about the activity. If the
     * specified activity is null, all text fields are cleared.
     *
     * @param activity the activity or null
     */
    private void showActivityDetails(final Activity activity) {
        if (activity != null) {
            // Fill the labels with info from the activity object.
            descriptionLabel.setText(activity.getDescription());
            customerLabel.setText(activity.getCustomer());
            projectLabel.setText(activity.getProject());
            durationLabel.setText(prepareDurationString(activity));
            cityLabel.setText(activity.getCity());
            dueDateLabel.setText(DateUtil.format(activity.getDueDate()));
            tagLabel.setText(activity.getTags().stream().collect(Collectors.joining(",")));
        } else {
            // Activity is null, remove all the text.
            descriptionLabel.setText("");
            customerLabel.setText("");
            projectLabel.setText("");
            durationLabel.setText("");
            cityLabel.setText("");
            dueDateLabel.setText("");
            tagLabel.setText("");
        }
    }

    private String prepareDurationString(final Activity activity) {
        int seconds = activity.getDuration();
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (day * 24);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60);
        //Creates Time String depending on the length of the duration
        StringBuilder timeText = new StringBuilder();
        if (day != 0) {
            timeText.append("Days: ").append(day).append(" ");
        }
        if (hours != 0) {
            timeText.append("Hours: ").append(hours).append(" ");
        }
        if (minute != 0) {
            timeText.append("Min: ").append(minute).append(" ");
        }
        if (second != 0) {
            timeText.append("Sec: ").append(second);
        }
        return timeText.toString();
    }

    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleDeleteActivity() {
        final int selectedIndex = activityTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            //activityTable.getItems().remove(selectedIndex);
            mainApp.activityData.remove(selectedIndex);
        } else {
            // Nothing selected.
            final Alert alert = new Alert(AlertType.WARNING);
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
        final Activity tempActivity = new Activity();
        final boolean okClicked = mainApp.showActivityEditDialog(tempActivity);
        if (okClicked) {
            mainApp.getActivityData().add(tempActivity);
            mainApp.updateOverViewController();
        }
    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected activity.
     */
    @FXML
    private void handleEditActivity() {
        final Activity selectedActivity = activityTable.getSelectionModel().getSelectedItem();
        if (selectedActivity != null) {
            final boolean okClicked = mainApp.showActivityEditDialog(selectedActivity);
            if (okClicked) {
                showActivityDetails(selectedActivity);
                mainApp.updateOverViewController();
            }

        } else {
            // Nothing selected.
            final Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Activity Selected");
            alert.setContentText("Please select a activity in the table.");

            alert.showAndWait();
        }
    }

    /**
     * @param actionEvent
     */
    public void resetAdvanced(final ActionEvent actionEvent) {
        vonDate.setValue(null);
        bisDate.setValue(null);
        tagSelector.getSelectionModel().clearSelection();
    }

}
