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
package de.radicalfish.debug;
import java.io.File;
import org.lwjgl.opengl.GL11;
import com.badlogic.gdx.Gdx;
import de.matthiasmann.twl.ActionMap;
import de.matthiasmann.twl.DesktopArea;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.renderer.Renderer;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;
import de.radicalfish.GameContainer;
import de.radicalfish.util.RadicalFishException;
import de.radicalfish.util.Utils;

/**
 * This class can be used at root pane and input handler for the debugging. It automatically updates and renders the
 * debug GUI for you. you can add widgets to the root pane which is a {@link DesktopArea}. So you want to mostly want to
 * add {@link ResizableFrame}.
 * 
 * @author Stefan Lange
 * @version 0.5.0
 * @since 15.08.2012
 */
public class Debug {
	
	private TWLRootPane root;
	private GUI gui;
	private LWJGLRenderer renderer;
	
	public Debug(GameContainer container) throws RadicalFishException {
		createGUI(container);
		update();
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void update() {
		int viewWidth = Gdx.graphics.getWidth();
		int viewHeight = Gdx.graphics.getHeight();
		if ((this.renderer.getWidth() != viewWidth) || (this.renderer.getHeight() != viewHeight)) {
			renderer.setViewport(0, 0, viewWidth, viewHeight);
			gui.setSize();
		}
		
		gui.updateTime();
		gui.handleKeyRepeat();
		gui.handleTooltips();
		gui.updateTimers();
		gui.invokeRunables();
		gui.validateLayout();
		
	}
	public void render() {
		gui.draw();
		gui.setCursor();
	}
	
	public void addToRoot(Widget widget) {
		Utils.notNull("widget", widget);
		root.add(widget);
	}
	
	// INTERN METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void createGUI(GameContainer container) throws RadicalFishException {
		Logger.info("Initiating Debug...");
		
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
		
		try {
			root = new TWLRootPane();
			root.setTheme("");
			
			ActionMap actionMap = new ActionMap();
			actionMap.addMapping(this);
			
			root.setActionMap(actionMap);
			
			renderer = new LWJGLRenderer();
			ThemeManager theme = loadTheme(renderer, "gui/RadicalFish.xml");
			
			gui = new GUI(root, renderer);
			gui.setTheme("");
			gui.applyTheme(theme);
			
			TWLInputForwarder inputForwarder = new TWLInputForwarder(gui);
			container.getInput().addInputProcessor(inputForwarder);
			
			Logger.info("Debug successfully loaded!");
		} catch (Throwable e) {
			throw new RadicalFishException("Could not initialize TWL GUI", e);
		} finally {
			GL11.glPopAttrib();
		}
	}
	private ThemeManager loadTheme(Renderer renderer, String path) throws Exception {
		return ThemeManager.createThemeManager(new File(path).toURI().toURL(), renderer);
	}
	
	// SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void setVisible(boolean visible) {
		root.setVisible(visible);
	}
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public Widget getCurrentFocusOwner() {
		return root.getCurrentFocusOwner();
	}
	public boolean isVisible() {
		return root.isVisible();
	}
}
