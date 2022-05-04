package models

import scala.concurrent.Future

case class MongoStatsModel(firstPost: Option[MongoModel],
                           mostPosts: Option[(String, Integer)],
                           longestMessage: Option[MongoModel],
                           shortestMessage: Option[MongoModel],
                           postsPerUser: Seq[(String, Integer)],
                           totalCharsPerUser: Seq[(String, Integer)],
                           mostRecentPostByUser: Seq[MongoModel]
                          )

