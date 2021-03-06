<link rel="stylesheet" type="text/css" href="doc.css"/>

# Version 0.4

This version contains minimal set of features to run Timetracker as a simple "multi-task timer"

# 2011-Dec-17

*   U1: As a user I can rename activity to correct my mistakes: made cell editable and get cell updates

# 2011-Dec-18

*   Remove TODO highlighting in *.apt files in IntelliJ (changed pattern to catch TODO\: instead of "TODO")

*   Plan backlog for version 0.5


# 2011-Dec-19

*   U3: As a user I want special "time tracking" activity
    to be started when I double-click its' name for edit so that I could
    easily track time spent on "time tracking" activities
    (version 0.5, caused by U1, depends on U2)

    *   Found a way to disable selection on cell editing in JTable
        (Double click generates two mouse events, on first event selection is adjusted
        second events triggers cell editing. With custom CellEditor it is possible to detect
        start editing situation by listening for focus event of underlying editor's component
        and trigger selection to whatever you want)


# 2011-Dec-25

*   U1: As a user I can rename activity to correct my mistakes
    (version 0.5)

    *   Rename activity in the database and see it is updated in the activity list

*   Vote for MASSEMBLY-449 in say thanks to the guy who have offered a workaround and remove TODO in the pom.xml

# 2011-Dec-26

*   U10: As a user I want to have distribution of TimeTracker to be able to run it on new workstations and
    with new database files.

# 2012-Jan-02

*   Resolve dependency

        <dependency>
            <groupId>org.apache.derby</groupId>
            <artifactId>derbytools</artifactId>
            <version>10.4.1.3</version>
        </dependency>


    (run mvn antrun:run -PtestDB to achive this)

# 2012-Jan-04

*   Connect to timeDB with my actual entries and issue several queries to see how much time was spent on certain
    task during specified interval of time.

*   U9: As a user I want to see time spent on activities in human readable format

*   U4: As a user I can hide activities from control panel to free it up for faster switching

*   Decided to implement activities hiding with separate column of controls

# 2012-Jan-05

*   U4: As a user I can hide activities from control panel to free it up for faster switching

    *   Add column with hide button in ActivityControlList

# 2012-Jan-06

*   U4: As a user I can hide activities from control panel to free it up for faster switching

    *   Implement `ActivityControlList#finishActivity` method together with test

    *   Implement cell editor and cell renderer with one button component and pass idx as a field to the component's
        action performed cellButtonListener when getTableCellEditorComponent is called to invoke finishActivity(idx) on button
        pressed.

    *   Implement callback for the button pressed

# 2012-Jan-08

*   Add @Null annotation to project and setup IDEA to honor it while doing its checks

*   U11: As a user I want "add activity" button to be on top of Activity Control List and to
    be "acceptably small" to avoid wasting of space on the screen

*   U15: I want activity control list to be vertically scrollable so that I could add a any amount of activities
    into it while preserving small height of the window.

*   U16: I want all columns on activity control list except name (the first one) to be fixed width,
    but showing its contents without cropping while first
    column with name of the task to take all remaining size so that I see as much of the task name as possible.

# 2012-Jan-09

*   U21: I want "done/finish" button on Activity Control List (3rd column) to have "X" label or icon so that
    it takes least size possible.

*   Think how to implement U18, U22 and U13 (have had a long phone call with brother)

*   U25: I want "very small" time entries to be not saved to avoid garbage in the history. Small time entries
    could appear due to mistaken selection and Swing jtable selection on double-click. Threshold to consider
    "small time entry" a garbage is 1 to 3 secs. You couldn't do anything sensible for this small amount of time.

*   U18: I want be able to increase timer value so that I could add several minutes if I forgot to
    start the timer. This should create new timeentry with time_end equal to current moment and times_start
    with specified amount of minutes ago.
    (blocked by U25)

    *   Implement GUI part
    *   Implement controller

*   U26: I want time cell to be empty (or have some "good value") when I double-click it for
    time adjustment so that I don't spent time on cleaning it manually.

# 2012-Jan-14

*   Added `pullTimeDB.sh` which allows to sync between to machines with timetracker over ssh

# 2012-Jan-19

*   U13: I want be able to decrease timer value so that I could decrease its value for 3 or 5 minutes if I forgot
    to turn the timer off when quit for cup of coffee. This is achieved by moving end_time of last time entry backwards.
    If amount of time asked to decrease is greater then duration of time entry then last time entry is removed
    and overall timer value is decreased on duration of time entry instead of requested amount.

    *   Implement `TimeEntryDao#getLastTimeEntry(int activityId)` method
    *   Implement time decrease in `AdjustTimeController`

# 2012-Jan-28

*   `TestTimeEntryDao#testLatestTimeEntryForEmptyActivity` fails when run in full suite after U13, fix it.

*   Timetracker doesn't start with class not found (Main) when build with assembly:single instead of assembly:assembly
    (bound assembly:single to package phase to use mvn package instead of assembly:assembly)

*   U20: I want name to be of 1024 length so that I could enter "very long" activity names

    *   Make changes
    *   Test database schema will be altered on already existing database
        (it's not altered as hbm2ddl doesn't alter column size in 3.3.2.GA version, it only adds column if it doesn't exist)
    *   Check newer version of Hibernate if it does alter already existing column length
        (Hibernate 3.5.4 doesn't alter)
    *   Implement custom column length alternation on startup

# 2012-Jan-29

*   U17: I want activity names to be wrapped so that I could see full activity name when it's too log to fit
    into cell width`

    *   Implemented textarea cell renderer with line wrapping, but without correct row height adjustment when
        word wrapping is used.
    *   Made row height adjustment to account word wrapping
    *   Properly plug row height adjustment code. Avoid invoking table.setRowHeight during paint as it causes
        painting again thus triggering infinite loop. (Done table rows height resize on table resize itself)
    *   Make renderer component to be of another color if row is selected.


# 2012-Jan-31

*   U17: I want activity names to be wrapped so that I could see full activity name when it's too log to fit
    into cell width`

    *   Implement textarea cell editor (not finished)

# 2012-Feb-12

*   U17: I want activity names to be wrapped so that I could see full activity name when it's too log to fit
    into cell width

    *   Implement textarea cell editor
    *   Output debug messages with log4j

