package controllers

import javax.inject._
import play.api._
import play.api.mvc._


@Singleton
class FormController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def form() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.form())
  }
}
