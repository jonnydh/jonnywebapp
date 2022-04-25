package services

import controllers.DataModel

import scala.collection.mutable.ListBuffer

class DataService {
  val database = new ListBuffer[DataModel]()

  def insert(record: DataModel): Unit = {
    database += record
  }

  def search(maybeName: Option[String] = None): ListBuffer[DataModel] = {
    maybeName.fold(database)(name => database.filter(_.name.equalsIgnoreCase(name)))
  }
}
