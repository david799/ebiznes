package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class RatingRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, orderRepository: OrderRepository)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._


  private class RatingTable(tag: Tag) extends Table[Rating](tag, "rating") {

    /** The ID column, which is the primary key, and auto incremented */
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    /** The name column */
    def grade = column[Float]("grade")

    /** The age column */
    def description = column[String]("description")

    def order = column[Int]("order")

    def orderFk = foreignKey("order_fk",order, order_tq)(_.id)


    /**
     * This is the tables default "projection".
     *
     * It defines how the columns are converted to and from the Person object.
     *
     * In this case, we are simply passing the id, name and page parameters to the Person case classes
     * apply and unapply methods.
     */
    def * = (id, grade, description, order) <> ((Rating.apply _).tupled, Rating.unapply)

  }

  /**
   * The starting point for all queries on the people table.
   */

  import orderRepository.OrderTable

  private val rating = TableQuery[RatingTable]

  private val order_tq = TableQuery[OrderTable]


  def create(grade: Float, description: String, order: Int): Future[Rating] = db.run {
    (rating.map(p => (p.grade, p.description, p.order))
      returning rating.map(_.id)
      into {case ((grade,description,order),id) => Rating(id,grade, description, order)}
      ) += (grade, description, order)
  }

  def list(): Future[Seq[Rating]] = db.run {
    rating.result
  }

  def getByOrder(orderId: Int): Future[Seq[Rating]] = db.run {
    rating.filter(_.order === orderId).result
  }

  def getById(id: Int): Future[Rating] = db.run {
    rating.filter(_.id === id).result.head
  }

  def getByIdOption(id: Int): Future[Option[Rating]] = db.run {
    rating.filter(_.id === id).result.headOption
  }

  def getByOrders(orderIds: List[Int]): Future[Seq[Rating]] = db.run {
    rating.filter(_.order inSet orderIds).result
  }

  def delete(id: Int): Future[Unit] = db.run(rating.filter(_.id === id).delete).map(_ => ())

  def update(id: Int, newRating: Rating): Future[Unit] = {
    val ratingToUpdate: Rating = newRating.copy(id)
    db.run(rating.filter(_.id === id).update(ratingToUpdate)).map(_ => ())
  }

}
