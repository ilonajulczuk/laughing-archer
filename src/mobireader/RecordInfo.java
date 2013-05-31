package mobireader;

import nl.flotsam.preon.annotation.BoundNumber;
import nl.flotsam.preon.buffer.ByteOrder;


public class RecordInfo {
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    public long recordDataOffset;
    @BoundNumber(size = "8", byteOrder = ByteOrder.BigEndian)
    public byte recordAttributes;
    @BoundNumber(size = "24", byteOrder = ByteOrder.BigEndian)
    public long uniqueID;

    public String toString() {
        String description = "record info contains:\n";
        description += "record Data offset: " + recordDataOffset + "\n";
        description += "recordAttributes: " + recordAttributes + "\n";
        description += "uniqueId: " + uniqueID + "\n";
        return description;
    }
}
