package domain

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter


class PrioritizedBookTest extends FunSuite with BeforeAndAfter {

  test("Create prioritized book with invalid priority") {
    intercept[IllegalArgumentException] {
      val Joe = new Author("Joe")
      val testBook = new Book("Aloes and different plants", Joe )
      new PrioritizedBook(testBook, -10)
    }
  }

  test("Create prioritized book with null book") {
    intercept[IllegalArgumentException] {
      new PrioritizedBook(null)
    }
  }

}
