package database_operations

import hello.DBHandler
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class DBHandlerRemovalTest extends FunSuite with BeforeAndAfter {

  before {
    val db  = new DBHandler("test.db")
  }

  test("Remove one book [simple case]") {

  }

  test("Remove two book [one author should also be removed]") {

  }

  test("Remove one author who written one book") {

  }

  test("Remove author with multiple books") {

  }
}
