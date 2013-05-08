package mobireader;

import nl.flotsam.preon.annotation.BoundNumber;
import nl.flotsam.preon.buffer.ByteOrder;

public class CompressionCommand {
    @BoundNumber(size="3", byteOrder=ByteOrder.BigEndian)
    int identifier;
    @BoundNumber(size="11", byteOrder=ByteOrder.BigEndian)
    int offset;
    @BoundNumber(size="3", byteOrder=ByteOrder.BigEndian)
    int textSize;
    
	@Override
	public String toString() {
		return "CompressionCommand [identifier=" + identifier + ", offset="
				+ offset + ", textSize=" + textSize + "]";
	}
    
    
}
