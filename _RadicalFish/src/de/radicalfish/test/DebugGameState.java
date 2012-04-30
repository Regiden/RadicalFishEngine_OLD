package de.radicalfish.test;
import java.net.URL;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.ResourceLoader;
import de.matthiasmann.twl.ActionMap;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.renderer.Renderer;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;
import de.radicalfish.TWLGameState;
import de.radicalfish.World;
import de.radicalfish.context.GameContext;
import de.radicalfish.context.GameDelta;
import de.radicalfish.extern.TWLInputForwarder;
import de.radicalfish.extern.TWLRootPane;

public class DebugGameState extends TWLGameState {
	
	private TWLRootPane root;
	private GUI gui;
	
	public DebugGameState(GameContext context, World world, int id) {
		super(context, world, id);
	}
	
	// GAME METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void init(GameContext context, World world) throws SlickException {
		initGUI(context);
		buildGUI(context);
	}
	public void update(GameContext context, World world, GameDelta delta) throws SlickException {
		updateGUI();
		
	}
	public void render(GameContext context, World world, Graphics g) throws SlickException {
		gui.draw();
	}
	
	// CALLBACKS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void keyboardFocusLost() {
		
	}
	
	public void layoutRootPane() {
		super.layoutRootPane();
	}
	@Override
	public void keyPressed(int key, char c) {
		System.out.println(key);
		System.out.println(root.hasKeyboardFocus());
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void updateGUI() {
		gui.setSize();
		gui.handleTooltips();
		gui.updateTimers();
		gui.invokeRunables();
		gui.validateLayout();
		gui.setCursor();
		
	}
	private void buildGUI(GameContext context) {
		
		Button button = new Button("test");
		root.add(button);
		
		button.setTooltipContent("Temp Data ist Temp");
		button.adjustSize();
		button.setPosition(100, 100);
		button.addCallback(new Runnable() {
			public void run() {
				System.out.println("print");
			}
		});
		
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
		String gui = context.getSettings().getProperty("gui.path", "null");
		if (gui.equals("null")) {
			throw new SlickException("no gui.path property is set!");
		}
		URL url = ResourceLoader.getResource(gui);
		assert url != null;
		return ThemeManager.createThemeManager(url, renderer);
	}
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public int getID() {
		return 100;
	}
	
}
