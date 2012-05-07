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
package de.radicalfish.debug;
import de.matthiasmann.twl.BoxLayout;
import de.matthiasmann.twl.BoxLayout.Direction;
import de.matthiasmann.twl.Alignment;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;

/**
 * Small Tool bar with buttons opening some default debug tools
 * 
 * @author Stefan Lange
 * @version 0.0.0
 * @since 07.05.2012
 */
public class ToolBox extends ResizableFrame {
	
	private BoxLayout buttonbox;
	
	public ToolBox() {
		createPanel();
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Adds a new tool to the tool box. The tool gets a button to hide/show if pressed.
	 * 
	 * @param name
	 *            the name of the button
	 * @param tooltip
	 *            can be null
	 * @param tool
	 *            the tool to add
	 */
	public void addTool(String name, String tooltip, final Widget tool) {
		addButton(name, null, new Runnable() {
			public void run() {
				tool.setVisible(!tool.isVisible());
				tool.requestKeyboardFocus();
			}
		});
		
	}
	/**
	 * Adds a new tool to the tool box with a custom callback.
	 * 
	 * @param name
	 *            the name of the button
	 * @param tooltip
	 *            can be null
	 * @param callback
	 *            the call back to attach to the button
	 */
	public void addButton(String name, String tooltip, Runnable callback) {
		Button button = new Button(name);
		button.addCallback(callback);
		button.setCanAcceptKeyboardFocus(false);
		button.setTooltipContent(tooltip);
		button.setAlignment(Alignment.FILL);
		buttonbox.add(button);
		invalidateLayout();
	}
	
	// TODO change box layout to colum layout!
	/**
	 * Fills up space buttons after be right handled.
	 */
	public void addFiller() {
		Widget w = new Widget();
		w.setTheme("empty");
		buttonbox.add(w);
		invalidateLayout();
	}
	/**
	 * Adds a vertical separator right to the last added button.
	 */
	public void addSeparator() {
		Widget w = new Widget();
		w.setTheme("toolboxseperator");
		buttonbox.add(w);
		invalidateLayout();
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void createPanel() {
		setTheme("");
		
		buttonbox = new BoxLayout(Direction.HORIZONTAL);
		buttonbox.setTheme("buttonBox");
		setPosition(0, 800);
		setSize(800, 0);
		
		add(buttonbox);
	}
}
