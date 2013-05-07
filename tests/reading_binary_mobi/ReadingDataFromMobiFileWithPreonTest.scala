package reading_binary_mobi

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import scala.collection.mutable.Stack
import mobireader.{Mobi, Header}
import java.io.File

class ReadingDataFromMobiFileWithPreonTest extends FunSuite with BeforeAndAfter {

	test("how split sentences work") {
		val mobi = new Mobi("test")
        val path = "/home/att/studia/semestr4/Java/resources/test.mobi"
        val file = new File(path)
        val header = mobi.createHeaderBasedOn(file)
        println(header.name)
        println(header)
        assert(header.name.length() != 0)
	}
}