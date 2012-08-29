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
import de.matthiasmann.twl.Alignment;
import de.matthiasmann.twl.ColumnLayout;
import de.matthiasmann.twl.ColumnLayout.Columns;
import de.matthiasmann.twl.ColumnLayout.Row;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Scrollbar.Orientation;
import de.matthiasmann.twl.ToggleButton;
import de.matthiasmann.twl.Widget;
import de.radicalfish.context.Settings;

/**
 * TWL Panel for manipulating default settings from {@link Settings}. A <code>hslider</code>-, <code>checkbox</code>- and
 * a <code>fixedlabel</code>(a label with a minWidth) theme must be implemented if a custom theme is uses. The
 * OptionsPanel runs with default values for the sliders which are:
 * 
 * <pre>
 * Music Volume: 0 - 100 (mapped from the settings by <code>settings.getMusicVolume() * 100</code>)
 * Sound Volume  - // -
 * TextSpeed: 0 - 10
 * </pre>
 * 
 * Use the <code>setMusicVolumeRange()</code>, <code>setSoundVolumeRange</code> and <code>setTextSpeedRange</code>
 * methods to apply your own. <code>appendWidget()</code> can be used to add custom widgets under the normal owns.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 01.05.2012
 */
public class OptionsPanel extends ResizableFrame {

	// the column names
	private static final String NAME = "name", VALUE = "value", SEP = "SEP", LABEL = "label";
	
	private Settings settings;
	
	private Columns checkBoxColumns, seperatorColumns, sliderColumns;
	private ColumnLayout layout;
	
	private ToggleButton fullscreen, debug, log, sound3D, vsync, smoothDelta;
	private Slider musicVolume, soundVolume, textSpeed;
	
	private int extraWidgetsCount;
	
