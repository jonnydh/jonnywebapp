package controllers

import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import services.DataService

import javax.inject.{Inject, Singleton}

@Singleton
class DataController @Inject() (dataService: DataService, cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def submissions(name: Option[String] = None, age: Option[String], message: Option[String])  = Action { implicit request: Request[AnyContent] =>
    val queryResult = dataService.search(name, age, message)
    Ok(views.html.submissions(queryResult))
  }
}
