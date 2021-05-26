package models

import play.api.libs.json._

case class OrderedProduct(id: Int, order: Int, product: Int)

object OrderedProduct {
  implicit val orderFormat: OFormat[OrderedProduct] = Json.format[OrderedProduct]
}

