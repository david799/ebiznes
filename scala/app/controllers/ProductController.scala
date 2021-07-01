package controllers

import javax.inject._
import models.{ProductRepository, Product}
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
class ProductController @Inject()(productsRepo: ProductRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val productForm: Form[CreateProductForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "category" -> number,
      "price" -> of[Float],
    )(CreateProductForm.apply)(CreateProductForm.unapply)
  }

  def getProducts: Action[AnyContent] = Action.async { implicit request =>
    val produkty = productsRepo.list()
    produkty.map( products => Ok(Json.toJson(products)).as("application/json"))
  }

  def getProduct(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val produkt = productsRepo.getByIdOption(id)
    produkt.map(product => product match {
      case Some(p) => Ok(Json.toJson(p))
      case None => BadRequest("Does not exist")
    })
  }

  def delete(id: Int): Action[AnyContent] = Action {
    productsRepo.delete(id)
    Redirect("/products")
  }

  def addProduct(): Action[AnyContent] = Action { implicit request =>
    val product_json = request.body.asJson.get
    val product = product_json.as[Product]
    productsRepo.create(product.name, product.description, product.category, product.price)
    Ok("Created")
  }
}

case class CreateProductForm(name: String, description: String, category: Int, price: Float)
