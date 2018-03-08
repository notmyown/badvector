/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bad.vector.client;

import bad.vector.BADVector;
import bad.vector.data.BulletData;
import bad.vector.data.ShipData;
import bad.vector.entities.Bullet;
import bad.vector.entities.Entity;
import bad.vector.entities.tasks.weapons.BulletControl;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

/**
 *
 * @author Admin
 */
public class SceneManager {

	private Map<Integer, ShipData> ships = new HashMap<>();
	private Map<Integer, Spatial> entities = new HashMap<>();
	private int myID;
	private final AssetManager assetManager;
	private BADVector client;
	private final BulletAppState bulletAppState;

	public SceneManager(BADVector c, BulletAppState bas, AssetManager assetM) {
		this.assetManager = assetM;
		this.client = c;
		this.bulletAppState = bas;
	}

	public void addOrRefreshShip(int id, ShipData shipData, Spatial spatial) {
		ships.put(id, shipData);
	}

	public void removeShip(final int id) {
		if (this.myID != id) {
			ships.remove(id);
			if (entities.containsKey(id)) {
				client.enqueue(new Callable<Object>() {
					@Override
					public Object call() throws Exception {
						((Node) client.getSpace().getSpatial()).detachChild(entities.get(id));
						bulletAppState.getPhysicsSpace().remove(entities.get(id));
						entities.remove(id);
						return null;
					}
				});
			}
		}
	}

	public boolean containsShip(int id) {
		return ships.containsKey(id);
	}

	public Map<Integer, ShipData> getShips() {
		return this.ships;
	}

	public void refeshShipList(Map<Integer, ShipData> ships) {
		this.ships = ships;
		updateAllShipEntities();
	}

	private void updateAllShipEntities() {
		for (Entry<Integer, ShipData> entry : ships.entrySet()) {
			updateShipEntity(entry.getKey(), entry.getValue());
		}
	}

	private void updateShipEntity(final Integer id, final ShipData ship) {
		if (this.myID != id) {
			final Spatial me = client.getShip().getSpatial();
			final Spatial space = client.getSpace().getSpatial();
			if (!entities.containsKey(id)) {
				loadAndAddShipToRootNode(id, ship);
				return;
			}
			client.enqueue(new Callable<Object>() {
				@Override
				public Object call() throws Exception {
					entities.get(id).setLocalTranslation(ship.getLocation());
					entities.get(id).setLocalRotation(ship.getRotation());
					return null;
				}
			});
		}

	}

	/**
	 * @return the myID
	 */
	public int getMyID() {
		return myID;
	}

	/**
	 * @param myID
	 *            the myID to set
	 */
	public void setMyID(int myid) {
		this.myID = myid;
	}

	private void loadAndAddShipToRootNode(final Integer id, ShipData ship) {
		if (ship != null && ship.getTexture() != null) {
			entities.put(id, loadSpatial(ship.getLocation(), ship.getRotation(), ship));
			entities.get(id).move(ship.getLocation());
			entities.get(id).setLocalRotation(ship.getRotation());
			client.enqueue(new Callable<Object>() {
				@Override
				public Object call() throws Exception {
					((Node) client.getSpace().getSpatial()).attachChild(entities.get(id));
					bulletAppState.getPhysicsSpace().addAll(entities.get(id));
					return null;
				}
			});
		}
	}

	private Spatial loadSpatial(Vector3f location, Quaternion rotation, ShipData data) {
		Spatial spatial = getSpatial(data);
		spatial.setLocalTranslation(location);
		spatial.setLocalRotation(rotation);
		return spatial;
	}

	public Spatial getSpatial(ShipData entity) {
		String name = entity.getTexture();
		Node node = new Node(name);
		// load picture
		Picture pic = new Picture(name);
		Texture2D tex = (Texture2D) assetManager.loadTexture("Textures/" + name + ".png");
		pic.setTexture(assetManager, tex, true);

		// adjust picture
		float width = tex.getImage().getWidth();
		float height = tex.getImage().getHeight();
		pic.setWidth(width);
		pic.setHeight(height);
		pic.move(-width / 2f, -height / 2f, 0);

		// add a material to the picture
		Material picMat = new Material(assetManager, "Common/MatDefs/Gui/Gui.j3md");
		picMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.AlphaAdditive);
		node.setMaterial(picMat);

		// set the radius of the spatial
		// (using width only as a simple approximation)
		node.setUserData("radius", width / 2);

		// attach the picture to the node and return it
		node.attachChild(pic);
		return node;
	}

	void addBullet(final BulletData bulletData) {
		client.enqueue(new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				Bullet bullet = new Bullet(client, bulletData.getType());
				Spatial bulletSpatial = bullet.getSpatial();
				Vector3f ps = new Vector3f(0, 0, 0);
				ps.addLocal(client.getSpace().getSpatial().getLocalTranslation());
				ps.negate();
				ps.subtractLocal(
						new Vector3f(client.getSettings().getWidth() / 2, client.getSettings().getHeight() / 2, 0));

				bulletSpatial.setLocalTranslation(bulletData.getLocation().addLocal(ps));
				float speed = 1100f / (bulletData.getType() + 1);
				bulletSpatial.addControl(new BulletControl(bulletData.getFlyDirection(),
						client.getSettings().getWidth(), client.getSettings().getHeight(), speed));
				client.getBulletNode().attachChild(bulletSpatial);
				return null;
			}
		});

	}
}
