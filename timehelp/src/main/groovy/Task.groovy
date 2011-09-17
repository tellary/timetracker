class Task {
	String projectName
	String taskName
	float timeSpent
  float timeStretch
	boolean noStretch = false
  StretchModel stretchModel

  void setTimeStretch(float timeStretch) {
    this.timeStretch = timeStretch
    if (stretchModel != null)
      TimeHelp.stretchTasks(stretchModel)
  }

  void setNoStretch(boolean noStretch) {
    this.noStretch = noStretch
    if (stretchModel != null)
      TimeHelp.stretchTasks(stretchModel)
  }
}