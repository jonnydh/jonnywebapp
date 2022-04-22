package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

@Singleton
class DataController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def submissions() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.submissions())
  }
}

