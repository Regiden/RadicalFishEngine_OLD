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
package de.radicalfish.font;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.badlogic.gdx.graphics.Color;
import de.radicalfish.font.commands.ColorCommand;
import de.radicalfish.util.RadicalFishException;
import de.radicalfish.util.Utils;

/**
 * A simple parser for styles. The format is easy to use. a text command must start with a [] container. A simple
 * examples which would color a character when parsed with this parser:
 * 
 * <pre>
 * String test = "This Letter is Red: [scol:1.0,0.0,0.0,1.0]R.
 * </pre>
 * 
 * This would tint the last Letter in Red. As you can see a command always starts with a name (explained in the
 * following). Then a ':' comes which tells us that now all parameters come.
 * <p>
 * All parameters must be separated by a ',' (without whitespaces).
 * <p>
 * <hr>
 * Here is a list for the supported commands:
 * <li>scol: tints a single character. 4 parameter must be given for the color as float values. eg. [scol:1,1,1,1]</li>
 * 
 * <li>col: tints all characters from the command on. 4 parameter as float values. eg. [col:1,1,1,1]</li>
 * <hr>
 * 
 * @author Stefan Lange
 * @version 0.5.0
 * @since 06.09.2012
 */
public class SimpleStyleParser implements StyleParser {
	
	/** A simple instance of the {@link SimpleStyleParser}. */
	public static final SimpleStyleParser INSTANCE = new SimpleStyleParser();
	
	private static final Pattern param = Pattern.compile(",");
	private static final Pattern lineSep = Pattern.compile("\r\n");
	private static final Pattern cp = Pattern.compile("\\[(\\w+)(?::([^,]+(?:,[^,]+?)*))?\\]");
	
	/**
	 * Contains all {@link StyleCommand}s after a {@link StyleParser#parseMultiLine(String, StyledText)} if the given
	 * {@link StyledText} was null.
	 */
	public final StyledText cachedText = new StyledText();
	/**
	 * Contains all {@link StyleCommand}s after a {@link StyleParser#parseLine(String, StyledLine)} if the given
	 * {@link StyledLine} was null.
	 */
	public final StyledLine cachedLine = new StyledLine();
	
	// METHODS
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	public String parseLine(String text, StyledLine output) {
		Utils.notNull("text", text);
		
		StyledLine con = output == null ? cachedLine : output;
		con.commands.clear();
		
		if (!text.isEmpty()) {
			text = _parse(text, con);
		}
		
		return text;
	}
	public String parseMultiLine(String text, StyledText output) {
		Utils.notNull("text", text);
		
		StyledText con = output == null ? cachedText : output;
		con.lines.clear();
		
		StringBuilder sb = new StringBuilder();
		String[] lines = lineSep.split(text);
		for (int i = 0; i < lines.length; i++) {
			StyledLine line = new StyledLine();
			
			sb.append(parseLine(lines[i], line) + lineSep.pattern());
			output.add(line);
		}
		return sb.toString();
	}
	
	// INTERN
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	private String _parse(String line, StyledLine output) {
		Matcher matcher = cp.matcher(line);
		while (matcher.find()) {
			String name;
			String command = matcher.group();
			int index = line.indexOf(command);
			line = line.replace(command, "");
			
			command = command.substring(1, command.length() - 1);
			if (command.contains(":")) {
				name = command.substring(0, command.indexOf(":"));
				command = command.substring(command.indexOf(":") + 1, command.length()).replace(" ", "");
			} else {
				name = command;
				command = "";
			}
			
			output.add(createCommand(name, command, index));
		}
		return line;
	}
	
	private StyleCommand createCommand(String name, String params, int charpoint) {
		if (name.equals("col")) {
			return createColorCommand(param.split(params), false, charpoint);
		} else if (name.equals("scol")) {
			return createColorCommand(param.split(params), true, charpoint);
		}
		throw new RadicalFishException("Could not parse command: " + name + " with paramaters: " + params + " at charpoint: " + charpoint);
	}
	private ColorCommand createColorCommand(String[] params, boolean single, int charpoint) {
		if (params.length != 4) {
			throw new RadicalFishException("Number of Parameters for color command must be 4 (r, g, b, a)");
		}
		Color c = new Color(parseFloat(params[0]), parseFloat(params[1]), parseFloat(params[2]), parseFloat(params[3]));
		return new ColorCommand(c, charpoint, single);
	}
	
	private float parseFloat(String val) {
		try {
			return Float.parseFloat(val);
		} catch (NumberFormatException e) {
			throw new RadicalFishException("Could not parse float value parameter (Maybe a parameter is not a float where it should be).");
		}
	}
	
}
