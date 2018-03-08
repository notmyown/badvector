/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bad.vector.entities.tasks;

import bad.vector.BADVector;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;

/**
 *
 * @author EBERMAY
 */
abstract public class Task implements ActionListener, AnalogListener {

    protected BADVector app;
    protected InputManager inputManager;

    public Task(BADVector bad) {
        app = bad;
        inputManager = bad.getInputManager();
        bind();
    }

    abstract public void bindInput();

    abstract public void bindControls();

    private void bind() {
        bindInput();
        bindControls();
    }
}
