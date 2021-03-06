package de.radicalfish.test.world;
import java.net.URL;
import java.util.List;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.ResourceLoader;
import de.matthiasmann.twl.ActionMap;
import de.matthiasmann.twl.Alignment;
import de.matthiasmann.twl.Color;
import de.matthiasmann.twl.FPSCounter;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.renderer.Renderer;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;
import de.radicalfish.context.GameContext;
import de.radicalfish.context.GameDelta;
import de.radicalfish.debug.DebugHook;
import de.radicalfish.debug.DeveloperConsole;
import de.radicalfish.debug.OptionsPanel;
import de.radicalfish.debug.PerformanceGraph;
import de.radicalfish.debug.PerformanceListener;
import de.radicalfish.debug.PropertyEditor;
import de.radicalfish.debug.TWLGameState;
import de.radicalfish.debug.TWLInputForwarder;
import de.radicalfish.debug.TWLRootPane;
import de.radicalfish.debug.ToneEditor;
import de.radicalfish.debug.ToolBox;
import de.radicalfish.debug.parser.PropertyInputParser;
import de.radicalfish.debug.parser.URLInputParser;
import de.radicalfish.world.Entity;
import de.radicalfish.world.EntitySystem;
import de.radicalfish.world.World;

public class DebugGameState extends TWLGameState implements PerformanceListener, DebugHook {
	
	private TWLRootPane root;
	private GUI gui;
	private PerformanceGraph graph;
	
	private boolean displayShortInfo = true;
	private int delta = 0;
	
	public DebugGameState(GameContext context, World world, int id) {
		super(context, world, id);
	}
	
	// GAME METHODS
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	public void init(GameContext context, World world) throws SlickException {
		initGUI(context);
		buildGUI(context);
		updateGUI(context);
	}
	public void update(GameContext context, World world, GameDelta delta) throws SlickException {
		this.delta = delta.getNormalDelta();
		
		graph.setFPS(context.getContainer().getFPS());
		graph.setDelta(this.delta);
		
		if (context.getInput().isKeyPressed(Input.KEY_F1)) {
			root.setVisible(!root.isVisible());
			if (root.isVisible()) {
				if (root.getCurrentFocusOwner() != null) {
					root.getCurrentFocusOwner().requestKeyboardFocus();
				}
			}
		}
		
		if (context.getInput().isKeyPressed(Input.KEY_F2)) {
			displayShortInfo = !displayShortInfo;
		}
		
		
	}
	public void render(GameContext context, World world, Graphics g) throws SlickException {
		renderShortInfo(context, world, g);
		
		updateGUI(context);
		gui.draw();
	}
	
	@Override
	public void keyPressed(int key, char c) {
		
		super.keyPressed(key, c);
	}
	
	// INTERFACE
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	public boolean isVisible() {
		return root.isVisible();
	}
	public void setVisible(boolean visible) {
		root.setVisible(visible);
	}
	public void addPerformanceListener(PerformanceListener listener, String name, Color color) {
		graph.addPerformanceListener(listener, name, color);
	}
	
	public long getMessuredTime() {
		return delta * 1000;
	}
	
	// INTERN
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	private void renderShortInfo(GameContext context, World world, Graphics g) throws SlickException {
		if (displayShortInfo) {
			g.pushTransform();
			g.resetTransform();
			g.setColor(org.newdawn.slick.Color.white);
			g.drawString("FPS: " + context.getContainer().getFPS(), 5, 5);
			g.drawString("DELTA: " + delta, 5, 17);
			g.drawString("ENTITES: " + getTotalEntites(world.getEntitySystems()) + " ( "
					+ getTotalVisibleEntites(context, world.getEntitySystems()) + " ) ", 5, 29);
			
			g.popTransform();
			
		}
	}
	private int getTotalEntites(List<EntitySystem> systems) throws SlickException {
		int num = 0;
		for (EntitySystem es : systems) {
			num += es.getEntities().size();
		}
		return num;
	}
	private int getTotalVisibleEntites(GameContext context, List<EntitySystem> systems) throws SlickException {
		int num = 0;
		for (EntitySystem es : systems) {
			for (Entity e : es.getEntities()) {
				if (!e.isOffscreen(context)) {
					num++;
				}
			}
		}
		return num;
	}
	
