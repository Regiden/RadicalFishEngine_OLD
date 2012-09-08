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
import de.radicalfish.graphics.BlendMode;
import de.radicalfish.graphics.Graphics;
import de.radicalfish.util.RadicalFishException;
import de.radicalfish.util.Utils;
import de.radicalfish.util.Version;

/**
 * Central point for the update loop of the engine. Pass a {@link Game} implementation and start this container via the
 * Platform specific Launchers (e.g. LwjglApplication class).
 * <p>
 * The container takes care of managing the update loop and calling the methods form the {@link Game} Interface.
 * <p>
 * If the Implementation of the Game Interface also implements the {@link InputProcessor} it will be automatically added
 * as listener for the input.
 * <p>
 * Use the <code>fire"Name"</code> methods to hack in your own code for init(), update(), render(), pause(), resume()
 * and dispose() methods.
 * <p>
 * is the container is paused, it will not call any update or render code.
 * 
 * @author Stefan Lange
 * @version 0.5.0
 * @since 08.08.2012
 */
public class GameContainer implements ApplicationListener {
	
	// TODO add aspect
	/**
	 * The type the game area can have. stretch will stretch the whole game to the window size, while fix will fix the
	 * width and height. Use {@link GameContainer#setClipViewport(boolean)} to enable clipping for fixed (is on by
	 * default).
	 */
	public enum VIEWTYPE {
		STRECH, FIX
	}
	
	/** The default font we use. */
	public BitmapFont defaultFont;
	protected SpriteBatch batch;
	protected DisplayMode currentDisplayMode;
	
	/** The {@link Game} handled by the {@link GameContainer}. */
	public final Game game;
	protected DebugCallback debugCallBack;
	protected GameInput input;
	protected Graphics graphics;
	protected VIEWTYPE viewtype = VIEWTYPE.FIX;
	
	protected String title = "";
	protected String fontPath = "", fontDefPath = "";
	
	/** last measured delta (updated per frame). */
	public float delta;
	/** last measured fps (updated per frame). */
	public int fps;
	
	/** the width of the game container. */
	public int width = 800;
	/** the height of the game container. */
	public int height = 600;
	
	private int batchSize = 2000;
	
	/** True if we want to use smooth delta (default is false). */
	public boolean smoothDelta = false;
	/** True if the transform should be reseted every frame (default is true). */
	public boolean resetTransform = true;
	/** True if the viewport (the size of the container) should be clipped or not (default is true). */
	public boolean clipViewport = true;
	/** True if we should shod the debug. */
	public boolean showDebug = true;
	/**
	 * True if we should clear the screen every frame with the clear color set in
	 * {@link Graphics#setClearColor(float, float, float)}. (default is true.
	 */
	public boolean clearScreen = true;
	
