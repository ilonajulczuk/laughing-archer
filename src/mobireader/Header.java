/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mobireader;

import nl.flotsam.preon.annotation.BoundNumber;
import nl.flotsam.preon.buffer.ByteOrder;
import nl.flotsam.preon.annotation.BoundString;

import java.util.Date;

/**
 * Class Header describes real binary header in mobi format.
 * More info about header:
 * //http://wiki.mobileread.com/wiki/PDB#Palm_Database_Format
 *"32shhIIIIII4s4sIIH" data specification in python struct format
 */
public class Header {
    public
    @BoundString(size = "32")
    String name;
    @BoundNumber
    public short attributes;
    @BoundNumber
    public short version;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long created;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long modified;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long backup;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long modnum;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long appInfoId;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long sortInfoId;
    @BoundString(size = "4")
    public String typeOfMobi;
    @BoundString(size = "4")
    public String creator;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long uniqueIDseed;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long nextRecordListId;
    @BoundNumber(size = "16", byteOrder = ByteOrder.BigEndian)
    public int numberOfRecords;

    public String toString() {
        String description = "Header contains:\n";
        description += "name: " + name + "\n";
        description += "attributes: " + String.valueOf(attributes) + "\n";
        description += "version: " + String.valueOf(version) + "\n";
        description += "created: " + String.valueOf((int) created) + " as date: " + (new Date((int) created)).toString() + "\n";
        description += "modified: " + String.valueOf(modified) + "\n";
        description += "backup: " + String.valueOf(backup) + "\n";
        description += "modnum: " + String.valueOf(modnum) + "\n";
        description += "appInfoId: " + String.valueOf(appInfoId) + "\n";
        description += "sortInfoId: " + String.valueOf(sortInfoId) + "\n";
        description += "type: " + typeOfMobi + "\n";
        description += "creator: " + creator + "\n";
        description += "uniqueIDseed: " + String.valueOf(uniqueIDseed) + "\n";
        description += "nextRecordListId: " + String.valueOf(nextRecordListId) + "\n";
        description += "numberOfRecords: " + String.valueOf(numberOfRecords) + "\n";
        return description;
    }
}