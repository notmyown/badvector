/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bad.vector.entities.tasks.helm;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

import bad.vector.BADVector;
import bad.vector.entities.Space;
import bad.vector.messages.ShipDataMessage;

public class HelmControl extends AbstractControl {

	private int screenWidth, screenHeight;
	// is the player currently moving?
	public boolean up, down, left, right;
	// speed of the player
	private float speed = 800f;
	// lastRotation of the player
	private float lastRotation = 0f;
	private float rotSpeed = 1f;
	private Space space;
	private final Quaternion rollLeft;
	private final Quaternion rollRight;
	private BADVector app;

	public HelmControl(BADVector bad, Space space, int width, int height) {
		this.app = bad;
		this.screenWidth = width;
		this.screenHeight = height;
		this.space = space;

		this.rollLeft = new Quaternion();
		this.rollLeft.fromAngleAxis(FastMath.PI / (180 * this.rotSpeed), new Vector3f(0, 0, 1));
		this.rollRight = new Quaternion();
		this.rollRight.fromAngleAxis(FastMath.PI / (180 * this.rotSpeed), new Vector3f(0, 0, -1));
	}

	@Override
	protected void controlUpdate(float tpf) {
		float amount = tpf * this.speed;
		// move the player in a certain direction
		// if he is not out of the screen
		if (this.up) {
			float[] angles = { 0f, 0f, 0f };
			angles = app.getShipNode().getLocalRotation().toAngles(angles);
			float hc = amount * FastMath.sin(angles[2]);
			float q = FastMath.sqrt(FastMath.sqr(amount) - FastMath.sqr(hc));
			if ((angles[0] > 3 && angles[1] > 3) || (angles[0] < -3 && angles[1] < -3)) {
				q *= -1;
			}
			app.getGameNode()
					.setLocalTranslation(app.getGameNode().getLocalTranslation().addLocal(new Vector3f(-q, -hc, 0)));
			app.getShipNode()
					.setLocalTranslation(app.getShipNode().getLocalTranslation().addLocal(new Vector3f(q, hc, 0)));
			this.app.getShipData().setLocation(this.app.getShipData().getLocation().addLocal(new Vector3f(q, hc, 0)));
		}
		if (this.down) {
			float[] angles = { 0f, 0f, 0f };
			angles = app.getShipNode().getLocalRotation().toAngles(angles);
			float hc = amount * FastMath.sin(angles[2]);
			float q = FastMath.sqrt(FastMath.sqr(amount) - FastMath.sqr(hc));
			if ((angles[0] > 3 && angles[1] > 3) || (angles[0] < -3 && angles[1] < -3)) {
				q *= -1;
			}
			app.getGameNode()
					.setLocalTranslation(app.getGameNode().getLocalTranslation().addLocal(new Vector3f(q, hc, 0)));
			app.getShipNode()
					.setLocalTranslation(app.getShipNode().getLocalTranslation().addLocal(new Vector3f(-q, -hc, 0)));
			this.app.getShipData().setLocation(this.app.getShipData().getLocation().addLocal(new Vector3f(-q, -hc, 0)));
		}
		if (this.left) {
			app.getShipNode().setLocalRotation(app.getShipNode().getLocalRotation().mult(this.rollLeft));

		}
		if (this.right) {
			app.getShipNode().setLocalRotation(app.getShipNode().getLocalRotation().mult(this.rollRight));
		}
		if (this.up || this.down || this.left || this.right) {
      this.app.getShipData().setRotation(app.getShipNode().getLocalRotation());
			this.app.getHudText().setText(this.app.getShipData().getLocation().toString());
			this.app.getClient().send(new ShipDataMessage(this.app.getShipData()));
		}

		this.up = false;
		this.down = false;
		this.left = false;
		this.right = false;
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
	}

	// reset the moving values (i.e. for spawning)
	/**
	 * 
	 *
	 * @author EBERMAY
	 * @since 15.02.2016
	 */
	public void reset() {
		this.up = false;
		this.down = false;
		this.left = false;
		this.right = false;
	}
}
