/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mobireader;

import nl.flotsam.preon.annotation.BoundNumber;
import nl.flotsam.preon.annotation.BoundString;

/**
 *
 * @author att
 */
public class MobiHeader {
     //headerfmt = '> IIII II 40s III IIIII IIII I 36s IIII 8s HHIIIII'
     @BoundNumber(size="32")
     long identifier;
     @BoundNumber(size="32")
     long header_length;
     @BoundNumber(size="32")
     long Mobi_type;
     @BoundNumber(size="32")
     long text_Encoding;
     
     @BoundNumber(size="32")
     long Unique_ID;
     @BoundNumber(size="32")
     long Generator_version;
     
     @BoundString(size="40")
     String Reserved;
     
     @BoundNumber(size="32")
     long FirstNonBookIndex;
     @BoundNumber(size="32")
     long FullNameOffset;
     @BoundNumber(size="32")
     long FullNameLength;

     @BoundNumber(size="32")
     long Language;
     @BoundNumber(size="32")
     long InputLanguage;
     @BoundNumber(size="32")
     long OutputLanguage;
     @BoundNumber(size="32")
     long FormatVersion;
     @BoundNumber(size="32")
     long FirstImageIndex;

     @BoundNumber(size="32")
     long FirstHuffRecord;
     @BoundNumber(size="32")
     long HuffRecordCount;
     @BoundNumber(size="32")
     long FirstDATPRecord;
     @BoundNumber(size="32")
     long DATPRecordCount;

     @BoundNumber(size="32")
     long EXTHFlags;

     @BoundString(size="36")
     String unknown36Bytes;

     @BoundNumber(size="32")
     long DRMOffset;
     @BoundNumber(size="32")
     long DRMCount;
     @BoundNumber(size="32")
     long DRMSize;
     @BoundNumber(size="32")
     long DRMFlags;

     @BoundString(size="8")
     String unknown8Bytes;
     
     @BoundNumber(size="16")
     int Unknown1;
     @BoundNumber(size="16")
     int LastImageRecord;
     @BoundNumber(size="32")
     long Unknown2;
     @BoundNumber(size="32")
     long FCISRecord;
     @BoundNumber(size="32")
     long Unknown3;
     @BoundNumber(size="32")
     long FLISRecord;
     @BoundNumber(size="32")
     long Unknown4;
}
