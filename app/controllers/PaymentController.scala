package controllers

import javax.inject._
import models.{Payment, PaymentRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats.floatFormat
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class PaymentController @Inject()(paymentRepository: PaymentRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val productForm: Form[CreatePaymentForm] = Form {
    mapping(
      "order" -> number,
      "amount" -> of[Float]
    )(CreatePaymentForm.apply)(CreatePaymentForm.unapply)
  }

  def getPayments: Action[AnyContent] = Action.async { implicit request =>
    val temp = paymentRepository.list()
    temp.map( payments => Ok(Json.toJson(payments)).as("application/json"))
  }

  def getPayment(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val temp = paymentRepository.getByIdOption(id)
    temp.map(payments => payments match {
      case Some(p) => Ok(Json.toJson(p))
      case None => Redirect(routes.PaymentController.getPayments())
    })
  }

  def delete(id: Int): Action[AnyContent] = Action {
    paymentRepository.delete(id)
    Redirect("/payments")
  }

  def addPayment(): Action[AnyContent] = Action { implicit request =>
    val payment_json = request.body.asJson.get
    val payment = payment_json.as[Payment]
    paymentRepository.create(payment.order, payment.amount)
    Ok("Created")
  }
}

case class CreatePaymentForm(order: Int, amount: Float)
