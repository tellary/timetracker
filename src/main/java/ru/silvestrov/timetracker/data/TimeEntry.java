package ru.silvestrov.timetracker.data;

import javax.persistence.*;

/**
 * Created by Silvestrov Ilya
 * Date: Jul 9, 2008
 * Time: 11:01:58 PM
 */
@SuppressWarnings({"FieldCanBeLocal", "UnusedDeclaration"})
@Entity
@Table(name = "time_entry")
public class TimeEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "time_start")
    private long timeStart;
    @Column(name = "time_end", nullable = true)
    private Long timeEnd;

    @ManyToOne
    @JoinColumn (name = "activity_id")
    private Activity activity;

    private TimeEntry() {
    }

    public TimeEntry(long timeStart, Activity activity) {
        this.timeStart = timeStart;
        this.activity = activity;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(long timeEnd) {
        this.timeEnd = timeEnd;
    }
}
