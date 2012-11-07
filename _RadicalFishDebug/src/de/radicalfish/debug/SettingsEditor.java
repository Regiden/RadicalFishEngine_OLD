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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.PropertySheet;
import de.matthiasmann.twl.PropertySheet.PropertyEditor;
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
 * Editor for properties. It uses an Implementation of the {@link Settings} Interface. The supported types are
 * <code>boolean</code>, <code>float</code>, <code>int</code>  <code>String</code>.
 * <p>
 * You can arrange you properties in sublists if they have a separator (default is '.'). The following examples
 * shows this in detail:
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
public class SettingsEditor extends ResizableFrame {
	
	private PropertySheet propertySheet;
	private Settings settings;
	
	private ArrayList<String> parentList;
	
	private char separator;
	
	/**
	 * Creates a PropertyEditor to use for the TWL Theme Editor. It uses subgroups and the "." as separator.
	 * <p>
	 * Nothing happes here because no settings are given. It is still usefull for debugging and as preview in the Theme
	 * Editor.
	 */
	public SettingsEditor() {
		this(null);
	}
	/**
	 * Creates a PropertyEditor with a settings implementation to use. It uses subgroups and the "." as separator.
	 */
	public SettingsEditor(Settings settings) {
		this(settings, '.');
	}
	/**
	 * Creates a PropertyEditor with a settings implementation to use and a character to use as separator for subgroups.
	 */
	public SettingsEditor(Settings settings, char separator) {
		this.settings = settings;
		this.separator = separator;
		parentList = new ArrayList<String>();
		createPanel();
	}
	
	// OVERRIDE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	protected void paintWidget(GUI gui) {
		updateSettings(propertySheet.getPropertyList(), parentList);
		super.paintWidget(gui);
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void updateSettings(SimplePropertyList list, List<String> parents) {
		for (int i = 0; i < list.getNumProperties(); i++) {
			Property<?> property = list.getProperty(i);
			if (property instanceof SimplePropertyList) {
				String parent = property.getName();
				parents.add(parent);
				updateSettings((SimplePropertyList) property, parents);
				parents.remove(parent);
			} else {
				String name = concatStringList(parentList, property.getName());
				if (checkBoolean(name, property.getPropertyValue().toString(), property)) {
					continue;
				}
				if (checkInt(name, property.getPropertyValue().toString(), property)) {
					continue;
				} else if (checkFloat(name, property.getPropertyValue().toString(), property)) {
					continue;
				}
			}
			
		}
		
	}
	private String concatStringList(List<String> list, String property) {
		String string = "";
		for (String s : list) {
			string += s + separator;
		}
		return string + property;
	}
	
	private void createPanel() {
		setTheme("resizableframe-title");
		setResizableAxis(ResizableAxis.BOTH);
		addCloseCallback(new Runnable() {
			public void run() {
				setVisible(false);
			}
		});
		setTitle("Settings Editor");
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
	
	private void loadSettings() {
		if (settings == null) {
			return;
		}
		
		List<String> keys = new ArrayList<String>();
		Map<String, ?> map = settings.getAllSettings();
		Iterator<String> set = map.keySet().iterator();
		
		while (set.hasNext()) {
			keys.add(set.next());
		}
		
		Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
		for (String name : keys) {
			addProperty(name, map.get(name).toString());
		}
		
	}
	private void addProperty(final String name, String value) {
		checkSubList(name, name, value, null, propertySheet.getPropertyList());
	}
	private Property<?> _addProperty(String name, final String original, String value) {
		Property<?> prop = null;
		if ((prop = tryBoolean(name, original, value)) != null) {
			return prop;
		} else if ((prop = tryInteger(name, original, value)) != null) {
			return prop;
		} else if ((prop = tryFloat(name, original, value)) != null) {
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
	
	private boolean checkBoolean(String name, String value, Property<?> property) {
		if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
			@SuppressWarnings("unchecked")
			SimpleProperty<Boolean> pr = (SimpleProperty<Boolean>) property;
			pr.setPropertyValue(settings.getProperty(name, false));
			return true;
		}
		return false;
	}
	private boolean checkInt(String name, String value, Property<?> property) {
		try {
			Integer val = Integer.parseInt(value);
			if (val != null) {
				@SuppressWarnings("unchecked")
				SimpleProperty<Integer> pr = (SimpleProperty<Integer>) property;
				
				pr.setPropertyValue(settings.getProperty(name, 0));
				return true;
			} else {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
	}
	private boolean checkFloat(String name, String value, Property<?> property) {
		try {
			Float val = Float.parseFloat(value);
			if (val != null) {
				@SuppressWarnings("unchecked")
				SimpleProperty<Float> pr = (SimpleProperty<Float>) property;
				pr.setPropertyValue(settings.getProperty(name, 0.0f));
				
				return true;
			} else {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
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
	private Property<?> tryInteger(String name, final String original, String value) {
		try {
			Integer val = Integer.parseInt(value);
			final SimpleProperty<Integer> prop = new SimpleProperty<Integer>(Integer.class, name, val);
			prop.addValueChangedCallback(new Runnable() {
				public void run() {
					settings.setProperty(original, prop.getPropertyValue().toString());
				}
			});
			return prop;
		} catch (NumberFormatException e) {
			return null;
		}
	}
	private Property<?> tryFloat(final String name, final String original, String value) {
		try {
			Float val = Float.parseFloat(value);
			final SimpleProperty<Float> prop = new SimpleProperty<Float>(Float.class, name, val);
			prop.addValueChangedCallback(new Runnable() {
				public void run() {
					settings.setProperty(original, prop.getPropertyValue().toString());
				}
			});
			return prop;
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	// INTERN CLASSES
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public static class IntegerEditor implements PropertyEditor, Runnable {
		
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
			adjuster.setPosition(x + 2, y);
			adjuster.setSize(width, height);
			return true;
		}
		
	}
	public static class IntegerFactory implements PropertyEditorFactory<Integer> {
		public PropertyEditor createEditor(Property<Integer> property) {
			return new IntegerEditor(property);
		}
	}
	public static class FloatEditor implements PropertyEditor, Runnable {
		
		private final Property<Float> property;
		private final ValueAdjusterFloat adjuster;
		private final SimpleFloatModel model;
		
		public FloatEditor(Property<Float> property) {
			this.property = property;
			model = new SimpleFloatModel(0.0f, Float.MAX_VALUE, property.getPropertyValue());
			adjuster = new ValueAdjusterFloat(model);
			adjuster.setStepSize(0.1f);
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
			adjuster.setPosition(x + 2, y);
			adjuster.setSize(width - 2, height);
			return true;
		}
		
	}
	public static class FloatFactory implements PropertyEditorFactory<Float> {
		public PropertyEditor createEditor(Property<Float> property) {
			return new FloatEditor(property);
		}
	}
	public static class BooleanEditor implements PropertyEditor, Runnable {
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
	public static class BooleanFactory implements PropertyEditorFactory<Boolean> {
		public PropertyEditor createEditor(Property<Boolean> property) {
			return new BooleanEditor(property);
		}
	}
	
}
