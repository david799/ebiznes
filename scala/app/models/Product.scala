package models

import play.api.libs.json.{Json, OFormat}

case class Product(id: Int, name: String, description: String, category: Int, price: Float)

object Product {
  implicit val productFormat: OFormat[Product] = Json.format[Product]
}
