/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bad.vector.data;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author marco.bergen
 */
@Serializable
public class ShipData {

    private Vector3f location = new Vector3f();
    private Quaternion rotation = new Quaternion();
    private Vector3f flyDirection = new Vector3f();
    //CLIENT
    private int id;
    private String name = "";
    private String texture;

    public ShipData() {
    }

    public ShipData(int _id) {
        this.id = _id;
    }

    public ShipData(int _id, String _name) {
        this.id = _id;
        this.name = _name;
    }

    /**
     * @return the location
     */
    public Vector3f getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(Vector3f location) {
        this.location = location;
    }

    /**
     * @return the rotation
     */
    public Quaternion getRotation() {
        return rotation;
    }

    /**
     * @param rotation the rotation to set
     */
    public void setRotation(Quaternion rotation) {
        this.rotation = rotation;
    }

    /**
     * @return the flyDirection
     */
    public Vector3f getFlyDirection() {
        return flyDirection;
    }

    /**
     * @param flyDirection the flyDirection to set
     */
    public void setFlyDirection(Vector3f flyDirection) {
        this.flyDirection = flyDirection;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
    
    /**
     * @return the id
     */
    public void setId(int i) {
        this.id = i;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the texture
     */
    public String getTexture() {
        return texture;
    }

    /**
     * @param texture the texture to set
     */
    public void setTexture(String texture) {
        this.texture = texture;
    }
}
