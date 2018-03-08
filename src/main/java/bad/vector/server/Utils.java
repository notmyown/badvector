/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bad.vector.server;

import bad.vector.data.BulletData;
import bad.vector.data.ShipData;
import bad.vector.messages.BulletCreateMessage;
import bad.vector.messages.BulletDestroyMessage;
import bad.vector.messages.CloseConnectionMessage;
import bad.vector.messages.ShipDataMessage;
import bad.vector.messages.ShipListMessage;
import com.jme3.network.serializing.Serializer;

/**
 *
 * @author EBERMAY
 */
public class Utils {

    public static String SERVER_HOST = "localhost";
    public static int SERVER_PORT = 4444;

    public static void serializeMessages() {
        Serializer.registerClass(ShipDataMessage.class);
        Serializer.registerClass(ShipListMessage.class);
        Serializer.registerClass(CloseConnectionMessage.class);
        Serializer.registerClass(BulletCreateMessage.class);
        Serializer.registerClass(BulletDestroyMessage.class);

        Serializer.registerClass(ShipData.class);
        Serializer.registerClass(BulletData.class);
    }
}
