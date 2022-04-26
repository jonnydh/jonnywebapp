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

  def search(maybeName: Option[String] = None, maybeAge: Option[String], maybeMessage: Option[String]): ListBuffer[DataModel] = {
    val cleansedMaybeName = convertEmptyInputToNone(maybeName)
    val cleansedMaybeAge = convertEmptyInputToNone(maybeAge)
    val cleansedMaybeMessage = convertEmptyInputToNone(maybeMessage)

    val predicates: List[DataModel => Boolean] = List(
      cleansedMaybeName.map(name => (record: DataModel) => record.name.equalsIgnoreCase(name)),
      cleansedMaybeAge.map(age => (record: DataModel) => record.age.equals(age.toInt)),
      cleansedMaybeMessage.map(message => (record:DataModel) => record.message.contains(message))
    ).flatten

    database.filter(record => predicates.forall(predicate => predicate(record)))
  }

  def convertEmptyInputToNone(input: Option[String]): Option[String] = input.filter(_.nonEmpty)
}

