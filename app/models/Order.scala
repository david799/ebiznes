package models

import play.api.libs.json._

case class Order(id: Int, customer: Int, address: Int)

object Order {
  implicit val orderFormat: OFormat[Order] = Json.format[Order]
}

