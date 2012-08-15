/*
 * Copyright (c) 2012, Stefan Lange
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
package de.radicalfish;
import java.util.Date;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.radicalfish.debug.DebugCallback;
import de.radicalfish.debug.Logger;
import de.radicalfish.util.RadicalFishException;
import de.radicalfish.util.Utils;
import de.radicalfish.util.Version;

/*
 * TODOS:
 * 	- implement resize stuff + 0.0.2
 */

/**
 * Central point for the update loop of the engine. This should be based into an implementation of the
 * {@link Application} class (in the C'Tor).
 * <p>
 * The container holds an class which implements the Interface {@link Game}. The container takes care of managing the
 * update loop and calling the methods form the {@link Game} Interface.
 * <p>
 * If the Implementation of the Game Interface also implements the {@link InputProcessor} it will be automatically added
 * as listener for the input.
 * 
 * @author Stefan Lange
 * @version 0.3.6
 * @since 08.08.2012
 */
public class GameContainer implements ApplicationListener, InputProcessor {
	
	private Application app;
	private SpriteBatch batch;
	private BitmapFont defaultFont;
	private DisplayMode currentDisplayMode;
	
	private GraphicsContext gContext;
	private DebugCallback debugCallBack;
	private GameInput input;
	private Graphics graphics;
	private Game game;
	
	private String title;
	private String fontPath, fontDefPath;
	
	private float delta;
	private int fps;
	
	private int width;
	private int height;
	private int spriteBatchSize;
	
	private boolean created;
	private boolean running;
	private boolean useGL2;
	private boolean clearScreen;
	private boolean showDebug;
	private boolean resetTransform;
	private boolean canSetFullScreen;
	private boolean fullscreen;
	private boolean smoothDelta;
	private boolean paused;
	
	long lastFrameTime;
	
