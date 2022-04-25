package controllers

import javax.inject._
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import play.api.data.validation.Constraints._
import scala.collection.mutable.ListBuffer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

case class DataModel(name: String, age: String, message: String, timestamp: Option[LocalDateTime])

@Singleton
class FormController @Inject() (cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  //Fake Database
  val database = new ListBuffer[DataModel]()

  val dataForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "age" -> nonEmptyText,
      "message" -> nonEmptyText,
      "timestamp" -> optional(localDateTime),
    )(DataModel.apply)(DataModel.unapply)
  )

  def incrementor(num: Int): Int = num + 1

  def form() = Action { implicit request =>
    Ok(views.html.form(dataForm))
  }

  def formPost() = Action { implicit request =>
    val formData = dataForm.bindFromRequest.get
    val dateTime = LocalDateTime.now()
    val timestampedForm = formData.copy(timestamp = Option(dateTime))
    database += timestampedForm
    Ok(views.html.index())
  }

  def submissions() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.submissions(database))
  }
}
