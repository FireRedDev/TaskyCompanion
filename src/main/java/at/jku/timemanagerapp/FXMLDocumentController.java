/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.jku.timemanagerapp;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import entities.Activity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
/**
 *
 * @author Cool IT Help
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label label;
    @FXML private TextField filterField;
    @FXML private TableView<Activity> tableview;
    @FXML private TableColumn<Activity, String> EmpID;
    @FXML private TableColumn<Activity, String> empName;
    @FXML private TableColumn<Activity, String> empEmail;
    @FXML private TableColumn<Activity, String> department;
    @FXML private TableColumn<Activity, String> salary;
    
   
                  
    //observalble list to store data
    private final ObservableList<Activity> dataList = FXCollections.observableArrayList();
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {      
                               
        EmpID.setCellValueFactory(new PropertyValueFactory<>("activityName"));       
        empName.setCellValueFactory(new PropertyValueFactory<>("description"));        
           
        
        Activity emp1 = new Activity(1, "tanzen", LocalDate.MAX, LocalTime.MIN, "ein Tanzkurs");
                Activity emp2 = new Activity(2, "zeichnen", LocalDate.MAX, LocalTime.MIN, "ein Zeichenkurs");

        dataList.addAll(emp1,emp2);

        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Activity> filteredData = new FilteredList<>(dataList, b -> true);
		
		// 2. Set the filter Predicate whenever the filter changes.
		filterField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(employee -> {
				// If filter text is empty, display all persons.
								
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				
				// Compare first name and last name of every person with filter text.
				String lowerCaseFilter = newValue.toLowerCase();
				
				if (employee.getActivityName().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
					return true; // Filter matches first name.
				} else if (employee.getDescription().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // Filter matches last name.
				}
				
				     else  
				    	 return false; // Does not match.
			});
		});
		
		// 3. Wrap the FilteredList in a SortedList. 
		SortedList<Activity> sortedData = new SortedList<>(filteredData);
		
		// 4. Bind the SortedList comparator to the TableView comparator.
		// 	  Otherwise, sorting the TableView would have no effect.
		sortedData.comparatorProperty().bind(tableview.comparatorProperty());
		
		// 5. Add sorted (and filtered) data to the table.
		tableview.setItems(sortedData);
               
        
    }    
    
}
