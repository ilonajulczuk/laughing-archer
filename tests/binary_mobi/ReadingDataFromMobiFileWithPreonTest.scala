package binary_mobi

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
    assert(header.name.length() != 0)
    assert(header.typeOfMobi === "BOOK")
    assert(header.creator == "MOBI")
    assert(header.numberOfRecords > 0 && header.numberOfRecords < 1000)
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
    val path = "/home/att/studia/semestr4/Java/resources/test.mobi"
    val file = new File(path)
    val header = mobi.createHeaderBasedOn(file)
    println(header.name)
    println(header)
    assert(header.name.length() != 0)

    mobi.header = header
    val offset: Int = mobi.headers.headerSize
    println("Offset is: " + offset + " bytes")
    val records: ArrayList[RecordInfo] = mobi.parseRecordInfoList(file, offset)
    assert(records.size > 0, "Records are empty")
    assert(records.get(0).uniqueID < records.get(3).uniqueID, "Id in records doesn't grow")
    assert(records.get(0).recordDataOffset < records.get(3).recordDataOffset, "Id in records doesn't grow")
  }

  test("Parse whole file") {
    val path = "/home/att/studia/semestr4/Java/resources/island.bin"
    val mobi = new Mobi(path)
    mobi.parse()
    println(mobi.header)
  }

}