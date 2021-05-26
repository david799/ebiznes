package models

import play.api.libs.json.Json

case class Rating(id: Int, grade: Float, description: String, order: Int)

object Rating {
  implicit val ratingFormat = Json.format[Rating]
}
