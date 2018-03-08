/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bad.vector.entities;

import bad.vector.BADVector;

/**
 *
 * @author EBERMAY
 */
public class Bullet extends Entity {

    
    private int rockettype = 0;
    
    public Bullet(BADVector app, int type) {
        super(app);
        this.rockettype = type;
    }

    @Override
    public String getTexture() {
        return getClassName();
    }

    public String getClassName() {
        switch (rockettype) {
            case 0:
                return "Bullet";
            case 1:
                return "Missile";
            case 2:
                return "Nuke";
        }
        return "Bullet";
    }
}
