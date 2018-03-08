/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bad.vector.server;

import bad.vector.data.ShipData;
import bad.vector.messages.BulletCreateMessage;
import bad.vector.messages.CloseConnectionMessage;
import bad.vector.messages.ShipDataMessage;
import com.jme3.network.Filters;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Server;

/**
 *
 * @author marco.bergen
 */
public class ServerListener implements MessageListener<HostedConnection> {

    private Server server;
    private final ServerDataManager serverDataManager;

    public ServerListener(Server s, ServerDataManager sdm) {
        this.serverDataManager = sdm;
        this.server = s;
    }

    public void messageReceived(HostedConnection source, Message m) {
        if (m instanceof ShipDataMessage) {
            ShipDataMessage sm = (ShipDataMessage) m;
            ShipData shipData = sm.getShipData();
            serverDataManager.addOrRefreshShip(shipData.getId(), shipData);
        } else if (m instanceof CloseConnectionMessage) {
            CloseConnectionMessage sm = (CloseConnectionMessage) m;
            serverDataManager.removeShip(sm.getId());
            server.broadcast(new CloseConnectionMessage(sm.getId()));
        } else if (m instanceof BulletCreateMessage) {
            BulletCreateMessage bm = (BulletCreateMessage) m;
            server.broadcast(Filters.notEqualTo(source), new BulletCreateMessage(bm.getBulletData()));
        }
    }
}
