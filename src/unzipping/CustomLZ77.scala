package unzipping

import java.io.ByteArrayInputStream
import java.io.DataInputStream


class CustomLZ77 {

// ported directly from the PalmDoc Perl library
//http://kobesearch.cpan.org/htdocs/EBook-Tools/EBook/Tools/PalmDoc.pm.html

def decompress(data: String): String= {
  val length = data.size
  var offset = 0
  var text: String = ""   

  while (offset < length) {
    
  
    val char = data(offset)
    offset += 1
    val ord = char.intValue

    // The long if-elsif chain is the best logic for $ord handling
    //## no critic (Cascading if-elsif chain)
    if (ord == 0) {
      text += char;
    }
    else if (ord <= 8) {
      println("(ord <= 8), adding: " + data.substring(offset, offset + ord) )
      text += data.substring(offset, offset + ord)
      offset += ord
    }
      
    else if (ord <= 0x7f) {
      println("(ord <= 0x7f), adding: " + char )
      text += char
    }
    else if (ord <= 0xbf) {
      // Data is LZ77-compressed

      /* From Wikipedia:
      "A length-distance pair is always encoded by a two-byte
      sequence. Of the 16 bits that make up these two bytes,
      11 bits go to encoding the distance, 3 go to encoding
      the length, and the remaining two are used to make sure
      the decoder can identify the first byte as the beginning
      of such a two-byte sequence."
	  */
      
      offset += 1;
      if (offset > data.size) {
        println("WARNING: offset to LZ77 bits is outside of the data: " + offset)
        return text
      }
        
      val bytes = data.substring(offset-2, offset).getBytes()
      val bais = new ByteArrayInputStream(bytes)
      val isr: DataInputStream  = new DataInputStream(bais)
      val signedUnsignedShort = isr.readShort()
      var lz77 = signedUnsignedShort &  0xffff
      println("signedUnsignedInt: " + signedUnsignedShort)
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
                " beginning of text! " + lz77)
          return ""
        }
          
        println("(ord <= 0xbf), adding: " + text.substring(textpos,textpos +1) )
        text += text.substring(textpos.toInt,(textpos+1).toInt)
        textlength += 1
      }
    }
      
    else{
      // 0xc0 - 0xff are single characters (XOR 0x80) preceded by
      // a space
      text += ' ' + (ord ^ 0x80).toChar;
    }
      
  }
  return text
}
}