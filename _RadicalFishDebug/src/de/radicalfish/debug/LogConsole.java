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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import de.matthiasmann.twl.Alignment;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Clipboard;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.Dimension;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.ScrollPane.Fixed;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.ToggleButton;
import de.matthiasmann.twl.ValueAdjusterInt;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.HasCallback;
import de.matthiasmann.twl.model.SimpleIntegerModel;
import de.matthiasmann.twl.textarea.Style;
import de.matthiasmann.twl.textarea.StyleAttribute;
import de.matthiasmann.twl.textarea.TextAreaModel;
import de.radicalfish.debug.Logger.LOGTYPE;

/**
 * A log console which can be added to a {@link Logger} and prints all log content to a text area. That is only if the
 * <code>Log to console</code> checkbox is checked.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 16.05.2012
 */
public class LogConsole extends ResizableFrame implements LogListener {
	
	private static final Style EMPTY_STYLE = new Style();
	
	private CopyTextAreaModel textModel;
	private TextArea textArea;
	private ToggleButton checkbox;
	
	public LogConsole() {
		createPanel();
	}
	
	// INTERFACE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void logChanged(List<String> log, String lastAdded, LOGTYPE type) {
		if (checkbox.isActive()) {
			setText(log);
		}
	}
	
	// OVERRIDE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void adjustSize() {
		setSize(200, 150);
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
		setTitle("Log");
		setSize(400, 400);
		setPosition(100, 5);
		
		textModel = new CopyTextAreaModel();
		textArea = new TextArea(textModel);
		
		checkbox = new ToggleButton();
		checkbox.setTheme("checkbox");
		checkbox.setActive(true);
		
		Button clear = new Button(" Clear ");
		clear.addCallback(new Runnable() {
			public void run() {
				textModel.setText("");
				Logger.LOG.clear();
			}
		});
		Button copy = new Button("To Clipboard");
		copy.addCallback(new Runnable() {
			public void run() {
				Clipboard.setClipboard(textModel.getText());
			}
		});
		
		
		final SimpleIntegerModel model = new SimpleIntegerModel(50, 5000, 1000);
		ValueAdjusterInt adjuster = new ValueAdjusterInt(model);
		model.addCallback(new Runnable() {
			public void run() {
				Logger.setMaxLogLines(model.getValue());
				setText(Logger.LOG);
			}
		});
		adjuster.setTooltipContent("Adjust the number of lines displayed on the log console");
		
		Widget w = new Widget();
		w.setTheme("hfiller");
		
		Label labelc = new Label("Log?");
		labelc.setLabelFor(checkbox);
		Label labelv = new Label("Max Lines ");
		labelv.setLabelFor(adjuster);
		
		
		ScrollPane scrollPane = new ScrollPane(textArea);
		scrollPane.setFixed(Fixed.HORIZONTAL);
		scrollPane.setExpandContentSize(true);
		
		DialogLayout l = new DialogLayout();
		l.setTheme("logconsole");
		l.addDefaultGaps();
		l.setDefaultGap(new Dimension(5, 5));
		
		l.setHorizontalGroup(l.createParallelGroup(scrollPane).addGroup(
				l.createSequentialGroup(labelv, adjuster, w, labelc, checkbox).addGap(10).addWidgets(copy, clear).addGap(10)));
		l.setVerticalGroup(l.createSequentialGroup(scrollPane).addGroup(
				l.createParallelGroup(labelv, adjuster, w, labelc, checkbox, copy, clear)));
		l.setWidgetAlignment(clear, Alignment.RIGHT);
		
		l.setWidgetAlignment(labelc, Alignment.CENTER);
		l.setWidgetAlignment(checkbox, Alignment.CENTER);
		l.setWidgetAlignment(clear, Alignment.RIGHT);
		
		add(l);
		
	}
	private void setText(List<String> log) {
		StringBuilder sb = new StringBuilder();
		for (String s : log) {
			sb.append(s).append("\n");
		}
		textModel.setText(sb.toString());
		
	}
	
	// GETTER & SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public String getText() {
		return textModel.getText();
	}
	
	public void appendText(String text) {
		textModel.setText(textModel.getText() + text + "\n");
	}
	public void setText(String text) {
		textModel.setText(text);
	}
	
	// PRIVATE CLASSES
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	// same as SimpleTextAreaMpdel but with getter for the text
	private class CopyTextAreaModel extends HasCallback implements TextAreaModel {
		private TextElement element;
		
		public Iterator<Element> iterator() {
			return ((element != null) ? Collections.<Element> singletonList(element) : Collections.<Element> emptyList()).iterator();
		}
		
		public String getText() {
			return element.getText();
		}
		
		public void setText(String text) {
			setText(text, true);
		}
		public void setText(String text, boolean preformatted) {
			Style style = EMPTY_STYLE;
			if (preformatted) {
				style = style.with(StyleAttribute.PREFORMATTED, Boolean.TRUE).with(StyleAttribute.TAB_SIZE, 3);
			}
			element = new TextElement(style, text);
			
			doCallback();
		}
		
	}
	
}
