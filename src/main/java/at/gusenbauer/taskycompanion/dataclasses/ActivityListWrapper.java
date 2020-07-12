package at.gusenbauer.taskycompanion.dataclasses;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author chris
 */
@XmlRootElement(name = "activities")
public class ActivityListWrapper {

    /**
     *
     */
    public ActivityListWrapper() {
    }

    private List<Activity> activities;

    /**
     * @return
     */
    @XmlElement(name = "activity")
    public List<Activity> getActivities() {
        return activities;
    }

    /**
     * @param activities
     */
    public void setActivities(final List<Activity> activities) {
        this.activities = activities;
    }
}