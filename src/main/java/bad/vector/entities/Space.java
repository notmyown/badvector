/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bad.vector.entities;

import bad.vector.BADVector;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;

/**
 *
 * @author EBERMAY
 */
public class Space extends Entity {
    
    float width = 3000000f;
    float height = 3000000f;

    public Space(BADVector app) {
        super(app);
        
    }
    
    @Override
    public String getTexture() {
        return "space";
    }

    
}
