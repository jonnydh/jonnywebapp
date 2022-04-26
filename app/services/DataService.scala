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

  def search(maybeName: Option[String] = None): ListBuffer[DataModel] = {
    maybeName.fold(database)(name => database.filter(_.name.equalsIgnoreCase(name)))
  }
}
