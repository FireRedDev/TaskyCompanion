package at.jku.timemanagerapp.testpackage.model;

import at.jku.timemanagerapp.testpackage.util.LocalDateAdapter;
import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;




public class Activity {

    private final StringProperty description;
    private final StringProperty customer;
    private final StringProperty project;
    private final IntegerProperty duration;
    private final StringProperty city;
    private final ObjectProperty<LocalDate> dueDate;

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