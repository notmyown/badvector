/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bad.vector.messages;

import bad.vector.data.ShipData;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author marco.bergen
 */
@Serializable
public class ShipDataMessage extends AbstractMessage {
    
    public ShipData shipData;
    
    public ShipDataMessage() {
    }

    public ShipDataMessage(ShipData sd) {
        this.shipData = sd;
    }

    /**
     * @return the message
     */
    public ShipData getShipData() {
        return shipData;
    }

    /**
     * @param message the message to set
     */
    public void setShipData(ShipData shipData) {
        this.shipData = shipData;
    }
    
    
}
