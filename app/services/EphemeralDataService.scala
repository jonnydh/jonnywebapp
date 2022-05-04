package services

import controllers.DataModel
import models.StatsModel

import javax.inject._
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scala.collection.mutable.ListBuffer

@Singleton
class EphemeralDataService @Inject() () {
  val database = new ListBuffer[DataModel]()

  def insert(record: DataModel): Unit = {
    database += record
  }

  def populateStats() = StatsModel(
    userWithMostPosts = userWithMostPosts(),
    firstPost = firstPost(),
    longestMessage = longestMessage(),
    shortestMessage = shortestMessage(),
    postsPerUser = postsPerUser(),
    totalCharsPerUser = totalCharsPerUser(),
    mostRecentPostByUser = mostRecentPostByUser()
  )

  def search(maybeName: Option[String] = None, maybeAge: Option[String], maybeMessage: Option[String]): ListBuffer[DataModel] = {
    val predicates: List[DataModel => Boolean] = List(
      maybeName.filter(_.nonEmpty).map(name => (record: DataModel) => record.name.equalsIgnoreCase(name)),
      maybeAge.filter(_.nonEmpty).map(age => (record: DataModel) => record.age.equals(age.toInt)),
      maybeMessage.filter(_.nonEmpty).map(message => (record: DataModel) => record.message.contains(message))
    ).flatten

    database.filter(record => predicates.forall(predicate => predicate(record)))
  }

  def firstPost(): Option[DataModel] = database.headOption

  def userWithMostPosts(): (String, Int) = database.groupBy(identity => identity.name)
    .view.mapValues(_.size)
    .maxByOption(_._2)
    .getOrElse(("No Data", 0))


  def longestMessage(): Option[(DataModel, Int)] = {
    val post: Option[DataModel] = database
      .foldLeft(Option.empty[DataModel]) {(acc, cur) => acc match {
        case None => Option(cur)
        case Some(prev) => if (cur.message.length > prev.message.length) Some(cur) else acc
      }}

    post.map(p => (p, p.message.length))
  }

  def shortestMessage(): Option[(DataModel, Int)] = {
    val post = database.minByOption(record => record.message.length)
    post.map(p => (p, p.message.length))
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

  def mostRecentPostByUser(): List[(String, Option[DataModel])] = database.groupBy(identity => identity.name)
    .view.mapValues(list => list.foldLeft(Option.empty[DataModel]) {(acc, cur) => acc match {
      case None => Option(cur)
      case Some(prev) => if (cur.timestamp.get.isAfter(prev.timestamp.get)) Some(cur) else acc
    }}).toList

}
