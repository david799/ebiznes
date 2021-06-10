package models

import play.api.libs.json._

case class Address(id: Int, name: String, customer: Int, address_line1: String, address_line2: String)

object Address {
  implicit val addressFormat: OFormat[Address] = Json.format[Address]
}

