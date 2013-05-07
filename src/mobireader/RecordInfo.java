package mobireader;
import nl.flotsam.preon.annotation.BoundNumber;


public class RecordInfo {
	@BoundNumber(size="32")
    long recordDataOffset;
    @BoundNumber(size="32")
    long uniqueID;
}
