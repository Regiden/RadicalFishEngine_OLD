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
import org.lwjgl.Sys;
import de.matthiasmann.twl.Alignment;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.Dimension;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.textarea.HTMLTextAreaModel;
import de.radicalfish.context.Settings;
import de.radicalfish.debug.parser.PropertyInputParser;
import de.radicalfish.debug.parser.URLInputParser;

/**
 * A Console for changing values and executing methods. if you submit you input the DeveloperCallback will be called (if
 * set). You can return the same message as teh input was to forward to the internal parser of this class. This makes
 * links visible if their fit in the design of an URL (e.g. www.xyz.de or http://xyz.de).
 * 
 * @author Stefan Lange
 * @version 0.0.0
 * @since 14.05.2012
 */
public class DevConsole extends ResizableFrame {
	
	private StringBuilder lineBuffer;
	
	private ScrollPane scrollPane;
	private HTMLTextAreaModel textAreaModel;
	private TextArea textArea;
	private EditField editField;
	private Button submit;
	
	private ArrayList<InputParser> callbacks;
	
	private boolean addedPropertyParser;
	
	public DevConsole() {
		callbacks = new ArrayList<InputParser>();
		
		addedPropertyParser = false;
		
		createPanel();
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Adds an {@link InputParser}.
	 * 
	 * @param parser
	 *            the parser to add
	 */
	public void addInputParser(InputParser parser) {
		if (parser == null) {
			throw new NullPointerException("parser is null!");
		}
		callbacks.add(parser);
	}
	/**
	 * Removes and {@link InputParser}
	 * 
	 * @param parser
	 *            the parser to remove
	 */
	public void removeInputParser(InputParser parser) {
		if (parser == null) {
			throw new NullPointerException("parser is null!");
		}
		callbacks.remove(parser);
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void createPanel() {
		setTheme("devconsole-frame");
		setResizableAxis(ResizableAxis.BOTH);
		addCloseCallback(new Runnable() {
			public void run() {
				setVisible(false);
			}
		});
		setTitle("Developer Console");
		setSize(300, 300);
		setPosition(5, 100);
		
		lineBuffer = new StringBuilder();
		
		editField = new EditField();
		editField.addCallback(new EditField.Callback() {
			public void callback(int key) {
				if (key == Event.KEY_RETURN) {
					submit();
				}
			}
		});
		textAreaModel = new HTMLTextAreaModel();
		textArea = new TextArea(textAreaModel);
		textArea.addCallback(new TextArea.Callback() {
			public void handleLinkClicked(String href) {
				Sys.openURL(href);
			}
		});
		textArea.setCanAcceptKeyboardFocus(false);
		
		scrollPane = new ScrollPane(textArea);
		scrollPane.setFixed(ScrollPane.Fixed.HORIZONTAL);
		scrollPane.setExpandContentSize(true);
		
		submit = new Button(" Submit ");
		submit.addCallback(new Runnable() {
			public void run() {
				submit();
			}
		});
		submit.setCanAcceptKeyboardFocus(false);
		
		DialogLayout l = new DialogLayout();
		l.setTheme("devconsole");
		l.addDefaultGaps();
		l.setDefaultGap(new Dimension(5, 5));
		l.setHorizontalGroup(l.createParallelGroup(scrollPane).addGroup(
				l.createSequentialGroup(editField).addGap(10).addWidget(submit).addGap(10)));
		l.setVerticalGroup(l.createSequentialGroup(scrollPane).addGroup(l.createParallelGroup(editField).addWidget(submit, Alignment.RIGHT)));
		l.setWidgetAlignment(submit, Alignment.RIGHT);
		
		add(l);
		
	}
	
	private void submit() {
		String text = editField.getText();
		
		if (!callbacks.isEmpty()) {
			String changed = "";
			for (InputParser parser : callbacks) {
				changed = parser.parseInput(text);
				if (!text.equals(changed)) {
					text = changed;
					break; // something parsed ignore other callbacks
				}
			}
			
		}
		
		appendLine(text);
		editField.setText("");
	}
	private void appendLine(String line) {
		if (line.isEmpty())
			return;
		lineBuffer.append("<div style=\"word-wrap: break-word \">");
		lineBuffer.append(line);
		lineBuffer.append("</div>");
		
		boolean isAtEnd = scrollPane.getMaxScrollPosY() == scrollPane.getScrollPositionY();
		
		textAreaModel.setHtml(lineBuffer.toString());
		
		if (isAtEnd) {
			scrollPane.validateLayout();
			scrollPane.setScrollPositionY(scrollPane.getMaxScrollPosY());
		}
	}
}
