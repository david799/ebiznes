package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class ReturnRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, orderRepository: OrderRepository)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class ReturnTable(tag: Tag) extends Table[Return](tag, "return") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def description = column[String]("description")

    def order = column[Int]("order")

    def orderFk = foreignKey("order_fk",order, order_tq)(_.id)

    def * = (id, description, order) <> ((Return.apply _).tupled, Return.unapply)
  }

  import orderRepository.OrderTable

  private val return_tq = TableQuery[ReturnTable]

  private val order_tq = TableQuery[OrderTable]

  def create(description: String, order: Int): Future[Return] = db.run {
    (return_tq.map(p => (p.description, p.order))
      returning return_tq.map(_.id)
      into {case ((description,order),id) => Return(id, description, order)}
      ) += (description, order)
  }

  def list(): Future[Seq[Return]] = db.run {
    return_tq.result
  }

  def getByOrder(orderId: Int): Future[Seq[Return]] = db.run {
    return_tq.filter(_.order === orderId).result
  }

  def getById(id: Int): Future[Return] = db.run {
    return_tq.filter(_.id === id).result.head
  }

  def getByIdOption(id: Int): Future[Option[Return]] = db.run {
    return_tq.filter(_.id === id).result.headOption
  }

  def getByOrders(orderIds: List[Int]): Future[Seq[Return]] = db.run {
    return_tq.filter(_.order inSet orderIds).result
  }

  def delete(id: Int): Future[Unit] = db.run(return_tq.filter(_.id === id).delete).map(_ => ())

  def update(id: Int, newRating: Return): Future[Unit] = {
    val returnToUpdate: Return = newRating.copy(id)
    db.run(return_tq.filter(_.id === id).update(returnToUpdate)).map(_ => ())
  }
}
