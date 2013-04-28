/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mobireader;

import nl.flotsam.preon.annotation.BoundNumber;

/**
 *
 * @author att
 */
public class PalmDocHeader {
    //headerfmt = '>HHIHHHH'
    @BoundNumber(size="16")
    int Compression;
    @BoundNumber(size="16")
    int Unused;
    @BoundNumber(size="32")
    long textLength;
    @BoundNumber(size="16")
    int recordCount;
    @BoundNumber(size="16")
    int recordSize;
    @BoundNumber(size="16")
    int encryptionType;
    @BoundNumber(size="16")
    int unknown;
    
}
