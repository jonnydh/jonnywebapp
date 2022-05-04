package persistence
import controllers.DataModel
import models.{MongoModel, MongoStatsModel}
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.model.Aggregates.{addFields, group, project, sort}
import org.mongodb.scala.model.{Accumulators, Field, Filters, Projections, Sorts}
import org.mongodb.scala.result.InsertOneResult
import org.mongodb.scala.model.Filters.{and, equal, regex}
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

import javax.inject.Inject
import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContext, Future}


class PostRepo @Inject()(mongoComponent: MongoComponent)(implicit ec: ExecutionContext) extends PlayMongoRepository[MongoModel](
    collectionName = "posts",
    mongoComponent = mongoComponent,
    domainFormat   = MongoModel.mongoFormat,
    indexes        = Seq.empty
) {

// queries and updates can now be implemented with the available `collection: org.mongodb.scala.MongoCollection`
  def findAll(): Future[Seq[MongoModel]] = {
    collection.find().toFuture
  }

  def save(post: MongoModel): Future[Unit] = {
    collection.insertOne(post).toFuture().map(_ => ())
  }

  def search(maybeName: Option[String] = None, maybeAge: Option[String] = None, maybeMessage: Option[String] = None): Future[Seq[MongoModel]] = {
    def handleEmptyParam(param: Option[String]): Option[String] = {
        param.filter(p => p != "")
    }

    val filters = Seq(
      handleEmptyParam(maybeName).map(n => Filters.equal("name", n)),
      handleEmptyParam(maybeAge).map(a => Filters.equal("age", a.toInt)),
      handleEmptyParam(maybeMessage).map(m => Filters.equal("message", m))
    ).flatten

    filters match {
      case Nil => collection.find().toFuture()
      case params => collection.find(Filters.and(params:_*)).toFuture()
    }
  }

  def firstPost(): Future[Option[MongoModel]] = collection
    .find()
    .first()
    .toFutureOption()

  def userWithMostPosts(): Future[Option[(String, Integer)]] = mongoComponent.database.getCollection("posts")
    .aggregate(Seq(
      group("$name", Accumulators.sum("postsByUser", 1)),
      sort(Sorts.descending("postsByUser"))
  ))
    .first().
    map(
      document => (document.getString("_id"), document.getInteger("postsByUser"))
    ).headOption()

  def longestMessage(): Future[Option[MongoModel]] = collection
    .aggregate(Seq(
      addFields(Field("MessageSize", BsonDocument("$strLenCP" -> "$message"))),
      sort(Sorts.descending("MessageSize")),
    ))
    .first()
    .headOption()

  def shortestMessage(): Future[Option[MongoModel]] = collection
    .aggregate(Seq(
      addFields(Field("MessageSize", BsonDocument("$strLenCP" -> "$message"))),
      sort(Sorts.ascending("MessageSize")),
    ))
    .headOption()

  def postsPerUser(): Future[Seq[(String, Integer)]] = mongoComponent.database.getCollection("posts")
    .aggregate(Seq(
      group("$name", Accumulators.sum("postsByUser", 1)),
      sort(Sorts.descending("postsByUser"))
    )).map(doc => (doc.getString("_id"), doc.getInteger("postsByUser")))
    .toFuture()

  def totalCharsPerUser(): Future[Seq[(String, Integer)]] = mongoComponent.database.getCollection("posts")
    .aggregate(Seq(
      addFields(Field("MessageSize", BsonDocument("$strLenCP" -> "$message"))),
      group("$name", Accumulators.sum("MessageSize", "$MessageSize")),
      sort(Sorts.descending("MessageSize"))
    ))
    .map(doc => (doc.getString("_id"), doc.getInteger("MessageSize")))
    .toFuture()

  def mostRecentPostByUser(): Future[Seq[MongoModel]] = collection
    .aggregate(Seq(
      sort(Sorts.descending("timestamp")),
      group("$name",
        Accumulators.first("timestamp", "$timestamp"),
        Accumulators.first("message", "$message"),
        Accumulators.first("age", "$age"),
        Accumulators.first("name", "$name")
      ),
      sort(Sorts.descending("timestamp"))
    )).toFuture()
}
