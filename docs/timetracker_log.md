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

# 2013-Apr-06

*   U39: I want to assign parent activity to the already created activity so that I could structure activities

    *   TU39.1: Enable drag-and-drop in _activities archive tree_, log DnD events
    
        Identified "TU39.1.4: Make expand/collapse node control appear after DnD is finished" task.

# 2013-Apr-07

*   U39: I want to assign parent activity to the already created activity so that I could structure activities

    *   TU39.1: Enable drag-and-drop in _activities archive tree_, log DnD events
    
        *   TU39.1.4: Made expand/collapse node control appear after DnD is finished
            
            Actually made DnD work properly by firing treeNodesInserted inserted
            for the newly add node (due to DnD).
            
*   T9: Move project to JDK1.7
    
*   T10: Fixed tests to run together

# 2013-Apr-17

*   U39: I want to assign parent activity to the already created activity so that I could structure activities

    *   TU39.1: Enable drag-and-drop in _activities archive tree_, log DnD events

        *   TU39.1.2: Remove just copied child from its previous location

            Also implemented proper recalculation of aggregate time on
            child removal

# 2013-May-28

*   U39: I want to assign parent activity to the already created activity so that I could structure activities

    *   TU39.1: Enable drag-and-drop in _activities archive tree_, log DnD events

        *   TU39.1.5: Make drop to root level work
        
            Made it work in ActivityTreeModelTestRunner.
            But implementation problem exist: it is implemented with
            generics, but it still has unsafe conversion in 
            ActivityJTreeTransferHandler#importData when
            adding new tree child.

            Created task TU39.1.6 to address this.
            
# 2013-Jun-06

*   U39: I want to assign parent activity to the already created activity so that I could structure activities

    *   TU39.1: Enable drag-and-drop in _activities archive tree_, log DnD events

        *   TU39.1.6: Get rid of unsafe conversion in ActivityJTreeTransferHandler#importData

            Simplest solution to the problem is to admit that "View" component (`ActivityJTree` +
            `ActivityTreeModel` + `ActivityJTreeTransferHandler`) not only read the tree,
            but also moves tree nodes from one parent to another.

            This requires that `interface MovableActivityTree` is introduced and used in "View".
            This interface must have all methods required to implement node move: `setParent` and `addChild`.

            Another solution would be to left `addChild` only interface,
            but make implementation lookup node by id. Found node is of required type
            which has `setParent` and other necessary functionality.

            Development of this idea is to have `moveNode(newParent, child)` method on a "Tree".
            This seems to be more cohesive as the method is implemented on a single "Tree" object
            instead of implementing it on every "node".

            Decided to go with "simplest solution" to avoid lookup of node by id

# 2013-Jun-08

*   U39: I want to assign parent activity to the already created activity so that I could structure activities

    *   TU39.1: Enable drag-and-drop in _activities archive tree_, log DnD events

        *   TU39.1.6: Get rid of unsafe conversion in ActivityJTreeTransferHandler#importData

            Tried to introduce interface `ParentActivityTree extends ActivityTree` and
            `MovableActivityTreeNode extends ParentActivityTree, ActivityTreeNode`.
            Found out a problem:
            `ActivityTree` "read" interface has `getChildren():List[ActivityTreeNode]` method.
            In order for "view" to navigate the tree and then move nodes this method should
            return `List[MovableActivityTreeNode]`.

            Considered 3 possible solutions:

            1.  Join `ParentActivityTree` and `ActivityTree` interfaces
            2.  Add second `getMovableChildren():List[MovableActivityTreeNode]` method
                to return required type along with "read-only" `getChildren():List[ActivityTreeNode]`.
            3.  Make `ActivityTree` generic on children type, so that you can set
                `ActivityTreeNode` or `MovableActivityTreeNode`.

            Decided to go with simplest option 1. Main reason for this is that
            we only have one client using the `ActivityTree` - the "view", so we have no good reason
            to segregate `ActivityTree/ParentActivityTree`.

# 2013-Jun-08

