package entities;


import java.time.LocalDate;
import java.time.LocalTime;

public class Activity {

    private int activityId;
    private String activityName;
    private LocalDate date;
    private LocalTime duration;
    private String description;
    private LocalTime starttime;
    private LocalTime endtime;

    public Activity(int activityId, String activityName, LocalDate date, LocalTime duration, String description) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.date = date;
        this.duration = duration;
        this.description = description;
    }

    public int getActivityId(){
        return activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getDuration() {
        return duration;
    }

    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
