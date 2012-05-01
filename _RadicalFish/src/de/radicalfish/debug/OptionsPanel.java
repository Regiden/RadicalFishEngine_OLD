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
import de.matthiasmann.twl.ColumnLayout;
import de.matthiasmann.twl.ColumnLayout.Row;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.ScrollPane.Fixed;
import de.matthiasmann.twl.ToggleButton;
import de.matthiasmann.twl.Widget;
import de.radicalfish.context.Settings;
import de.radicalfish.extern.TWLRootPane;

/**
 * Panel for manipulating default settings from {@link Settings}.
 * 
 * @author Stefan Lange
 * @version 0.0.0
 * @since 01.05.2012
 */
public class OptionsPanel extends ResizableFrame {
	
	private static final String NAME = "name", VALUE = "value";
	
	private Settings context;
	
	/**
	 * Creates a new OptionsPanel
	 * 
	 * @param context
	 *            the context of the game
	 * @param root
	 *            the panel where the optionPanel should be added
	 */
	public OptionsPanel(Settings context, TWLRootPane root) {
		this.context = context;
		createPanel(root);
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void createPanel(TWLRootPane root) {
		setTheme("/resizableframe-title");
		setTitle("Options");
		setPosition(800, 0);
		setResizableAxis(ResizableAxis.NONE);
		
		addCloseCallback(new Runnable() {
			public void run() {
				setVisible(false);
			}
		});
		
		ScrollPane scroll = new ScrollPane();
		scroll.setFixed(Fixed.HORIZONTAL);
		
		ColumnLayout layout = new ColumnLayout();
		layout.setTheme("");
		
		// Widgets
		addButtons(layout);
		
		add(scroll);
		adjustSize();
		root.add(this);
	}
	private void addButtons(ColumnLayout layout) {
		addCheckBoxRow(layout, "Debug", context.isDebugging());
		addCheckBoxRow(layout, "Log", context.isLogging());
		addCheckBoxRow(layout, "Fullscreen", context.isFullscreen());
		addCheckBoxRow(layout, "3D Sound", context.is3DSound());
		
	}
	
	private void addCheckBoxRow(ColumnLayout layout, String label, boolean value) {
		ToggleButton checkBox = new ToggleButton();
		checkBox.setTheme("checkbox");
		checkBox.setActive(value);
		addRow(layout, label, checkBox);
	}
	private void addRow(ColumnLayout layout, String label, Widget widget) {
		Row row = layout.addRow(NAME, VALUE);
		row.addLabel(label).add(widget);
	}
	
}
