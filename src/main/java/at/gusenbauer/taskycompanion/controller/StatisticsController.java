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
 * The controller for the time statistics view.
 * The controller supports two different modes: Months of a year or
 * Days of a Week and populates the View with a Graph of the Time spent in
 * that timeframe
 */
public class StatisticsController {

    /**
     *
     */
    public StatisticsController() {

    }

    /**
     * Around which date will we display the data - to choose Year/week
     */
    @FXML
    public DatePicker chartDate;

    /**
     *
     */
    @FXML
    public ChoiceBox<ChartCategory> chartChoice;
    @FXML
    private BarChart<String, Integer> barChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    private final ObservableList<String> indexNames = FXCollections.observableArrayList();

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        final ObservableList<ChartCategory> chartCategories //
                = FXCollections.observableArrayList(ChartCategory.YEAR, ChartCategory.WEEK);


        chartChoice.setItems(chartCategories);
        chartChoice.setValue(ChartCategory.YEAR);
        // Get an array with the English month names.
        final String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();
        // Convert it to a list and add it to our ObservableList of months.
        indexNames.addAll(Arrays.asList(months));

        // Assign the month names as categories for the horizontal axis.
        xAxis.setCategories(indexNames);
        //setup Axis Labels
        xAxis.setLabel("Monate des Jahres");
        yAxis.setLabel("Gesamte Zeit der Tasks in Stunden");

    }

    /**
     * Sets the activitys to show the statistics for.
     *
     * @param activitys
     * @param date
     */
    public void setActivityData(final List<Activity> activitys, final LocalDate date) {
        final XYChart.Series<String, Integer> series = new XYChart.Series<>();

        ChartCategory value = chartChoice.getValue();
        if (value == ChartCategory.YEAR) {
            prepareYearStatistics(activitys, date, series);
        } else if (value == ChartCategory.WEEK) {
            prepareWeekStatistics(activitys, date, series);
        }


        barChart.getData().clear();
        barChart.layout();
        barChart.getData().add(series);
    }

    /**
     * Prepare XYChart that shows the cummulated duration of all activities in a
     * certain month in a selected year.
     *
     * @param activitys
     * @param date
     * @param series
     */
    private void prepareYearStatistics(final List<Activity> activitys, final LocalDate date, final XYChart.Series<String, Integer> series) {
        int[] monthCounter = new int[12];
        activitys.stream().filter(p -> (p.getDueDate().getYear() == date.getYear())).forEachOrdered(p -> {
            final int month = p.getDueDate().getMonthValue() - 1;
            monthCounter[month] = monthCounter[month] + (p.getDuration() / 60);
        });
        xAxis.setLabel("Monate des Jahres");
        yAxis.setLabel("Gesamte Zeit der Tasks in Stunden");
        final String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();
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

    /**
     * * Prepare XYChart that shows the cummulated duration of all activities in a
     * certain day in a selected week.
     *
     * @param activitys
     * @param date
     * @param series
     */
    private void prepareWeekStatistics(final List<Activity> activitys, final LocalDate date, final XYChart.Series<String, Integer> series) {
        String[] days;
        int[] dayOfWeekCounter = new int[7];
        final DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        final DayOfWeek lastDayOfWeek = DayOfWeek.of((firstDayOfWeek.getValue() + 5) % DayOfWeek.values().length + 1);
        activitys.forEach(p -> {
            final LocalDate firstDate = date.with(TemporalAdjusters.previousOrSame(firstDayOfWeek)); // first day
            final LocalDate lastDate = date.with(TemporalAdjusters.nextOrSame(lastDayOfWeek));
            final LocalDate currentDate = p.getDueDate();
            if (currentDate.isEqual(date) || currentDate.isAfter(firstDate) && currentDate.isBefore(lastDate)) {
                final int day = currentDate.getDayOfWeek().getValue() - 1;
                dayOfWeekCounter[day] = dayOfWeekCounter[day] + (p.getDuration() / 60);
            }
        });
        xAxis.setLabel("Tage der Woche");
        days = DateFormatSymbols.getInstance(Locale.ENGLISH).getWeekdays();
        // Convert it to a list and add it to our ObservableList of months.
        indexNames.clear();
        indexNames.addAll(Arrays.asList(days));


        // Assign the Day names as categories for the horizontal axis.
        xAxis.setCategories(indexNames);
        // Create a XYChart.Data object for each day. Add it to the series.
        for (int i = 0; i < dayOfWeekCounter.length; i++) {
            series.getData().add(new XYChart.Data<>(indexNames.get(i), dayOfWeekCounter[i]));
        }
    }
}