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
import com.badlogic.gdx.utils.Array;
import de.matthiasmann.twl.Alignment;
import de.matthiasmann.twl.BorderLayout;
import de.matthiasmann.twl.BorderLayout.Location;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.DialogLayout.Group;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.EditField.Callback;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.PropertySheet;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.ScrollPane.Fixed;
import de.matthiasmann.twl.TabbedPane;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.Property;
import de.matthiasmann.twl.model.SimpleProperty;
import de.matthiasmann.twl.model.SimplePropertyList;
import de.radicalfish.context.GameVariables;
import de.radicalfish.debug.SettingsEditor.BooleanFactory;
import de.radicalfish.debug.SettingsEditor.FloatFactory;
import de.radicalfish.debug.SettingsEditor.IntegerFactory;

/**
 * A widget to monitor any primitves mapped in {@link GameVariables}. Changes to the values will be reflected on the
 * {@link GameVariables} instance provided in the C'Tor.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 17.09.2012
 */
public class GameVariablesEditor extends ResizableFrame {
	
	private GameVariables varis;
	
	private TabbedPane tpane;
	private PropertySheet bools, strings, ints, floats;
	private EditField key, value;
	
	private int size_bools, size_strings, size_ints, size_floats;
	
	private int tabindex;
	
	public GameVariablesEditor() {
		this(null);
	}
	public GameVariablesEditor(GameVariables varis) {
		this.varis = varis;
		createPanel();
	}
	
	// OVERRIDE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	protected void paintWidget(GUI gui) {
		super.paintWidget(gui);
		
		if (varis != null) {
			if (size_bools < varis.getNumberOfBooleans()) {
				size_bools = varis.getNumberOfBooleans();
				loadBools();
				bools.validateLayout();
			}
			if (size_ints < varis.getNumberOfInts()) {
				size_ints = varis.getNumberOfInts();
				loadIntegers();
				ints.validateLayout();
			}
			if (size_floats < varis.getNumberOfFloats()) {
				size_floats = varis.getNumberOfFloats();
				loadFloats();
				floats.validateLayout();
			}
			if (size_strings < varis.getNumberOfStrings()) {
				size_strings = varis.getNumberOfStrings();
				loadStrings();
				strings.validateLayout();
			}
			pullChanges();
		}
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void createPanel() {
		setTheme("gamevariables-frame");
		setResizableAxis(ResizableAxis.BOTH);
		addCloseCallback(new Runnable() {
			public void run() {
				setVisible(false);
			}
		});
		setTitle("Game Variables");
		setSize(300, 400);
		setPosition(5, 5);
		
		BorderLayout bl = new BorderLayout();
		
		tpane = new TabbedPane();
		
		bools = new PropertySheet();
		bools.registerPropertyEditorFactory(Boolean.class, new BooleanFactory());
		strings = new PropertySheet();
		ints = new PropertySheet();
		ints.registerPropertyEditorFactory(Integer.class, new IntegerFactory());
		floats = new PropertySheet();
		floats.registerPropertyEditorFactory(Float.class, new FloatFactory());
		
		tpane.addTab("Booleans", makeTable(bools));
		tpane.addTab("Strings", makeTable(strings));
		tpane.addTab("Ints", makeTable(ints));
		tpane.addTab("Floats", makeTable(floats));
		
		bl.add(tpane);
		
		add(bl);
		
		DialogLayout cl = new DialogLayout();
		cl.setTheme("");
		cl.setBorderSize(0, 0, 2, 0);
		
		key = new EditField();
		key.setTheme("gm-editfield");
		value = new EditField();
		value.setTheme("gm-editfield");
		key.addCallback(new Callback() {
			public void callback(int key) {
				if (key == Event.KEY_RETURN) {
					submit(GameVariablesEditor.this.key.getText(), value.getText(), GameVariablesEditor.this.key);
				}
			}
		});
		value.addCallback(new Callback() {
			public void callback(int key) {
				if (key == Event.KEY_RETURN) {
					submit(GameVariablesEditor.this.key.getText(), value.getText(), value);
				}
			}
		});
		
		Button button = new Button("Add Value");
		button.addCallback(new Runnable() {
			public void run() {
				submit(key.getText(), value.getText(), key);
			}
		});
		
		// hacky way to get some info of the current tab index...
		tpane.getActiveTab().addCallback(new Runnable() {
			public void run() {
				tabindex = 0;
			}
		});
		tpane.cycleTabs(1);
		tpane.getActiveTab().addCallback(new Runnable() {
			public void run() {
				tabindex = 1;
			}
		});
		tpane.cycleTabs(1);
		tpane.getActiveTab().addCallback(new Runnable() {
			public void run() {
				tabindex = 2;
			}
		});
		tpane.cycleTabs(1);
		tpane.getActiveTab().addCallback(new Runnable() {
			public void run() {
				tabindex = 3;
			}
		});
		tpane.cycleTabs(1);
		
		Group h = cl.createSequentialGroup().addWidget(key, Alignment.CENTER).addGap(3, 3, 3).addWidget(value, Alignment.CENTER)
				.addGap(4, 4, 4).addWidget(button).addGap(6, 6, 6);
		Group v = cl.createParallelGroup().addWidget(key, Alignment.FILL).addWidget(value, Alignment.FILL).addWidget(button, Alignment.CENTER);
		
		cl.setHorizontalGroup(h);
		cl.setVerticalGroup(v);
		
		bl.add(cl, Location.SOUTH);
		
		loadVaris();
	}
	private Widget makeTable(PropertySheet sheet) {
		sheet.setTheme("gm-table");
		ScrollPane scrollPane = new ScrollPane(sheet);
		scrollPane.setFixed(Fixed.HORIZONTAL);
		scrollPane.setTheme("tableScrollPane");
		return scrollPane;
	}
	
