/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bad.vector.client;

import bad.vector.messages.BulletCreateMessage;
import bad.vector.messages.CloseConnectionMessage;
import bad.vector.messages.ShipListMessage;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 *
 * @author marco.bergen
 */
public class ClientListener implements MessageListener<Client> {

    Client client;
    private SceneManager sceneManager;
    
    public ClientListener(Client c, SceneManager sm) {
        this.client = c;
        this.sceneManager = sm;
    }

    @Override
    public void messageReceived(Client source, Message m) {
        if (m instanceof ShipListMessage) {
            ShipListMessage slm = (ShipListMessage) m;
            sceneManager.refeshShipList(slm.getShips());
        } else if (m instanceof CloseConnectionMessage) {
            CloseConnectionMessage sm = (CloseConnectionMessage) m;
            sceneManager.removeShip(sm.getId());
            if (sm.getId() == source.getId()) {
              client.close();
            }
        } else if (m instanceof BulletCreateMessage) {
            BulletCreateMessage bm = (BulletCreateMessage) m;
            sceneManager.addBullet(bm.getBulletData());
        }
    }
    
}
