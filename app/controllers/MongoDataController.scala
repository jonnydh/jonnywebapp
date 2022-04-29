package controllers

import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

import javax.inject.{Inject, Singleton}

@Singleton
class MongoDataController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def submissions(name: Option[String] = None, age: Option[String], message: Option[String])  = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.mongoSubmissions())
  }

  def stats() = Action {implicit request: Request[AnyContent] =>
    Ok(views.html.mongoStats())
  }
}
