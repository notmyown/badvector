package bad.vector.server;

import bad.vector.messages.ShipListMessage;
import com.jme3.app.SimpleApplication;
import com.jme3.network.Network;
import com.jme3.renderer.RenderManager;
import com.jme3.network.Server;
import com.jme3.system.JmeContext;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * test
 *
 * @author normenhansen
 */
public class ServerMain extends SimpleApplication {

    private Server server;
    private Logger log = Logger.getLogger(ServerMain.class.getName());
    private ServerDataManager serverDataManager;

    private String ip;
    private int port;
    
    public ServerMain(String ip, int port) {
      this.ip =ip;
      this.port = port;
    }
    
    public static void startServer() {
        ServerMain app = new ServerMain(Utils.SERVER_HOST, Utils.SERVER_PORT);
        app.start(JmeContext.Type.Headless);
    }

    @Override
    public void simpleInitApp() {
        initDataManager();
        initServer();
    }

    private void initServer() {
        try {
            server = Network.createServer(port);
            server.addMessageListener(new ServerListener(server, serverDataManager));
            server.start();
            log.log(Level.INFO, "Server gestartet");
        } catch (IOException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
        server.broadcast(new ShipListMessage(serverDataManager.getShips()));
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    private void initDataManager() {
        serverDataManager = new ServerDataManager();
    }
    
     public static boolean initServer(String host, int port) {
        boolean portTaken = false;
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            portTaken = true;
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) { /* e.printStackTrace(); */ }
            }
        }
        if (!portTaken) {
            ServerMain.startServer();
        }
        return true;
    }
}
