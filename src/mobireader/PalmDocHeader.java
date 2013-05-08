/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mobireader;


import nl.flotsam.preon.annotation.BoundNumber;
import nl.flotsam.preon.buffer.ByteOrder;

/**
 *
 * @author att
 */
public class PalmDocHeader {
    //headerfmt = '>HHIHHHH'
    @BoundNumber(size="16", byteOrder=ByteOrder.BigEndian)
    public int Compression;
    @BoundNumber(size="16", byteOrder=ByteOrder.BigEndian)
    public int Unused;
    @BoundNumber(size="32", byteOrder=ByteOrder.BigEndian)
    public long textLength;
    @BoundNumber(size="16", byteOrder=ByteOrder.BigEndian)
    public int recordCount;
    @BoundNumber(size="16", byteOrder=ByteOrder.BigEndian)
    public int recordSize;
    @BoundNumber(size="16", byteOrder=ByteOrder.BigEndian)
    public int encryptionType;
    @BoundNumber(size="16", byteOrder=ByteOrder.BigEndian)
    int unknown;
    
	@Override
	public String toString() {
		return "PalmDocHeader [Compression=" + Compression + ", Unused="
				+ Unused + ", textLength=" + textLength + ", recordCount="
				+ recordCount + ", recordSize=" + recordSize
				+ ", encryptionType=" + encryptionType + ", unknown=" + unknown
				+ "]";
	}
    
    
}
