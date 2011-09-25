/**
 * Created by Silvestrov Ilya
 * Date: 9/14/11
 * Time: 12:48 AM
 */
class StretchModel {
  List<Task> tasks
  long timeInOffice
  Calendar date
  long alreadyReported

  void setTimeInOffice(long timeInOffice) {
    this.timeInOffice = timeInOffice
    TimeHelp.stretchTasks(this)
  }
}
