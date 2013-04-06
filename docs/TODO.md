<link rel="stylesheet" type="text/css" href="doc.css"/>

# 2012-Mar-10

*   T2: Implement model for activities tree with time aggregation and write tests for it (for U35).

    *   Started to design the domain model and its interaction with tree view, created LazyActivityTreeNode node stub.

# 2012-May-01

*   T3: Implement database and DAO support for parent/child relation of Activities. (for T2)

    *   Got rid of DataConfiguration class and in favor of object's initialization by Spring
    *   Implemented child/parent relationships and read/update operations for them with tests
    *   Wrote down "Change activity parent" use case

# 2012-May-02

*   T2: Implement model for activities tree with time aggregation and write tests for it (for U35, U7).

    *   Designed the the tree view model, update LazyActivityTreeNode javadoc
    *   Implemented lazy-load tree model with time aggregation and invalidation.

# 2012-Jul-21

*   T4: Implement functionality to populate activity tree from database (for U35, U7)

    *   T4.1: Implement tests on real database with data setup.

# 2012-Jul-22

*   T5: Create view showing all activities in tree (for U35, U7)

    *   T5.1: Find how to implement a tree which will support drag-and-drop (seems that JTree supports drag-and-drop)
    *   Think how should I display own time of parent task in the tree (created U38)
    *   Think how to show all activities tree in application (Decided to implement a main frame
        which will have control to show <activity control tree> and <archive activity tree>, T6 created)

# 2012-Jul-23

*   T6: Create main frame for the time tracker which has controls to show _activity control tree_ and
    _activity archive tree_ (for T5, U35, U7)

    *   Created mainFrame which shows ActivityControlList
    *   Plug _all activities tree_ to be show on button press in MainFrame

*   T5: Create view showing all activities in tree (for U35, U7)

    *   T5.2: Make tree to display aggregate time spent in clock format

# 2012-Jul-24

*   U39: I want to assign parent activity to the already created activity so that I could structure activities

    *   TU39.1: Enable drag-and-drop in _activities archive tree_, log DnD events
    
        *   TU39.1.1: Implemented basic DnD sample with ActivityJTree with copy of the child without its removal

# 2013-Apr-04

*   T7: Change git commit history so that Ilya Silvestrov author has an email tellary@gmail.com,
    but commit and author dates are not changed.

*   T8: Rewrite existing docs to Markdown

    *   T8.1: Rewrite TODO list in Markdown format instead of APT.
    *   T8.2: Convert `DONE-0.4.apt` to Markdown
    *   T8.4: Convert `activity_tree_use_case.apt` to Markdown

# 2013-Apr-05

*   T8: Rewrite existing docs to Markdown

    *   T8.3: Convert `goal.apt` to Markdown and make `README.md` out of it
    
        *   Push to github and make sure it is displayed correctly.

# Version 0.5

This version contains minimal set of features to start run Timetracker for work

        
*   T6: Create main frame for the time tracker which has controls to show _activity control tree_ and
    _activity archive tree_ (for T5, U35, U7)

    *   T6.1: Make ActivityControlList to save its size after it is closed and then opened again from MainFrame
    *   T6.2: Make MainFrame to display _activity control tree_ and _activity archive tree_ frames if they
        are not shown yet only: avoid duplicate windows.

*   U39: I want to assign parent activity to the already created activity so that I could structure activities

    *   TU39.1: Enable drag-and-drop in _activities archive tree_, log DnD events

        *   TU39.1.2: Remove just copied child from its previous location
        *   TU39.1.3: Make all exceptions to be shown on DnD, extensively test sample app, fix all errors

    *   TU39.2: Implement set parent functionality on drag-n-drop event

*   U38: I want to see parent task's own time as a child node in the task tree so that parent node in the tree
    displays aggregate time only

*   U35: I want to view all inactive activities as a tree with children time aggregated so what I could
    restore activities from "inactive" to "active" state.

*   U32: I want be able to assign parent to the activity so that I could see correct activity tree in the
    "activity tree with time aggregation" view.

*   U7: As a user I want to view activities grouped in trees by days so that I could easily find activities to
    to restore them back into control list, to understand what was done on certain day, and to trigger export
    to toggl CSV for certain day.
    (depends on U32 as I need to create Activities relationship somehow prior building a tree)

