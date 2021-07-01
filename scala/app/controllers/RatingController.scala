package controllers

import javax.inject._
import models.{Rating, RatingRepository}
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
class RatingController @Inject()(ratingRepository: RatingRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val productForm: Form[CreateRatingForm] = Form {
    mapping(
      "grade" -> of[Float],
      "description" -> nonEmptyText,
      "order" -> number
    )(CreateRatingForm.apply)(CreateRatingForm.unapply)
  }

  def getRatings: Action[AnyContent] = Action.async { implicit request =>
    val temp = ratingRepository.list()
    temp.map( ratings => Ok(Json.toJson(ratings)).as("application/json"))
  }

  def getRating(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val temp = ratingRepository.getByIdOption(id)
    temp.map(ratings => ratings match {
      case Some(p) => Ok(Json.toJson(p))
      case None => BadRequest("Does not exist")
    })
  }

  def delete(id: Int): Action[AnyContent] = Action {
    ratingRepository.delete(id)
    Redirect("/ratings")
  }

  def addRating(): Action[AnyContent] = Action { implicit request =>
    val rating_json = request.body.asJson.get
    val rating = rating_json.as[Rating]
    ratingRepository.create(rating.grade, rating.description, rating.order)
    Ok("Created")
  }
}

case class CreateRatingForm(grade: Float, description: String, order: Int)
