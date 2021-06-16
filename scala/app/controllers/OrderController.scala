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
      val tempProduct = Await.ready(productRepository.getById(orderedProduct.product), Duration.Inf).value.get.get
      products = tempProduct :: products
    }
    val orderForFrontend = OrderForFrontend(id, address.name, address.addressLine1, address.addressLine2, products)

    Ok(Json.toJson(orderForFrontend))
  }

  def delete(id: Int): Action[AnyContent] = Action {
    orderRepository.delete(id)
    Redirect("/orders")
  }

  def addOrder(): Action[AnyContent] = Action { implicit request =>
    val orderJson = request.body.asJson.get
    val orderContent = orderJson.as[OrderContentForm]
    val customer: Customer = Await.ready(customerRepository.create(orderContent.name), Duration.Inf).value.get.get
    val address: Address = Await.ready(addressRepository.create(orderContent.name, customer.id, orderContent.addressLine1, orderContent.addressLine2), Duration.Inf).value.get.get
    val order: Order = Await.ready(orderRepository.create(customer.id, address.id), Duration.Inf).value.get.get

    for (product_id <- orderContent.products){
      Await.ready(orderedProductRepository.create(order.id, product_id), Duration.Inf).value.get.get
    }

    Ok(order.id.toString)
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