	/**
	 * Creates a new GameContainer with the given parameters. fullscreen will be set to false.
	 * 
	 * @param title
	 *            the title of the game. Set it in the configuration to make the title appear nicer in the window
	 * @param game
	 *            the game being held by the container
	 * @param width
	 *            the width of the game (the window)
	 * @param height
	 *            the height of the game (the window)
	 * @param useGL2
	 *            true if we use GL20
	 * @throws RadicalFishException
	 */
	public GameContainer(String title, Game game, int width, int height, boolean useGL2) throws RadicalFishException {
		this(title, game, width, height, useGL2, false);
	}
	/**
	 * Creates a new GameContainer with the given parameters.
	 * 
	 * @param title
	 *            the title of the game. Set it in the configuration to make the title appear nicer in the window
	 * @param game
	 *            the game being held by the container
	 * @param width
	 *            the width of the game (the window)
	 * @param height
	 *            the height of the game (the window)
	 * @param useGL2
	 *            true if we use GL20
	 * @param fullscreen
	 *            true if fullscreen should be used (set it in the configuration to have a nicer start)
	 * @throws RadicalFishException
	 */
	public GameContainer(String title, Game game, int width, int height, boolean useGL2, boolean fullscreen) throws RadicalFishException {
		this.game = game;
		this.width = width;
		this.height = height;
		this.useGL2 = useGL2;
		
		this.title = title;
		
		created = false;
		running = true;
		clearScreen = true;
		showDebug = true;
		resetTransform = true;
		smoothDelta = false;
		paused = false;
		this.fullscreen = fullscreen;
		spriteBatchSize = 1000;
		
		fontPath = "";
		fontDefPath = "";
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void exit() {
		running = false;
	}
	
	// OVERRIDE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void create() {
		// cap the debug level to log all since we want to use the logger from the RadicalFishEngine
		Gdx.app.setLogLevel(com.badlogic.gdx.utils.Logger.DEBUG);
		
		// log shit
		Logger.info(new Date() + " RadicalFishEngine Version: " + Version.VERSION);
		Logger.info(new Date() + " libGDX Version: " + com.badlogic.gdx.Version.VERSION);
		Logger.info(new Date() + " System: " + logSystemOS());
		Logger.info(new Date() + " Original DisplayMode: " + Gdx.graphics.getDesktopDisplayMode());
		currentDisplayMode = getCurrentDisplayMode();
		Logger.info(new Date() + " Current DisplayMode: " + currentDisplayMode);
		
		// set gdx stuff
		app = Gdx.app;
		input = new GameInput();
		input.addInputProcessor(this);
		checkIfFullscreenSupported();
		setFullScreen(fullscreen);
		
		// init the context used by the Graphics class
		gContext = new GraphicsContext(width, height);
		gContext.position.set(0, 0, 0);
		
		// is GL2 is in use, use the default shader for the spritebatch
		if (useGL2) {
			batch = new SpriteBatch(spriteBatchSize, SpriteBatch.createDefaultShader());
		} else {
			batch = new SpriteBatch(spriteBatchSize);
		}
		
		// init Graphics context which should work like the one in Slick2D
		graphics = new Graphics(batch, gContext);
		
		// load default gdx font if now default font if defined.
		if (!fontPath.equals("") && !fontDefPath.equals("")) {
			defaultFont = new BitmapFont(Gdx.files.internal(fontPath), Gdx.files.internal(fontDefPath), true);
		} else {
			defaultFont = new BitmapFont(true);
		}
		
		if (debugCallBack != null) {
			if (app.getType() != ApplicationType.Desktop) {
				debugCallBack = null;
			}
		}
		
		if (game instanceof InputProcessor) {
			input.addInputProcessor((InputProcessor) game);
		}
		if (debugCallBack != null && debugCallBack instanceof InputProcessor) {
			input.addInputProcessor((InputProcessor) debugCallBack);
		}
		
		// call init on the game implementation and the debug callback
		try {
			game.init(this);
			if (debugCallBack != null) {
				debugCallBack.init(this);
			}
		} catch (RadicalFishException e) {
			e.printStackTrace();
			Gdx.app.exit();
		}
		
		created = false;
	}
	public void render() {
		try {
			if (paused) {
				return;
			}
			if (running) {
				
				delta = Gdx.graphics.getDeltaTime();
				fps = Gdx.graphics.getFramesPerSecond();
				
				if (app.getType() != ApplicationType.Android && smoothDelta) {
					if (fps != 0) {
						delta = (1000 / fps) / 1000f;
					}
				}
				
				game.update(this, delta);
				if (debugCallBack != null) {
					debugCallBack.update(this, delta);
				}
				
				// render
				if (clearScreen) {
					Gdx.gl.glClearColor(0, 0, 0, 1);
					Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
				}
				
				game.render(this, graphics, batch);
				if (debugCallBack != null) {
					debugCallBack.render(this, graphics, batch);
				}
				
				// reset all to make each frame normal
				if (resetTransform) {
					graphics.resetTransform(true);
				}
				
				// render fps
				if (showDebug) {
					batch.begin();
					int calls = batch.renderCalls;
					defaultFont.draw(batch, "Fps: " + fps, 10, 10);
					defaultFont.draw(batch, "Delta: " + (int) (delta * 1000) + "ms", 10, 25);
					defaultFont.draw(batch, "Render Calls: " + calls, 10, 40);
					batch.end();
				}
				
				input.update();
			} else {
				Gdx.app.exit();
			}
		} catch (RadicalFishException e) {
			e.printStackTrace();
			Gdx.app.exit();
		}
	}
	
	public void pause() {
		paused = true;
		try {
			game.pause(this);
		} catch (RadicalFishException e) {
			e.printStackTrace();
			Gdx.app.exit();
		}
	}
	public void resume() {
		paused = false;
		try {
			game.resume(this);
		} catch (RadicalFishException e) {
			e.printStackTrace();
			Gdx.app.exit();
		}
	}
	
	public void resize(int width, int height) {
		gContext.setToOrtho(width, height);
	}
	
