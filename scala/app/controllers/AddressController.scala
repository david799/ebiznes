package controllers

import javax.inject._
import models.{Address, AddressRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}

@Singleton
class AddressController @Inject()(addressRepository: AddressRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val productForm: Form[CreateAddressForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "customer" -> number,
      "address_line1" -> nonEmptyText,
      "address_line2" -> nonEmptyText
    )(CreateAddressForm.apply)(CreateAddressForm.unapply)
  }

  def getAddresses: Action[AnyContent] = Action.async { implicit request =>
    val temp = addressRepository.list()
    temp.map( addresses => Ok(Json.toJson(addresses)).as("application/json"))
  }

  def getAddress(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val temp = addressRepository.getByIdOption(id)
    temp.map(addresses => addresses match {
      case Some(p) => Ok(Json.toJson(p))
      case None => Redirect(routes.AddressController.getAddresses())
    })
  }

  def delete(id: Int): Action[AnyContent] = Action {
    addressRepository.delete(id)
    Redirect("/categories")
  }

  def addAddress(): Action[AnyContent] = Action { implicit request =>
    val address_json = request.body.asJson.get
    val address = address_json.as[Address]
    addressRepository.create(address.name, address.customer, address.address_line1, address.address_line2)
    Ok("Created")
  }
}

case class CreateAddressForm(name: String, customer: Int, address_line1: String, address_line2: String)