	@SuppressWarnings("unchecked")
	private void pullChanges() {
		for (int i = 0; i < bools.getPropertyList().getNumProperties(); i++) {
			Property<Boolean> prop = (Property<Boolean>) bools.getPropertyList().getProperty(i);
			if(varis.getBoolean(prop.getName(), false) != prop.getPropertyValue()) {
				prop.setPropertyValue(varis.getBoolean(prop.getName(), false));
			}
		}
		for (int i = 0; i < ints.getPropertyList().getNumProperties(); i++) {
			Property<Integer> prop = (Property<Integer>) ints.getPropertyList().getProperty(i);
			if(varis.getInt(prop.getName(), 0) != prop.getPropertyValue()) {
				prop.setPropertyValue(varis.getInt(prop.getName(), 0));
			}
		}
		for (int i = 0; i < floats.getPropertyList().getNumProperties(); i++) {
			Property<Float> prop = (Property<Float>) floats.getPropertyList().getProperty(i);
			if(varis.getFloat(prop.getName(), 0) != prop.getPropertyValue()) {
				prop.setPropertyValue(varis.getFloat(prop.getName(), 0));
			}
		}
		for (int i = 0; i < strings.getPropertyList().getNumProperties(); i++) {
			Property<String> prop = (Property<String>) floats.getPropertyList().getProperty(i);
			if(varis.getString(prop.getName(), "") != prop.getPropertyValue()) {
				prop.setPropertyValue(varis.getString(prop.getName(), ""));
			}
		}
	}
	
	private void loadVaris() {
		if (varis == null) {
			return;
		}
		
		size_bools = varis.getNumberOfBooleans();
		size_strings = varis.getNumberOfStrings();
		size_ints = varis.getNumberOfInts();
		size_floats = varis.getNumberOfFloats();
		
		loadBools();
		loadIntegers();
		loadFloats();
		loadStrings();
	}
	private void loadBools() {
		if (size_bools == 0) {
			return;
		}
		SimplePropertyList list = bools.getPropertyList();
		Array<String> keys = varis.getBooleanKeys();
		for (int i = 0; i < size_bools; i++) {
			if (!contains(keys.get(i), list)) {
				final SimpleProperty<Boolean> prop = new SimpleProperty<Boolean>(Boolean.class, keys.get(i), varis.getBoolean(keys.get(i),
						false));
				prop.addValueChangedCallback(new Runnable() {
					public void run() {
						varis.putBoolean(prop.getName(), prop.getPropertyValue());
					}
				});
				list.addProperty(prop);
			}
		}
	}
	private void loadIntegers() {
		if (size_ints == 0) {
			return;
		}
		SimplePropertyList list = ints.getPropertyList();
		Array<String> keys = varis.getIntKeys();
		for (int i = 0; i < size_ints; i++) {
			if (!contains(keys.get(i), list)) {
				final SimpleProperty<Integer> prop = new SimpleProperty<Integer>(Integer.class, keys.get(i), varis.getInt(keys.get(i), 0));
				prop.addValueChangedCallback(new Runnable() {
					public void run() {
						varis.putInt(prop.getName(), prop.getPropertyValue());
					}
				});
				list.addProperty(prop);
			}
		}
	}
	private void loadFloats() {
		if (size_floats == 0) {
			return;
		}
		SimplePropertyList list = floats.getPropertyList();
		Array<String> keys = varis.getFloatKeys();
		for (int i = 0; i < size_floats; i++) {
			if (!contains(keys.get(i), list)) {
				final SimpleProperty<Float> prop = new SimpleProperty<Float>(Float.class, keys.get(i), varis.getFloat(keys.get(i), 0));
				prop.addValueChangedCallback(new Runnable() {
					public void run() {
						varis.putFloat(prop.getName(), prop.getPropertyValue());
					}
				});
				list.addProperty(prop);
			}
		}
	}
	private void loadStrings() {
		if (size_strings == 0) {
			return;
		}
		SimplePropertyList list = strings.getPropertyList();
		Array<String> keys = varis.getStringKeys();
		for (int i = 0; i < size_strings; i++) {
			if (!contains(keys.get(i), list)) {
				final SimpleProperty<String> prop = new SimpleProperty<String>(String.class, keys.get(i), varis.getString(keys.get(i), ""));
				prop.addValueChangedCallback(new Runnable() {
					public void run() {
						varis.putString(prop.getName(), prop.getPropertyValue());
					}
				});
				list.addProperty(prop);
			}
		}
	}
	
