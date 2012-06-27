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
import java.net.URL;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.newdawn.slick.opengl.shader.ShaderProgram;
import org.newdawn.slick.util.ResourceLoader;
import de.matthiasmann.twl.BorderLayout;
import de.matthiasmann.twl.BorderLayout.Location;
import de.matthiasmann.twl.DesktopArea;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.renderer.Renderer;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;

public class BorderLayoutTest extends DesktopArea {
	
	static ShaderProgram prog;
	static PixelFormat pixelFormat;
	
	public static void main(String[] args) {
		try {
			
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.setResizable(true);
			// Display.setTitle("TWL Slider Demo");
			Display.setVSyncEnabled(true);
			
			LWJGLRenderer renderer = new LWJGLRenderer();
			BorderLayoutTest sliders = new BorderLayoutTest();
			GUI gui = new GUI(sliders, renderer);
			
			ThemeManager theme = loadTheme(renderer, "de/radicalfish/testdata/gui/RadicalFish.xml");
			gui.applyTheme(theme);
			
			prog = ShaderProgram.loadProgram("de/radicalfish/testdata/shader/simple.vert", "de/radicalfish/testdata/shader/simple.frag");
			
			while (!Display.isCloseRequested()) {
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
				
				gui.update();
				Display.update();
				reduceInputLag();
				
				if (Display.wasResized()) {
					GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
					renderer.setViewport(0, 0, Display.getWidth(), Display.getHeight());
				}
			}
			
			gui.destroy();
			theme.destroy();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Display.destroy();
	}
	private static void reduceInputLag() {
		GL11.glGetError(); // this call will burn the time between vsyncs
		Display.processMessages(); // process new native messages since Display.update();
		Mouse.poll(); // now update Mouse events
		Keyboard.poll(); // and Keyboard too
	}
	private static ThemeManager loadTheme(Renderer renderer, String path) throws Exception {
		URL url = ResourceLoader.getResource(path);
		assert url != null;
		return ThemeManager.createThemeManager(url, renderer);
	}
	
	public BorderLayoutTest() {
		setTheme("");
		
		ResizableFrame frame = new ResizableFrame();
		frame.setTheme("resizableframe-title");
		frame.setTitle("");
		
		ScrollPane p = makeScrollPaneWithEditField();
		
		BorderLayout l = new BorderLayout();
		l.add(makeScrollPaneWithEditField(), Location.NORTH);
		l.add(makeScrollPaneWithEditField(), Location.SOUTH);
		l.add(makeScrollPaneWithEditField(), Location.EAST);
		l.add(p, Location.WEST);
		l.add(makeScrollPaneWithEditField(), Location.CENTER);
		
		frame.add(l);
		
		add(frame);
		
	}
	
	private ScrollPane makeScrollPaneWithEditField() {
		ScrollPane pane = new ScrollPane();
		pane.setExpandContentSize(true);
		EditField editField = new EditField();
		editField.setMultiLine(true);
		
		pane.setContent(editField);
		
		return pane;
		
	}
	
}
