package persistence
import models.MongoModel
import org.mongodb.scala.result.InsertOneResult
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

import javax.inject.Inject
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


}





