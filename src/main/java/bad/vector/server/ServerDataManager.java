/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bad.vector.server;

import bad.vector.data.ShipData;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Admin
 */
public class ServerDataManager {

    private Map<Integer, ShipData> entities = new HashMap<>();

    public ServerDataManager() {
    }

    public void addOrRefreshShip(int id, ShipData shipData) {
        entities.put(id, shipData);
    }

    public void removeShip(int id) {
        entities.remove(id);
    }

    public boolean containsShip(int id) {
        return entities.containsKey(id);
    }

    public Map<Integer, ShipData> getShips() {
        return this.entities;
    }

}
