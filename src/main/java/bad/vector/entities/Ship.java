/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bad.vector.entities;

import bad.vector.BADVector;
import bad.vector.entities.Entity;

/**
 *
 * @author EBERMAY
 */
public class Ship extends Entity {
    
    public Ship(BADVector app) {
        super(app);
    }

    @Override
    public String getTexture() {
        return getClassName();
    }
    
    public String getClassName() {
        return "Skylla";
    }
    
}
