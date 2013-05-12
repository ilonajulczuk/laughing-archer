package unzipping

import java.io.ByteArrayInputStream
import java.io.DataInputStream
import mobireader.CompressionCommand
import nl.flotsam.preon.Codec
import nl.flotsam.preon.Codecs

class CustomLZ77 {


def decompress(data: Array[Byte]): String= {
  val length = data.size
  val byteData = data
  //println("First bytes")
  for(byte <- byteData.slice(0, 40)) {
     //println(byte & 0xff)
  }
 
  var offset = 0
  var text: String = ""   

  while (offset < length) {
    val byteValue = byteData(offset)
    
    val ord = byteValue & 0xff
    offset += 1
    
    if (ord == 0) {
      text += ord.toChar;
    }
    else if (ord <= 8) {
      text += byteData.slice(offset, offset + ord)
      offset += ord
    }
    else if (ord <= 0x7f) {
      text += ord.toChar
    }
    else if (ord <= 0xbf) {
      offset += 1
      if (offset > data.size) {
        return text
      }
      
      val byte1 = byteData(offset-2) & 0x00ff
      val byte2 = byteData(offset-1) & 0x00ff
      
      var lz77 = (byte1 << 8 | byte2)
      lz77 &= 0x3fff
      val lz77length = (lz77 & 0x7) + 3
      val lz77offset = lz77 >> 3
      
      if (lz77offset < 1) {
        print("WARNING: LZ77 decompression offset is invalid!")
        return text
      }
      
      var textlength = text.size
      for (lz77pos <- 0 until lz77length) 
      {
        val textpos = textlength - lz77offset
        //println("textpos: " + textpos)
        if (textpos < 0) {
          println("WARNING: LZ77 decompression reference is before"+
                  " beginning of text! " )
          return text
        }
          
        //println("(ord <= 0xbf), adding: " + text.substring(textpos,textpos +1) )
        text += text.substring(textpos,textpos+1)
        textlength += 1
      }
    }
      
    else if(ord <= 0xff) {
      // 0xc0 - 0xff are single characters (XOR 0x80) preceded by
      // a space
      //println("And finally: ")
      //println(" " + (ord ^ 0x80).toChar)
      text += " " + (ord ^ 0x80).toChar
    }
    else {
      //println("Not in range? " + ord)
    }
  }
  return text
}
}