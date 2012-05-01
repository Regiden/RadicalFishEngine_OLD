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
package de.radicalfish.test;
import java.io.File;
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
import de.radicalfish.World;
import de.radicalfish.context.DefaultGameDelta;
import de.radicalfish.context.DefaultGameVariables;
import de.radicalfish.context.DefaultSettings;
import de.radicalfish.context.GameContext;
import de.radicalfish.context.GameDelta;
import de.radicalfish.context.GameVariables;
import de.radicalfish.context.Settings;
import de.radicalfish.text.FontRenderer;
import de.radicalfish.text.FontSheet;
import de.radicalfish.text.SpriteFont;
import de.radicalfish.text.SpriteFontRenderer;
import de.radicalfish.text.StyledFont;

public class GameContextTest extends StateBasedGame implements GameContext {
	
	private static Settings settings;
	private FontRenderer fontRenderer;
	private GameContainer container;
	private GameVariables variables;
	private GameDelta gameSpeed;
	private StyledFont defaultFont;
	private World world;
	
	private static int gameScale = 2;
	private static int gameWidth = 320, gameHeight = 240;
	private int maxTextureSize = 1024, lastDelta = 0;
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
		
		int width = gameWidth * gameScale, height = gameHeight * gameScale;
		
		AppGameContainer app = new AppGameContainer(new GameContextTest(), width, height, false);
		
		app.setVSync(settings.getProperty("startup.vsync", "false").equals("true"));
		app.setSmoothDeltas(settings.getProperty("startup.smoothdelta", "false").equals("true"));
		app.setForceExit(false);
		app.setMouseGrabbed(!settings.isDebugging());
		app.setShowFPS(false);
		
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
		
		checkGraphicCapabilities();
		
		if (settings.isLogging()) {
			System.out.println(settings);
		}
		
		addState(new DebugGameState(this, world, 100));
		addState(new TestGameState(this, world, 0));
		
	}
	
	protected void preUpdateState(GameContainer container, int delta) throws SlickException {
		gameSpeed.update(this, world, delta);
	}
	protected void postUpdateState(GameContainer container, int delta) throws SlickException {
		lastDelta = delta;
	}
	
	protected void preRenderState(GameContainer container, Graphics g) throws SlickException {
		g.setFont(defaultFont);
	}
	protected void postRenderState(GameContainer container, Graphics g) throws SlickException {
		g.resetTransform();
		
		g.drawString("FPS:   " + container.getFPS(), 5, 5);
		g.drawString("DELTA: " + lastDelta, 5, 17);
		
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void checkGraphicCapabilities() {
		ContextCapabilities c = GLContext.getCapabilities();
		settings.getGraphicDetails().setFBOSupported(c.GL_EXT_framebuffer_object);
		settings.getGraphicDetails().setShaderSupported(c.GL_ARB_shader_objects && c.GL_ARB_vertex_shader && c.GL_ARB_fragment_shader);
		
		boolean error = false;
		
		if (Image.getMaxSingleImageSize() < maxTextureSize) {
			Sys.alert("ERROR", "Your System does not support the max texture size the games uses. Please update your Driver!");
			error = true;
		}
		if (!settings.getGraphicDetails().isFBOSupported()) {
			Sys.alert("ERROR", "Your system does not support FBO's Please update your Driver!");
		}
		
		if (error) {
			container.exit();
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
		return container.getWidth();
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
	public GameDelta getGameSpeed() {
		return gameSpeed;
	}
	public GameVariables getGameVariables() {
		return variables;
	}
	public FontRenderer getFontRenderer() {
		return fontRenderer;
	}
	public Font getDefaultFont() {
		return defaultFont;
	}
}
