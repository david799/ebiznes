package models

import play.api.libs.json.Json

case class Return(id: Int, description: String, order: Int)

object Return {
  implicit val returnFormat = Json.format[Return]
}
