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
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * The Main Class orchestrating the LifeCycle of this JavaFX Application
 *
 * @author chris
 */
public class MainApp extends Application {

    /**
     * The data as an observable list of Activitys.
     */
    public ObservableList<Activity> activityData = FXCollections.observableArrayList();
    private Stage primaryStage;
    private BorderPane rootLayout;
    private ActivityOverviewController overviewcontroller;
    private LinkedList<String> tagList;

    /**
     * Constructor with default Values
     */
    public MainApp() {
        super();
        final Activity withTags = new Activity("Dancing", "Tanzlehrer");
        withTags.setTags(FXCollections.observableArrayList("Sport", "Lernen"));
        activityData.add(withTags);
        activityData.add(new Activity("Angular App schreiben", "Mueller Elektronik GMBH"));
        activityData.add(new Activity("Api Doku lesen", "Bundesregierung"));
        activityData.add(new Activity("Ausflug Steiermark", "Familie", "Reisen", 62 * 10, "Weinviertel", LocalDate.of(2020, 6, 28), Color.BLACK));
        activityData.add(new Activity("Meeting Steiermark", "Familie", "Reisen", 61 * 200000, "Weinviertel", LocalDate.of(2020, 6, 27), Color.ORANGE));
        final Activity withTags2 = new Activity("Swaggiges Treffen", "Coolio", "Swag", 60 * 35, "Swagtown", LocalDate.of(2020, 6, 26), Color.PINK);
        withTags2.setTags(FXCollections.observableArrayList("Spaß", "Lernen"));
        activityData.add(withTags2);
    }

    /**
     * Launches the JavaFX App
     *
     * @param args
     */
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
     * Initializes the root layout, root styling and tries to load the last opened
     * activity file.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file in the Ressources Folder.
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("RootLayout" + ".fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            final Scene scene = new Scene(rootLayout);

            scene.getStylesheets().add("DarkTheme.css");
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            final RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Try to load last opened activity file.
        final File file = getActivityFilePath();
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

            final FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("ActivityOverview" + ".fxml"));
            final AnchorPane activityOverview = (AnchorPane) loader.load();

            // Set activity overview into the center of root layout.
            rootLayout.setCenter(activityOverview);

            // Give the controller access to the main app.
            final ActivityOverviewController controller = loader.getController();
            controller.setMainApp(this);
            overviewcontroller = controller;
            // Clear activity details.
            preprareFiltering(controller);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets Up several Event Listeners to trigger the filtering of the TableList
     * Also initializes the Drop Down with the Tags
     *
     * @param controller
     */
    private void preprareFiltering(final ActivityOverviewController controller) {
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).

        final FilteredList<Activity> filteredData = new FilteredList<>(activityData, p -> true);

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
        final SortedList<Activity> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(controller.activityTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        controller.activityTable.setItems(sortedData);
    }

    /**
     * Prepares ComboBox
     *
     * @param controller
     */
    public void initTagList(final ActivityOverviewController controller) {
        tagList = activityData.stream().flatMap(listContainer -> listContainer.getTags().stream()).distinct().collect(Collectors.toCollection(LinkedList::new));
        controller.tagSelector.setItems(FXCollections.observableList(tagList));
    }

    /**
     * Filters the data based on several criterias: The input text, date range and tag selection
     *
     * @param controller
     * @param filteredData
     * @param newValue
     */
    private void filter(final ActivityOverviewController controller, final FilteredList<Activity> filteredData, final String newValue) {
        filteredData.setPredicate(activity -> {

            Boolean correctDate = true;
            Boolean correctTag = true;
            final String lowerCaseFilter = controller.filterField.getText();
            if (controller.tagSelector.getSelectionModel().getSelectedItem() != null && !activity.getTags().contains((String) controller.tagSelector.getSelectionModel().getSelectedItem())) {
                correctTag = false;
            }
            final LocalDate dateToCheck = activity.getDueDate();
            final LocalDate startDate = controller.vonDate.getValue();
            final LocalDate endDate = controller.bisDate.getValue();

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
    public boolean showActivityEditDialog(final Activity activity) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            final FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("ActivityEditDialog" + ".fxml"));
            final AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            final Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Activity");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            final Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the activity into the controller.
            final ActivityEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setActivity(activity);
            controller.setMainApp(this);
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
     * Opens a dialog to show time statistics.
     */
    public void showStatistics() {
        try {
            // Load the fxml file and create a new stage for the popup.
            final FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("Statistics" + ".fxml"));
            final AnchorPane page = (AnchorPane) loader.load();
            final Stage dialogStage = new Stage();
            dialogStage.setTitle("Statistics");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            final Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the dialog icon.
            dialogStage.getIcons().add(new Image(this.getClass().getResourceAsStream("32x32.png")));


            // Set the activitys into the controller.
            final StatisticsController controller = loader.getController();
            final LocalDate currentDate = LocalDate.now();
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
     * This is based on a Properties File since the Preference API is deprecated. If no such
     * preference can be found, null is returned.
     *
     *
     * @return
     */
    public File getActivityFilePath() {

        final Properties prefs = new Properties();
        try {
            prefs.load(MainApp.class.getResourceAsStream("app.properties"));
        } catch (IOException e) {
            System.out.println("No properties file exists" + e.getLocalizedMessage());
        }
        final String filePath = prefs.getProperty("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the Properties File.
     *
     * @param file the file or null to remove the path
     */
    public void setActivityFilePath(final File file) {
        final Properties prefs = new Properties();

        try (InputStream in = MainApp.class.getResourceAsStream("app.properties");) {
            prefs.load(in);
        } catch (IOException e) {
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
        } catch (IOException e) {
            System.out.println("No properties file exists");
        }

    }

    /**
     * Loads activity data from the specified file. The current activity data will
     * be replaced.
     *
     * @param file
     */
    public void loadActivityDataFromFile(final File file) {
        try {
            final JAXBContext context = JAXBContext
                    .newInstance(ActivityListWrapper.class);
            final Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            final ActivityListWrapper wrapper = (ActivityListWrapper) um.unmarshal(file);

            activityData.clear();
            activityData.addAll(wrapper.getActivities());

            // Save the file path to the registry.
            setActivityFilePath(file);

        } catch (JAXBException e) { // catches ANY exception
            final Alert alert = new Alert(Alert.AlertType.ERROR);
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
    public void saveActivityDataToFile(final File file) {
        try {
            final JAXBContext context = JAXBContext
                    .newInstance(ActivityListWrapper.class);
            final Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our activity data.
            final ActivityListWrapper wrapper = new ActivityListWrapper();
            wrapper.setActivities(activityData);

            // Marshalling and saving XML to the file.
            m.marshal(wrapper, file);

            // Save the file path to the registry.
            setActivityFilePath(file);
        } catch (JAXBException e) { // catches ANY exception
            final Alert alert = new Alert(Alert.AlertType.ERROR);
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

    /**
     * Updates the OverViewController - its list - when changes happen in the detail view
     * like Tags getting added, edited or removed
     */
    public void updateOverViewController() {
        tagList.clear();
        tagList.addAll(
                activityData.stream().flatMap(listContainer -> listContainer.getTags().stream()).distinct().collect(Collectors.toList()));
    }

}