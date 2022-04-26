package controllers

import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import services.DataService

import javax.inject.{Inject, Singleton}

@Singleton
class DataController @Inject() (dataService: DataService, cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def submissions(name: Option[String] = None)  = Action { implicit request: Request[AnyContent] =>
    val queryName = request.getQueryString(key="name")
    val queryAge = request.getQueryString(key="age")
    val queryMessage = request.getQueryString(key="message")
    val queryResult = dataService.search(queryName, queryAge, queryMessage)
    Ok(views.html.submissions(queryResult))
  }
}
