package mobireader


class MobiDescriptor(mobi: Mobi) {
	def firstHeaderInfo(): String = {
	  var description = ""
	  description += "Name: " + mobi.header.name + "\n"
	  description += "Type of mobi: " + mobi.header.typeOfMobi + "\n"
	  description += "Creator of mobi: " + mobi.header.creator + "\n"
	  description += "File contains: " + mobi.header.numberOfRecords + " records\n"
	  description
	}
	
	def mobiHeaderInfo(): String = {
	  var description = ""
	  description += "Identifier: " + mobi.mobiHeader.identifier + "\n"
	  description += "Header length: " + mobi.mobiHeader.headerLength + "\n"
	  description += "Mobi type: " + mobi.mobiHeader.mobiType + "\n"
	  description += "First non book index: " + mobi.mobiHeader.firstNonBookIndex + "\n"
	  description += "Full name offset: " + mobi.mobiHeader.fullNameOffset + "\n"
	  description += "Full name length: " + mobi.mobiHeader.fullNameLength + "\n"
	  
	  description
	}
	
	def palmdocHeaderInfo(): String = {
	  var description = ""
	  
	  def getDescriptionType(x: Int): String = x match {
        case 1 => "no compression"
        case 2 => "lz77 compression"
        case 17480 => "Huffman compression algorithm"
        case _ => "unknown"
     }	  
      description += "Compression: " + getDescriptionType(mobi.palmdocHeader.Compression) + "\n"
      description += "Length of text: " + mobi.palmdocHeader.textLength + "\n"
      description += "Count of records: " + mobi.palmdocHeader.recordCount + "\n"
      description += "Maximum record size: " + mobi.palmdocHeader.recordSize + "\n"
	  description
	}
}