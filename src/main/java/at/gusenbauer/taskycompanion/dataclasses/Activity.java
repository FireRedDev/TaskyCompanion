package at.gusenbauer.taskycompanion.dataclasses;

import at.gusenbauer.taskycompanion.util.LocalDateAdapter;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;


public class Activity {

    private final StringProperty description;
    private final StringProperty customer;
    private final StringProperty project;
    private final IntegerProperty duration;
    private final StringProperty city;
    private final ObjectProperty<LocalDate> dueDate;
    private final ObjectProperty<Color> color;
    private final ListProperty<String> tags;

    /**
     * Default constructor.
     */
    public Activity() {
        this(null, null);
    }

    /**
     * Constructor with some initial data.
     *
     * @param description
     * @param customer
     */
    public Activity(String description, String customer) {
        this.description = new SimpleStringProperty(description);
        this.customer = new SimpleStringProperty(customer);

        // Some initial dummy data, just for convenient testing.
        this.project = new SimpleStringProperty("project xy");
        this.duration = new SimpleIntegerProperty(60);
        this.city = new SimpleStringProperty("some city");
        this.dueDate = new SimpleObjectProperty<LocalDate>(LocalDate.of(1999, 2, 21));
        this.color = new SimpleObjectProperty<Color>(Color.LIGHTBLUE);
        ObservableList<String> observableList = FXCollections.observableArrayList();
        this.tags = new SimpleListProperty<String>(observableList);
    }

    public Activity(String description, String customer, String project, int duration, String city, LocalDate dueDate, Color color) {
        this.description = new SimpleStringProperty(description);
        this.customer = new SimpleStringProperty(customer);
        this.project = new SimpleStringProperty(project);
        this.duration = new SimpleIntegerProperty(duration);
        this.city = new SimpleStringProperty(city);
        this.dueDate = new SimpleObjectProperty<LocalDate>(dueDate);
        this.color = new SimpleObjectProperty<Color>(color);
        ObservableList<String> observableList = FXCollections.observableArrayList();
        this.tags = new SimpleListProperty<String>(observableList);
    }

    public ObservableList<String> getTags() {
        return tags.get();
    }

    public void setTags(ObservableList<String> tags) {
        this.tags.set(tags);
    }

    public ListProperty<String> tagsProperty() {
        return tags;
    }

    public Color getColor() {
        return color.get();
    }

    public void setColor(Color color) {
        this.color.set(color);
    }

    public ObjectProperty<Color> colorProperty() {
        return color;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty DescriptionProperty() {
        return description;
    }

    public String getCustomer() {
        return customer.get();
    }

    public void setCustomer(String customer) {
        this.customer.set(customer);
    }

    public StringProperty CustomerProperty() {
        return customer;
    }

    public String getProject() {
        return project.get();
    }

    public void setProject(String project) {
        this.project.set(project);
    }

    public StringProperty projectProperty() {
        return project;
    }

    public int getDuration() {
        return duration.get();
    }

    public void setDuration(int postalCode) {
        this.duration.set(postalCode);
    }

    public IntegerProperty durationProperty() {
        return duration;
    }

    public String getCity() {
        return city.get();
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public StringProperty cityProperty() {
        return city;
    }

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    public LocalDate getDueDate() {
        return dueDate.get();
    }

    public void setDueDate(LocalDate birthday) {
        this.dueDate.set(birthday);
    }

    public ObjectProperty<LocalDate> birthdayProperty() {
        return dueDate;
    }
}