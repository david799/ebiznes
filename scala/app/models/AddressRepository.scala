package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AddressRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class AddressTable(tag: Tag) extends Table[Address](tag, "address") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def customer = column[Int]("customer")
    def addressLine1 = column[String]("address_line1")
    def addressLine2 = column[String]("address_line2")
    def * = (id, name, customer, addressLine1, addressLine2) <> ((Address.apply _).tupled, Address.unapply)
  }

  val address = TableQuery[AddressTable]

  def create(name: String, customer: Int, addressLine1: String, addressLine2: String): Future[Address] = db.run {
    (address.map(c => (c.name, c.customer, c.addressLine1, c.addressLine2))
      returning address.map(_.id)
      into {case ((name, customer, addressLine1, addressLine2),id) => Address(id, name, customer, addressLine1, addressLine2)}
      ) += (name, customer, addressLine1, addressLine2)
  }

  def list(): Future[Seq[Address]] = db.run {
    address.result
  }

  def getById(id: Int): Future[Address] = db.run {
    address.filter(_.id === id).result.head
  }

  def getByIdOption(id: Int): Future[Option[Address]] = db.run {
    address.filter(_.id === id).result.headOption
  }

  def delete(id: Int): Future[Unit] = db.run(address.filter(_.id === id).delete).map(_ => ())

  def update(id: Int, newAddress: Address): Future[Unit] = {
    val addressToUpdate: Address = newAddress.copy(id)
    db.run(address.filter(_.id === id).update(addressToUpdate)).map(_ => ())
  }
}

