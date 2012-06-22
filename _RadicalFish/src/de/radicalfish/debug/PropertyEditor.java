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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import de.matthiasmann.twl.PropertySheet;
import de.matthiasmann.twl.PropertySheet.PropertyEditorFactory;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.ScrollPane.Fixed;
import de.matthiasmann.twl.ToggleButton;
import de.matthiasmann.twl.ValueAdjusterFloat;
import de.matthiasmann.twl.ValueAdjusterInt;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.Property;
import de.matthiasmann.twl.model.SimpleFloatModel;
import de.matthiasmann.twl.model.SimpleIntegerModel;
import de.matthiasmann.twl.model.SimpleProperty;
import de.matthiasmann.twl.model.SimplePropertyList;
import de.radicalfish.context.Settings;

/**
 * Editor for simple properties. It uses an Implementation of the {@link Settings} Interface. The supported type are
 * <code>boolean</code>, <code>float</code>, <code>int</code>, <code>String</code>.
 * <p>
 * The PropertyEditor supports one depth of sublists separated by a char (Default is ".").
 * 
 * <pre>
 * Properties:
 * 	debug.test
 * 	debug.test2
 * 
 * Will be:
 * 
 * 	debug // header of the sub list
 * 	 -> test
 * 	 -> test2
 * </pre>
 * <p>
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 19.06.2012
 */
public class PropertyEditor extends ResizableFrame {
	
	private PropertySheet propertySheet;
	private Settings settings;
	
	private char separator;
	
