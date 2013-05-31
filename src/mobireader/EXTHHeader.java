/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mobireader;

import nl.flotsam.preon.annotation.BoundNumber;
import nl.flotsam.preon.buffer.ByteOrder;

/**
 * @author att
 */
public class EXTHHeader {
    //headerfmt = '>III'
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    long identifier;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    long headerLength;
    @BoundNumber(size = "32", byteOrder = ByteOrder.BigEndian)
    long recordCount;

    @Override
    public String toString() {
        return "EXTHHeader [identifier=" + identifier + ", headerLength="
                + headerLength + ", recordCount=" + recordCount + "]";
    }

}
