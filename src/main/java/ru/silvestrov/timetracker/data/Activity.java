package ru.silvestrov.timetracker.data;

import ru.silvestrov.timetracker.annotations.Nullable;

import javax.persistence.*;
import java.util.Collection;

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
    @Column (length = 1024)
    private String name;
    @Column
    private boolean finished;
    @OneToOne
    private TimeEntry currentTimeEntry;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Activity parent;
    @OneToMany(mappedBy = "parent")
    private Collection<Activity> children;


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

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public TimeEntry getCurrentTimeEntry() {
        return currentTimeEntry;
    }

    public void setCurrentTimeEntry(@Nullable TimeEntry currentTimeEntry) {
        this.currentTimeEntry = currentTimeEntry;
    }

    public Collection<Activity> getChildren() {
        return children;
    }

    public Activity getParent() {
        return parent;
    }

    void setParent(Activity parent) {
        this.parent = parent;
    }
}
