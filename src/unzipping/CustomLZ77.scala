package unzipping

class CustomLZ77 {
def decompress(data: Array[Byte]): String= {
  val length = data.size
  val byteData = data
  var offset = 0
  var text: String = ""
  while (offset < length) {
    val byteValue = byteData(offset)
    val ord = byteValue & 0xff
    offset += 1
    if (ord == 0) {
      text += ord.toChar
    }
    else if (ord <= 8) {
      val raw = byteData.slice(offset, offset + ord)
      val prepared = (for(byte <-raw) yield byte.toChar).mkString("")
      text += prepared
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
      var lz77 = byte1 << 8 | byte2
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
        if (textpos < 0) {
          println("WARNING: LZ77 decompression reference is before"+
                  " beginning of text! " )
          return text
        }
        text += text.substring(textpos,textpos+1)
        textlength += 1
      }
    }
    else if(ord <= 0xff) {
      text += " " + (ord ^ 0x80).toChar
    }
  }
  return text
}
}