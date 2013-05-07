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
        /*
         * Offset is: 80 bytes
record info contains:
record Data offset: 59392
recordAttributes: 0
uniqueId: 0

record info contains:
record Data offset: 33802
recordAttributes: 0
uniqueId: 1

record info contains:
record Data offset: 19219
recordAttributes: 0
uniqueId: 2
         */
        mobi.header = header 
        val offset: Int = mobi.headerSize 
        println("Offset is: " + offset + " bytes")
        val records: ArrayList[RecordInfo] = mobi.parseRecordInfoList(file, offset)
        assert(records.size > 0, "Records are empty")
        for(i <- 0 until 10) {
          println(records.get(i))
        }
	}
	
	test("how mobi about mound look like") {
		val mobi = new Mobi("test")
        val path = "/home/att/studia/semestr4/Java/resources/test.mobi"
        val file = new File(path)
        val header = mobi.createHeaderBasedOn(file)
        println(header.name)
        println(header)
        assert(header.name.length() != 0)
        
        mobi.header = header
        val offset: Int = mobi.headerSize
        println("Offset is: " + offset + " bytes")
        val records: ArrayList[RecordInfo] = mobi.parseRecordInfoList(file, offset)
        assert(records.size > 0, "Records are empty")
        val recordsSize = records.size
        for(i <- recordsSize -10  until recordsSize) {
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