/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bad.vector.entities.tasks.weapons;

import bad.vector.BADVector;
import bad.vector.data.BulletData;
import bad.vector.entities.Bullet;
import bad.vector.entities.tasks.Task;
import bad.vector.messages.BulletCreateMessage;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author EBERMAY
 */
public class WeaponsTask extends Task {

	public static int rockettype = 0;
	private long bulletCooldown = 0l;
	private float bulletCooldownTime = 83f;
	private long bulletChangeCooldown;

	public WeaponsTask(BADVector bad) {
		super(bad);
	}

	@Override
	public void bindInput() {
		this.inputManager.addMapping("return", new KeyTrigger(KeyInput.KEY_RETURN));
		this.inputManager.addListener(this, "return");
	}

	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		this.bulletChangeCooldown = System.currentTimeMillis();
		if (name.equals("key1")) {
			rockettype = 0;
		} else if (name.equals("key2")) {
			rockettype = 1;
		} else if (name.equals("key3")) {
			rockettype = 2;
		}
		switch (rockettype) {
		case 0: {
			this.bulletCooldownTime = 83f;
			break;
		}
		case 1: {
			this.bulletCooldownTime = 500f;
			break;
		}
		case 2: {
			this.bulletCooldownTime = 1000f;
			break;
		}

		}
	}

	@Override
	public void onAnalog(String name, float value, float tpf) {
		if ((Boolean) this.app.getShip().getSpatial().getUserData("alive")) {
			if (name.equals("mousePick")) {
				// shoot Bullet
				if (System.currentTimeMillis() - this.bulletCooldown > this.bulletCooldownTime) {
					this.bulletCooldown = System.currentTimeMillis();

					Vector3f aim = getAimDirection();
					Vector3f offset = new Vector3f(aim.y / 3, -aim.x / 3, 0);

					// init bullet 1
					Bullet bullet = new Bullet(this.app, rockettype);
					Spatial bulletSpatial = bullet.getSpatial();
					Vector3f finalOffset = aim.add(offset).mult(30);
					Vector3f trans = this.app.getShipNode().getLocalTranslation().add(finalOffset);
					bulletSpatial.setLocalTranslation(trans);
					bulletSpatial.addControl(new BulletControl(aim, this.app.getSettings().getWidth(),
							this.app.getSettings().getHeight(), 1100f / (rockettype + 1)));
					this.app.getBulletNode().attachChild(bulletSpatial);
					Vector3f bStart = new Vector3f(0, 0, 0);
					bStart.addLocal(trans);
					bStart.addLocal(this.app.getShipData().getLocation());
					BulletData bd = new BulletData();
					bd.setType(rockettype);
					bd.setLocation(bStart);
					bd.setFlyDirection(aim);
					this.app.getClient().send(new BulletCreateMessage(bd));

					if (rockettype == 0) {
						// init bullet 2
						Bullet bullet2 = new Bullet(this.app, rockettype);
						Spatial bulletSpatial2 = bullet2.getSpatial();
						finalOffset = aim.add(offset.negate()).mult(30);
						trans = this.app.getShipNode().getLocalTranslation().add(finalOffset);
						bulletSpatial2.setLocalTranslation(trans);
						bulletSpatial2.addControl(new BulletControl(aim, this.app.getSettings().getWidth(),
								this.app.getSettings().getHeight(), 1100f / (rockettype + 1)));
						this.app.getBulletNode().attachChild(bulletSpatial2);
						Vector3f bStart2 = new Vector3f(0, 0, 0);
						bStart2.addLocal(trans);
						bStart2.addLocal(this.app.getShipData().getLocation());
						BulletData bd2 = new BulletData();
						bd2.setType(rockettype);
						bd2.setLocation(bStart2);
						bd2.setFlyDirection(aim);
						this.app.getClient().send(new BulletCreateMessage(bd2));
					}
				}
			}
		}
	}

	private Vector3f getAimDirection() {
		Vector2f mouse = this.inputManager.getCursorPosition();
		System.err.println(mouse.x + " - " + mouse.y);
		Vector3f playerPos = this.app.getShipNode().getLocalTranslation();
		System.err.println(playerPos.x + " - " + playerPos.y);
		Vector3f dif = new Vector3f(mouse.x - playerPos.x, mouse.y - playerPos.y, 0);
		return dif.normalizeLocal();
	}

	@Override
	public void bindControls() {
		this.inputManager.addMapping("mousePick", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		this.inputManager.addListener(this, "mousePick");
		this.inputManager.addMapping("key1", new KeyTrigger(KeyInput.KEY_1));
		this.inputManager.addMapping("key2", new KeyTrigger(KeyInput.KEY_2));
		this.inputManager.addMapping("key3", new KeyTrigger(KeyInput.KEY_3));
		this.inputManager.addListener(this, "key1");
		this.inputManager.addListener(this, "key2");
		this.inputManager.addListener(this, "key3");
	}
}
