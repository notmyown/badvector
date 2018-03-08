package bad.vector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Network;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;

import bad.vector.client.ClientListener;
import bad.vector.client.SceneManager;
import bad.vector.data.ShipData;
import bad.vector.entities.Entity;
import bad.vector.entities.Ship;
import bad.vector.entities.Space;
import bad.vector.entities.tasks.Task;
import bad.vector.entities.tasks.helm.HelmTask;
import bad.vector.entities.tasks.weapons.WeaponsTask;
import bad.vector.menu.BadScreenController;
import bad.vector.messages.CloseConnectionMessage;
import bad.vector.messages.ShipDataMessage;
import bad.vector.server.Utils;
import bad.vector.utils.AppInit;
import de.lessvoid.nifty.Nifty;

/**
 * test
 *
 * @author normenhansen
 */
public class BADVector extends SimpleApplication implements ClientStateListener {

	private Entity ship;
	private Space space;
	private Node bulletNode;
	private Node gameNode = new Node("Game Node");
	private Node shipNode = new Node("Ship Node");
	private ViewPort gameViewPort;
	private List<Task> tasks = new ArrayList<>();
	private SceneManager sceneManager;
	private BulletAppState bulletAppState;
	private ShipData shipData;
	private Client client;
	private BitmapText hudText;

	public int gameState = 0;
	public Nifty nifty;

	private static BADVector app;

	public static void main(String[] args) {
		Utils.serializeMessages();
		app = new BADVector();
		AppSettings settings = new AppSettings(true);
		settings.setResolution(850, 567);
		settings.setResizable(true);
		settings.setFullscreen(false);
		app.setSettings(settings);
		// ServerMain.initServer(Utils.SERVER_HOST, Utils.SERVER_PORT);
		app.setPauseOnLostFocus(false);
		app.start(JmeContext.Type.Display);
	}

	public static BADVector instance() {
		return app;
	}

	public void initApp() {
		nifty.gotoScreen("empty");

		AppInit.init2DCam(this);
		AppInit.showStatView(this, false);
		initGameViewPort();

		this.bulletAppState = new BulletAppState();
		this.stateManager.attach(this.bulletAppState);
		this.bulletAppState.setDebugEnabled(true);
		this.sceneManager = new SceneManager(this, this.bulletAppState, this.assetManager);

		initShipData();

		this.space = new Space(this);
		this.space.getSpatial().move(this.settings.getWidth() / 2, this.settings.getHeight() / 2, 0);
		this.gameNode.attachChild(this.space.getSpatial());

		// Setup the player
		this.ship = new Ship(this);
		this.ship.getSpatial().setUserData("alive", true);
		this.shipNode.attachChild(this.ship.getSpatial());
		this.shipNode.move(this.settings.getWidth() / 2, this.settings.getHeight() / 2, 0);
		this.gameNode.attachChild(this.shipNode);
		this.shipData.setTexture(this.ship.getTexture());

		this.bulletNode = new Node("bullets");
		this.gameNode.attachChild(this.bulletNode);

		// Die Tasks müssen über die GUI gewählt werden
		this.tasks.add(new HelmTask(this));
		this.tasks.add(new WeaponsTask(this));
		this.gameNode.move(0, 0, 0);
		this.rootNode.attachChild(this.gameNode);

		this.hudText = new BitmapText(this.guiFont, false);
		getHudText().setSize(this.guiFont.getCharSet().getRenderedSize()); // font
																			// size
		getHudText().setColor(ColorRGBA.White); // font color
		getHudText().setText("You can write any string here"); // the text
		getHudText().setLocalTranslation(0, getHudText().getLineHeight(), 0); // position
		this.guiNode.attachChild(getHudText());

		initConnection();
	}

	@Override
	public void simpleInitApp() {
		if (this.gameState == 0) {
			loadStartScreen();
		}
	}

	public void loadStartScreen() {

		NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(this.assetManager, this.inputManager, this.audioRenderer,
				this.guiViewPort);
		this.nifty = niftyDisplay.getNifty();
		this.nifty.fromXml("Interface/start.xml", "start");
		BadScreenController bsc = (BadScreenController) this.nifty.getCurrentScreen().getScreenController();
		bsc.app = this;
		// bsc.app = this;
		// attach the nifty display to the gui view port as a processor
		this.guiViewPort.addProcessor(niftyDisplay);
		// disable the fly cam
		this.flyCam.setEnabled(false);
	}

	public AppSettings getSettings() {
		return this.settings;
	}

	public Entity getShip() {
		return this.ship;
	}

	public ShipData getShipData() {
		return this.shipData;
	}

	public Space getSpace() {
		return this.space;
	}

	public Node getGameNode() {
		return this.gameNode;
	}

	public Node getShipNode() {
		return this.shipNode;
	}

	public Node getBulletNode() {
		return this.bulletNode;
	}

	@Override
	public void simpleUpdate(float tpf) {
		//
	}

	@Override
	public void simpleRender(RenderManager rm) {
		// TODO: add render code
	}

	private void initGameViewPort() {
		// Create a new cam for the gui
		Camera gameCam = new Camera(this.settings.getWidth(), this.settings.getHeight());
		this.gameViewPort = this.renderManager.createMainView("Game Default", gameCam);
		// gameViewPort.setClearFlags(false, false, false);
		this.gameNode.setQueueBucket(RenderQueue.Bucket.Gui);
		this.gameNode.setCullHint(Spatial.CullHint.Never);
		this.gameViewPort.attachScene(this.gameNode);
		FilterPostProcessor fpp = new FilterPostProcessor(this.assetManager);
		BloomFilter bloom = new BloomFilter();
		bloom.setBloomIntensity(2f);
		bloom.setExposurePower(2);
		bloom.setExposureCutOff(0f);
		bloom.setBlurScale(1.5f);

		fpp.addFilter(bloom);
		this.gameViewPort.addProcessor(fpp);
		this.gameViewPort.setClearColor(true);
	}

	private void initShipData() {
		this.shipData = new ShipData();
	}

	private void initConnection() {
		try {
			this.client = Network.connectToServer(Utils.SERVER_HOST, Utils.SERVER_PORT);
			this.client.addMessageListener(new ClientListener(this.client, this.sceneManager));
			this.client.addClientStateListener(this);
			this.client.start();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void clientConnected(Client c) {
		this.shipData.setId(c.getId());
		this.sceneManager.setMyID(c.getId());
		this.client.send(new ShipDataMessage(this.shipData));
	}

	public void clientDisconnected(Client c, ClientStateListener.DisconnectInfo info) {
	}

	@Override
	public void destroy() {
		if (this.bulletAppState != null) {
			this.bulletAppState.cleanup();
		}
		if (this.client != null) {
			this.client.send(new CloseConnectionMessage(this.shipData.getId()));
		}
		super.destroy();
	}

	/**
	 * @return the hudText
	 */
	public BitmapText getHudText() {
		return this.hudText;
	}

	public Client getClient() {
		return this.client;
	}
}
