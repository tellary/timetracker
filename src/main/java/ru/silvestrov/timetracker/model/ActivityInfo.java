package ru.silvestrov.timetracker.model;

/**
 * Created by Silvestrov Ilya
 * Date: Jul 12, 2008
 * Time: 9:14:02 PM
 */
public class ActivityInfo implements Cloneable {
    private String name;
    private long time;

    public ActivityInfo(String name, long time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public long getTime() {
        return time;
    }
}
