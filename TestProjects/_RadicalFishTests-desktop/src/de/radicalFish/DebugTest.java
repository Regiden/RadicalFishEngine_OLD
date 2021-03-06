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
import de.radicalfish.context.GameWithContext;
import de.radicalfish.context.Settings;
import de.radicalfish.debug.AssetsViewer;
import de.radicalfish.debug.DebugAdapter;
import de.radicalfish.debug.DebugPanel;
import de.radicalfish.debug.DeveloperConsole;
import de.radicalfish.debug.EngineSettingsEditor;
import de.radicalfish.debug.GameVariablesEditor;
import de.radicalfish.debug.PerformanceListener;
import de.radicalfish.debug.SettingsEditor;
import de.radicalfish.debug.ToolBox;
import de.radicalfish.debug.inputparser.PropertyInputParser;
import de.radicalfish.debug.inputparser.URLInputParser;
import de.radicalfish.graphics.Graphics;
import de.radicalfish.tests.GameTest;
import de.radicalfish.util.RadicalFishException;

public class DebugTest extends DebugAdapter {
	
	private Array<PerformanceListener> perListener;
	
	private ToolBox toolbox;
	private DebugPanel debug;
	private GameVariablesEditor gvmonitor;
	private AssetsViewer av;
	
	private GameContext context;
	private Settings settings;
	private boolean hasContext;
	
	// MAIN and C'TOR
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	public DebugTest() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Debug Test";
		config.useGL20 = true;
		config.width = 800;
		config.height = 600;
		config.useCPUSynch = false; // to true on release, else minimizing window will freak out
		
		GameContainer app = new GameContainer(config.title, new GameTest(), config.width, config.height, config.useGL20);
		app.setDebugCallBack(this);
		app.setSmoothDeltas(true);
		new LwjglApplication(app, config);
	}
	public static void main(String[] args) {
		new DebugTest();
	}
	
	// GAME METHODS
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	public void init(GameContainer container) {
		// if the game from the container is ContextGame, we can get the context
		if (container.game instanceof GameWithContext) {
			context = ((GameWithContext) container.game).getGameContext();
			hasContext = true;
		}
		
		// for testing we load all the settings from an internal file for startup
		// to avoid making our life hard we just use the Properties class here
		if (hasContext) {
			settings = context.getSettings();
			loadSettings();
			
			context.getGameVariables().putBoolean("test", true);
			context.getGameVariables().putBoolean("test2", true);
			context.getGameVariables().putInt("bla", 23123);
			context.getGameVariables().putInt("bla2", 23123);
		}
		
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
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	public void addPerformanceListener(PerformanceListener listener) {
		
	}
	public void removePerformanceListener(PerformanceListener listener) {
		
	}
	
	// INTERN METHODS
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
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
		final EngineSettingsEditor ese = new EngineSettingsEditor(container);
		
		if (hasContext) {
			dev.addInputParser(new PropertyInputParser(settings));
		}
		
		final SettingsEditor set = new SettingsEditor(settings);
		if (hasContext) {
			gvmonitor = new GameVariablesEditor(context.getGameVariables());
		} else {
			gvmonitor = new GameVariablesEditor();
		}
		
		if (hasContext) {
			av = new AssetsViewer(context.getAssets());
		}
		
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
		toolbox.addButton("Variables", new Runnable() {
			public void run() {
				gvmonitor.setVisible(!gvmonitor.isVisible());
			}
		});
		toolbox.addButton("Assets", new Runnable() {
			public void run() {
				av.setVisible(!av.isVisible());
			}
		});
		toolbox.addButton("Engine", new Runnable() {
			public void run() {
				ese.setVisible(!ese.isVisible());
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
		
		ese.setVisible(true);
		av.setVisible(false);
		dev.setVisible(false);
		set.setVisible(false);
		gvmonitor.setVisible(false);
		
		debug.addToRoot(dev);
		debug.addToRoot(set);
		debug.addToRoot(gvmonitor);
		debug.addToRoot(toolbox);
		debug.addToRoot(av);
		debug.addToRoot(ese);
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
