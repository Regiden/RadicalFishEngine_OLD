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
package de.radicalfish.twl;

import de.matthiasmann.twl.PropertySheet.PropertyEditor;
import de.matthiasmann.twl.PropertySheet.PropertyEditorFactory;
import de.matthiasmann.twl.ToggleButton;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.Property;

public class TWLPropertyFactory {
	
	 public static PropertyEditorFactory<Boolean> createBooleanFactory() {
		 return new BooleanEditorFactory();
	 }
	
	// INTERN CLASSES
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	static class BooleanEditor implements PropertyEditor, Runnable {
		
		private final ToggleButton checkBox;
		private final Property<Boolean> property;
		
		public BooleanEditor(Property<Boolean> property) {
			this.property = property;
			checkBox = new ToggleButton();
			checkBox.setTheme("checkbox");
			checkBox.addCallback(this);
			resetValue();
		}
		
		public void run() {
			if(!property.isReadOnly()) {
                try {
                    property.setPropertyValue(checkBox.isActive());
                } catch (IllegalArgumentException ex) {
                	property.setPropertyValue(false);
                }
            }
		}
		
		@Override
		public Widget getWidget() {
			return checkBox;
		}
		@Override
		public void valueChanged() {
			 resetValue();
		}
		
		@Override
		public void preDestroy() {
			checkBox.removeCallback(this);
		}
		
		@Override
		public void setSelected(boolean selected) {
			
		}
		private void resetValue() {
			checkBox.setText(null);
		}
		
	}
	static class BooleanEditorFactory implements PropertyEditorFactory<Boolean> {
		public PropertyEditor createEditor(Property<Boolean> property) {
			return new BooleanEditor(property);
		}
	}
}
