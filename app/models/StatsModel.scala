package models

import controllers.DataModel

case class StatsModel(
                       firstPost: Option[DataModel],
                       userWithMostPosts: (String, Int),
                       longestMessage: Option[(DataModel, Int)],
                       shortestMessage: Option[(DataModel, Int)],
                       postsPerUser: List[(String, Int)],
                       totalCharsPerUser: List[(String, Int)],
                       mostRecentPostByUser: List[(String, Option[DataModel])]
                     )