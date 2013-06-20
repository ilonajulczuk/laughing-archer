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
 * Describes mobi header, more information on:
 * http://wiki.mobileread.com/wiki/MOBI
 */
public class MobiHeader {
    //headerfmt = '> IIII II 40s III IIIII IIII I 36s IIII 8s HHIIIII'
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long identifier;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long headerLength;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long mobiType;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long textEncoding;

    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long UniqueID;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long GeneratorVersion;

    @BoundString(size = "40")
    public String reserved;

    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long firstNonBookIndex;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long fullNameOffset;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long fullNameLength;

    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long language;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long inputLanguage;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long outputLanguage;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long formatVersion;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long firstImageIndex;

    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long firstHuffRecord;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long huffRecordCount;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long firstDATPRecord;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long DATPRecordCount;

    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long EXTHFlags;

    @BoundString(size = "36")
    public String unknown36Bytes;

    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long DRMOffset;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long DRMCount;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long DRMSize;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long DRMFlags;

    @BoundString(size = "8")
    public String unknown8Bytes;

    @BoundNumber(size = "16", byteOrder = ByteOrder.BigEndian)
    int Unknown1;
    @BoundNumber(size = "16", byteOrder = ByteOrder.BigEndian)
    int LastImageRecord;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long Unknown2;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long FCISRecord;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long Unknown3;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long FLISRecord;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    long Unknown4;

    public boolean hasEXTHHeader() {
        if (EXTHFlags == 80) {
            return true;
        } else {
            System.out.println("EXTHFlags doesn't equal 0x40, but " + EXTHFlags);
            return false;
        }
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append(this.getClass().getName());
        result.append(" Object {");
        result.append(newLine);

        //determine fields declared in this class only (no fields of superclass)
        Field[] fields = this.getClass().getDeclaredFields();

        //print field names paired with their values
        for (Field field : fields) {
            result.append("  ");
            try {
                result.append(field.getName());
                result.append(": ");
                //requires access to private field:
                result.append(field.get(this));
            } catch (IllegalAccessException ex) {
                System.out.println(ex);
            }
            result.append(newLine);
        }
        result.append("}");

        return result.toString();
    }
}
