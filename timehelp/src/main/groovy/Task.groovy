class Task {
	String projectName
	String taskName
	float timeSpent
  float timeStretch
	boolean noStretch = false
  StretchModel stretchModel
  List<TaskModificationListener> taskModificationListeners = []

  private void notifyListeners() {
    for (TaskModificationListener listener: taskModificationListeners) {
      listener.taskModified(this)
    }
  }

  void setTimeStretch(float timeStretch) {
    this.timeStretch = timeStretch
    if (stretchModel != null) {
      TimeHelp.stretchTasks(stretchModel)
      notifyListeners()
    }
  }

  void setNoStretch(boolean noStretch) {
    this.noStretch = noStretch
    if (stretchModel != null) {
      TimeHelp.stretchTasks(stretchModel)
      notifyListeners()
    }
  }
}