package viewHelpers

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object formatHelper {

  def timeFormatter(time: Option[LocalDateTime]) = {
    val timeFormatter = DateTimeFormatter.ofPattern("kk:mm:ss")
    time.get.format(timeFormatter)
  }

  def dateFormatter(date: Option[LocalDateTime]) = {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    date.get.format(dateFormatter)
  }

}
