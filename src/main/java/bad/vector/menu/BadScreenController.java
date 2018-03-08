/**
 *
 ******************************************************************
 *     Copyright VW AG, Germany     *
 ******************************************************************
 *
 ******************************************************************
 *Administrative Information (automatically filled in by MKS)
 ******************************************************************
 *
 * $ProjectName: $
 * $Author: $
 * $Date: $
 * $Name:  $
 * $ProjectRevision: 1.81 $
 * $Revision: 1.141 $
 * $Source: central.mak $
 ******************************************************************
**/
package bad.vector.menu;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

import bad.vector.BADVector;
import bad.vector.server.ServerMain;
import bad.vector.server.Utils;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class BadScreenController extends AbstractAppState implements ScreenController {

	private Nifty nifty;
	public Application app;
	private Screen screen;

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		this.app = app;
	}

	@Override
	public void update(float tpf) {
		// TODO: implement behavior during runtime
	}

	@Override
	public void cleanup() {
		super.cleanup();
		// TODO: clean up what you initialized in the initialize method,
		// e.g. remove all spatials from rootNode
		// this is called on the OpenGL thread after the AppState has been
		// detached
	}

	@Override
	public void bind(Nifty nifty, Screen screen) {
		this.nifty = nifty;
		this.screen = screen;
		System.err.println("bind");
	}

	@Override
	public void onStartScreen() {

	}

	@Override
	public void onEndScreen() {

	}

	public void start() {
		ServerMain.initServer(Utils.SERVER_HOST, Utils.SERVER_PORT);
		BADVector.instance().initApp();
	}

	public void settings() {
		System.err.println("Settings");
	}

	public void screen(String nextScreen) {
		nifty.gotoScreen(nextScreen);
	}

	private int startmode = 0;
	private static int maxstarts = 1;

	public void switchStartMode(String dir) {
		if ("left".equals(dir)) {
			startmode--;
		} else if ("right".equals(dir)) {
			startmode++;
		}
		Element image = nifty.getCurrentScreen().findElementByName("modusimage");
		if (startmode < 0) {
			startmode = maxstarts;
		} else if (startmode > maxstarts) {
			startmode = 0;
		}
		image.setStyle("bad-start-" + startmode);
	}

	public void switchStart() {
		if (startmode == 1) {
			screen("createserver_settings");
		}
	}

	private int gamemode = 0;
	private static int maxmodes = 3;

	public void switchGameMode(String dir) {
		if ("left".equals(dir)) {
			gamemode--;
		} else if ("right".equals(dir)) {
			gamemode++;
		}
		Element image = nifty.getCurrentScreen().findElementByName("modusimage");
		if (gamemode < 0) {
			gamemode = maxmodes;
		} else if (gamemode > maxmodes) {
			gamemode = 0;
		}
		image.setStyle("bad-modus-" + gamemode);
	}

}
