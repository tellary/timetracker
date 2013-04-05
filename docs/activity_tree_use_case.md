# Activity tree use cases

## Change activity parent use case

1.  User drags an activity in `ActivityTreeView` to have new parent
2.  Activity tree controller invokes 
    [change activity parent procedure](#change-activity-parent-procedure) on `ActivityTreeModel`
3.  Activity tree controller initiates the [redraw of the `ActivityTreeView`](#redraw-ActivityTreeView)


## Change activity parent procedure

<a id="change-activity-parent-procedure"/>

1.  `ActivityTreeModel` calls `ActivityDao#setParent` method to set new parent for the activity
2.  `ActivityTreeModel` finds `LazyActivityTreeNode` for the old parent of the activity and removes
    a corresponding child from it.
3.  Old parent `LazyActivityTreeNode` marks itself as aggregate time invalid and makes the
    recursive invalidation call to its parent.
4.  `ActivityTreeModel` finds `LazyActivityTreeNode` for the new parent of the activity and adds the
    child `LazyActivityTreeNode` to the list of new parent's children
5.  New parent `LazyActivityTreeNode` marks itself as aggregate time invalid and makes the
    recursive invalidation call to its parent.


## Redraw of ActivityTreeView

<a id="redraw-ActivityTreeView"/>
`ActivityTreeView` walks over `LazyActivityTreeNode`s in recursive order and draws them
by calling `getName`, `getAggregateTimeSpent` and `getTimeSpent` functions.

## Call getAggregateTimeSpent on LazyActivityTreeNode

1. `LazyActivityTreeNode` checks if it is not aggregate time valid
2.  For each child node it calls `getAggregateTimeSpent` and sums up the results as `aggregateTimeSpent`
3.  It adds its own `timeSpent` to the `aggregateTimeSpent`
4.  It stores and returns the `aggregateTimeSpent`

### Extensions

1.  a.  If LazyActivityTreeNode is aggregate time valid then it continues from step 3.