*   U39: I want to assign parent activity to the already created activity so that I could structure activities

    *   TU39.1: Enable drag-and-drop in _activities archive tree_, log DnD events

        *   TU39.1.6: Get rid of unsafe conversion in ActivityJTreeTransferHandler#importData

            Here is an explanation why do we have no interface segregation in
            `ActivityTree/ActivityTreeNode`.

            All the methods in these interfaces could be split into these groups,
            according to their usage by client:

            1.  `getAggregateTimeSpent`, `getTimeSpent`, `getChildren` are used to read data by "view".
                (read group)
            2.  `setParent` is used by "view" to move node from one parent to another.
                (move group)
            3.  `add/removeChild` and `invalidateTree` are used by "moving" functionality by itself.
                (parent)

            Usage of tree is following: view client reads nodes, then it moves node by
            passing new parent node as an argument to `setParent` method. Then `setParent`
            functionality calls `add/remove` and `invalidateTree`. So, the same object should
            have methods of all 3 groups implemented.

            To split the interface you should rather downcast where required,
            or lookup implementation specific instance by id.
            For example, you want to move "add/remove/invalidate" to separate
            interface. When you implement "move" functionality you should
            downcast corresponding "tree" instance to "ParentTree" interface,
            or lookup "ParentTree" nodes by id and then call "add/remove/invalidate"
            on found instances.

    *   TU39.3: Handle parent in dragged to be child of a child node

        Currently this just fails with an exception.

# 2013-Jun-11

*   U39: I want to assign parent activity to the already created activity so that I could structure activities

    *   TU39.2: Implement set parent functionality on drag-n-drop event

        Implementing `DBActivityTree` which delegates to `ActivityTree` and makes record
        to database on `setParent` call requires delegating a lot of methods.
        Decided to segregate `ActivityTreeNodeMover`.

# 2013-Jun-15, Sat

*   U39: I want to assign parent activity to the already created activity so that I could structure activities

    *   TU39.2: Implement set parent functionality on drag-n-drop event

        Set parent functionality implemented, besides issue exist described in TU39.2.1

        *   TU39.2.1: Stopping timer for an activity on the control list overrides
            setting new parent for this activity

            Solved the problem by storing activity ids in "activity control list"
            and loading activity each time modification is done.

            Also added transaction on node move in ActivityTreeManager.

            Added tests on node move in ActivityTreeManagerWithRealDataTest

*   U32: I want be able to assign parent to the activity so that I could see correct activity tree in the
    "activity tree with time aggregation" view.

    Duplicate of U39.

# 2013-Jun-16, Sun

*   U38: I want to see parent task's own time as a child node in the task tree so that parent node in the tree
    displays aggregate time only

    Created less restrictive user story to handle this: U40.

    This task is moved to backlog.

*   U41: I want to see aggregate time for the whole tree

*   U40: I want to see task's own time as well as aggregate time if they differ.

# 2013-Jul-14, Sun

*   U39: I want to assign parent activity to the already created activity so that I could structure activities

    *   TU39.1: Enable drag-and-drop in _activities archive tree_, log DnD events

        *   TU39.1.3: Make all exceptions to be shown on DnD, extensively test sample app, fix all errors

# 2013-Aug-18, Sun

*   U7: As a user I want to view activities grouped in trees by days so that I could easily find activities to
    to restore them back into control list, to understand what was done on certain day, and to trigger export
    to toggl CSV for certain day.
    (depends on U32 as I need to create Activities relationship somehow prior building a tree)

    *   TU7.2: Implement "lazy" activity tree which goes to the database to load child activities information

        The tree should not reload children unless notified explicitly, but it should reload aggregate time.

        `LazyActivityTree` will use `TreeNodeLoadStrategy` to load child `ActivityTreeNode`s.
        The reason why it doesn't use DAO directly is because we have several modes of loading:

        1.  Load all child nodes.
        2.  Load nodes in time range.

        Find out that there is no pragmatic reason to make tree responsible to load itself,
        but there is a disadvantage: with tree loading itself "lazily" it is difficult
        to load whole tree in one transaction.

        The only benefit of tree loading itself is to get rid of `HashActivityTree`,
        but the same can be achieved with external load as well - TU7.5.

        Nevertheless, `TreeNodeLoadStrategy` is a good idea which can be used
        to decouple different modes of tree loading and be useful in TU7.3 and TU7.4.

        Decided to update current load routine in `ActivityTreeManager` with the strategy - TU7.6.

        Discovered a performance optimization idea while playing with "lazy nodes" + "node load strategy".
        I can load several layers of descendants while loading child nodes, thus making less database calls - T12.

