package controllers
import models.{MongoModel, MongoStatsModel}
import persistence.PostRepo
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MongoDataController @Inject()(cc: ControllerComponents, postRepo: PostRepo)(implicit ec: ExecutionContext) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def submissions(name: Option[String] = None, age: Option[String], message: Option[String])  = Action.async { implicit request: Request[AnyContent] =>
    postRepo.search(name, age, message).map(data => Ok(views.html.mongoSubmissions(data)))
  }

  def stats() = Action.async {implicit request: Request[AnyContent] =>

    for {
      firstPost            <- postRepo.firstPost()
      mostPosts            <- postRepo.userWithMostPosts()
      longestMessage       <- postRepo.longestMessage()
      shortestMessage      <- postRepo.shortestMessage()
      postsPerUser         <- postRepo.postsPerUser()
      totalCharsPerUser    <- postRepo.totalCharsPerUser()
      mostRecentPostByUser <- postRepo.mostRecentPostByUser()
      processedData        = MongoStatsModel(firstPost, mostPosts, longestMessage, shortestMessage, postsPerUser, totalCharsPerUser, mostRecentPostByUser)
      template             =  views.html.mongoStats(processedData)
    } yield Ok(template)
  }
}
