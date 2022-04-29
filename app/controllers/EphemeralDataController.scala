package controllers

import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import services.EphemeralDataService

import javax.inject.{Inject, Singleton}

@Singleton
class EphemeralDataController @Inject()(ephemeralDataService: EphemeralDataService, cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def submissions(name: Option[String] = None, age: Option[String], message: Option[String])  = Action { implicit request: Request[AnyContent] =>
    val queryResult = ephemeralDataService.search(name, age, message)
    Ok(views.html.ephemeralSubmissions(queryResult))
  }

  def stats() = Action {implicit request: Request[AnyContent] =>
    Ok(views.html.ephemeralStats(ephemeralDataService.populateStats())
    )
  }
}
