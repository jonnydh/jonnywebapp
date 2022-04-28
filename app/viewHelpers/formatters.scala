package viewHelpers

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object formatHelper {

  def timeFormatter(time: Option[LocalDateTime]) = {
    val timeFormatter = DateTimeFormatter.ofPattern("kk:mm:ss")
    time match {
      case None => "No Data"
      case _ => time.get.format(timeFormatter)
    }
  }

  def dateFormatter(date: Option[LocalDateTime]) = {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    date match {
      case None => "No Data"
      case _ => date.get.format(dateFormatter)
    }
  }

}
