
/**
 * Created by Silvestrov Ilya
 * Date: 9/25/11
 * Time: 5:39 PM
 */
class TogglHelper {
  static boolean splitEnds(String split) {
    if (split.endsWith('"""'))
      return true
    else if (split.endsWith('""'))
      return false
    else if (split.endsWith('"'))
      return true

    return false
  }

  static StretchModel parseTasksFromTogglCSV(String filename) {
    return parseTasksFromTogglCSV(new FileReader(new File(filename)))
  }

  static StretchModel parseTasksFromTogglCSV(Reader togglCSV) {
    StretchModel stretchModel = new StretchModel()
    stretchModel.tasks = [] as List<Task>
    togglCSV.eachLine {String line ->
      if (!line.startsWith("User,Client,Project") && !line.trim().isEmpty()) {
        List<String> splits = [] as List<String>
        String currentSplit
        line.split(',').each {String split ->
          //Begin of multi-split sentence
          if (currentSplit == null && split.startsWith('"')) {
            split = split.substring(1)
            if (splitEnds(split)) {
              split = split.substring(0, split.length() - 1)
              splits.add(split.replace('""', '"'))
              return
            }
            currentSplit = split
            return
          }

          //End of multi-split sentence
          if (currentSplit != null && splitEnds(split)) {
            split = split.substring(0, split.length() - 1)
            currentSplit += ','
            currentSplit += split
            splits.add(currentSplit.replace('""', '"'))
            currentSplit = null
            return
          }
          //Middle of multi-split sentence
          if (currentSplit != null) {
            currentSplit += ','
            currentSplit += split
          }
          //Single split sentence
          else {
            splits.add(split)
          }
        }

        Task task = new Task();
        task.projectName = splits[2]
        task.taskName = splits[3]
        task.timeSpent = TimeHelp.timeStringToMillis(splits[7])
        task.timeStretch = task.timeSpent
        task.stretchModel = stretchModel
        stretchModel.tasks.add(task)
      }
    }

    return stretchModel
  }
}
