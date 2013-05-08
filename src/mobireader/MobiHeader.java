/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mobireader;

import java.lang.reflect.Field;

import nl.flotsam.preon.annotation.BoundNumber;
import nl.flotsam.preon.annotation.BoundString;
import nl.flotsam.preon.buffer.ByteOrder;
/**
 *
 * @author att
 */
public class MobiHeader {
     //headerfmt = '> IIII II 40s III IIIII IIII I 36s IIII 8s HHIIIII'
     @BoundNumber(size="32",  byteOrder=ByteOrder.BigEndian)
     long identifier;
     @BoundNumber(size="32",  byteOrder=ByteOrder.BigEndian)
     long headerLength;
     @BoundNumber(size="32",  byteOrder=ByteOrder.BigEndian)
     long mobiType;
     @BoundNumber(size="32",  byteOrder=ByteOrder.BigEndian)
     long textEncoding;
     
     @BoundNumber(size="32",  byteOrder=ByteOrder.BigEndian)
     long UniqueID;
     @BoundNumber(size="32",  byteOrder=ByteOrder.BigEndian)
     long GeneratorVersion;
     
     @BoundString(size="40")
     String reserved;
     
     @BoundNumber(size="32",  byteOrder=ByteOrder.BigEndian)
     long firstNonBookIndex;
     @BoundNumber(size="32",  byteOrder=ByteOrder.BigEndian)
     long fullNameOffset;
     @BoundNumber(size="32",  byteOrder=ByteOrder.BigEndian)
     long fullNameLength;

     @BoundNumber(size="32",  byteOrder=ByteOrder.BigEndian)
     long language;
     @BoundNumber(size="32",  byteOrder=ByteOrder.BigEndian)
     long inputLanguage;
     @BoundNumber(size="32",  byteOrder=ByteOrder.BigEndian)
     long outputLanguage;
     @BoundNumber(size="32",  byteOrder=ByteOrder.BigEndian)
     long formatVersion;
     @BoundNumber(size="32",  byteOrder=ByteOrder.BigEndian)
     long firstImageIndex;

     @BoundNumber(size="32",  byteOrder=ByteOrder.BigEndian)
     long firstHuffRecord;
     @BoundNumber(size="32",  byteOrder=ByteOrder.BigEndian)
     long huffRecordCount;
     @BoundNumber(size="32",  byteOrder=ByteOrder.BigEndian)
     long firstDATPRecord;
     @BoundNumber(size="32",  byteOrder=ByteOrder.BigEndian)
     long DATPRecordCount;

     @BoundNumber(size="32",  byteOrder=ByteOrder.BigEndian)
     long EXTHFlags;

     @BoundString(size="36")
     String unknown36Bytes;

     @BoundNumber(size="32",  byteOrder=ByteOrder.BigEndian)
     long DRMOffset;
     @BoundNumber(size="32",  byteOrder=ByteOrder.BigEndian)
     long DRMCount;
     @BoundNumber(size="32",  byteOrder=ByteOrder.BigEndian)
     long DRMSize;
     @BoundNumber(size="32",  byteOrder=ByteOrder.BigEndian)
     long DRMFlags;

     @BoundString(size="8")
     String unknown8Bytes;
     
     @BoundNumber(size="16", byteOrder=ByteOrder.BigEndian)
     int Unknown1;
     @BoundNumber(size="16", byteOrder=ByteOrder.BigEndian)
     int LastImageRecord;
     @BoundNumber(size="32",  byteOrder=ByteOrder.BigEndian)
     long Unknown2;
     @BoundNumber(size="32",  byteOrder=ByteOrder.BigEndian)
     long FCISRecord;
     @BoundNumber(size="32",  byteOrder=ByteOrder.BigEndian)
     long Unknown3;
     @BoundNumber(size="32",  byteOrder=ByteOrder.BigEndian)
     long FLISRecord;
     @BoundNumber(size="32",  byteOrder=ByteOrder.BigEndian)
     long Unknown4;
     
     public boolean hasEXTHHeader() {
    	 if(EXTHFlags == 0x40) {
    		 return true;
    	 }
    	 else {
    		 System.out.println("EXTHFlags doesn't equal 0x40, but " + EXTHFlags);
    		 return false;
    	 }
     }
     
     public String toString() {
    	  StringBuilder result = new StringBuilder();
    	  String newLine = System.getProperty("line.separator");

    	  result.append( this.getClass().getName() );
    	  result.append( " Object {" );
    	  result.append(newLine);

    	  //determine fields declared in this class only (no fields of superclass)
    	  Field[] fields = this.getClass().getDeclaredFields();

    	  //print field names paired with their values
    	  for ( Field field : fields  ) {
    	    result.append("  ");
    	    try {
    	      result.append( field.getName() );
    	      result.append(": ");
    	      //requires access to private field:
    	      result.append( field.get(this) );
    	    } catch ( IllegalAccessException ex ) {
    	      System.out.println(ex);
    	    }
    	    result.append(newLine);
    	  }
    	  result.append("}");

    	  return result.toString();
    	}
}
