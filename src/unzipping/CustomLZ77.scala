package unzipping

import java.io.ByteArrayInputStream
import java.io.DataInputStream
import mobireader.CompressionCommand
import nl.flotsam.preon.Codec
import nl.flotsam.preon.Codecs

class CustomLZ77 {


def decompress(data: String): String= {
  val length = data.size
  val byteData = data.getBytes()
  var offset = 0
  var text: String = ""   

  while (offset < length) {
    val byteValue = byteData(offset)
    val ord = byteValue & 0xff;
    offset += 1
    if(ord < 0)
    {
      println("WTF? ord: " + ord)
    }
    else if (ord == 0) {
      text += ord.toChar;
    }
    else if (ord <= 8) {
      println("(ord <= 8), adding: " + data.substring(offset, offset + ord) )
      text += byteData.slice(offset, offset + ord)
      offset += ord
    }
    else if (ord <= 0x7f) {
      println("(ord <= 0x7f), adding: " + ord.toChar )
      text += ord.toChar
    }
    else if (ord <= 0xbf) {
      offset += 1;
      if (offset > data.size) {
        println("WARNING: offset to LZ77 bits is outside of the data: " + offset)
        return text
      }
      val bytes = new Array[Byte](2)
      val byte1 = byteData(offset-2) & 0x000000ff
      val byte2 = byteData(offset-1) & 0x000000ff
      
      
      var lz77 = (byte1 << 8 | byte2)
      println("lz77: " + lz77)
      // Leftmost two bits are ID bits and need to be dropped
      lz77 &= 0x3fff;
      println("lz77 after removing two leftmost bits: " + lz77)
      
      // Length is rightmost 3 bits + 3
      val lz77length = (lz77 & 0x0007) + 3
      println("lz77length: " + lz77length)
      // Remaining 11 bits are offset
      val lz77offset = lz77 >> 3
      println("lz77offset: " + lz77offset)
      
      if (lz77offset < 1) {
        print("WARNING: LZ77 decompression offset is invalid!")
        return text
      }
      /* Getting text from the offset is a little tricky, because
      # in theory you can be referring to characters you haven't
      # actually decompressed yet. You therefore have to check
      # the reference one character at a time.
      * 
      */
      var textlength = text.size
      println("textlength: " + textlength)
      for (lz77pos <- 0 until lz77length) 
      {
        val textpos = textlength - lz77offset
        println("textpos: " + textpos)
        if (textpos < 0) {
          println("WARNING: LZ77 decompression reference is before"+
                " beginning of text! " )
          return text
        }
          
        println("(ord <= 0xbf), adding: " + text.substring(textpos,textpos +1) )
        text += text.substring(textpos,textpos+1)
        textlength += 1
      }
    }
      
    else if(ord <= 0xff) {
      // 0xc0 - 0xff are single characters (XOR 0x80) preceded by
      // a space
      println("And finally: ")
      println(" " + (ord ^ 0x80).toChar)
      text += " " + (ord ^ 0x80).toChar
    }
    else {
      println("Not in range? " + ord)
    }
  }
  return text
}
}