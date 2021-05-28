package controllers

import javax.inject._
import models.{Order, OrderRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class OrderController @Inject()(orderRepository: OrderRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val productForm: Form[CreateOrderForm] = Form {
    mapping(
      "customer" -> number,
      "address" -> number,
    )(CreateOrderForm.apply)(CreateOrderForm.unapply)
  }

  def getOrders: Action[AnyContent] = Action.async { implicit request =>
    val temp = orderRepository.list()
    temp.map( orders => Ok(Json.toJson(orders)).as("application/json"))
  }

  def getOrder(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val temp = orderRepository.getByIdOption(id)
    temp.map(orders => orders match {
      case Some(p) => Ok(Json.toJson(p))
      case None => Redirect(routes.OrderController.getOrders())
    })
  }

  def delete(id: Int): Action[AnyContent] = Action {
    orderRepository.delete(id)
    Redirect("/orders")
  }

  def addOrder(): Action[AnyContent] = Action { implicit request =>
    val order_json = request.body.asJson.get
    val order = order_json.as[Order]
    orderRepository.create(order.customer, order.address)
    Ok("Created")
  }
}

case class CreateOrderForm(customer: Int, address: Int)