*   U6: As a user I can restore activities back to control list to be able to start/stop them
    (depends on U7)

*   U31: I want to see aggregate time spent for a day for activities
    which have children in the "activity trees by day" view so that I could see
    how much were done for certain "parent activity" during the day.
    (depends on U7)

*   U8: As a user I want to export "JIRA tasks done by day report" in CSV format compatible with toggl to be able
    to import it into JIRA with timehelp tool.
    (depends on U31 because it will reuse the same "make JIRA task tree with time aggregation" logic)

*   U28: I want to select and edit time entries: two fields out of three ones: timeStart, timeEnd, duration so that
    I could add time entries tracked outside of the timetracker into it and I could increase/decrease time entries
    if I failed to do it with the last time entry.

*   U33: I want activities of type "reportable task" to be shown for certain day with to be shown for certain day
    with all their children time accumulated so that I could see how much time will be reported on each "reportable
    task" for the day.

*   U34: I want to select parent activity (from the tree) for the newly created task. Or to mark this task as
    top-level.

# Version 0.6

This version improves "multi-task timer" (Activity Control List) usability

*   U14: I want "add activity" form to have the same size and position as it was when
    last closed to avoid wasting time on resizing and repositioning

*   U18: I want activity control list to have only time summed up for one day so that I can see how much
    time is spent on the task during the day to understand how overall time tracked per day is summed up.

*   U19: I want to see sum up time for tasks in activity control list for the day so that I could understand
    how much time did I already spent for this day to estimate this day efficiency

*   U2: As a user I want to see "time tracking" activity on the control list, so that I could
    easily start it when renaming/creating/hiding activities or doing some other time tracking
    related stuff.
    (version 0.5, caused by U3)

    *   U2.1: Add time tracking activity to the database if not present on construction of ActivityControlList

*   U3: As a user I want special "time tracking" activity
    to be started when I double-click its' name for edit so that I could
    easily track time spent on "time tracking" activities
    (version 0.5, caused by U1, depends on U2)

    *   Start the "time tracking" activity (U2) on "start edit" event

*   U23: I want just added activity to be started so that I don't waste time on starting it additionly

*   U22: I want to add new time entry specifying time_start and duration, so that I could add some time
    measurements done outside of the timetracker with stopwatch and piece of paper or from another timer program.

    *   U22.1: Implement GUI stub which will form the database update request and output it into sysout

    *   U22.2: Add time edit controller which will update the database

*   U24: I want to be notified when requested time decrease for activity is greater then duration of activity
    so that I could see that my decrease request won't be executed fully.
    See ActivityControlList#processStopData method

*   U27: I want to adjust time without using of keyboard with predefined set of values (i.e +3m -3m +2m -2m etc)
    so that I could save some time on switching between mouse and keyboard.

    *   U27.1: How should I handle if user wants to decrease on some value which is not predefined with mouse only?

*   T1: Avoid saving name to database on every text change in the name cell. This was introduced under U17 as resize
    listener uses "old" value to set the row height and it doesn't see textarea content being update right now.

*   U29: I want activity name being edited to be shown even if 'Time tracking' activity is auto switched (see U3)
    and scrollable frame cannot show them both simultaneously due to a lot of over activities so that I don't spend
    time to scroll back to edited row after auto-switch happened.

*   U30: I want activity name editing in the table to be finished by ctrl+enter key stroke so that I could finish
    its editing without grabbing a mouse.

# Backlog

*   U12: As a user I want to see time I started to spend time on the activity today and time
    I finished to spend time to ???
    (is there real necessity in it?)

*   U36: I want database to be updated automatically if I start another version of timetracker.
    Database should store version information. If the version of the timetracker doesn't match 
    the version of database then upgrade/downgrade script is started.

*   U37: I want to distinguish three states of Activity: active, inactive, archived, so that I could
    better control which ones should appear on activity control list. Active activities are always appeared,
    Inactive are not appeared, but I want easily restore them back to active state. Archived activities are
    unlikely to appear on activity control list. All activities ever created in the tool will become
    archived at some point.
