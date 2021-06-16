package models

import play.api.libs.json._

case class Address(id: Int, name: String, customer: Int, addressLine1: String, addressLine2: String)

object Address {
  implicit val addressFormat: OFormat[Address] = Json.format[Address]
}

