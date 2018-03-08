/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bad.vector.entities.tasks.helm;

import bad.vector.BADVector;
import bad.vector.entities.tasks.Task;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;

/**
 *
 * @author EBERMAY
 */
public class HelmTask extends Task {

    HelmControl control;

    public HelmTask(BADVector bad) {
        super(bad);
        control = app.getShip().getSpatial().getControl(HelmControl.class);
    }

    @Override
    public void bindInput() {
        inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("up", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("down", new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addListener(this, "left");
        inputManager.addListener(this, "right");
        inputManager.addListener(this, "up");
        inputManager.addListener(this, "down");
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if ((Boolean) app.getShip().getSpatial().getUserData("alive")) {
            
        }

    }

    @Override
    public void bindControls() {
        app.getShip().getSpatial().addControl(new HelmControl(app, app.getSpace(), app.getSettings().getWidth(), app.getSettings().getHeight()));
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        if ((Boolean) app.getShip().getSpatial().getUserData("alive")) {
            if (name.equals("up")) {
                control.up = true;
            } else if (name.equals("down")) {
                control.down = true;
            } else if (name.equals("left")) {
                control.left = true;
            } else if (name.equals("right")) {
                control.right = true;
            }
        }
    }
}
