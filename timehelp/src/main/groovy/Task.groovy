class Task {
  String projectName
  String taskName
  long timeSpent
  long timeStretch
  boolean noStretch = false
  StretchModel stretchModel
  List<TaskModificationListener> taskModificationListeners = []

  private void notifyListeners() {
    for (TaskModificationListener listener: taskModificationListeners) {
      listener.taskModified(this)
    }
  }

  void setTimeStretch(long timeStretch) {
    this.timeStretch = timeStretch
    if (stretchModel != null) {
      noStretch = true
      TimeHelp.stretchTasks(stretchModel)
      notifyListeners()
    }
  }

  void setNoStretch(boolean noStretch) {
    this.noStretch = noStretch
    if (stretchModel != null) {
      TimeHelp.stretchTasks(stretchModel)
    }
  }

  void doStretch(float coefficient) {
    if (!noStretch) {
      if (coefficient < 1.0)
        timeStretch = timeSpent
      else
        timeStretch = timeSpent * coefficient
      notifyListeners()
    }
  }
}