	/**
	 * Creates a PropertyEditor to use for the TWL Theme Editor. It uses subgroups and the "." as separator.
	 */
	public PropertyEditor() {
		this(null);
	}
	/**
	 * Creates a PropertyEditor with a settings implementation to use. It uses subgroups and the "." as separator.
	 */
	public PropertyEditor(Settings settings) {
		this(settings, '.');
	}
	/**
	 * Creates a PropertyEditor with a settings implementation to use and a character to use as separator for subgroups.
	 */
	public PropertyEditor(Settings settings, char separator) {
		this.settings = settings;
		this.separator = separator;
		createPanel();
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void createPanel() {
		setTheme("resizableframe-title");
		setResizableAxis(ResizableAxis.BOTH);
		addCloseCallback(new Runnable() {
			public void run() {
				setVisible(false);
			}
		});
		setTitle("Property Editor");
		setSize(300, 200);
		setPosition(100, 5);
		
		propertySheet = new PropertySheet();
		propertySheet.setTheme("table");
		propertySheet.registerPropertyEditorFactory(Boolean.class, new BooleanFactory());
		propertySheet.registerPropertyEditorFactory(Integer.class, new IntegerFactory());
		propertySheet.registerPropertyEditorFactory(Float.class, new FloatFactory());
		
		ScrollPane scrollPane = new ScrollPane(propertySheet);
		scrollPane.setFixed(Fixed.HORIZONTAL);
		scrollPane.setTheme("tableScrollPane");
		
		add(scrollPane);
		
		loadSettings();
		
	}
	
	@Override
	protected void layout() {
		super.layout();
	}
	
	private void loadSettings() {
		if (settings == null) {
			return;
		}
		
		Properties prop = settings.getProperties();
		List<String> keys = new ArrayList<String>();
		Enumeration<Object> e = prop.keys();
		
		while (e.hasMoreElements()) {
			keys.add(e.nextElement().toString());
		}
		
		Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
		for (String name : keys) {
			System.out.println(name);
			addProperty(name, prop.getProperty(name));
		}
		
	}
	private void addProperty(final String name, String value) {
		checkSubList(name, name, value, null, propertySheet.getPropertyList());
	}
	private Property<?> _addProperty(String name, final String original, String value) {
		Property<?> prop = null;
		if ((prop = tryBoolean(name, original, value)) != null) {
			return prop;
		} else if ((prop = tryInteger(name, value)) != null) {
			return prop;
		} else if ((prop = tryFloat(name, value)) != null) {
			return prop;
		} else {
			final Property<?> property = new SimpleProperty<String>(String.class, name, value);
			property.addValueChangedCallback(new Runnable() {
				public void run() {
					settings.setProperty(original, property.getPropertyValue().toString());
				}
			});
			return property;
		}
	}
	
	private boolean isSubGroup(String name) {
		if (name.contains(separator + "")) {
			return true;
		}
		return false;
	}
	
	private void checkSubList(String name, String original, String value, String master, SimplePropertyList owner) {
		if (!isSubGroup(name)) {
			owner.addProperty(_addProperty(name, original, value));
		} else {
			int idx = indexOf(name, separator);
			String pre = name.substring(0, idx);
			String after = name.substring(idx + 1, name.length());
			owner = findOrCreateList(pre, owner);
			
			checkSubList(after, original, value, pre, owner);
		}
	}
	private int indexOf(String search, char c) {
		int index = search.indexOf(c, 0);
		if (index < 0) {
			return search.length();
		}
		return index;
	}
	private SimplePropertyList findOrCreateList(String name, SimplePropertyList base) {
		SimplePropertyList found = null;
		
		for (int i = 0; i < base.getNumProperties(); i++) {
			Property<?> prop = base.getProperty(i);
			if (prop instanceof SimplePropertyList) {
				if (prop.getName().equals(name)) {
					found = (SimplePropertyList) prop;
				}
				
			}
		}
		
		if (found == null) {
			found = new SimplePropertyList(name);
			base.addProperty(found);
		}
		
		return found;
	}
	
	private Property<?> tryBoolean(String name, final String original, String value) {
		if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
			boolean val = value.equalsIgnoreCase("true");
			final SimpleProperty<Boolean> prop = new SimpleProperty<Boolean>(Boolean.class, name, val);
			prop.addValueChangedCallback(new Runnable() {
				public void run() {
					settings.setProperty(original, prop.getPropertyValue().toString());
				}
			});
			return prop;
		}
		return null;
	}
	private Property<?> tryInteger(final String name, String value) {
		try {
			Integer val = Integer.parseInt(value);
			final SimpleProperty<Integer> prop = new SimpleProperty<Integer>(Integer.class, name, val);
			prop.addValueChangedCallback(new Runnable() {
				public void run() {
					settings.setProperty(name, prop.getPropertyValue().toString());
				}
			});
			return prop;
		} catch (NumberFormatException e) {
			return null;
		}
	}
	private Property<?> tryFloat(final String name, String value) {
		try {
			Float val = Float.parseFloat(value);
			final SimpleProperty<Float> prop = new SimpleProperty<Float>(Float.class, name, val);
			prop.addValueChangedCallback(new Runnable() {
				public void run() {
					settings.setProperty(name, prop.getPropertyValue().toString());
				}
			});
			return prop;
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	// INTERN CLASSES
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private class IntegerEditor implements de.matthiasmann.twl.PropertySheet.PropertyEditor, Runnable {
		
		private final Property<Integer> property;
		private final ValueAdjusterInt adjuster;
		private final SimpleIntegerModel model;
		
		public IntegerEditor(Property<Integer> property) {
			this.property = property;
			model = new SimpleIntegerModel(0, Integer.MAX_VALUE, property.getPropertyValue());
			adjuster = new ValueAdjusterInt(model);
			model.addCallback(this);
			valueChanged();
		}
		
		public void run() {
			if (!property.isReadOnly()) {
				property.setPropertyValue(model.getValue());
			}
		}
		
		public Widget getWidget() {
			return adjuster;
		}
		public void valueChanged() {
			model.setValue(property.getPropertyValue());
			adjuster.setEnabled(!property.isReadOnly());
		}
		public void preDestroy() {
			model.removeCallback(this);
		}
		public void setSelected(boolean selected) {
			
		}
		public boolean positionWidget(int x, int y, int width, int height) {
			return false;
		}
		
	}
	private class IntegerFactory implements PropertyEditorFactory<Integer> {
		public de.matthiasmann.twl.PropertySheet.PropertyEditor createEditor(Property<Integer> property) {
			return new IntegerEditor(property);
		}
	}
	private class FloatEditor implements de.matthiasmann.twl.PropertySheet.PropertyEditor, Runnable {
		
		private final Property<Float> property;
		private final ValueAdjusterFloat adjuster;
		private final SimpleFloatModel model;
		
		public FloatEditor(Property<Float> property) {
			this.property = property;
			model = new SimpleFloatModel(0.0f, 1.0f, property.getPropertyValue());
			adjuster = new ValueAdjusterFloat(model);
			model.addCallback(this);
			valueChanged();
		}
		
		public void run() {
			if (!property.isReadOnly()) {
				property.setPropertyValue(model.getValue());
			}
		}
		
		public Widget getWidget() {
			return adjuster;
		}
		public void valueChanged() {
			model.setValue(property.getPropertyValue());
			adjuster.setEnabled(!property.isReadOnly());
		}
		public void preDestroy() {
			model.removeCallback(this);
		}
		public void setSelected(boolean selected) {
			
		}
		public boolean positionWidget(int x, int y, int width, int height) {
			return false;
		}
		
	}
	private class FloatFactory implements PropertyEditorFactory<Float> {
		public de.matthiasmann.twl.PropertySheet.PropertyEditor createEditor(Property<Float> property) {
			return new FloatEditor(property);
		}
	}
	private class BooleanEditor implements de.matthiasmann.twl.PropertySheet.PropertyEditor, Runnable {
		private final ToggleButton checkbox;
		private final Property<Boolean> property;
		
		public BooleanEditor(Property<Boolean> property) {
			this.property = property;
			checkbox = new ToggleButton();
			checkbox.setTheme("checkbox");
			checkbox.addCallback(this);
			valueChanged();
		}
		
		public void preDestroy() {
			checkbox.removeCallback(this);
		}
		public void valueChanged() {
			checkbox.setActive(property.getPropertyValue());
			checkbox.setEnabled(!property.isReadOnly());
		}
		
		public boolean positionWidget(int x, int y, int width, int height) {
			checkbox.setPosition(x + width / 2 - checkbox.getPreferredWidth() / 2, y);
			checkbox.setSize(checkbox.getPreferredWidth(), checkbox.getPreferredHeight());
			return true;
		}
		public Widget getWidget() {
			return checkbox;
		}
		
		public void setSelected(boolean selected) {}
		
		public void run() {
			if (!property.isReadOnly()) {
				property.setPropertyValue(checkbox.isActive());
			}
			
		}
		
	}
	private class BooleanFactory implements PropertyEditorFactory<Boolean> {
		public de.matthiasmann.twl.PropertySheet.PropertyEditor createEditor(Property<Boolean> property) {
			return new BooleanEditor(property);
		}
	}
	
}
