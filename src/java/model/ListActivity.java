package model;

import java.util.ArrayList;
import java.util.List;

public class ListActivity {
    private String activityName;
    private int duration;
    private boolean isChecked;
    private boolean isCompleted;

    private static final List<ListActivity> allActivities = new ArrayList<>();

    public ListActivity(String activityName, int duration) {
        this.activityName = activityName;
        this.duration = duration;
        this.isChecked = false;
        this.isCompleted = false;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public static List<ListActivity> getAllActivities() {
        return allActivities;
    }

    public static void addActivity(String name, int duration) {
        allActivities.add(new ListActivity(name, duration));
    }

    public static List<ListActivity> getCheckedActivities() {
        List<ListActivity> checkedActivities = new ArrayList<>();
        for (ListActivity activity : allActivities) {
            if (activity.isChecked()) {
                checkedActivities.add(activity);
            }
        }
        return checkedActivities;
    }
}
