package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}
@Singleton
class CouponRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class CouponTable(tag: Tag) extends Table[Coupon](tag, "coupon") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def discount = column[Int]("discount")
    def * = (id, name, discount) <> ((Coupon.apply _).tupled, Coupon.unapply)
  }

  val coupon = TableQuery[CouponTable]

  def create(name: String, discount: Int): Future[Coupon] = db.run {
    (coupon.map(c => (c.name, c.discount))
      returning coupon.map(_.id)
      into {case ((name, discount), id) => Coupon(id, name, discount)}
      ) += (name, discount)
  }

  def list(): Future[Seq[Coupon]] = db.run {
    coupon.result
  }

  def getById(id: Int): Future[Coupon] = db.run {
    coupon.filter(_.id === id).result.head
  }

  def getByIdOption(id: Int): Future[Option[Coupon]] = db.run {
    coupon.filter(_.id === id).result.headOption
  }

  def delete(id: Int): Future[Unit] = db.run(coupon.filter(_.id === id).delete).map(_ => ())

  def update(id: Int, new_coupon: Coupon): Future[Unit] = {
    val couponToUpdate: Coupon = new_coupon.copy(id)
    db.run(coupon.filter(_.id === id).update(couponToUpdate)).map(_ => ())
  }
}

