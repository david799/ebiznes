package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class ProductRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, categoryRepository: CategoryRepository)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._


  private class ProductTable(tag: Tag) extends Table[Product](tag, "product") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def description = column[String]("description")

    def category = column[Int]("category")

    def price = column[Float]("price")

    def categoryFk = foreignKey("cat_fk",category, cat)(_.id)

    def * = (id, name, description, category, price) <> ((Product.apply _).tupled, Product.unapply)

  }

  import categoryRepository.CategoryTable

  private val product = TableQuery[ProductTable]

  private val cat = TableQuery[CategoryTable]


  def create(name: String, description: String, category: Int, price: Float): Future[Product] = db.run {
    (product.map(p => (p.name, p.description,p.category, p.price))
      returning product.map(_.id)
      into {case ((name,description,category, price),id) => Product(id,name, description,category, price)}
      ) += (name, description, category, price)
  }

  def list(): Future[Seq[Product]] = db.run {
    product.result
  }

  def getByCategory(categoryId: Int): Future[Seq[Product]] = db.run {
    product.filter(_.category === categoryId).result
  }

  def getById(id: Int): Future[Product] = db.run {
    product.filter(_.id === id).result.head
  }

  def getByIdOption(id: Int): Future[Option[Product]] = db.run {
    product.filter(_.id === id).result.headOption
  }

  def getByCategories(categoryIds: List[Int]): Future[Seq[Product]] = db.run {
    product.filter(_.category inSet categoryIds).result
  }

  def delete(id: Int): Future[Unit] = db.run(product.filter(_.id === id).delete).map(_ => ())

  def update(id: Int, newProduct: Product): Future[Unit] = {
    val productToUpdate: Product = newProduct.copy(id)
    db.run(product.filter(_.id === id).update(productToUpdate)).map(_ => ())
  }

}

