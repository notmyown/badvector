/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bad.vector.utils;

import bad.vector.BADVector;
import com.jme3.math.Vector3f;

/**
 *
 * @author EBERMAY
 */
public class AppInit {
    
    public static void init2DCam(BADVector app) {
        app.getCamera().setParallelProjection(true);
        app.getCamera().setLocation(new Vector3f(0,0,0.5f));
        app.getFlyByCamera().setEnabled(false);
    }
    
    public static void showStatView(BADVector app, boolean show) {
        app.setDisplayStatView(show);
        app.setDisplayFps(show);
    }
}
