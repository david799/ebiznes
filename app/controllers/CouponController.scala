package controllers

import javax.inject._
import models.{Coupon, CouponRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class CouponController @Inject()(couponRepository: CouponRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val productForm: Form[CreateCouponForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "discount" -> number
    )(CreateCouponForm.apply)(CreateCouponForm.unapply)
  }

  def getCoupons: Action[AnyContent] = Action.async { implicit request =>
    val temp = couponRepository.list()
    temp.map( coupons => Ok(Json.toJson(coupons)).as("application/json"))
  }

  def getCoupon(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val temp = couponRepository.getByIdOption(id)
    temp.map(coupons => coupons match {
      case Some(p) => Ok(Json.toJson(p))
      case None => Redirect(routes.CouponController.getCoupons())
    })
  }

  def delete(id: Int): Action[AnyContent] = Action {
    couponRepository.delete(id)
    Redirect("/coupons")
  }

  def addCoupon(): Action[AnyContent] = Action { implicit request =>
    val coupon_json = request.body.asJson.get
    val coupon = coupon_json.as[Coupon]
    couponRepository.create(coupon.name, coupon.discount)
    Ok("Created")
  }
}

case class CreateCouponForm(name: String, discount: Int)
