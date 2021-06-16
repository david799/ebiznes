package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class PaymentRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, orderRepository: OrderRepository)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._


  private class PaymentTable(tag: Tag) extends Table[Payment](tag, "payment") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def order = column[Int]("order")

    def amount = column[Float]("amount")

    def orderFk = foreignKey("order_fk", order, order_tq)(_.id)

    def * = (id, order, amount) <> ((Payment.apply _).tupled, Payment.unapply)

  }


  import orderRepository.OrderTable

  private val payment = TableQuery[PaymentTable]

  private val order_tq = TableQuery[OrderTable]


  def create(order: Int, amount: Float): Future[Payment] = db.run {
    (payment.map(p => (p.order, p.amount))
      returning payment.map(_.id)
      into {case ((order,amount),id) => Payment(id,order,amount)}
      ) += (order,amount)
  }

  def list(): Future[Seq[Payment]] = db.run {
    payment.result
  }

  def getByOrder(orderId: Int): Future[Seq[Payment]] = db.run {
    payment.filter(_.order === orderId).result
  }

  def getById(id: Int): Future[Payment] = db.run {
    payment.filter(_.id === id).result.head
  }

  def getByIdOption(id: Int): Future[Option[Payment]] = db.run {
    payment.filter(_.id === id).result.headOption
  }

  def getByOrders(orders_ids: List[Int]): Future[Seq[Payment]] = db.run {
    payment.filter(_.order inSet orders_ids).result
  }

  def delete(id: Int): Future[Unit] = db.run(payment.filter(_.id === id).delete).map(_ => ())

  def update(id: Int, newPayment: Payment): Future[Unit] = {
    val productToUpdate: Payment = newPayment.copy(id)
    db.run(payment.filter(_.id === id).update(productToUpdate)).map(_ => ())
  }
}
