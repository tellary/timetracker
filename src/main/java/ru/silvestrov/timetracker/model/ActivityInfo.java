package ru.silvestrov.timetracker.model;

/**
 * Created by Silvestrov Ilya
 * Date: Jul 12, 2008
 * Time: 9:14:02 PM
 */
public class ActivityInfo implements Cloneable {
    private long id;
    private String name;
    private long time;

    public ActivityInfo(long id, String name, long time) {
        this.id = id;
        this.name = name;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getTime() {
        return time;
    }
}
