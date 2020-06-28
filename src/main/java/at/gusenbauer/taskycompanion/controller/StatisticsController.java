package at.gusenbauer.taskycompanion.controller;

import at.gusenbauer.taskycompanion.dataclasses.Activity;
import at.gusenbauer.taskycompanion.util.ChartCategory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;

import java.text.DateFormatSymbols;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * The controller for the birthday statistics view.
 */
public class StatisticsController {

    @FXML
    public DatePicker chartDate;
    @FXML
    public ChoiceBox<ChartCategory> chartChoice;
    @FXML
    private BarChart<String, Integer> barChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    private ObservableList<String> indexNames = FXCollections.observableArrayList();

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        ObservableList<ChartCategory> chartCategories //
                = FXCollections.observableArrayList(ChartCategory.YEAR, ChartCategory.WEEK);


        chartChoice.setItems(chartCategories);
        chartChoice.setValue(ChartCategory.YEAR);
        // Get an array with the English month names.
        String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();
        // Convert it to a list and add it to our ObservableList of months.
        indexNames.addAll(Arrays.asList(months));

        // Assign the month names as categories for the horizontal axis.
        xAxis.setCategories(indexNames);
        xAxis.setLabel("Monate des Jahres");
        yAxis.setLabel("Gesamte Zeit der Tasks in Stunden");

    }

    /**
     * Sets the activitys to show the statistics for.
     *
     * @param activitys
     */
    public void setActivityData(List<Activity> activitys, LocalDate date) {
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        ObservableList<String> qo = FXCollections.observableArrayList();
        switch (chartChoice.getValue()) {
            case YEAR:
                prepareYearStatistics(activitys, date, series);
                break;
            case WEEK:


                prepareWeekStatistics(activitys, date, series);
                break;
        }


        barChart.getData().clear();
        barChart.layout();
        barChart.getData().add(series);
    }

    private void prepareYearStatistics(List<Activity> activitys, LocalDate date, XYChart.Series<String, Integer> series) {
        int[] monthCounter = new int[12];
        for (Activity p : activitys) {
            if (p.getDueDate().getYear() == date.getYear()) {
                int month = p.getDueDate().getMonthValue() - 1;
                monthCounter[month] = monthCounter[month] + (p.getDuration() / 60);
            }
        }
        xAxis.setLabel("Monate des Jahres");
        yAxis.setLabel("Gesamte Zeit der Tasks in Stunden");
        String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();
        // Convert it to a list and add it to our ObservableList of months.

        indexNames.clear();
        indexNames.addAll(Arrays.asList(months));

        // Assign the month names as categories for the horizontal axis.
        xAxis.setCategories(indexNames);
        // Create a XYChart.Data object for each month. Add it to the series.
        for (int i = 0; i < monthCounter.length; i++) {
            series.getData().add(new XYChart.Data<>(indexNames.get(i), monthCounter[i]));
        }
    }

    private void prepareWeekStatistics(List<Activity> activitys, LocalDate date, XYChart.Series<String, Integer> series) {
        String[] months;
        int[] dayOfWeekCounter = new int[7];
        final DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        final DayOfWeek lastDayOfWeek = DayOfWeek.of(((firstDayOfWeek.getValue() + 5) % DayOfWeek.values().length) + 1);
        for (Activity p : activitys) {

            LocalDate firstDate = date.with(TemporalAdjusters.previousOrSame(firstDayOfWeek)); // first day
            LocalDate lastDate = date.with(TemporalAdjusters.nextOrSame(lastDayOfWeek));
            LocalDate currentDate = p.getDueDate();
            if (currentDate.isEqual(date) || (currentDate.isAfter(firstDate) && currentDate.isBefore(lastDate))) {
                int day = currentDate.getDayOfWeek().getValue() - 1;
                dayOfWeekCounter[day] = dayOfWeekCounter[day] + (p.getDuration() / 60);
            }
        }
        xAxis.setLabel("Tage der Woche");
        months = DateFormatSymbols.getInstance(Locale.ENGLISH).getWeekdays();
        // Convert it to a list and add it to our ObservableList of months.
        indexNames.clear();
        indexNames.addAll(Arrays.asList(months));


        // Assign the month names as categories for the horizontal axis.
        xAxis.setCategories(indexNames);
        // Create a XYChart.Data object for each month. Add it to the series.
        for (int i = 0; i < dayOfWeekCounter.length; i++) {
            series.getData().add(new XYChart.Data<>(indexNames.get(i), dayOfWeekCounter[i]));
        }
    }
}