	private void updateGUI(GameContext context) {
		gui.setSize();
		gui.handleTooltips();
		gui.updateTimers();
		gui.invokeRunables();
		gui.validateLayout();
		gui.setCursor();
	}
	private void buildGUI(final GameContext context) {
		final OptionsPanel options = new OptionsPanel(context.getSettings());
		final DeveloperConsole console = new DeveloperConsole();
		final ToneEditor toneeditor = new ToneEditor(context.getGameTone());
		final PropertyEditor propeditor = new PropertyEditor(context.getSettings());
		graph = new PerformanceGraph();
		
		ToolBox toolbox = new ToolBox(context.getContainerWidth(), context.getContainerHeight());
		toolbox.setCanAcceptKeyboardFocus(false);
		
		toolbox.addButton("Options", options);
		toolbox.addButton("Console", console);
		toolbox.addButton(" Tone ", toneeditor);
		toolbox.addButton("Performance", graph);
		toolbox.addButton("Properties", propeditor);
		
		toolbox.addFiller();
		toolbox.addSeparator();
		{
			Label fps = new Label("FPS:");
			FPSCounter fpsCounter = new FPSCounter();
			
			toolbox.addCustomWidget(fps, Alignment.CENTER);
			toolbox.addCustomWidget(fpsCounter, Alignment.CENTER);
		}
		toolbox.addSeparator();
		
		toolbox.addButton("Adjust", new Runnable() {
			public void run() {
				options.adjustSize();
				console.adjustSize();
				toneeditor.adjustSize();
				graph.adjustSize();
				propeditor.adjustSize();
			}
		});
		toolbox.addButton("Close All", new Runnable() {
			public void run() {
				options.setVisible(false);
				console.setVisible(false);
				toneeditor.setVisible(false);
				graph.setVisible(false);
				propeditor.setVisible(false);
			}
		});
		toolbox.addButton("Exit", new Runnable() {
			public void run() {
				context.getContainer().exit();
			}
		});
		
		toolbox.createToolbox();
		options.setVisible(false);
		toneeditor.setVisible(false);
		console.setVisible(false);
		graph.setVisible(false);
		propeditor.setVisible(false);
		
		console.addInputParser(new URLInputParser());
		console.addInputParser(new PropertyInputParser(context.getSettings()));
		
		root.add(options);
		root.add(toolbox);
		root.add(console);
		root.add(toneeditor);
		root.add(graph);
		root.add(propeditor);
		
		graph.addPerformanceListener(this, "Total Time", Color.ORANGE);
	}
	
	private void initGUI(GameContext context) throws SlickException {
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
		try {
			root = new TWLRootPane(this);
			root.setTheme("");
			
			ActionMap actionMap = new ActionMap();
			actionMap.addMapping(this);
			
			root.setActionMap(actionMap);
			
			Renderer renderer = new LWJGLRenderer();
			ThemeManager theme = loadTheme(renderer, context);
			
			gui = new GUI(root, renderer);
			gui.setTheme("");
			gui.applyTheme(theme);
			gui.update();
			
			TWLInputForwarder inputForwarder = new TWLInputForwarder(gui, context.getInput());
			context.getInput().addPrimaryListener(inputForwarder);
		} catch (Throwable e) {
			throw new SlickException("Could not initialize TWL GUI", e);
		} finally {
			GL11.glPopAttrib();
		}
	}
	private ThemeManager loadTheme(Renderer renderer, GameContext context) throws Exception {
		String gui = context.getSettings().getProperty("misc.guipath", "null");
		if (gui.equals("null")) {
			throw new SlickException("no gui.path property is set!");
		}
		URL url = ResourceLoader.getResource(gui);
		assert url != null;
		return ThemeManager.createThemeManager(url, renderer);
	}
	
	// GETTER
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	public int getID() {
		return 100;
	}
	
}
