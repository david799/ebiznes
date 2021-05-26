package models

import play.api.libs.json._

case class Coupon(id: Int, name: String, discount: Int)

object Coupon {
  implicit val couponFormat: OFormat[Coupon] = Json.format[Coupon]
}

