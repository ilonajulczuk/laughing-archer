package mobireader;
import nl.flotsam.preon.annotation.BoundNumber;


public class RecordInfo {
	@BoundNumber(size="32")
    public long recordDataOffset;
    @BoundNumber(size="32")
    public long uniqueID;
    
    public String toString() {
    	String description = "record info contains:\n";
    	description += "record Data offset: " + recordDataOffset + "\n";
    	description += "uniqueId: " + uniqueID + "\n";
    	return description;
    }
}
