/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bad.vector.entities;

import bad.vector.BADVector;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;

/**
 *
 * @author EBERMAY
 */
abstract public class Entity {

    BADVector app;
    private AssetManager assetManager;
    private InputManager inputManager;
    
    private Spatial spatial;
    
    public Entity(BADVector bad) {
        this.app = bad;
        this.assetManager = bad.getAssetManager();
        this.inputManager = bad.getInputManager();
    }
    
    public Spatial getSpatial() {
        if (this.spatial == null) {
            this.spatial = Entity.getSpatial(this);
        }
        return this.spatial;
    }
    
    abstract public String getTexture();

    public static Spatial getSpatial(Entity entity) {
        String name = entity.getTexture();
        Node node = new Node(name);
//        load picture
        Picture pic = new Picture(name);
        Texture2D tex = (Texture2D) entity.assetManager.loadTexture("Textures/" + name + ".png");
        pic.setTexture(entity.assetManager, tex, true);

//        adjust picture
        float width = tex.getImage().getWidth();
        float height = tex.getImage().getHeight();
        pic.setWidth(width);
        pic.setHeight(height);
        pic.move(-width / 2f, -height / 2f, 0);

//        add a material to the picture
        Material picMat = new Material(entity.assetManager, "Common/MatDefs/Gui/Gui.j3md");
        picMat.getAdditionalRenderState().setBlendMode(BlendMode.AlphaAdditive);
        node.setMaterial(picMat);

//        set the radius of the spatial
//        (using width only as a simple approximation)
        node.setUserData("radius", width / 2);

//        attach the picture to the node and return it
        node.attachChild(pic);
        return node;
    }
}
