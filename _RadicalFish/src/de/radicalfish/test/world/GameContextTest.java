/*
 * Copyright (c) 2011, Stefan Lange
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Stefan Lange nor the names of its contributors may
 *       be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.radicalfish.test.world;
import java.io.File;
import java.util.Arrays;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GLContext;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import de.radicalfish.context.DefaultGameDelta;
import de.radicalfish.context.DefaultGameVariables;
import de.radicalfish.context.DefaultSettings;
import de.radicalfish.context.GameContext;
import de.radicalfish.context.GameDelta;
import de.radicalfish.context.GameVariables;
import de.radicalfish.context.Resources;
import de.radicalfish.context.Settings;
import de.radicalfish.debug.DebugHook;
import de.radicalfish.debug.LWJGLDebugUtil;
import de.radicalfish.debug.Logger;
import de.radicalfish.effects.FBOPostProcesser;
import de.radicalfish.effects.PostProcesser;
import de.radicalfish.effects.ToneModel;
import de.radicalfish.effects.ToneShaderEffect;
import de.radicalfish.text.FontRenderer;
import de.radicalfish.text.FontSheet;
import de.radicalfish.text.SpriteFont;
import de.radicalfish.text.SpriteFontRenderer;
import de.radicalfish.text.StyledFont;
import de.radicalfish.world.Animator;
import de.radicalfish.world.World;

public class GameContextTest extends StateBasedGame implements GameContext {
	
	private static Settings settings;
	private FontRenderer fontRenderer;
	private GameContainer container;
	private GameVariables variables;
	private GameDelta gameSpeed;
	private StyledFont defaultFont;
	private World world;
	private Resources res;
	
	private ToneModel gameTone;
	private PostProcesser postProcess;
	private ToneShaderEffect postEffect;
	
	private DebugGameState debug;
	
	private static int gameScale = 2;
	private static int gameWidth = 320, gameHeight = 240;
	private int maxTextureSize = 1024;
	private boolean canDebug;
	
	private int avgfps, avgdelta, runs;
	
	private final int[][] widths = new int[][] { { 3, 3, 5, 7, 5, 7, 7, 3, 4, 4, 5, 5, 4, 5, 3, 5 },
			{ 5, 3, 5, 5, 5, 5, 5, 5, 5, 5, 3, 4, 5, 5, 5, 5 }, { 7, 5, 5, 5, 5, 5, 5, 5, 5, 3, 5, 5, 5, 7, 6, 5 },
			{ 5, 5, 5, 5, 5, 5, 5, 7, 5, 5, 5, 4, 5, 4, 5, 5 } };
	
	// MAIN AND C'TOR
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public GameContextTest() {
		super("State Bases Game Test");
	}
	public static void main(String[] args) throws SlickException {
		System.setProperty("org.lwjgl.librarypath",
				new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
		System.setProperty("net.java.games.input.librarypath", System.getProperty("org.lwjgl.librarypath"));
		
		try {
			settings = new DefaultSettings("options.txt");
		} catch (SlickException e) {
			Sys.alert("ERROR", e.getMessage());
			System.exit(0);
		}
		
		Logger.none("------------------------ Slick2D ------------------------");
		int width = gameWidth * gameScale, height = gameHeight * gameScale;
		
		AppGameContainer app = new AppGameContainer(new GameContextTest(), width, height, false);
		
		app.setVSync(settings.isVSync());
		app.setSmoothDeltas(settings.isSmoothDelta());
		app.setForceExit(false);
		app.setMouseGrabbed(!settings.isDebugging());
		app.setShowFPS(false);
		app.setAlwaysRender(settings.isDebugging());
		
		app.start();
		
		
	}
	
	// GAME METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void initStatesList(GameContainer container) throws SlickException {
		this.container = container;
		variables = new DefaultGameVariables();
		gameSpeed = new DefaultGameDelta();
		
		fontRenderer = new SpriteFontRenderer();
		defaultFont = new SpriteFont(new FontSheet(("de/radicalfish/testdata/font.png"), widths, 11), -1, ' ');
		fontRenderer.addFont("normal", defaultFont);
		
		container.setFullscreen(settings.isFullscreen());
		container.setDefaultFont(defaultFont);
		
		res = new Resources();
		
		checkGraphicsCapabilities();
		if (settings.isLogging()) {
			Logger.none("------------------------ OpenGL Information ------------------------");
			LWJGLDebugUtil.printInformationToLog();
			Logger.none("------------------------ Settings ------------------------");
			settings.printSettings();
			Logger.none("------------------------ GAME ------------------------");
		}
		
		gameTone = new ToneModel();
		postProcess = new FBOPostProcesser(this);
		postEffect = new ToneShaderEffect();
		postProcess.setEffect(postEffect);
		
		Animator ani = new Animator();
		ani.loadAnimations(this, "de/radicalfish/assets/ani.xml", Arrays.asList(new String[] {"idle"}));
		
		TestGameState test = new TestGameState(this, world, 1);
		addState(test);
		
		canDebug = settings.isDebugging();
		if(canDebug) {
			debug = new DebugGameState(this, world, 0);
			debug.init(this, world);
		}
		
		debug.addPerformanceListener(test, "Test State", de.matthiasmann.twl.Color.LIGHTBLUE);
	}
	
	protected void preUpdateState(GameContainer container, int delta) throws SlickException {
		gameSpeed.update(this, world, delta);
		
		runs++;
		avgfps += container.getFPS();
		avgdelta += delta;
		
	}
	protected void postUpdateState(GameContainer container, int delta) throws SlickException {
		updateDebug();
		updateSettings(container);
	}
	
	protected void preRenderState(GameContainer container, Graphics g) throws SlickException {
		g.setFont(defaultFont);
		postProcess.bind(this, g);
		g.clear();
	}
	protected void postRenderState(GameContainer container, Graphics g) throws SlickException {
		postProcess.unbind(this, g);
		
		postProcess.renderScene(this, g);
		
		renderDebug(g);
		
	}
	
	// OVERRIDE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	@Override
	public boolean closeRequested() {
		closeGame();
		return super.closeRequested();
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void updateDebug() throws SlickException {
		if (canDebug) {
			debug.update(this, world, getGameDelta());
		}
		
	}
	private void renderDebug(Graphics g) throws SlickException {
		if (canDebug) {
			debug.render(this, world, g);
		}
	}
	private void updateSettings(GameContainer container) throws SlickException {
		if (container.isFullscreen() != settings.isFullscreen()) {
			container.setFullscreen(settings.isFullscreen());
		}
		
		if (settings.isVSync()) {
			container.setSmoothDeltas(false);
		}
		if (container.isVSyncRequested() != settings.isVSync()) {
			container.setVSync(settings.isVSync());
		}
		container.setSmoothDeltas(settings.isSmoothDelta());
		
	}
	private void checkGraphicsCapabilities() {
		ContextCapabilities c = GLContext.getCapabilities();
		settings.getGraphicDetails().setFBOSupported(c.GL_EXT_framebuffer_object);
		settings.getGraphicDetails().setShaderSupported(c.GL_ARB_shader_objects && c.GL_ARB_vertex_shader && c.GL_ARB_fragment_shader);
		
		boolean error = false;
		
		if (Image.getMaxSingleImageSize() < maxTextureSize) {
			Sys.alert("ERROR", "Your System does not support the max texture size the games uses. Please update your Driver!");
			error = true;
		}
		if (!settings.getGraphicDetails().isFBOSupported()) {
			Sys.alert("ERROR", "Your system does not support FBOs. Please update your Driver!");
		}
		if (!settings.getGraphicDetails().isShaderSupported()) {
			Sys.alert("ERROR", "Your system does not support Shader. Please update your Driver!");
		}
		
		if (error) {
			container.exit();
		}
	}
	private void closeGame() {
		Logger.none("Exit Game:");
		if(runs != 0) {
			Logger.none("\tTotal Runs:   " + runs);
			Logger.none("\tAverage FPS:   " + (avgfps / runs));
			Logger.none("\tAverage Delta: " + (avgdelta / runs));
		}
		
	}
	
	// GAME CONTEXT
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public float getGameScale() {
		return gameScale;
	};
	public int getGameWidth() {
		return gameWidth;
	}
	public int getGameHeight() {
		return gameHeight;
	}
	public int getContainerWidth() {
		return container.getWidth();
	}
	public int getContainerHeight() {
		return container.getHeight();
	}
	public StateBasedGame getGame() {
		return this;
	}
	public GameContainer getContainer() {
		return container;
	}
	public Input getInput() {
		return container.getInput();
	}
	public Settings getSettings() {
		return settings;
	}
	public GameDelta getGameDelta() {
		return gameSpeed;
	}
	public GameVariables getGameVariables() {
		return variables;
	}
	public ToneModel getGameTone() {
		return gameTone;
	}
	public PostProcesser getPostProcesser() {
		return postProcess;
	}
	public FontRenderer getFontRenderer() {
		return fontRenderer;
	}
	public Font getDefaultFont() {
		return defaultFont;
	}
	public Resources getResources() {
		return res;
	}
	public DebugHook getDebugHook() {
		return debug;
	}

}
