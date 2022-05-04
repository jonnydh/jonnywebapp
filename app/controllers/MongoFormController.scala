package controllers

import models.MongoModel
import persistence.PostRepo

import javax.inject._
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import play.api.data.validation.Constraints._

import scala.collection.mutable.ListBuffer
import java.time.LocalDateTime
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MongoFormController @Inject() (cc: ControllerComponents, postRepo: PostRepo)(implicit ec: ExecutionContext) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  val document = Form(
    mapping(
      "name" -> nonEmptyText(maxLength = 100),
      "age" -> number(min = 0, max = 123),
      "message" -> nonEmptyText(maxLength = 500),
      "timestamp" -> optional(localDateTime),
    )(MongoModel.apply)(MongoModel.unapply)
  )

  def form() = Action { implicit request =>
    Ok(views.html.mongoForm(document))
  }

  def formPost() = Action.async { implicit request =>
    document.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.mongoForm(formWithErrors)))
      },
      formSuccess => {
        val dateTime = LocalDateTime.now()
        val timestampedForm = formSuccess.copy(timestamp = Option(dateTime))
        postRepo.save(timestampedForm).map(_ => Redirect(routes.HomeController.index()))
      }
    )
  }

}
