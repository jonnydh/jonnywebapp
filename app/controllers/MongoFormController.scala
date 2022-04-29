package controllers

import javax.inject._
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import play.api.data.validation.Constraints._

import scala.collection.mutable.ListBuffer
import java.time.LocalDateTime

case class MongoModel(name: String, age: Int, message: String, timestamp: Option[LocalDateTime])

@Singleton
class MongoFormController @Inject() (cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  val document = Form(
    mapping(
      "name" -> nonEmptyText(maxLength = 100),
      "age" -> number(min = 0, max = 123),
      "message" -> nonEmptyText(maxLength = 500),
      "timestamp" -> optional(localDateTime),
    )(MongoModel.apply)(MongoModel.unapply)
  )

  def form() = Action { implicit request =>
    Ok(views.html.mongoForm())
  }

  def formPost() = Action { implicit request =>
    Ok(views.html.mongoForm())
  }

}
