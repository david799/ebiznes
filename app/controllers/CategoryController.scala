package controllers

import javax.inject._
import models.{CategoryRepository, Category}
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
class CategoryController @Inject()(categoryRepository: CategoryRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val productForm: Form[CreateCategoryForm] = Form {
    mapping(
      "name" -> nonEmptyText,
    )(CreateCategoryForm.apply)(CreateCategoryForm.unapply)
  }

  def getCategories: Action[AnyContent] = Action.async { implicit request =>
    val temp = categoryRepository.list()
    temp.map( categories => Ok(Json.toJson(categories)).as("application/json"))
  }

  def getCategory(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val temp = categoryRepository.getByIdOption(id)
    temp.map(categories => categories match {
      case Some(p) => Ok(Json.toJson(p))
      case None => Redirect(routes.CategoryController.getCategories())
    })
  }

  def delete(id: Int): Action[AnyContent] = Action {
    categoryRepository.delete(id)
    Redirect("/categories")
  }

  def addCategory(): Action[AnyContent] = Action { implicit request =>
    val category_json = request.body.asJson.get
    val category = category_json.as[Category]
    categoryRepository.create(category.name)
    Ok("Created")
  }
}

case class CreateCategoryForm(name: String)
