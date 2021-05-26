package models

import play.api.libs.json._

case class Customer(id: Int, nick: String)

object Customer {
  implicit val customerFormat: OFormat[Customer] = Json.format[Customer]
}

