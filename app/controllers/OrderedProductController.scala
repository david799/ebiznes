package controllers

import javax.inject._
import models.{OrderedProduct, OrderedProductRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class OrderedProductController @Inject()(orderedProductRepository: OrderedProductRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val productForm: Form[CreateOrderedProductForm] = Form {
    mapping(
      "order" -> number,
      "product" -> number,
    )(CreateOrderedProductForm.apply)(CreateOrderedProductForm.unapply)
  }

  def getOrderedProducts: Action[AnyContent] = Action.async { implicit request =>
    val temp = orderedProductRepository.list()
    temp.map( orderedProducts => Ok(Json.toJson(orderedProducts)).as("application/json"))
  }

  def getOrderedProduct(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val temp = orderedProductRepository.getByIdOption(id)
    temp.map(orderedProducts => orderedProducts match {
      case Some(p) => Ok(Json.toJson(p))
      case None => Redirect(routes.OrderedProductController.getOrderedProducts())
    })
  }

  def delete(id: Int): Action[AnyContent] = Action {
    orderedProductRepository.delete(id)
    Redirect("/orderedproducts")
  }

  def addOrderedProduct(): Action[AnyContent] = Action { implicit request =>
    val orderedproduct_json = request.body.asJson.get
    val orderedproduct = orderedproduct_json.as[OrderedProduct]
    orderedProductRepository.create(orderedproduct.order, orderedproduct.product)
    Ok("Created")
  }
}

case class CreateOrderedProductForm(order: Int, product: Int)
