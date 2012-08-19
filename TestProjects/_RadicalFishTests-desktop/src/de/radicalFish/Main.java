package de.radicalFish;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.Array;
import de.matthiasmann.twl.Alignment;
import de.matthiasmann.twl.FPSCounter;
import de.matthiasmann.twl.Label;
import de.radicalFish.tests.GameTest;
import de.radicalfish.GameContainer;
import de.radicalfish.Graphics;
import de.radicalfish.debug.Debug;
import de.radicalfish.debug.DebugCallback;
import de.radicalfish.debug.DeveloperConsole;
import de.radicalfish.debug.PerformanceListener;
import de.radicalfish.debug.ToolBox;
import de.radicalfish.debug.parser.URLInputParser;
import de.radicalfish.util.RadicalFishException;

public class Main implements DebugCallback {
	
	private Array<PerformanceListener> perListener;
	
	private Debug debug;
	private ToolBox toolbox;
	
	
	
	// MAIN and C'TOR
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	public Main() throws RadicalFishException {
		LwjglApplicationConfiguration config = createConfig();
		
		GameContainer app = new GameContainer(config.title, new GameTest(), 800, 600, config.useGL20);
		app.setDebugCallBack(this);
		app.setSmoothDelta(true);
		
		new LwjglApplication(app, config);
	}
	public static void main(String[] args) throws RadicalFishException {
		new Main();
	}
	
	public static void write() {
		try {
			PrintWriter w = new PrintWriter("insert.bla");
			
			w.write("switch (gdxKeyCode) {\n");
			for (int i = 0; i < 257; i++) {
				w.write("case " + (i - 1) + ":\n");
				w.write("return 0;\n");
			}
			w.write("}");
			
			w.flush();
			w.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// GAME METHODS
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	public void init(GameContainer container) throws RadicalFishException {
		debug = new Debug(container);
		debug.setVisible(false);
		buildGUI(container);
		toolbox.show();
	}
	public void update(GameContainer container, float delta) throws RadicalFishException {
		if (container.getInput().isKeyPressed(Keys.F1)) {
			debug.setVisible(!debug.isVisible());
			if (debug.isVisible()) {
				toolbox.show();
				if (debug.getCurrentFocusOwner() != null) {
					debug.getCurrentFocusOwner().requestKeyboardFocus();
				}
			} else {
				toolbox.hide();
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
		cfg.useGL20 = true;
		cfg.width = 1000;
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