	private boolean contains(String key, SimplePropertyList list) {
		for (int i = 0; i < list.getNumProperties(); i++) {
			if (list.getProperty(i).getName().equals(key)) {
				return true;
			}
		}
		return false;
	}
	
	private void submit(String key, String value, final EditField field) {
		if (varis == null) {
			this.key.setText("");
			this.value.setText("");
			field.setErrorMessage("Can create new pair because there is no GameVariables instance!");
			return;
		}
		if (key.isEmpty() || value.isEmpty()) {
			this.key.setText("");
			this.value.setText("");
			field.setErrorMessage("Both a key and a value must be set!");
			return;
		} else {
			this.key.setErrorMessage(null);
			this.value.setErrorMessage(null);
		}
		
		if (tabindex == 0) {
			submitBoolean(key, value, field);
		} else if (tabindex == 1) {
			submitString(key, value, field);
		} else if (tabindex == 2) {
			submitInt(key, value, field);
		} else if (tabindex == 3) {
			submitFloat(key, value, field);
		}
		
		this.key.setText("");
		this.value.setText("");
	}
	private void submitBoolean(String key, String value, EditField field) {
		if (!value.equals("true") && !value.equals("false")) {
			field.setErrorMessage("Boolean Property must be true or false!");
			this.value.setText("");
			return;
		} else {
			this.value.setErrorMessage(null);
		}
		
		boolean val = value.endsWith("true");
		
		varis.putBoolean(key, val);
		
		final SimpleProperty<Boolean> prop = new SimpleProperty<Boolean>(Boolean.class, key, val, false);
		prop.addValueChangedCallback(new Runnable() {
			public void run() {
				varis.putBoolean(prop.getName(), prop.getPropertyValue());
			}
		});
		bools.getPropertyList().addProperty(prop);
		
		bools.invalidateLayout();
	}
	private void submitInt(String key, String value, EditField field) {
		int val = 0;
		field.setErrorMessage(null);
		try {
			val = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			field.setErrorMessage("Value cannot be parsed to int!");
			return;
		}
		
		varis.putInt(key, val);
		
		final SimpleProperty<Integer> prop = new SimpleProperty<Integer>(Integer.class, key, val, false);
		prop.addValueChangedCallback(new Runnable() {
			public void run() {
				varis.putInt(prop.getName(), prop.getPropertyValue());
			}
		});
		ints.getPropertyList().addProperty(prop);
		
		ints.invalidateLayout();
	}
	private void submitFloat(String key, String value, EditField field) {
		float val = 0;
		field.setErrorMessage(null);
		try {
			val = Float.parseFloat(value);
		} catch (NumberFormatException e) {
			field.setErrorMessage("Value cannot be parsed to float!");
			return;
		}
		varis.putFloat(key, val);
		final SimpleProperty<Float> prop = new SimpleProperty<Float>(Float.class, key, val, false);
		prop.addValueChangedCallback(new Runnable() {
			public void run() {
				varis.putFloat(prop.getName(), prop.getPropertyValue());
			}
		});
		floats.getPropertyList().addProperty(prop);
		
		floats.invalidateLayout();
	}
	private void submitString(String key, String value, EditField field) {
		field.setErrorMessage(null);
		varis.putString(key, value);
		final SimpleProperty<String> prop = new SimpleProperty<String>(String.class, key, value, false);
		prop.addValueChangedCallback(new Runnable() {
			public void run() {
				varis.putString(prop.getName(), prop.getPropertyValue());
			}
		});
		strings.getPropertyList().addProperty(prop);
		
		strings.invalidateLayout();
	}
	
}