*   T12: Load several layers of activity descendants to reduce number of DB queries

    Let's say I load children together with grand-children. When I can avoid database call to load children of children
    as grand-children are already loaded, but for grand children I have to make a database call again.

    Implementation hint for this is that I should use different loader to load children every time and
    I should return child nodes together with loaders for grand-children for each child node. Then
    I should use these loaders to load grand-children. If my query loads children with grand-children,
    then grand-children's loaders should by "preloaded". They just return cached grand-children instead
    of going to the DB. This scales for 3, 4 etc layers of descendants.

# 2013-Aug-25, Sun

*   U7: As a user I want to view activities grouped in trees by days so that I could easily find activities to
    to restore them back into control list, to understand what was done on certain day, and to trigger export
    to toggl CSV for certain day.
    (depends on U32 as I need to create Activities relationship somehow prior building a tree)

    *   TU7.2: Implement "lazy" activity tree which goes to the database to load child activities information

        *   TU7.2.1: Clean up git

    *   TU7.6: Update `ActivityTreeManager` to use `TreeNodeLoadStrategy` to abstract all vs time range modes of operation.

        Who loads the tree, knows its implementation. This is why it should also be responsible for implementing
        "tree node mover". It's `ActivityTreeManager` who does both know. I cannot move responsibility for loading
        tree into `TreeNodeLoadStrategy`. Deleted `TreeNodeLoadStrategy` interface and `AllTimeTreeNodeLoadStrategy`
        implementation.

    *   TU7.5: Load activity tree without accessing nodes by id in `HashMap`.

        Changed `ActivityTreeManager` to not use `HashActivityTree`. Need to clean up other (mainly test)
        usages of it.

# 2013-Aug-26, Mon

*   U7: As a user I want to view activities grouped in trees by days so that I could easily find activities to
    to restore them back into control list, to understand what was done on certain day, and to trigger export
    to toggl CSV for certain day.
    (depends on U32 as I need to create Activities relationship somehow prior building a tree)

    *   TU7.5: Load activity tree without accessing nodes by id in `HashMap`.

        Cleaned up usages of `HashActivityTree` in tests.

# 2015-Jun-28, Sun

* Restart the project, pick the priority

    Found some uncommitted changes related to the TU7.1 task:
    Create method to load tree containing activities
    having a time entry started or stopped.

    I think that continuing the story U7 is a right way to go:
    As a user I want to view activities grouped in trees by days.

    This log has story descriptions mixed with a project log and
    a TODO list. Decided to separate them - 1

* 1: Separate story descriptions, project log and TODO list

    Story descriptions grouped by their respective version numbers
    go to vX.Y.Z.md files, where X.Y.Z is the version number.

    Current project log goes to `timetracker_log.md` file,
    that's done for older versions goes to `timetracker_vX.Y.Z_log.md`
    files.

    TODO list to be tracked by [Pivotal Tracker](pivotaltracker.com)
    outside of this system. Pivotal is used to assign
    stories and tasks identifiers instead of maintainging
    `U{ID}` and `TU{ID}` prefixes manually.

    - Renamed `TODO.md` to `timetracker_log.md`.
    - Created `v0.5.md` and `v0.6.md` files, by moving text
      out of `timetracker_log.md`.
    - Renamed `DONE-0.4.md` and `DONE-0.4.1.md` to
      `timetracker_v0.4_log.md` and `timetracker_v0.4.1_log.md`

