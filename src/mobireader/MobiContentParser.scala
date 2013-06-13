package mobireader

import org.jsoup.Jsoup

/**
 *
 * @param html
 */
class MobiContentParser(html: String) {
  val doc = Jsoup.parse(html)
  val body = doc.body()
  val bodyText = body.text()
  lazy val paragraphs = body.select("p")

  def howManyParagraphs() = paragraphs.size

  def bodyWithParagraphs() = {
    val text = new StringBuilder
    val it = paragraphs.iterator
    while (it.hasNext()) {
      val para = it.next()
      text ++= para.text + "\n\n"
    }
    text.toString
  }

}