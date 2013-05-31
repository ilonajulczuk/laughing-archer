package stuff

import mobireader._
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class ParsingMobiContentTest extends FunSuite {
  test("Get content of book in html") {
    val path = "/home/att/studia/semestr4/Java/resources/mound.mobi"
    val mobi = new Mobi(path)
    mobi.parse()
    val html = mobi.readAllRecords()
    assert(html.startsWith("<html>"), "Document doesn't start like valid html document")
    assert(html.contains("</html>"), "Document doesn't end like valid html")
    println(html.substring(0, 1000))
  }

  test("Get body of book in text") {
    val path = "/home/att/studia/semestr4/Java/resources/mound.mobi"
    val mobi = new Mobi(path)
    mobi.parse()
    val html = mobi.readAllRecords()
    val parser = new MobiContentParser(html)
    println(parser.bodyText.size)
    assert(!parser.bodyText.isEmpty(), "Parser didn't extract any content")
    println(parser.bodyText.substring(0, 500))
  }

}