	public void dispose() {
		debugCallBack.dispose();
		game.dispose();
		defaultFont.dispose();
		batch.dispose();
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void checkIfFullscreenSupported() {
		if (app.getType() == ApplicationType.Android || app.getType() == ApplicationType.iOS || app.getType() == ApplicationType.Applet) {
			canSetFullScreen = false;
		} else {
			canSetFullScreen = true;
		}
	}
	private void checkFullscreen(boolean fullscreen) {
		if (this.fullscreen != fullscreen) {
			this.fullscreen = fullscreen;
			if (fullscreen == app.getGraphics().isFullscreen()) {
				return;
			}
			if (!app.getGraphics().supportsDisplayModeChange()) {
				return;
			}
			
			if (app.getGraphics().setDisplayMode(width, height, fullscreen)) {
				Logger.info(new Date() + " Switched to " + (fullscreen ? "fullscreen" : "windowed"));
			} else {
				Logger.info(new Date() + " Could not switch fullscreen");
			}
		}
	}
	private DisplayMode getCurrentDisplayMode() {
		DisplayMode[] modes = Gdx.graphics.getDisplayModes();
		DisplayMode mode = null, target = null;
		int freq = 0;
		for (int i = 0; i < modes.length; i++) {
			mode = modes[i];
			if (mode.width == width && mode.height == height) {
				if (target == null || mode.refreshRate >= freq) {
					if ((target == null) || (mode.bitsPerPixel > target.bitsPerPixel)) {
						target = mode;
						freq = target.refreshRate;
					}
				}
				if ((mode.bitsPerPixel == Gdx.graphics.getDesktopDisplayMode().bitsPerPixel)
						&& (mode.refreshRate == Gdx.graphics.getDesktopDisplayMode().refreshRate)) {
					target = mode;
					break;
				}
			}
		}
		return target;
	}
	private String logSystemOS() {
		ApplicationType type = Gdx.app.getType();
		switch (type) {
			case Android:
				return "Android (" + Gdx.app.getVersion() + ", " + Gdx.input.getNativeOrientation() + ")";
			case Desktop:
				return "Desktop (" + System.getProperty("os.name").toLowerCase() + ")";
			case Applet:
				return "Applet (" + System.getProperty("os.name").toLowerCase() + ")";
			case iOS:
				return "iOS";
			case WebGL:
				return "HTML";
			default:
				return "Unkown";
		}
	}
	
	// SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Sets the number of a single batch of sprites. only affected before starting the app.
	 */
	public void setSpriteBatchSize(int size) {
		spriteBatchSize = size;
	}
	
	/**
	 * Sets the title and appends and extra string to it. This way {@link GameContainer#getTitle()} will only return the
	 * title, but not the <code>extras</code>.
	 */
	public void setTitle(String title, String extras) {
		Utils.notNull("title", title);
		Utils.notNull("extras", extras);
		
		this.title = title;
		app.getGraphics().setTitle(title + extras);
	}
	/**
	 * Sets the paths for the default font to use. Set it before launching the Application to directly load your
	 * {@link BitmapFont}. You can change it while in the game loop to. This will unload the current {@link BitmapFont}
	 * and load the new. If the paths a empty (""), the default GDX font will be loaded.
	 * 
	 * @param fontPath
	 *            the path to the image file (must be internal path)
	 * @param fontDefPath
	 *            the path of the def file (must be internal path)
	 * @throws RadicalFishException
	 */
	public void setDefaultFont(String fontPath, String fontDefPath) throws RadicalFishException {
		Utils.notNull("font path", fontPath);
		Utils.notNull("dont def path", fontDefPath);
		
		this.fontPath = fontPath;
		this.fontDefPath = fontDefPath;
		
		if (created) {
			try {
				defaultFont.dispose();
				defaultFont = new BitmapFont(Gdx.files.internal(fontDefPath), Gdx.files.internal(fontPath), true);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RadicalFishException(e.getMessage());
			}
		}
	}
	/**
	 * True if we want to clear the frame each loop, false otherwise.
	 */
	public void setClearEachFrame(boolean clear) {
		clearScreen = clear;
	}
	/**
	 * True if we should render the debug on the top left of the screen.
	 */
	public void setShowDebug(boolean showDebug) {
		this.showDebug = showDebug;
	}
	/**
	 * True if the transform should be reseted every frame.
	 */
	public void setResetTransformEachFrame(boolean resetTransform) {
		this.resetTransform = resetTransform;
	}
	/**
	 * True if we want fullscreen. Note that on Android and iOS this will not work! Currently Applets to not support
	 * fullscreen too.
	 */
	public void setFullScreen(boolean fullscreen) {
		if (!canSetFullScreen) {
			this.fullscreen = false;
			return;
		}
		checkFullscreen(fullscreen);
	}
	/**
	 * True if the delta value should be smoothed out.
	 */
	public void setSmoothDelta(boolean smoothDelta) {
		this.smoothDelta = smoothDelta;
	}
	/**
	 * True if we want to render always event if the window is out of focus.
	 */
	public void setAlwaysRender(boolean alwaysRender) {
		app.getGraphics().setContinuousRendering(alwaysRender);
	}
	/**
	 * Sets the {@link DebugCallback} to use. Note this only works under Desktop implementations. If you set a callback
	 * in another implementation the methods of the callback will not be called and a warning will be logged when the
	 * container is created. If the callback implements the {@link InputProcessor} it will be added to the input.
	 * 
	 */
	public void setDebugCallBack(DebugCallback debugCallBack) {
		Utils.notNull("debug callback", debugCallBack);
		this.debugCallBack = debugCallBack;
	}
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return the system the application runs on, can be Android, Desktop, Applet, WebGL or iOS.
	 */
	public ApplicationType getSystem() {
		return app.getType();
	}
	/**
	 * @return the input used for the game.
	 */
	public GameInput getInput() {
		return input;
	}
	/**
	 * @return the debug callback, can be null if on another implementation then Desktop.
	 */
	public DebugCallback getDebugCallBack() {
		return debugCallBack;
	}
	/**
	 * 
	 * @param name
	 *            the name of the specific preference.
	 * @return the preferences use to store data across runs.
	 */
	public Preferences getPreferences(String name) {
		return app.getPreferences(name);
	}
	/**
	 * @return the default font used by the container.
	 */
	public BitmapFont getFont() {
		return defaultFont;
	}
	
	/**
	 * @return the title of the game.
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @return true if the screen gets cleared every frame.
	 */
	public boolean isClearScreen() {
		return clearScreen;
	}
	/**
	 * @return true if we show the debug output.
	 */
	public boolean isShowDebug() {
		return showDebug;
	}
	/**
	 * @return true if we reset complete transform each frame.
	 */
	public boolean isResetTransformEachFrame() {
		return resetTransform;
	}
	/**
	 * @return true if the application runs in fullscreen, false otherwise (always returns false on android, web, iOS).
	 */
	public boolean isFullscreen() {
		return app.getGraphics().isFullscreen() && canSetFullScreen;
	}
	/**
	 * @return true if we always render.
	 */
	public boolean isAlwaysRender() {
		return app.getGraphics().isContinuousRendering();
	}
	/**
	 * @return true if the container runs.
	 */
	public boolean isRunning() {
		return running;
	}
	
	/**
	 * @return the time since the last frame in seconds.
	 */
	public float getDelta() {
		return delta;
	}
	/**
	 * @return the last recorded FPS.
	 */
	public int getFPS() {
		return fps;
	}
	
	/**
	 * @return the width of the container.
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * @return the height of the container.
	 */
	public int getHeight() {
		return height;
	}
	
	// INPUT
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public boolean keyDown(int keycode) {
		return false;
	}
	public boolean keyUp(int keycode) {
		return false;
	}
	public boolean keyTyped(char character) {
		return false;
	}
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}
	public boolean scrolled(int amount) {
		return false;
	}
	public boolean touchMoved(int arg0, int arg1) {
		return false;
	}
	
}
