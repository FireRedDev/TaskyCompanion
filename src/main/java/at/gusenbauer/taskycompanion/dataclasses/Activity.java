package at.gusenbauer.taskycompanion.dataclasses;

import at.gusenbauer.taskycompanion.util.LocalDateAdapter;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

/**
 * The Data Object containing Activities
 *
 * @author chris
 */
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
     * @param string
     * @param string1
     */
    public Activity(final String description, final String customer) {
        this.description = new SimpleStringProperty(description);
        this.customer = new SimpleStringProperty(customer);

        // Some initial dummy data, just for convenient testing.
        this.project = new SimpleStringProperty("project xy");
        this.duration = new SimpleIntegerProperty(60);
        this.city = new SimpleStringProperty("some city");
        this.dueDate = new SimpleObjectProperty<>(LocalDate.of(1999, 2, 21));
        this.color = new SimpleObjectProperty<>(Color.LIGHTBLUE);
        final ObservableList<String> observableList = FXCollections.observableArrayList();
        this.tags = new SimpleListProperty<>(observableList);
    }

    /**
     * Constructor
     *
     * @param string
     * @param string1
     * @param string2
     * @param i
     * @param string3
     * @param ld
     * @param color
     */
    public Activity(final String description, final String customer, final String project, final int duration, final String city, final LocalDate dueDate, final Color color) {
        this.description = new SimpleStringProperty(description);
        this.customer = new SimpleStringProperty(customer);
        this.project = new SimpleStringProperty(project);
        this.duration = new SimpleIntegerProperty(duration);
        this.city = new SimpleStringProperty(city);
        this.dueDate = new SimpleObjectProperty<>(dueDate);
        this.color = new SimpleObjectProperty<>(color);
        final ObservableList<String> observableList = FXCollections.observableArrayList();
        this.tags = new SimpleListProperty<>(observableList);
    }

    /**
     * Gets a List of Tags applying to this Activity
     *
     * @return
     */
    public ObservableList<String> getTags() {
        return tags.get();
    }

    /**
     * @param tags
     */
    public void setTags(final ObservableList<String> tags) {
        this.tags.set(tags);
    }

    /**
     * @return
     */
    public ListProperty<String> tagsProperty() {
        return tags;
    }

    /**
     * Gets the picked Color to display this Activity with
     *
     * @return
     */
    public Color getColor() {
        return color.get();
    }

    /**
     * @param color
     */
    public void setColor(final Color color) {
        this.color.set(color);
    }

    /**
     * @return
     */
    public ObjectProperty<Color> colorProperty() {
        return color;
    }

    /**
     * Description
     *
     * @return
     */
    public String getDescription() {
        return description.get();
    }

    /**
     * @param description
     */
    public void setDescription(final String description) {
        this.description.set(description);
    }

    /**
     * @return
     */
    public StringProperty DescriptionProperty() {
        return description;
    }

    /**
     * Returns the Participants
     *
     * @return
     */
    public String getCustomer() {
        return customer.get();
    }

    /**
     * @param customer
     */
    public void setCustomer(final String customer) {
        this.customer.set(customer);
    }

    /**
     * @return
     */
    public StringProperty CustomerProperty() {
        return customer;
    }

    /**
     * Returns the Project
     *
     * @return
     */
    public String getProject() {
        return project.get();
    }

    /**
     * @param project
     */
    public void setProject(final String project) {
        this.project.set(project);
    }

    /**
     * @return
     */
    public StringProperty projectProperty() {
        return project;
    }

    /**
     * Returns the amount of time spent on the activity
     *
     * @return
     */
    public int getDuration() {
        return duration.get();
    }

    /**
     * @param postalCode
     */
    public void setDuration(final int postalCode) {
        this.duration.set(postalCode);
    }

    /**
     * @return
     */
    public IntegerProperty durationProperty() {
        return duration;
    }

    /**
     * Returns the Location
     *
     * @return
     */
    public String getCity() {
        return city.get();
    }

    /**
     * @param city
     */
    public void setCity(final String city) {
        this.city.set(city);
    }

    /**
     * @return
     */
    public StringProperty cityProperty() {
        return city;
    }

    /**
     * @return
     */
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    public LocalDate getDueDate() {
        return dueDate.get();
    }

    /**
     * @param birthday
     */
    public void setDueDate(final LocalDate birthday) {
        this.dueDate.set(birthday);
    }

    /**
     * @return
     */
    public ObjectProperty<LocalDate> dueDateProperty() {
        return dueDate;
    }
}