package controllers

import persistence.PostRepo
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class MongoDataController @Inject()(cc: ControllerComponents, postRepo: PostRepo)(implicit ec: ExecutionContext) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def submissions(name: Option[String] = None, age: Option[String], message: Option[String])  = Action.async { implicit request: Request[AnyContent] =>
    postRepo.findAll().map(data => Ok(views.html.mongoSubmissions(data)))
  }

  def stats() = Action {implicit request: Request[AnyContent] =>
    //Ok(views.html.mongoStats(StatsModel = ))
    ???
  }
}
