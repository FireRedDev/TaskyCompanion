package at.gusenbauer.taskycompanion;

import at.gusenbauer.taskycompanion.controller.ActivityEditDialogController;
import at.gusenbauer.taskycompanion.controller.ActivityOverviewController;
import at.gusenbauer.taskycompanion.controller.RootLayoutController;
import at.gusenbauer.taskycompanion.controller.StatisticsController;
import at.gusenbauer.taskycompanion.dataclasses.Activity;
import at.gusenbauer.taskycompanion.dataclasses.ActivityListWrapper;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class MainApp extends Application {

    /**
     * The data as an observable list of Activitys.
     */
    public ObservableList<Activity> activityData = FXCollections.observableArrayList();
    private Stage primaryStage;
    private BorderPane rootLayout;

    /**
     * Constructor
     */
    public MainApp() {
        // Add some sample data
        Activity withTags = new Activity("Dancing", "Tanzlehrer");
        withTags.setTags(FXCollections.observableArrayList("Sport", "Lernen"));
        activityData.add(withTags);
        activityData.add(new Activity("Angular App schreiben", "Mueller Elektronik GMBH"));
        activityData.add(new Activity("Api Doku lesen", "Bundesregierung"));
        activityData.add(new Activity("Ausflug Steiermark", "Familie", "Reisen", 60 * 10, "Weinviertel", LocalDate.of(2020, 6, 28), Color.BLACK));
        activityData.add(new Activity("Meeting Steiermark", "Familie", "Reisen", 60 * 20, "Weinviertel", LocalDate.of(2020, 6, 27), Color.ORANGE));
        Activity withTags2 = new Activity("Swaggiges Treffen", "Coolio", "Swag", 60 * 35, "Swagtown", LocalDate.of(2020, 6, 26), Color.PINK);
        withTags2.setTags(FXCollections.observableArrayList("Spaß", "Lernen"));
        activityData.add(withTags2);


    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Returns the data as an observable list of Activitys.
     *
     * @return
     */
    public ObservableList<Activity> getActivityData() {
        return activityData;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Tasky");

        // Set the application icon.
        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("32x32.png")));

        initRootLayout();

        showActivityOverview();
    }

    /**
     * Initializes the root layout and tries to load the last opened
     * activity file.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("RootLayout" + ".fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);

            scene.getStylesheets().add("DarkTheme.css");
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Try to load last opened activity file.
        File file = getActivityFilePath();
        if (file != null) {
            loadActivityDataFromFile(file);
        }
    }

    /**
     * Shows the activity overview inside the root layout.
     */
    public void showActivityOverview() {
        try {
            // Load activity overview.

            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("ActivityOverview" + ".fxml"));
            AnchorPane activityOverview = (AnchorPane) loader.load();

            // Set activity overview into the center of root layout.
            rootLayout.setCenter(activityOverview);

            // Give the controller access to the main app.
            ActivityOverviewController controller = loader.getController();
            controller.setMainApp(this);
            // Clear activity details.
            preprareFiltering(controller);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void preprareFiltering(ActivityOverviewController controller) {
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).

        FilteredList<Activity> filteredData = new FilteredList<>(activityData, p -> true);

        initTagList(controller);

        // 2. Set the filter Predicate whenever the filter changes.
        controller.filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filter(controller, filteredData, newValue);
        });
        controller.tagSelector.valueProperty().addListener((observable, oldValue, newValue) -> {
            filter(controller, filteredData, (String) newValue);
        });
        controller.bisDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            filter(controller, filteredData, newValue != null ? newValue.toString() : null);
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Activity> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(controller.activityTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        controller.activityTable.setItems(sortedData);
    }

    public void initTagList(ActivityOverviewController controller) {
        List liste = activityData.stream().flatMap(listContainer -> listContainer.getTags().stream()).distinct().collect(Collectors.toList());

        controller.tagSelector.setItems(FXCollections.observableArrayList(liste));
    }

    private void filter(ActivityOverviewController controller, FilteredList<Activity> filteredData, String newValue) {
        filteredData.setPredicate(activity -> {

            // If filter text is empty, display all persons.
            // if (newValue == null || newValue.isEmpty()) {
            //  return true;
            //}

            Boolean correctDate = true;
            Boolean correctTag = true;
            String lowerCaseFilter = controller.filterField.getText();
            if (controller.tagSelector.getSelectionModel().getSelectedItem() != null && !activity.getTags().contains((String) controller.tagSelector.getSelectionModel().getSelectedItem())) {
                correctTag = false;
            }
            LocalDate dateToCheck = activity.getDueDate();
            LocalDate startDate = controller.vonDate.getValue();
            LocalDate endDate = controller.bisDate.getValue();

            if (
                    endDate != null && startDate != null &&
                            !(!dateToCheck.isBefore(startDate)
                                    && !dateToCheck.isAfter(endDate))) {
                correctDate = false;
            }
            if (correctDate && correctTag && activity.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (correctDate && correctTag && activity.getProject().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (correctDate && correctTag && activity.getDueDate().toString().contains(lowerCaseFilter)) {
                return true;
            }

            return false; // Does not match.
        });
    }

    /**
     * Opens a dialog to edit details for the specified activity. If the user
     * clicks OK, the changes are saved into the provided activity object and true
     * is returned.
     *
     * @param activity the activity object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showActivityEditDialog(Activity activity) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("ActivityEditDialog" + ".fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Activity");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the activity into the controller.
            ActivityEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setActivity(activity);

            // Set the dialog icon.


            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Opens a dialog to show birthday statistics.
     */
    public void showStatistics() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("Statistics" + ".fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Statistics");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the dialog icon.
            dialogStage.getIcons().add(new Image("file:resources/images/calendar.png"));

            // Set the activitys into the controller.
            StatisticsController controller = loader.getController();
            LocalDate currentDate = LocalDate.now();
            controller.setActivityData(activityData, currentDate);
            controller.chartDate.setValue(currentDate);
            controller.chartDate.valueProperty().addListener((observable, oldValue, newValue) -> {
                controller.setActivityData(activityData, controller.chartDate.getValue());
            });
            controller.chartChoice.valueProperty().addListener((observable, oldValue, newValue) -> {
                controller.setActivityData(activityData, controller.chartDate.getValue());
            });
            dialogStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the activity file preference, i.e. the file that was last opened.
     * The preference is read from the OS specific registry. If no such
     * preference can be found, null is returned.
     *
     * @return
     */
    public File getActivityFilePath() {

        Properties prefs = new Properties();
        try {
            prefs.load(MainApp.class.getResourceAsStream("app.properties"));
        } catch (Exception e) {
            System.out.println("No properties file exists" + e.getLocalizedMessage());
        }
        String filePath = prefs.getProperty("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the OS specific registry.
     *
     * @param file the file or null to remove the path
     */
    public void setActivityFilePath(File file) {
        Properties prefs = new Properties();
        InputStream in = MainApp.class.getResourceAsStream("app.properties");
        try {
            prefs.load(in);
        } catch (Exception e) {
            System.out.println("No properties file exists");
        }
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Update the stage title.
            primaryStage.setTitle("AddressApp - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Update the stage title.
            primaryStage.setTitle("AddressApp");
        }
        try {
            prefs.store(new FileOutputStream("app.properties"), "Appproperties");
        } catch (Exception e) {
            System.out.println("No properties file exists");
        }
    }

    /**
     * Loads activity data from the specified file. The current activity data will
     * be replaced.
     *
     * @param file
     */
    public void loadActivityDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(ActivityListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            ActivityListWrapper wrapper = (ActivityListWrapper) um.unmarshal(file);

            activityData.clear();
            activityData.addAll(wrapper.getActivities());

            // Save the file path to the registry.
            setActivityFilePath(file);

        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * Saves the current activity data to the specified file.
     *
     * @param file
     */
    public void saveActivityDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(ActivityListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our activity data.
            ActivityListWrapper wrapper = new ActivityListWrapper();
            wrapper.setActivities(activityData);

            // Marshalling and saving XML to the file.
            m.marshal(wrapper, file);

            // Save the file path to the registry.
            setActivityFilePath(file);
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * Returns the main stage.
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
}