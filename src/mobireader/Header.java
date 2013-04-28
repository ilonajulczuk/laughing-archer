/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mobireader;

import nl.flotsam.preon.annotation.BoundNumber;
import nl.flotsam.preon.annotation.BoundString;

/**
 *
 * @author att
 */
public class Header {
    public @BoundString(size="32") String name;
    @BoundNumber int attributes;
    @BoundNumber int version;
    @BoundNumber int created;
    @BoundNumber int modified;
    @BoundNumber int backup;
    @BoundNumber int modnum;
    @BoundNumber int appInfoId;
    @BoundNumber int sortInfoId;
    @BoundString(size="4") String type;
    @BoundString(size="4") String creator;
    @BoundNumber int uniqueIDseed;
    @BoundNumber int nextRecordListId;
    @BoundNumber int number_of_records;
}