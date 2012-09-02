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
package de.radicalfish.debug.parser;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;
import de.radicalfish.context.Settings;
import de.radicalfish.debug.DeveloperConsole;
import de.radicalfish.debug.InputParser;

/**
 * Property parser for the {@link DeveloperConsole}. To change a property use this pattern:
 * 
 * <pre>
 * set name.of.property value
 * 
 * e.g.
 * set debug.hotboxes true
 * </pre>
 * 
 * The parser checks on the keyword <b>set</b> and the splits the input to obtain the key and value of the property. If
 * the property can't be found an error message will be printed to the log screen.
 * 
 * @author Stefan Lange
 * @version 0.5.0
 * @since 14.05.2012
 */
public class PropertyInputParser implements InputParser {
	
	private static Pattern split = Pattern.compile(" ");
	
	private Settings settings;
	private ArrayList<String> keys, compList;
	
	public PropertyInputParser(Settings settings) {
		this.settings = settings;
		
		keys = new ArrayList<String>();
		compList = new ArrayList<String>();
		Iterator<String> set = settings.getAll().keySet().iterator();
		
		while (set.hasNext()) {
			keys.add(set.next());
		}
		
		// add extras
		keys.add("log");
		keys.add("debugging");
	}
	
	// INTERFACE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public String parseInput(String message, DeveloperConsole console) {
		if (message.startsWith("set")) {
			String pair[] = split.split(message);
			if (pair.length == 3) {
				String temp = checkDefaultSettings(pair[1], pair[2]);
				
				if (temp.equals("")) {
					if (settings.contains(pair[1])) {
						settings.setProperty(pair[1], pair[2]);
						return makeSuccessMessage(pair[1], "" + pair[2]);
					} else {
						return makeErrorMessage("No such Property: " + pair[1]);
					}
				} else {
					return temp;
				}
			} else {
				return makeErrorMessage("Invalid parameter length. Use the format: \"set propertyname value\"");
			}
		}
		return message;
	}
	public ArrayList<String> getAutoCompletionContent(String input) {
		compList.clear();
		if ("set".regionMatches(true, 0, input, 0, input.length())) {
			compList.add("set");
			return compList;
		}
		if (input.startsWith("set ")) {
			input = input.replace("set ", "");
			for (String temp : keys) {
				if (temp.regionMatches(0, input, 0, input.length())) {
					compList.add("set " + temp);
				}
			}
			if (!compList.isEmpty()) {
				return compList;
			}
		}
		return null;
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private String checkDefaultSettings(String name, String value) {
		if (name.equals("fullscreen")) {
			settings.setFullscreen(value.equals("true"));
			return makeSuccessMessage(name, "" + value.equals("true"));
		}
		if (name.equals("logging") || name.equals("log")) {
			settings.setLogging(value.equals("true"));
			return makeSuccessMessage(name, "" + value.equals("true"));
		}
		if (name.equals("debugging") || name.equals("debug")) {
			settings.setDebugging(value.equals("true"));
			return makeSuccessMessage(name, "" + value.equals("true"));
		}
		if (name.equals("vsync")) {
			settings.setVSync(value.equals("true"));
			return makeSuccessMessage(name, "" + value.equals("true"));
		}
		if (name.equals("smootdelta")) {
			settings.setSmoothDelta(value.equals("true"));
			return makeSuccessMessage(name, "" + value.equals("true"));
		}
		if (name.toLowerCase().equals("sound3d")) {
			settings.setSound3D(value.equals("true"));
			return makeSuccessMessage(name, "" + value.equals("true"));
		}
		
		if (name.equals("music")) {
			float temp = castFloat(value);
			
			if (temp < 0) {
				return makeErrorMessage("could not parse value to float: " + value);
			} else {
				settings.setMusicVolume(temp);
				return makeSuccessMessage(name, value);
			}
			
		}
		if (name.equals("sound")) {
			float temp = castFloat(value);
			
			if (temp < 0) {
				return makeErrorMessage("could not parse value to float: " + value);
			} else {
				settings.setSoundVolume(temp);
				return makeSuccessMessage(name, value);
			}
			
		}
		return "";
	}
	
	private float castFloat(String value) {
		float temp = -1;
		try {
			temp = Float.parseFloat(value);
		} catch (NumberFormatException e) {}
		return temp;
	}
	
	private String makeErrorMessage(String error) {
		return "<div style=\"font:error\" >" + error + "</div>";
	}
	private String makeSuccessMessage(String prop, String value) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Assigned '");
		sb.append("<span style=\"font:success\" >").append(prop).append("</span>");
		sb.append("' with '");
		sb.append("<span style=\"font:success\" >").append(value).append("</span>'");
		
		return sb.toString();
	}
	
}