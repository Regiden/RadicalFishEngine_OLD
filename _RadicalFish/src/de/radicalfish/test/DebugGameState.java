package de.radicalfish.test;
import java.net.URL;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.ResourceLoader;
import de.matthiasmann.twl.ActionMap;
import de.matthiasmann.twl.Alignment;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.ColumnLayout;
import de.matthiasmann.twl.ColumnLayout.Row;
import de.matthiasmann.twl.Dimension;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.ToggleButton;
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
	
	private Image image;
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
		
		image = new Image("de/radicalfish/testdata/TESTBLOCK.png", false, Image.FILTER_NEAREST);
	}
	public void update(GameContext context, World world, GameDelta delta) throws SlickException {
		updateGUI();
		
	}
	public void render(GameContext context, World world, Graphics g) throws SlickException {
		image.draw(10, 10);
		GL11.glPushMatrix();
		g.scale(2, 2);
		GL11.glPushMatrix();
		image.draw(20, 20);
		GL11.glPopMatrix();
		image.draw(40, 40);
		GL11.glPopMatrix();
		image.draw(60, 60);
		
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
		
		ResizableFrame frame = new ResizableFrame();
		frame.setTheme("/resizableframe-title");
		frame.setTitle("Options");
		frame.setPosition(800, 0);
		// frame.setResizableAxis(ResizableAxis.NONE);
		
		
		Button button = new Button("Temp");
		Label label1 = new Label("Label ");
		ToggleButton checkbox1 = new ToggleButton();
		checkbox1.setTheme("/checkbox");
		Label label2 = new Label("Label 2  ");
		ToggleButton checkbox2 = new ToggleButton();
		checkbox2.setTheme("checkbox");
		
		//checkbox1.setAlignment(Alignment.RIGHT);
		
		ColumnLayout layout = new ColumnLayout();
		layout.setDefaultGap(new Dimension(3, 5));
		layout.setTheme("");
		
		Row row = layout.addRow("Name", "Value");
		row.add(label1).add(checkbox1);
		row = layout.addRow("Name", "Value");
		row.add(label2).add(checkbox2);
		
		
		frame.add(layout);
		frame.adjustSize();
		root.add(frame);
		
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
