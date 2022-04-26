package controllers

import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import services.DataService

import javax.inject.{Inject, Singleton}

@Singleton
class DataController @Inject() (dataService: DataService, cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def submissions(name: Option[String] = None)  = Action { implicit request: Request[AnyContent] =>
    val query = request.getQueryString(key="name")
    val queryResult = dataService.search(query)
    Ok(views.html.submissions(queryResult))
  }

}
