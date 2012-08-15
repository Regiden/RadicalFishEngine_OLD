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
import java.util.ArrayList;
import com.badlogic.gdx.math.Interpolation;
import de.matthiasmann.twl.Alignment;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.ColumnLayout;
import de.matthiasmann.twl.ColumnLayout.Row;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;

/**
 * Small Tool bar with buttons opening some default debug tools. A call to <code>createToolBox()</code> must used to
 * make all added buttons visible! The themes used for this GUI are:
 * 
 * <p>
 * <li>buttonBox: the box which display all buttons.</li>
 * <li>empty: used for the filler and should have it's maxWidth and maxHeight set to a high value</li>
 * <li>toolboxseparator: used as the vertical separator. this should have a minHeight set to a value you like and a
 * max/minWidth of 1 (or another desired value</li>
 * <li>button: I simple button</li>
 * 
 * <pre> </pre>
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 07.05.2012
 */
public class ToolBox extends ResizableFrame {
	
	private ColumnLayout layout;
	
	private ArrayList<WidgetWithAlginment> preWidgets;
	
	private float currentPosition;
	private int currentDirection;
	
	private boolean created;
	
	/**
	 * Creates a tool box.
	 * 
	 * @param containerWidth
	 *            used the make the tool box as wide as the container width
	 * @param gameHeight
	 *            used to set the position to the bottom of the container
	 */
	public ToolBox() {
		created = false;
		createPanel();
		currentPosition = 0;
		currentDirection = 0;
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Pops out the {@link ToolBox} from the bottom.
	 */
	public void show() {
		currentPosition = 0;
		currentDirection = 1;
	}
	/**
	 * Hides the {@link ToolBox} at the bottom of the bar.
	 */
	public void hide() {
		currentPosition = getHeight();
		currentDirection = -1;
	}
	
	/**
	 * Adds a new button to the tool box. The tool gets a button to hide/show if pressed.
	 * 
	 * @param name
	 *            the name of the button
	 * @param tool
	 *            the tool to add
	 */
	public void addButton(String name, final Widget tool) {
		addButton(name, new Runnable() {
			public void run() {
				tool.setVisible(!tool.isVisible());
				tool.requestKeyboardFocus();
			}
		});
		
	}
	/**
	 * Adds a new button to the tool box with a custom callback.
	 * 
	 * @param name
	 *            the name of the button
	 * @param callback
	 *            the call back to attach to the button
	 */
	public void addButton(String name, Runnable callback) {
		Button button = new Button(name);
		button.addCallback(callback);
		button.setCanAcceptKeyboardFocus(false);
		
		preWidgets.add(new WidgetWithAlginment(button, Alignment.LEFT));
	}
	/**
	 * Fills up space buttons after be right handled.
	 */
	public void addFiller() {
		Widget w = new Widget();
		w.setTheme("empty");
		
		preWidgets.add(new WidgetWithAlginment(w, Alignment.FILL));
	}
	/**
	 * Adds a vertical separator right to the last added button.
	 */
	public void addSeparator() {
		Widget w = new Widget();
		w.setTheme("toolboxseperator");
		preWidgets.add(new WidgetWithAlginment(w, Alignment.CENTER));
		
	}
	
	/**
	 * Adds a custom widget too the tool box like a small edit field or something.
	 * 
	 * @param widget
	 *            the widget to add
	 * @param alignment
	 *            the alignment to use
	 */
	public void addCustomWidget(Widget widget, Alignment alignment) {
		if (widget == null || alignment == null) {
			throw new NullPointerException("The widget or alginment is null");
		}
		preWidgets.add(new WidgetWithAlginment(widget, alignment));
	}
	
	/**
	 * adds all the buttons, separator and fillers to the toolbox. Must be called in order to make them visible!
	 */
	public void createToolbox() {
		if (created) {
			throw new IllegalAccessError("tool box alread created!");
		}
		String[] columns = new String[preWidgets.size()];
		for (int i = 0; i < columns.length; i++) {
			columns[i] = "" + (i + 1);
		}
		Row row = layout.addRow(columns);
		
		for (WidgetWithAlginment w : preWidgets) {
			row.add(w.widget, w.alignment);
		}
		created = true;
		
		invalidateLayout();
		
	}
	
	// OVERRIDE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	@Override
	public void add(Widget child) {
		throw new UnsupportedOperationException("Use addButton or addCustomWidget!");
	}
	
	@Override
	protected void paint(GUI gui) {
		if (currentDirection == 1) {
			if (currentPosition != getHeight()) {
				setPosition(0, getParent().getHeight() - getShowTime());
			}
		} else if (currentDirection == -1) {
			if (currentPosition != getHeight()) {
				setPosition(0, getParent().getHeight() - getHideTime());
			}
		}
		
		if (getWidth() != getParent().getWidth()) {
			setSize(getParent().getWidth(), 0);
		}
		super.paint(gui);
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private int getShowTime() {
		currentPosition = Interpolation.linear.apply(currentPosition, getHeight(), 0.2f);
		if (currentPosition > getHeight()) {
			currentPosition = getHeight();
		}
		return (int) currentPosition;
	}
	private int getHideTime() {
		currentPosition = Interpolation.linear.apply(currentPosition, 0, 0.2f);
		if (currentPosition < 0) {
			currentPosition = 0;
		}
		return (int) currentPosition;
	}
	private void createPanel() {
		setTheme("");
		
		layout = new ColumnLayout();
		layout.setTheme("buttonBox");
		
		super.add(layout);
		
		preWidgets = new ArrayList<WidgetWithAlginment>();
	}
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return true if the {@link ToolBox} is completely visible.
	 */
	public boolean isShown() {
		if(currentDirection == 0) {
			return false;
		} else if(currentDirection == 1) {
			return currentPosition == getHeight();
		}
		return false;
	}
	/**
	 * @return true if the {@link ToolBox} is completely hidden.
	 */
	public boolean isHidden() {
		if(currentDirection == 0) {
			return false;
		} else if(currentDirection == -1) {
			return currentPosition == 0;
		}
		return false;
	}
	
	// PRIVATE CLASSES
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private static class WidgetWithAlginment {
		Widget widget;
		Alignment alignment;
		
		public WidgetWithAlginment(Widget widget, Alignment alignment) {
			this.widget = widget;
			this.alignment = alignment;
		}
		
	}
}
