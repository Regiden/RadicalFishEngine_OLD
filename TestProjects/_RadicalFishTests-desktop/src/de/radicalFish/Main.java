package de.radicalfish;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.Array;
import de.matthiasmann.twl.Alignment;
import de.matthiasmann.twl.FPSCounter;
import de.matthiasmann.twl.Label;
import de.radicalfish.debug.Debug;
import de.radicalfish.debug.DebugCallback;
import de.radicalfish.debug.DeveloperConsole;
import de.radicalfish.debug.PerformanceListener;
import de.radicalfish.debug.ToolBox;
import de.radicalfish.debug.parser.URLInputParser;
import de.radicalfish.graphics.Graphics;
import de.radicalfish.tests.SpriteBatchError;
import de.radicalfish.util.RadicalFishException;

public class Main implements DebugCallback {
	
	private Array<PerformanceListener> perListener;
	
	private Debug debug;
	private ToolBox toolbox;
	
	
	
	// MAIN and C'TOR
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	public Main() throws RadicalFishException {
		LwjglApplicationConfiguration config = createConfig();
		new LwjglApplication(new SpriteBatchError(config.width, config.height), config);
	}
	public static void main(String[] args) throws RadicalFishException {
		new Main();
	}
	
	// GAME METHODS
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	public void init(GameContainer container) throws RadicalFishException {
		debug = new Debug(container);
		debug.setVisible(false);
		buildGUI(container);
	}
	public void update(GameContainer container, float delta) throws RadicalFishException {
		if (container.getInput().isKeyPressed(Keys.F1)) {
			debug.setVisible(!debug.isVisible());
			if (debug.isVisible()) {
				if (debug.getCurrentFocusOwner() != null) {
					debug.getCurrentFocusOwner().requestKeyboardFocus();
				}
			}
		}
	}
	public void render(GameContainer container, Graphics g) throws RadicalFishException {
		debug.update();
		debug.render();
	}
	
	public void pause(GameContainer container) throws RadicalFishException {
		
	}
	public void resume(GameContainer container) throws RadicalFishException {
		
	}
	public void dispose() {
		
	}
	
	// METHODS
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	public void addPerformanceListener(PerformanceListener listener) {
		
	}
	public void removePerformanceListener(PerformanceListener listener) {
		
	}
	
	// INTERFACE
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	
	// INTERN METHODS
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	private LwjglApplicationConfiguration createConfig() {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "RadicalFishTests";
		cfg.useGL20 = false;
		cfg.width = 800;
		cfg.height = 600;
		cfg.useCPUSynch = false; // to true on release, else minimizing window will freak out
		return cfg;
	}
	private void buildGUI(final GameContainer container) {
		DeveloperConsole dev = new DeveloperConsole();
		dev.addInputParser(new URLInputParser());
		
		Label fps = new Label("FPS:");
		FPSCounter fpsCounter = new FPSCounter();
		
		toolbox = new ToolBox();
		toolbox.setCanAcceptKeyboardFocus(false);
		
		toolbox.addFiller();
		toolbox.addSeparator();
		
		toolbox.addCustomWidget(fps, Alignment.CENTER);
		toolbox.addCustomWidget(fpsCounter, Alignment.CENTER);
		
		toolbox.addSeparator();
		
		toolbox.addButton("Exit", new Runnable() {
			public void run() {
				container.exit();
			}
		});
		
		toolbox.createToolbox();
		
		debug.addToRoot(dev);
		debug.addToRoot(toolbox);
	}
	
	// SETTER
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	public void setVisible(boolean visible) {
		debug.setVisible(visible);
	}
	
	// GETTER
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	public Array<PerformanceListener> getPerformanceListener() {
		return perListener;
	}
	public boolean isVisible() {
		return debug.isVisible();
	}
}
