package entities;


import java.util.LinkedList;

public class Project {

    String projectName;
    String colorcode;
    Double hourlywages;
    Projectstatus projectstatus;
    LinkedList<Activity> activities;

    public Project(String projectName, String colorcode, Double hourlywages, Projectstatus projectstatus) {
        this.projectName = projectName;
        this.colorcode = colorcode;
        this.hourlywages = hourlywages;
        this.projectstatus = projectstatus;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getColorcode() {
        return colorcode;
    }

    public void setColorcode(String colorcode) {
        this.colorcode = colorcode;
    }

    public Double getHourlywages() {
        return hourlywages;
    }

    public void setHourlywages(Double hourlywages) {
        this.hourlywages = hourlywages;
    }

    public Projectstatus getProjectstatus() {
        return projectstatus;
    }

    public void setProjectstatus(Projectstatus projectstatus) {
        this.projectstatus = projectstatus;
    }

    public void addActivity(Activity activity){
        this.activities.add(activity);
    }

    public boolean checkIfActivityExists(Activity activity){
        boolean isUsed = false;
        for(int i = 0; i < activities.size() && isUsed ==false; i++){
            if(activities.get(i).getActivityName() == activity.getActivityName()){
                isUsed = true;
            }
        }
        return isUsed;
    }

    public LinkedList<Activity> getActivities(){
        return this.activities;
    }
}
