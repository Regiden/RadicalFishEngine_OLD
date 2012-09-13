package de.radicalfish;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.Array;
import de.matthiasmann.twl.Alignment;
import de.matthiasmann.twl.FPSCounter;
import de.matthiasmann.twl.Label;
import de.radicalfish.context.GameContext;
import de.radicalfish.context.Settings;
import de.radicalfish.debug.DebugAdapter;
import de.radicalfish.debug.DebugPanel;
import de.radicalfish.debug.DeveloperConsole;
import de.radicalfish.debug.PerformanceListener;
import de.radicalfish.debug.SettingsEditor;
import de.radicalfish.debug.ToolBox;
import de.radicalfish.debug.parser.PropertyInputParser;
import de.radicalfish.debug.parser.URLInputParser;
import de.radicalfish.graphics.Graphics;
import de.radicalfish.state.StateBasedGame;
import de.radicalfish.tests.StatesTest;
import de.radicalfish.util.RadicalFishException;

public class DebugTest extends DebugAdapter {
	
	private Array<PerformanceListener> perListener;
	
	private ToolBox toolbox;
	private DebugPanel debug;
	
	private GameContext context;
	private Settings settings;
	private boolean hasContext;
	
	// MAIN and C'TOR
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public DebugTest() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Debug Test";
		config.useGL20 = false;
		config.width = 800;
		config.height = 600;
		config.useCPUSynch = false; // to true on release, else minimizing window will freak out
		
		GameContainer app = new GameContainer(config.title, new StatesTest(), config.width, config.height, config.useGL20);
		app.setDebugCallBack(this);
		app.setSmoothDelta(true);
		new LwjglApplication(app, config);
	}
	public static void main(String[] args) {
		new DebugTest();
	}
	
	// GAME METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void init(GameContainer container) {
		// if the game from the container is ContextGame, we can get the context
		if (container.game instanceof ContextGame) {
			context = ((StateBasedGame) container.game).getGameContext();
			hasContext = true;
		}
		
		// for testing we load all the settings from an internal file for startup
		// to avoid making our life hard we just use the Properties class here
		if (hasContext) {
			settings = context.getSettings();
			loadSettings();
		}
		settings.printSettings();
		
		debug = new DebugPanel(container);
		debug.setVisible(false);
		buildGUI(container);
		
	}
	public void update(GameContainer container, float delta) {
		if (container.getInput().isKeyPressed(Keys.F1)) {
			debug.setVisible(!debug.isVisible());
			if (debug.isVisible()) {
				if (debug.getCurrentFocusOwner() != null) {
					debug.getCurrentFocusOwner().requestKeyboardFocus();
				}
			}
		}
		debug.update();
	}
	public void render(GameContainer container, Graphics g) {
		
		debug.render();
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void addPerformanceListener(PerformanceListener listener) {
		
	}
	public void removePerformanceListener(PerformanceListener listener) {
		
	}
	
	// INTERN METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void loadSettings() {
		// lets load the options.txt we have in an internal folder
		// in a real game this should be done
		// we assume that a key-value pair is split by a :
		// Properties class gives more insight in how you could read your properties first time.
		
		Map<String, String> map = new HashMap<String, String>();
		BufferedReader br = new BufferedReader(Gdx.files.internal("data/options.txt").reader());
		String line;
		try {
			while ((line = br.readLine()) != null) {
				// skip comments and white lines
				if (line.isEmpty() || line.startsWith("#")) {
					continue;
				}
				String[] keyvalue = line.split(":");
				if (keyvalue.length == 2) {
					// only add those with a key and a value
					map.put(keyvalue[0], keyvalue[1]);
				}
			}
		} catch (IOException e) {
			throw new RadicalFishException(e.getMessage());
		}
		
		// add all the values 
		settings.setAllSettings(map);
	}
	
	private void buildGUI(final GameContainer container) {
		final DeveloperConsole dev = new DeveloperConsole();
		dev.addInputParser(new URLInputParser());
		if(hasContext) {
			dev.addInputParser(new PropertyInputParser(settings));
		}
		
		final SettingsEditor set = new SettingsEditor(settings);
		
		Label fps = new Label("FPS:");
		FPSCounter fpsCounter = new FPSCounter();
		
		toolbox = new ToolBox();
		toolbox.setCanAcceptKeyboardFocus(false);
		
		toolbox.addButton("Console", new Runnable() {
			public void run() {
				dev.setVisible(!dev.isVisible());
			}
		});
		toolbox.addButton("Settings", new Runnable() {
			public void run() {
				set.setVisible(!set.isVisible());
			}
		});
		
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
		debug.addToRoot(set);
		debug.addToRoot(toolbox);
	}
	
	// SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void setVisible(boolean visible) {
		debug.setVisible(visible);
	}
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public Array<PerformanceListener> getPerformanceListener() {
		return perListener;
	}
	public boolean isVisible() {
		return debug.isVisible();
	}
}