	/**
	 * Creates a new OptionsPanel
	 * 
	 * @param settings
	 *            the context of the game
	 */
	public OptionsPanel(Settings settings) {
		this.settings = settings;
		extraWidgetsCount = 0;
		createPanel();
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Sets the range of the music volume slider.
	 * 
	 * @param min
	 *            the min value
	 * @param max
	 *            the max value
	 */
	public void setMusicVolumeRange(int min, int max) {
		musicVolume.setMinMaxValue(min, max);
	}
	/**
	 * Sets the range of the sound volume slider.
	 * 
	 * @param min
	 *            the min value
	 * @param max
	 *            the max value
	 */
	public void setSoundVolumeRange(int min, int max) {
		soundVolume.setMinMaxValue(min, max);
	}
	/**
	 * Sets the range of the text speed slider.
	 * 
	 * @param min
	 *            the min value
	 * @param max
	 *            the max value
	 */
	public void setTextSpeedRange(int min, int max) {
		textSpeed.setMinMaxValue(min, max);
	}
	/**
	 * Adds a custom widget to the OptionsPanel
	 * 
	 * @param name
	 *            the name of the label next to the widget
	 * @param widget
	 *            the widget to add
	 * @param alignment
	 *            the Alignment to use
	 */
	public void appendWidget(String name, Widget widget, Alignment alignment) {
		if (widget == null) {
			throw new NullPointerException("Widget is null!");
		}
		if (extraWidgetsCount == 0) {
			addSeparator();
		}
		extraWidgetsCount++;
		
		Label label = new Label(name);
		label.setTooltipContent(widget.getTooltipContent());
		
		Row row = layout.addRow(checkBoxColumns);
		row.add(label, Alignment.LEFT).add(widget);
		layout.setWidgetAlignment(widget, alignment);
		
	}
	
	// OVERRIDE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	@Override
	protected void paintWidget(GUI gui) {
		checkValues();
		super.paintWidget(gui);
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void checkValues() {
		fullscreen.setActive(settings.isFullscreen());
		log.setActive(settings.isLogging());
		debug.setActive(settings.isDebugging());
		sound3D.setActive(settings.isSound3D());
		vsync.setActive(settings.isVSync());
		smoothDelta.setActive(settings.isSmoothDelta());
		
		musicVolume.setValue((int)(settings.getMusicVolume() * 100));
		soundVolume.setValue((int)(settings.getSoundVolume() * 100));
	}
	private void createPanel() {
		setTheme("resizableframe-title");
		setTitle("Options");
		setPosition(800, 0);
		setResizableAxis(ResizableAxis.HORIZONTAL);
		addCloseCallback(new Runnable() {
			public void run() {
				setVisible(false);
			}
		});
		
		layout = new ColumnLayout();
		layout.addDefaultGaps();
		
		sliderColumns = layout.getColumns(NAME, VALUE, LABEL);
		checkBoxColumns = layout.getColumns(NAME, VALUE);
		seperatorColumns = layout.getColumns(SEP);
		
		
		addCheckboxes();
		addSeparator();
		addMusicSliders();
		addSeparator();
		
		add(layout);
	}
	private void addSeparator() {
		Row row = layout.addRow(seperatorColumns);
		Widget w = new Widget();
		w.setTheme("hseparator");
		row.add(w);
	}
	
	private void addMusicSliders() {
		final Label value = new Label("" + (int)(settings.getMusicVolume() * 100));
		value.setTheme("fixedlabel");
		musicVolume = new Slider(Orientation.HORIZONTAL);
		musicVolume.addCallback(new Runnable() {
			public void run() {
				settings.setMusicVolume(musicVolume.getValue() / 100f);
				value.setText("" + musicVolume.getValue());
			}
		});
		musicVolume.setValue((int) (settings.getMusicVolume() * 100));
		Row row = layout.addRow(sliderColumns);
		row.addLabel("Music Volume: ").add(musicVolume, Alignment.FILL).add(value, Alignment.CENTER);
		
		final Label value2 = new Label("" + (int)(settings.getMusicVolume() * 100));
		value2.setTheme("fixedlabel");
		soundVolume = new Slider(Orientation.HORIZONTAL);
		soundVolume.addCallback(new Runnable() {
			public void run() {
				settings.setSoundVolume(soundVolume.getValue() / 100f);
				value2.setText("" + soundVolume.getValue());
			}
		});
		soundVolume.setValue((int) (settings.getSoundVolume() * 100));
		row = layout.addRow(sliderColumns);
		row.addLabel("Sound Volume: ").add(soundVolume, Alignment.FILL).add(value2, Alignment.CENTER);
		
	}
	private void addCheckboxes() {
		fullscreen = new ToggleButton();
		Runnable callback = new Runnable() {
			public void run() {
				settings.setFullscreen(fullscreen.isActive());
			}
		};
		fullscreen.addCallback(callback);
		addCheckBoxRow(fullscreen, "Fullscreen: ", "Toggles fullscreen (duh)", settings.isFullscreen());
		
		debug = new ToggleButton();
		callback = new Runnable() {
			public void run() {
				settings.setDebugging(debug.isActive());
			}
		};
		debug.addCallback(callback);
		addCheckBoxRow(debug, "Debug: ", "Needs a restart (too disable all debug tools)", settings.isDebugging());
		
		log = new ToggleButton();
		callback = new Runnable() {
			public void run() {
				settings.setLogging(log.isActive());
			}
		};
		log.addCallback(callback);
		addCheckBoxRow(log, "Log: ", "Logs outputs", settings.isLogging());
		
		sound3D = new ToggleButton();
		callback = new Runnable() {
			public void run() {
				settings.setSound3D(sound3D.isActive());
			}
		};
		sound3D.addCallback(callback);
		addCheckBoxRow(sound3D, "3D Sound: ", "Use 3D sound", settings.isSound3D());
		
		vsync = new ToggleButton();
		callback = new Runnable() {
			public void run() {
				settings.setVSync(vsync.isActive());
			}
		};
		vsync.addCallback(callback);
		addCheckBoxRow(vsync, "VSync: ", "Tries to sync with the display refresh rate", settings.isVSync());
		
		smoothDelta = new ToggleButton();
		callback = new Runnable() {
			public void run() {
				settings.setSmoothDelta(smoothDelta.isActive());
			}
		};
		smoothDelta.addCallback(callback);
		addCheckBoxRow(smoothDelta, "Smooth Delta: ", "Smooths the delta values", settings.isSmoothDelta());
		
	}
	private void addCheckBoxRow(ToggleButton checkBox, String label, String tooltip, boolean value) {
		checkBox.setTheme("checkbox");
		checkBox.setActive(value);
		checkBox.setTooltipContent(tooltip);
		
		Row row = layout.addRow(checkBoxColumns);
		row.addLabel(label).add(checkBox, Alignment.CENTER);
	}
	
}
