package services

import controllers.DataModel
import javax.inject._

import scala.collection.mutable.ListBuffer

@Singleton
class DataService @Inject() () {
  val database = new ListBuffer[DataModel]()

  def insert(record: DataModel): Unit = {
    database += record
    println("State of database after insert:")
    println(database)
    println(this)
  }

  def search(maybeName: Option[String] = None): ListBuffer[DataModel] = {
    println("State of database before search:")
    println(database)
    println(this)
    maybeName.fold(database)(name => database.filter(_.name.equalsIgnoreCase(name)))

  }
}
