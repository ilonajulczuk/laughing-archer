package stuff

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import scala.collection.mutable.Stack
import mobireader.{Mobi, Header, RecordInfo}
import unzipping.{AdaptiveHuffmanDecompress, LZ77}
import java.io.File
import java.util.ArrayList

class DecompressionTest extends FunSuite {
	test("how decompressing mobi (lz77) works") {
	  val path = "/home/att/studia/semestr4/Java/resources/mound.mobi"
	  val mobi = new Mobi(path)
	  mobi.parse()
	  val firstRecord = mobi.readRecord(1, false)
	  println(firstRecord)
	  val secondRecord = mobi.readRecord(2, false)
	  println(secondRecord)
	}
	
	test("how the 9 record looks like in mobi") {
	  val path = "/home/att/studia/semestr4/Java/resources/mound.mobi"
	  val mobi = new Mobi(path)
	  mobi.parse()
	  val hundredRecord = mobi.readRecord(9, false)
	  println(hundredRecord)
	}
	
	/*test("how decompressing mobi (huffman) works") {
	  val path = "/home/att/studia/semestr4/Java/resources/test.mobi"
	  val mobi = new Mobi(path)
	  mobi.parse()
	  val firstRecord = mobi.readRecord(1, false)
	  println(firstRecord)
	  val secondRecord = mobi.readRecord(2, false)
	  println(secondRecord)
	}*/
}