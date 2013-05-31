package odt

import org.jdom.Element
import org.jdom.Text
import org.jdom.input.SAXBuilder
import java.util.zip.ZipFile
import java.util.zip.ZipEntry

class OpenOfficeParser {
  var TextBuffer = new StringBuffer

  def processElement(o: Any) {
    o match {
      case e: Element => {
        val elementName = e.getQualifiedName
        if (elementName contains "text") {
          if (elementName endsWith ":tab")
            TextBuffer.append("\t")
          else if (elementName endsWith ":s")
            TextBuffer.append(" ")
          else {
            val children = e.getContent
            val iterator = children.iterator()
            while (iterator.hasNext) {
              val child = iterator.next()
              child match {
                case t: Text => TextBuffer.append(t.getValue)
                case a: Any => {
                  processElement(a)
                }
              }
            }
          }
        }
        else {
          val nonTextList = e.getContent
          val it = nonTextList.iterator()
          while (it.hasNext) {
            val nonTextChild = it.next()
            processElement(nonTextChild)
          }
        }

      }
      case _ => null
    }
  }

  def getText(fileName: String) = {
    val zipFile = new ZipFile(fileName)
    val entries = zipFile.entries()

    var contentFound = false
    while (entries.hasMoreElements && !contentFound) {
      val entry: ZipEntry = entries.nextElement()
      if (entry.getName.equals("content.xml")) {
        TextBuffer = new StringBuffer()
        val sax = new SAXBuilder()
        val doc = sax.build(zipFile.getInputStream(entry))
        val rootElement = doc.getRootElement
        processElement(rootElement)
        contentFound = true
      }
    }
    TextBuffer.toString
  }

}