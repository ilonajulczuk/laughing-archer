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
public class EXTHHeader {
    //headerfmt = '>III'
    @BoundNumber(size="32")
    long identifier;
    @BoundNumber(size="32")
    long headerLength;
    @BoundNumber(size="32")
    long recordCount;
  

}
