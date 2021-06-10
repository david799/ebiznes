package models

import play.api.libs.json._

case class Payment(id: Int, order: Int, amount: Float)

object Payment {
  implicit val paymentFormat: OFormat[Payment] = Json.format[Payment]
}

