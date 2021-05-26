package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CustomerRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class CustomerTable(tag: Tag) extends Table[Customer](tag, "customer") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def nick = column[String]("nick")
    def * = (id, nick) <> ((Customer.apply _).tupled, Customer.unapply)
  }

  val customer = TableQuery[CustomerTable]

  def create(nick: String): Future[Customer] = db.run {
    (customer.map(c => (c.nick))
      returning customer.map(_.id)
      into ((nick, id) => Customer(id, nick))
      ) += (nick)
  }

  def list(): Future[Seq[Customer]] = db.run {
    customer.result
  }

  def getById(id: Int): Future[Customer] = db.run {
    customer.filter(_.id === id).result.head
  }

  def getByIdOption(id: Int): Future[Option[Customer]] = db.run {
    customer.filter(_.id === id).result.headOption
  }

  def delete(id: Int): Future[Unit] = db.run(customer.filter(_.id === id).delete).map(_ => ())

  def update(id: Int, new_customer: Customer): Future[Unit] = {
    val customerToUpdate: Customer = new_customer.copy(id)
    db.run(customer.filter(_.id === id).update(customerToUpdate)).map(_ => ())
  }
}
