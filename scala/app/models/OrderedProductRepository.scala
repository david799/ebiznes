package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderedProductRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class OrderedProductTable(tag: Tag) extends Table[OrderedProduct](tag, "orderedProduct") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def order = column[Int]("order")
    def product = column[Int]("product")
    def * = (id, order, product) <> ((OrderedProduct.apply _).tupled, OrderedProduct.unapply)
  }

  val orderedProduct = TableQuery[OrderedProductTable]

  def create(order: Int, product: Int): Future[OrderedProduct] = db.run {
    (orderedProduct.map(c => (c.order, c.product))
      returning orderedProduct.map(_.id)
      into {case ((order, product), id) => OrderedProduct(id, order, product)}
      ) += (order, product)
  }

  def list(): Future[Seq[OrderedProduct]] = db.run {
    orderedProduct.result
  }

  def getById(id: Int): Future[OrderedProduct] = db.run {
    orderedProduct.filter(_.id === id).result.head
  }

  def getByIdOption(id: Int): Future[Option[OrderedProduct]] = db.run {
    orderedProduct.filter(_.id === id).result.headOption
  }

  def getOrderProducts(orderId: Int)= db.run {
    orderedProduct.filter(_.order === orderId).result
  }

  def delete(id: Int): Future[Unit] = db.run(orderedProduct.filter(_.id === id).delete).map(_ => ())

  def update(id: Int, newOrderedProduct: OrderedProduct): Future[Unit] = {
    val orderedProductToUpdate: OrderedProduct = newOrderedProduct.copy(id)
    db.run(orderedProduct.filter(_.id === id).update(orderedProductToUpdate)).map(_ => ())
  }
}
