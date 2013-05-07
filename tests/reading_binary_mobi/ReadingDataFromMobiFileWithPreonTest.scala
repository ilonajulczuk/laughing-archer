package reading_binary_mobi

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import scala.collection.mutable.Stack
import mobireader.{Mobi, Header, RecordInfo}
import java.io.File
import java.util.ArrayList

class ReadingDataFromMobiFileWithPreonTest extends FunSuite with BeforeAndAfter {

	test("how mobi about physics look like") {
		val mobi = new Mobi("test")
        val path = "/home/att/studia/semestr4/Java/resources/test.mobi"
        val file = new File(path)
        val header = mobi.createHeaderBasedOn(file)
        println(header.name)
        println(header)
        assert(header.name.length() != 0)
	}
	test("how mobi about slavery look like") {
		val mobi = new Mobi("test")
        val path = "/home/att/studia/semestr4/Java/resources/slavery.mobi"
        val file = new File(path)
        val header = mobi.createHeaderBasedOn(file)
        println(header.name)
        println(header)
        assert(header.name.length() != 0)
	}
	
	test("how mobi about mound look like") {
		val mobi = new Mobi("test")
        val path = "/home/att/studia/semestr4/Java/resources/mound.mobi"
        val file = new File(path)
        val header = mobi.createHeaderBasedOn(file)
        println(header.name)
        println(header)
        assert(header.name.length() != 0)
        
        mobi.header = header
        val records: ArrayList[RecordInfo] = mobi.parseRecordInfoList(file)
        assert(records.size > 0, "Records are empty")
        for(i <- 0 until 5) {
          println(records.get(i))
        }
	}
	
	test("how about reading twice same data from mobi with preon") {
		val mobi = new Mobi("test")
        val path = "/home/att/studia/semestr4/Java/resources/mound.mobi"
        val file = new File(path)
        val header = mobi.createHeaderBasedOn(file)
        println(header)
        assert(header.name.length() != 0)
        
        val header2 = mobi.createHeaderBasedOn(file)
        println("Reading same date second time" )
        println(header2)
        
	}
}