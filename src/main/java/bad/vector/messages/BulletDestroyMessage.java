/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bad.vector.messages;

import bad.vector.data.BulletData;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author marco.bergen
 */
@Serializable
public class BulletDestroyMessage extends AbstractMessage {
    
    public BulletData bulletData;
    
    public BulletDestroyMessage() {
    }

    public BulletDestroyMessage(BulletData sd) {
        this.bulletData = sd;
    }

    /**
     * @return the message
     */
    public BulletData getBulletData() {
        return bulletData;
    }

    /**
     * @param message the message to set
     */
    public void setBulletData(BulletData bulletData) {
        this.bulletData = bulletData;
    }
    
    
}
