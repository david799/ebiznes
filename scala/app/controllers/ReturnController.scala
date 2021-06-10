package controllers

import javax.inject._
import models.{Category, CategoryRepository, Return, ReturnRepository}
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
class ReturnController @Inject()(returnRepository: ReturnRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val productForm: Form[CreateReturnForm] = Form {
    mapping(
      "description" -> nonEmptyText,
      "order" -> number
    )(CreateReturnForm.apply)(CreateReturnForm.unapply)
  }

  def getReturns: Action[AnyContent] = Action.async { implicit request =>
    val temp = returnRepository.list()
    temp.map( categories => Ok(Json.toJson(categories)).as("application/json"))
  }

  def getReturn(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val temp = returnRepository.getByIdOption(id)
    temp.map(returns => returns match {
      case Some(p) => Ok(Json.toJson(p))
      case None => Redirect(routes.ReturnController.getReturns())
    })
  }

  def delete(id: Int): Action[AnyContent] = Action {
    returnRepository.delete(id)
    Redirect("/returns")
  }

  def addReturn(): Action[AnyContent] = Action { implicit request =>
    val return_json = request.body.asJson.get
    val returnn = return_json.as[Return]
    returnRepository.create(returnn.description, returnn.order)
    Ok("Created")
  }
}

case class CreateReturnForm(description: String, order: Int)
