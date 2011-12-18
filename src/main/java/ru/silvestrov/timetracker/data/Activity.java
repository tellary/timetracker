package ru.silvestrov.timetracker.data;

import ru.silvestrov.timetracker.annotations.Nullable;

import javax.persistence.*;

/**
 * Created by Silvestrov Ilya
 * Date: Jul 9, 2008
 * Time: 11:02:56 PM
 */
@Entity
@Table(name = "activity")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private boolean finished;
    @OneToOne
    private TimeEntry currentTimeEntry;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public TimeEntry getCurrentTimeEntry() {
        return currentTimeEntry;
    }

    public void setCurrentTimeEntry(@Nullable TimeEntry currentTimeEntry) {
        this.currentTimeEntry = currentTimeEntry;
    }
}