	private boolean created = false;
	private boolean running = true;
	private boolean canSetFullScreen = true;
	private boolean fullscreen = false;
	private boolean paused = false;
	private boolean vsync = true;
	private boolean useGL20 = false;
	
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
	 * @param useGL20
	 *            true if we use GL20
	 */
	public GameContainer(String title, Game game, int width, int height, boolean useGL20) {
		this(title, game, width, height, useGL20, false);
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
	 * @param useGL20
	 *            true if we use GL20
	 * @param fullscreen
	 *            true if fullscreen should be used (set it in the configuration to have a nicer start)
	 */
	public GameContainer(String title, Game game, int width, int height, boolean useGL20, boolean fullscreen) {
		this.game = game;
		this.width = width;
		this.height = height;
		this.useGL20 = useGL20;
		
		this.fullscreen = fullscreen;
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Notifies the container to stop the game and dispose all content.
	 */
	public void exit() {
		running = false;
	}
	/**
	 * @return true if the system the game is running on supports fullscreen (Currently only Desktop and Web).
	 */
	public boolean supportsFullscreen() {
		if (Gdx.app.getType() == ApplicationType.Android || Gdx.app.getType() == ApplicationType.iOS
				|| Gdx.app.getType() == ApplicationType.Applet) {
			return false;
		} else {
			return true;
		}
	}
	
	// PROTECTED
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Gets called to call init on the {@link Game} implementation. Override for your own code.
	 */
	protected void fireInit() {
		game.init(this);
	}
	/**
	 * Gets called to call update on the {@link Game} implementation. Override for your own code.
	 */
	protected void fireUpdate() {
		game.update(this, delta);
	}
	/**
	 * Gets called to call render on the {@link Game} implementation. Override for your own code.
	 */
	protected void fireRender() {
		game.render(this, graphics);
	}
	/**
	 * Gets called to call pause on the {@link Game} implementation. Override for your own code.
	 */
	protected void firePause() {
		game.pause(this);
	}
	/**
	 * Gets called to call resume on the {@link Game} implementation. Override for your own code.
	 */
	protected void fireResume() {
		game.resume(this);
	}
	/**
	 * Gets called to call dispose on the {@link Game} implementation. Override for your own code.
	 */
	protected void fireDispose() {
		game.dispose();
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
		input = new GameInput();
		canSetFullScreen = supportsFullscreen();
		setFullScreen(fullscreen);
		Gdx.graphics.setVSync(vsync);
		
		// init Graphics context which should work like the one in Slick2D
		graphics = new Graphics(width, height, useGL20, batchSize);
		batch = graphics.getSpriteBatch();
		
		// load default gdx font if now default font if defined.
		if (!fontPath.equals("") && !fontDefPath.equals("")) {
			defaultFont = new BitmapFont(Gdx.files.internal(fontPath), Gdx.files.internal(fontDefPath), true);
		} else {
			defaultFont = new BitmapFont(true);
		}
		
		if (debugCallBack != null) {
			if (Gdx.app.getType() != ApplicationType.Desktop) {
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
		fireInit();
		if (debugCallBack != null) {
			debugCallBack.init(this);
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
				
				if (Gdx.app.getType() != ApplicationType.Android && smoothDelta) {
					if (fps != 0) {
						delta = (1000 / fps) / 1000f;
					}
				}
				
				fireUpdate();
				if (debugCallBack != null) {
					debugCallBack.update(this, delta);
				}
				
				// render
				if (clipViewport) {
					Gdx.gl.glClearColor(0, 0, 0, 1);
					Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
					Gdx.gl.glEnable(GL10.GL_SCISSOR_TEST);
					Gdx.gl.glScissor(0, Gdx.graphics.getHeight() - height, width, height);
					graphics.setClearColor(graphics.getClearColor());
				}
				
				if (clearScreen) {
					graphics.clearScreen();
				}
				
				fireRender();
				if (debugCallBack != null) {
					debugCallBack.render(this, graphics);
				}
				
				// reset all to make each frame normal
				if (resetTransform) {
					// TODO fix to center and shit?
					graphics.resetTransform(true);
				}
				
				graphics.setBlendMode(BlendMode.NORMAL);
				
				// render fps
				if (showDebug) {
					batch.begin();
					defaultFont.draw(batch, "Fps: " + fps, 5, 5);
					defaultFont.draw(batch, "Delta: " + (int) (delta * 1000) + "ms", 5, 20);
					batch.end();
				}
				
				if (clipViewport) {
					Gdx.gl.glDisable(GL10.GL_SCISSOR_TEST);
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
		firePause();
	}
	public void resume() {
		paused = false;
		fireResume();
	}
	
	public void resize(int width, int height) {
		if (viewtype == VIEWTYPE.FIX) {
			graphics.resize(width, height);
		}
		
	}
	
	public void dispose() {
		if (debugCallBack != null) {
			debugCallBack.dispose();
		}
		fireDispose();
		defaultFont.dispose();
		graphics.dispose();
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void checkFullscreen(boolean fullscreen) {
		if (this.fullscreen != fullscreen) {
			this.fullscreen = fullscreen;
			if (fullscreen == Gdx.graphics.isFullscreen()) {
				return;
			}
			if (!Gdx.graphics.supportsDisplayModeChange()) {
				return;
			}
			
			if (Gdx.graphics.setDisplayMode(width, height, fullscreen)) {
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
	 * Sets the {@link DebugCallback} to use. Note this only works under Desktop implementations. If you set a callback
	 * in another implementation the methods of the callback will not be called and a warning will be logged when the
	 * container is created. If the callback implements the {@link InputProcessor} it will be added to the input.
	 * 
	 */
	public void setDebugCallBack(DebugCallback debugCallBack) {
		Utils.notNull("debug callback", debugCallBack);
		this.debugCallBack = debugCallBack;
	}
	/**
	 * Sets the title and appends and extra string to it. This way {@link GameContainer#getTitle()} will only return the
	 * title, but not the <code>extras</code>.
	 */
	public void setTitle(String title, String extras) {
		Utils.notNull("title", title);
		Utils.notNull("extras", extras);
		
		this.title = title;
		Gdx.graphics.setTitle(title + extras);
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
	 */
	public void setDefaultFont(String fontPath, String fontDefPath) {
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
	 * Sets the {@link VIEWTYPE} to use.
	 */
	public void setViewType(VIEWTYPE viewtype) {
		this.viewtype = viewtype;
		if (this.viewtype == VIEWTYPE.STRECH) {
			graphics.resize(width, height);
		}
	}
	/**
	 * Sets the size of the batch. this size will be used when creating the batch. It has not effect setting it while
	 * the game runs!
	 */
	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
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
			Logger.info("Can't set fullscreen on system: " + getSystem());
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
	 * True if the viewport of the game should be clipped. If the window gets resized, the "game area" will clip the
	 * width and height set in the constructor. This works well when the
	 * 
	 * @param clipViewport
	 */
	public void setClipViewport(boolean clipViewport) {
		this.clipViewport = clipViewport;
		graphics.setClearColor(graphics.getClearColor());
	}
	/**
	 * True if we want to use Vsync. Set before starting the container for proper handling. default value is true.
	 * 
	 * @param vsync
	 */
	public void setVSync(boolean vsync) {
		this.vsync = vsync;
		Gdx.graphics.setVSync(vsync);
	}
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return a {@link Preferences} with teh given <code>name</code>. returns null if the app was not yet created.
	 */
	public Preferences getPrefereces(String name) {
		if (Gdx.app != null) {
			return Gdx.app.getPreferences(name);
		}
		return null;
	}
	
	/**
	 * @return the system the application runs on, can be Android, Desktop, Applet, WebGL or iOS.
	 */
	public ApplicationType getSystem() {
		return Gdx.app.getType();
	}
	/**
	 * @return the graphics object used for translation etc.
	 */
	public Graphics getGraphics() {
		return graphics;
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
		return Gdx.app.getPreferences(name);
	}
	/**
	 * @return the default font used by the container.
	 */
	public BitmapFont getFont() {
		return defaultFont;
	}
	/**
	 * @return the view type we use.
	 */
	public VIEWTYPE getViewType() {
		return viewtype;
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
		return Gdx.graphics.isFullscreen() && canSetFullScreen;
	}
	/**
	 * @return true if the container runs.
	 */
	public boolean isRunning() {
		return running;
	}
	/**
	 * @return true if we clip the viewport.
	 */
	public boolean isClipViewport() {
		return clipViewport;
	}
	/**
	 * @return true if VSync is enabled.
	 */
	public boolean isVSyncEnabled() {
		return vsync;
	}
	/**
	 * @return true if we use GL20.
	 */
	public boolean isUseGL20() {
		return useGL20;
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
	/**
	 * @return the width of the display.
	 */
	public int getDisplayWidth() {
		return Gdx.graphics.getWidth();
	}
	/**
	 * @return the height of the display.
	 */
	public int getDisplayHeight() {
		return Gdx.graphics.getHeight();
	}
	
}
