package odt

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class OdtParsingTest extends FunSuite {
  test("Read test file") {
    val parser = new OpenOfficeParser
    val testText = parser.getText("/home/att/eclipse/MyProject/resources/test.odt")
    assert(testText === "This is a test")
  }
  test("Read more complicated test file") {
    val parser = new OpenOfficeParser
    val testText = parser.getText("/home/att/eclipse/MyProject/resources/epr.odt")
    println(testText)
  }
}