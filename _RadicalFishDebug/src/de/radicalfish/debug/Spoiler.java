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
import de.matthiasmann.twl.Alignment;
import de.matthiasmann.twl.BorderLayout;
import de.matthiasmann.twl.Container;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.DialogLayout.Group;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ToggleButton;
import de.matthiasmann.twl.Widget;

/**
 * A widget which contains another widget and hides or shows it.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 23.09.2012
 */
public class Spoiler extends BorderLayout {
	
	/**
	 * Callback for the button in the header.
	 */
	public interface Callback {
		/** Called when the spoiler view gets toggled. */
		public void viewChanged(boolean visible, boolean afterLayout);
	}
	
	private Widget spoilerWidget;
	private DialogLayout headerBox;
	private Container centerContainer;
	private ToggleButton headerButton;
	private Label headerLabel;
	
	private Callback callback;
	
	private String headerText;
	private boolean toggled = false;
	
	/**
	 * Creates a {@link Spoiler} with no widget (a instance of {@link Widget} will be used instead). The text will be
	 * "Show Widget" or "Hide Widget".
	 */
	public Spoiler() {
		this(new Widget());
	}
	/**
	 * Creates a {@link Spoiler} with a widget to be hidden. The text will be "Show Widget" or "Hide Widget".
	 */
	public Spoiler(Widget spoilerWidget) {
		this(spoilerWidget, "Widget");
	}
	/**
	 * Creates a {@link Spoiler} with a widget to be hidden and a text to be displayed (will have the text show/hide
	 * before the text).
	 */
	public Spoiler(Widget spoilerWidget, String headerText) {
		if (spoilerWidget == null) {
			throw new NullPointerException("widget is null");
		}
		if (headerText == null) {
			throw new NullPointerException("header text is null");
		}
		this.spoilerWidget = spoilerWidget;
		this.headerText = headerText;
		createPanel();
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Set a callback to listener to toggles.
	 */
	public void setCallback(Callback callback) {
		if (callback == null) {
			throw new NullPointerException("callback is null");
		}
		this.callback = callback;
	}
	
	/**
	 * Sets the spoiler widget to show/hide
	 */
	public void setSpoilerWidget(Widget widget) {
		if (spoilerWidget == null) {
			throw new NullPointerException("widget is null");
		}
		centerContainer.removeAllChildren();
		spoilerWidget = widget;
		centerContainer.add(spoilerWidget);
	}
	/**
	 * Removes the spoiler widget.
	 */
	public void removeSpoilerWidget() {
		centerContainer.removeAllChildren();
	}
	/**
	 * Sets the header text.
	 */
	public void setSpoilerText(String text) {
		if (headerText == null) {
			throw new NullPointerException("header text is null");
		}
		headerText = text;
		if (headerButton.isActive()) {
			headerLabel.setText("Hide " + headerText);
		} else {
			headerLabel.setText("Show " + headerText);
		}
	}
	/**
	 * @return true if the content is not visibleö
	 */
	public boolean isHidden() {
		return !headerButton.isActive();
	}
	/**
	 * Toggles the spoiler.
	 */
	public void toggle() {
		headerButton.setActive(!headerButton.isActive());
		handleButton();
	}
	
	// OVERRIDE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public int getMinHeight() {
		if (headerButton.isActive()) {
			return headerBox.getMinHeight() + centerContainer.getMinHeight();
		}
		return super.getMinHeight();
	}
	public int getPreferredHeight() {
		if (headerButton.isActive()) {
			return headerBox.getPreferredHeight() + centerContainer.getPreferredHeight();
		}
		return super.getPreferredHeight();
	}
	
	protected void layout() {
		super.layout();
		if (toggled) {
			if (callback != null) {
				callback.viewChanged(headerButton.isActive(), true);
			}
			toggled = false;
		}
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void createPanel() {
		centerContainer = new Container();
		centerContainer.setTheme("container");
		centerContainer.add(spoilerWidget);
		headerButton = new ToggleButton();
		headerButton.addCallback(new Runnable() {
			public void run() {
				handleButton();
			}
		});
		headerButton.setTheme("headerbutton");
		headerLabel = new Label("Show " + headerText);
		headerBox = new DialogLayout();
		headerBox.setTheme("headerbox");
		
		Group h = headerBox.createSequentialGroup().addWidget(headerButton).addGap("headerbox-spacing").addWidget(headerLabel, Alignment.LEFT);
		Group v = headerBox.createParallelGroup(headerButton).addWidget(headerLabel, Alignment.FILL);
		
		headerBox.setHorizontalGroup(h);
		headerBox.setVerticalGroup(v);
		
		add(headerBox, Location.NORTH);
	}
	
	private void handleButton() {
		if (spoilerWidget == null) {
			return;
		}
		toggled = true;
		if (callback != null) {
			callback.viewChanged(headerButton.isActive(), false);
		}
		
		if (headerButton.isActive()) {
			headerLabel.setText("Hide " + headerText);
			add(centerContainer, Location.CENTER);
			invalidateLayout();
		} else {
			headerLabel.setText("Show " + headerText);
			removeChild(Location.CENTER);
		}
	}
	
}
