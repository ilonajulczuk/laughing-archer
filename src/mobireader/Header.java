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
 *
 * @author att
 */
public class Header {
	//http://wiki.mobileread.com/wiki/PDB#Palm_Database_Format
	//"32shhIIIIII4s4sIIH"
    public @BoundString(size="32") String name;
    @BoundNumber short attributes;
    @BoundNumber short version;
    @BoundNumber(size="32", byteOrder=ByteOrder.BigEndian)
    long created;
    @BoundNumber(size="32", byteOrder=ByteOrder.BigEndian)
    long modified;
    @BoundNumber(size="32", byteOrder=ByteOrder.BigEndian)
    long backup;
    @BoundNumber(size="32", byteOrder=ByteOrder.BigEndian)
    long modnum;
    @BoundNumber(size="32", byteOrder=ByteOrder.BigEndian)
    long appInfoId;
    @BoundNumber(size="32", byteOrder=ByteOrder.BigEndian)
    long sortInfoId;
    @BoundString(size="4") String type;
    @BoundString(size="4") String creator;
    @BoundNumber(size="32", byteOrder=ByteOrder.BigEndian)
    long uniqueIDseed;
    @BoundNumber(size="32", byteOrder=ByteOrder.BigEndian)
    long nextRecordListId;
    @BoundNumber(size="16", byteOrder=ByteOrder.BigEndian)
    public int numberOfRecords;
    
    public String toString() {
    	String description = "Header contains:\n";
    	description += "name: " + name + "\n";
    	description += "attributes: " + String.valueOf(attributes) + "\n";
    	description += "version: " + String.valueOf(version) + "\n";
    	description += "created: " + String.valueOf((int)created) + " as date: " + (new Date((int)created)).toString() +  "\n";
    	description += "modified: " + String.valueOf(modified) + "\n";
    	description += "backup: " + String.valueOf(backup) + "\n";
    	description += "modnum: " + String.valueOf(modnum) + "\n";
    	description += "appInfoId: " + String.valueOf(appInfoId) + "\n";
    	description += "sortInfoId: " + String.valueOf(sortInfoId) + "\n";
    	description += "type: " + type + "\n";
    	description += "creator: " + creator + "\n";
    	description += "uniqueIDseed: " + String.valueOf(uniqueIDseed) + "\n";
    	description += "nextRecordListId: " + String.valueOf(nextRecordListId) + "\n";
    	description += "numberOfRecords: " + String.valueOf(numberOfRecords) + "\n";
        return description;
    }
}