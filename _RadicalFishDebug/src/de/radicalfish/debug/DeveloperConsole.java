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
import java.util.Collections;
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
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.AutoCompletionDataSource;
import de.matthiasmann.twl.model.AutoCompletionResult;
import de.matthiasmann.twl.model.SimpleAutoCompletionResult;
import de.matthiasmann.twl.textarea.HTMLTextAreaModel;

/**
 * A Console for changing values and executing methods. Every input will be logged if parsed. If not nothing will be
 * displayed. You can add {@link InputParser} to the DevConsole to parse the input and make something out of it. The
 * DevConsole itself supports some small methods too which will be always checked before anything else:
 * 
 * @author Stefan Lange
 * @version 0.0.0
 * @since 14.05.2012
 */
public class DeveloperConsole extends ResizableFrame {
	
	/** The maximum number of results for auto completion. */
	public static int MAX_AUTOCOMPLETION_RESULTS = 50;
	
	private StringBuilder lineBuffer;
	
	private ScrollPane scrollPane;
	private HTMLTextAreaModel textAreaModel;
	private TextArea textArea;
	private EditField editField;
	private Button submit;
	
	private ArrayList<InputParser> callbacks;
	
	
	public DeveloperConsole() {
		callbacks = new ArrayList<InputParser>();
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
	
	/**
	 * Removes all text from the console.
	 */
	public void flushConsole() {
		textAreaModel.setHtml("");
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
		editField.setAutoCompletion(new AutoCompleteMerger());
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
		
		Widget hseparator = new Widget();
		hseparator.setTheme("hseparator");
		textArea.registerWidget("hseparator", hseparator);
		
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
		
		addInputParser(new ConsoleInputParser());
	}
	
	private void submit() {
		String text = editField.getText();
		String old = "" + text;
		
		if (!callbacks.isEmpty()) {
			String changed = "";
			for (InputParser parser : callbacks) {
				changed = parser.parseInput(text, this);
				if (!text.equals(changed)) {
					text = changed;
					break;
				}
			}
			
		}
		appendLine(old.equals(text) ? "" : text);
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
	
	// PRIVATE CLASSES
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private class AutoCompleteMerger implements AutoCompletionDataSource {
		public AutoCompletionResult collectSuggestions(String text, int cursorPos, AutoCompletionResult prev) {
			text = text.substring(0, cursorPos);
			ArrayList<String> result = new ArrayList<String>();
			int counter = 0;
			
			for (InputParser parser : callbacks) {
				ArrayList<String> internlist = parser.getAutoCompletionContent(text);
				if (internlist != null) {
					for (String element : internlist) {
						result.add(element);
						counter++;
						if (counter >= MAX_AUTOCOMPLETION_RESULTS) {
							break;
						}
					}
				} else {
					continue;
				}
				if (counter >= MAX_AUTOCOMPLETION_RESULTS) {
					break;
				}
			}
			Collections.sort(result, String.CASE_INSENSITIVE_ORDER);
			return new SimpleAutoCompletionResult(text, 0, result);
		}
		
	}
	private static class ConsoleInputParser implements InputParser {
		
		private ArrayList<String> keys, compList;
		
		private String keywords[] = new String[] { "flushlog", "gc", "print" };
		
		public ConsoleInputParser() {
			keys = new ArrayList<String>();
			compList = new ArrayList<String>();
			for (int i = 0; i < keywords.length; i++) {
				keys.add(keywords[i]);
			}
		}
		
		public String parseInput(String message, DeveloperConsole console) {
			for (String key : keys) {
				if (message.startsWith(key)) {
					if (key.equals("gc")) {
						return gc();
					} else if (key.equals("flushlog")) {
						return flushlog(console);
					} else if (key.equals("print")) {
						if (message.length() >= 7) {
							return print(message);
						}
					}
				}
			}
			return message;
		}
		public ArrayList<String> getAutoCompletionContent(String input) {
			compList.clear();
			for (String temp : keys) {
				if (temp.regionMatches(0, input, 0, input.length())) {
					compList.add(temp);
				}
			}
			if (!compList.isEmpty()) {
				return compList;
			} else {
				return null;
			}
		}
		
		private String gc() {
			System.gc();
			return "invoked <span style=\"font:success\" >garbage collector</span>!";
		}
		private String flushlog(DeveloperConsole console) {
			console.flushConsole();
			return "";
		}
		private String print(String input) {
			return "<div style=\"word-wrap: break-word;\">" + input.substring(6)
					+ "</div><img src=\"separator\" alt=\":)\" style=\"margin: 3px; width: 100%; height: 100%;\" />";
		}
		
	}
}
