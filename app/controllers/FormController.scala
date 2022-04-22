package controllers

import javax.inject._
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import play.api.data.validation.Constraints._
import scala.collection.mutable.ListBuffer

case class DataModel(name: String, age: String, message: String)

@Singleton
class FormController @Inject() (cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  //Fake Database
  var database = new ListBuffer[DataModel]()

  val dataForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "age" -> nonEmptyText,
      "message" -> nonEmptyText
    )(DataModel.apply)(DataModel.unapply)
  )

  def incrementor(num: Int): Int = num + 1

  def form() = Action { implicit request =>
    Ok(views.html.form(dataForm))
  }

  def formPost() = Action { implicit request =>
    val formData = dataForm.bindFromRequest.get
    database += formData
    Ok(views.html.index())
  }
}
