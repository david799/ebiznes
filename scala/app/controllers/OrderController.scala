package controllers

import javax.inject._
import models.{Order, OrderRepository, Customer, CustomerRepository, Address, AddressRepository, OrderedProduct, OrderedProductRepository, ProductRepository, Product}
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.{Json, OFormat}
import play.api.mvc._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}

@Singleton
class OrderController @Inject()(orderRepository: OrderRepository, customerRepository: CustomerRepository,
                                addressRepository: AddressRepository, orderedProductRepository: OrderedProductRepository,
                                productRepository: ProductRepository, cc: MessagesControllerComponents)
                               (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val productForm: Form[CreateOrderForm] = Form {
    mapping(
      "customer" -> number,
      "address" -> number,
    )(CreateOrderForm.apply)(CreateOrderForm.unapply)
  }

  def getOrders: Action[AnyContent] = Action.async { implicit request =>
    val orders = orderRepository.list()
    orders.map( orders => Ok(Json.toJson(orders)).as("application/json"))
  }

  def getOrder(id: Int): Action[AnyContent] = Action  { implicit request =>
    val order: Order = Await.ready(orderRepository.getById(id), Duration.Inf).value.get.get
    val address = Await.ready(addressRepository.getById(order.address), Duration.Inf).value.get.get
    val orderedProducts = Await.ready(orderedProductRepository.getOrderProducts(id), Duration.Inf).value.get.get
    var products = List[Product]()
    for (orderedProduct <- orderedProducts){
      val temp_product = Await.ready(productRepository.getById(orderedProduct.product), Duration.Inf).value.get.get
      products = temp_product :: products
    }
    val orderForFrontend = OrderForFrontend(id, address.name, address.address_line1, address.address_line2, products)

    Ok(Json.toJson(orderForFrontend))
  }

  def delete(id: Int): Action[AnyContent] = Action {
    orderRepository.delete(id)
    Redirect("/orders")
  }

  def addOrder(): Action[AnyContent] = Action { implicit request =>
    val order_json = request.body.asJson.get
    val order_content = order_json.as[OrderContentForm]
    val customer: Customer = Await.ready(customerRepository.create(order_content.name), Duration.Inf).value.get.get
    val address: Address = Await.ready(addressRepository.create(order_content.name, customer.id, order_content.addressLine1, order_content.addressLine2), Duration.Inf).value.get.get
    val order: Order = Await.ready(orderRepository.create(customer.id, address.id), Duration.Inf).value.get.get

    for (product_id <- order_content.products){
      Await.ready(orderedProductRepository.create(order.id, product_id), Duration.Inf).value.get.get
    }

    Ok("Created")
  }
}

case class CreateOrderForm(customer: Int, address: Int)

object OrderContentForm {
  implicit val orderFormat: OFormat[OrderContentForm] = Json.format[OrderContentForm]
}
case class OrderContentForm(name: String, addressLine1: String, addressLine2: String, products: List[Int])

object OrderForFrontend {
  implicit val orderFormat: OFormat[OrderForFrontend] = Json.format[OrderForFrontend]
}
case class OrderForFrontend(id: Int, name:String, addressLine1: String, addressLine2: String, products: List[Product])
