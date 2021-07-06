package controllers

import javax.inject._
import models.{Customer, CustomerRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class CustomerController @Inject()(customerRepository: CustomerRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val productForm: Form[CreateCustomerForm] = Form {
    mapping(
      "nick" -> nonEmptyText,
    )(CreateCustomerForm.apply)(CreateCustomerForm.unapply)
  }

  def getCustomers: Action[AnyContent] = Action.async { implicit request =>
    val customers = customerRepository.list()

    customers.map( customers => Ok(Json.toJson(customers)).as("application/json"))
  }

  def getCustomer(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val temp = customerRepository.getByIdOption(id)
    temp.map(customers => customers match {
      case Some(p) => Ok(Json.toJson(p))
      case None => BadRequest("Does not exist")
    })
  }

  def delete(id: Int): Action[AnyContent] = Action {
    customerRepository.delete(id)
    Redirect("/customers")
  }

  def addCustomer(): Action[AnyContent] = Action { implicit request =>
    val customerJson = request.body.asJson.get
    val customer = customerJson.as[Customer]
    customerRepository.create(customer.nick)
    Ok("Created")
  }
}

case class CreateCustomerForm(nick: String)
