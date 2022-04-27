package services

import controllers.DataModel
import javax.inject._

import scala.collection.mutable.ListBuffer

@Singleton
class DataService @Inject() () {
  val database = new ListBuffer[DataModel]()

  def insert(record: DataModel): Unit = {
    database += record
  }

  def isEmpty(): Boolean = {
    database.isEmpty
  }

  def search(maybeName: Option[String] = None, maybeAge: Option[String], maybeMessage: Option[String]): ListBuffer[DataModel] = {
    val predicates: List[DataModel => Boolean] = List(
      maybeName.filter(_.nonEmpty).map(name => (record: DataModel) => record.name.equalsIgnoreCase(name)),
      maybeAge.filter(_.nonEmpty).map(age => (record: DataModel) => record.age.equals(age.toInt)),
      maybeMessage.filter(_.nonEmpty).map(message => (record: DataModel) => record.message.contains(message))
    ).flatten

    database.filter(record => predicates.forall(predicate => predicate(record)))
  }

  def firstPost(): DataModel = database.head

  def userWithMostPosts(): (String, Int) = database.groupBy(identity => identity.name)
    .view.mapValues(_.size)
    .maxBy(_._2)

  def longestMessage(): (DataModel, Int) = {
    val post = database.sortBy(record => record.message.length()).last
    val messageLength = post.message.length
    (post, messageLength)
  }

  def shortestMessage(): (DataModel, Int) = {
    val post = database.sortBy(record => record.message.length()).head
    val messageLength = post.message.length
    (post, messageLength)
  }

  def postsPerUser(): List[(String, Int)] = database.groupBy(identity => identity.name)
    .view.mapValues(_.size)
    .toList
    .sortBy(_._2)
    .reverse

  def totalCharsPerUser(): List[(String, Int)] = database.groupBy(identity => identity.name)
    .view.mapValues(groupedByName => groupedByName.map(record => record.message.length))
    .toList
    .map(tup => (tup._1, tup._2.reduce((a,b) => a + b)))
    .sortBy(_._2)
    .reverse

}
