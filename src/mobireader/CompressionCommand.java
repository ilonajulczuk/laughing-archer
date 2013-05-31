package mobireader;

import nl.flotsam.preon.annotation.BoundNumber;
import nl.flotsam.preon.buffer.ByteOrder;

public class CompressionCommand {
    @BoundNumber(size = "3", byteOrder = ByteOrder.BigEndian)
    public int textSize;

    @BoundNumber(size = "11", byteOrder = ByteOrder.BigEndian)
    public int offset;


    @BoundNumber(size = "3", byteOrder = ByteOrder.BigEndian)
    public int identifier;

    @Override
    public String toString() {
        return "CompressionCommand [identifier=" + identifier + ", offset="
                + offset + ", textSize=" + textSize + "]";
    }


}
