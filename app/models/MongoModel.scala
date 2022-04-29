package models

import java.time.LocalDateTime
import play.api.libs.functional.syntax._
import play.api.libs.json.{Format, __}

case class MongoModel(name: String, age: Int, message: String, timestamp: Option[LocalDateTime])

object MongoModel {

  val mongoFormat: Format[MongoModel] = {
  ((__ \ "name").format[String]
  ~ (__ \ "age").format[Int]
  ~ (__ \ "message").format[String]
  ~ (__ \ "timestamp").formatNullable[LocalDateTime]
    ) (MongoModel.apply, unlift(MongoModel.unapply))
  }

}