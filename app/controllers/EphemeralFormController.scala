package controllers

import javax.inject._
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import play.api.data.validation.Constraints._
import services.EphemeralDataService

import scala.collection.mutable.ListBuffer
import java.time.LocalDateTime

case class DataModel(name: String, age: Int, message: String, timestamp: Option[LocalDateTime])

@Singleton
class EphemeralFormController @Inject() (ephemeralDataService: EphemeralDataService, cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  val dataForm = Form(
    mapping(
      "name" -> nonEmptyText(maxLength = 100),
      "age" -> number(min = 0, max = 123),
      "message" -> nonEmptyText(maxLength = 500),
      "timestamp" -> optional(localDateTime),
    )(DataModel.apply)(DataModel.unapply)
  )

  def incrementor(num: Int): Int = num + 1

  def form() = Action { implicit request =>
    Ok(views.html.ephemeralForm(dataForm))
  }

  def formPost() = Action { implicit request =>
    dataForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.ephemeralForm(formWithErrors))
      },
      formSuccess => {
        val dateTime = LocalDateTime.now()
        val timestampedForm = formSuccess.copy(timestamp = Option(dateTime))
        ephemeralDataService.insert(timestampedForm)
        Redirect(routes.HomeController.index())
      }
    )
  }

}
