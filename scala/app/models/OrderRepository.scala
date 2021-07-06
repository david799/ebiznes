package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class OrderTable(tag: Tag) extends Table[Order](tag, "order") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def customer = column[Int]("customer")
    def address = column[Int]("address")
    def * = (id, customer, address) <> ((Order.apply _).tupled, Order.unapply)
  }

  val order = TableQuery[OrderTable]

  def create(customer: Int, address: Int): Future[Order] = db.run {
    (order.map(c => (c.customer, c.address))
      returning order.map(_.id)
      into {case ((customer, address), id) => Order(id, customer, address)}
      ) += (customer, address)
  }

  def list(): Future[Seq[Order]] = db.run {
    order.result
  }

  def getById(id: Int): Future[Order] = db.run {
    order.filter(_.id === id).result.head
  }

  def getByIdOption(id: Int): Future[Option[Order]] = db.run {
    order.filter(_.id === id).result.headOption
  }

  def delete(id: Int): Future[Unit] = db.run(order.filter(_.id === id).delete).map(_ => ())

  def update(id: Int, newOrder: Order): Future[Unit] = {
    val orderToUpdate: Order = newOrder.copy(id)
    db.run(order.filter(_.id === id).update(orderToUpdate)).map(_ => ())
  }
}

