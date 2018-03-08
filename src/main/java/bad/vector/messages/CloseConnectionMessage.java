/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bad.vector.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;

/**
 *
 * @author Admin
 */
@Serializable
public class CloseConnectionMessage extends AbstractMessage {
    
    private int id;
    
    public CloseConnectionMessage() {
    }
    
    public CloseConnectionMessage(int id) {
        this.id = id;
    }
    
    public int getId() {
      return this.id;
    }
    
}
