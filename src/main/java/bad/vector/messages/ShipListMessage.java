/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bad.vector.messages;

import bad.vector.data.ShipData;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;
import java.util.Map;

/**
 *
 * @author Admin
 */
@Serializable
public class ShipListMessage extends AbstractMessage {
    
    private Map<Integer, ShipData> entities;

    public ShipListMessage() {
    }

    public ShipListMessage(Map<Integer, ShipData> ships) {
        this.entities = ships;
    }

    public Map<Integer, ShipData> getShips() {
        return entities;
    }
}
