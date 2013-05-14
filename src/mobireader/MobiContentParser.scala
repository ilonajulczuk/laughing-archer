package mobireader

import org.jsoup.Jsoup
import org.jsoup.nodes._

class MobiContentParser(html: String) {
	val doc = Jsoup.parse(html);
	val body = doc.body()
	val bodyText = body.text()
}