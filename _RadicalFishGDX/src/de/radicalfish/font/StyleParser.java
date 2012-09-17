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
import com.badlogic.gdx.utils.Array;
import de.radicalfish.font.commands.ColorCommand;
import de.radicalfish.font.commands.FadeCommand;
import de.radicalfish.font.commands.GroupCommand;
import de.radicalfish.font.commands.MoveCommand;
import de.radicalfish.font.commands.RepeatCommand;
import de.radicalfish.font.commands.ResetCommand;
import de.radicalfish.font.commands.ResetCommand.RESET;
import de.radicalfish.font.commands.StyleCommand;
import de.radicalfish.state.transitions.FadeTransition.FADE;
import de.radicalfish.util.RadicalFishException;
import de.radicalfish.util.Utils;

/**
 * A simple text parser for styles. The format is easy to use. a text command must start with a [] container. A simple
 * examples which would color a character when parsed with this parser:
 * 
 * <pre>
 * String test = "This Letter is Red: [sc:1.0,0.0,0.0,1.0]R.
 * </pre>
 * 
 * This would tint the last Letter in Red. As you can see a command always starts with a name (explained in the
 * following). Then a ':' comes which tells us that now all parameters come.
 * <p>
 * All parameters must be separated by a ',' (without whitespace).
 * <p>
 * <hr>
 * Here is a list for the supported commands:
 * <li>sc: tints a single character. 4 parameter must be given for the color as float values. eg. [sc:1,1,1,1]</li>
 * <li>co: tints all characters from the command on. 4 parameter as float values. eg. [co:1,1,1,1]</li>
 * <li>x: resets the state of the {@link StyleInfo} used for transformation
 * <ul>
 * <li>[x:color] to reset the color in all 4 corners (to white)</li>
 * <li>[x:all] for alpha value of the color in all 4 corners (to 1.0f)</li>
 * <li>[x:geom] for the size,rotation etc</li>
 * <li>[x:alpha] to reset all</li>
 * </ul>
 * <li>fd: fades a set of characters. 3 Parameters:
 * <ul>
 * <li>first: duration of the fade in seconds</li>
 * <li>second: type of fade ('in' or 'out'), if wrong 'out' will be taken
 * <li>example: [fd:2.0,out] fades out the character within 2 seconds.</li>
 * </ul>
 * <li>gp: groups a set of commands. 3 Parameters:
 * <ul>
 * <li>first: a command in a like other commands but in a () container.</li>
 * <li>second: delay for each command to execute. (eg a fade effect where one char after another appears)</li>
 * <li>third: number of characters to apply the effect.</li>
 * <li>example: [gp:(fd:1.0,out),0.5, 4] fades 4 char in 1 second with a 0.5 delay for each char.</li>
 * </ul>
 * </li>
 * <li>rp: repeats a set of commands. 2 Parameters:
 * <ul>
 * <li>first: a command in a like other commands but in a () container.</li>
 * <li>second: number of characters to apply the effect.</li>
 * <li>example: [rp:(fd:1.0,out), 4] fades 4 char in 1 second.</li>
 * </ul>
 * </li>
 * <li>sm: moves a character to a offset. 3 Parameters:
 * <ul>
 * <li>first: the x offset to move to.</li>
 * <li>second: the y offset to move to.</li>
 * <li>third: the time the move takes. In seconds.</li>
 * <li>example: [sm:10,10,2] moves a single character to a offset of 10,10 relative to the characters position.</li>
 * </ul>
 * </li>
 * <li>mv: moves all characters to a offset. 3 Parameters:
 * <ul>
 * <li>first: the x offset to move to.</li>
 * <li>second: the y offset to move to.</li>
 * <li>third: the time the move takes. In seconds.</li>
 * <li>example: [sv:10,10,2] moves all character to a offset of 10,10 relative to the characters positions.</li>
 * </ul>
 * </li> </li>
 * <hr>
 * 
 * @author Stefan Lange
 * @version 0.6.0
 * @since 06.09.2012
 */
public class StyleParser {
	
	/** A simple instance of the {@link StyleParser}. */
	public static final StyleParser INSTANCE = new StyleParser();
	
	// lookahead so nested brackets should work (SHOULD not sure I suck an regex)
	private static final Pattern param = Pattern.compile(",(?![^()]*+\\))");
	// should work on unix system that way
	private static final Pattern lineSep = Pattern.compile("\r?\n");
	// version 3.0 hopefully covers all needs
	private static final Pattern cp = Pattern.compile("\\[(\\w+)(?::([^,\\]]+(?:,[^,\\]]+?)*))?\\]");
	
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
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
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
			sb.append(parseLine(lines[i], line) + "\n");
			output.add(line);
		}
		return sb.toString();
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
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
		if (name.equals("co")) {
			return createColorCommand(param.split(params), false, charpoint);
		} else if (name.equals("sc")) {
			return createColorCommand(param.split(params), true, charpoint);
		} else if (name.equals("fd")) {
			return createFadeCommand(param.split(params), charpoint);
		} else if (name.equals("x")) {
			return createResetCommand(param.split(params), charpoint);
		} else if (name.equals("gp")) {
			return createGroupCommand(param.split(params), charpoint);
		} else if (name.equals("rp")) {
			return createRepeatCommand(param.split(params), charpoint);
		} else if (name.equals("mv")) {
			return createMoveCommand(param.split(params), charpoint, false);
		} else if (name.equals("sm")) {
			return createMoveCommand(param.split(params), charpoint, true);
		} 
		throw new RadicalFishException("Could not parse command: " + name + " with paramaters: " + params + " at charpoint: " + charpoint);
	}
	private StyleCommand createColorCommand(String[] params, boolean single, int charpoint) {
		if (params.length != 4) {
			throw new RadicalFishException("Number of Parameters for color/scolor (co/sc) command must be 4 (r, g, b, a)");
		}
		Color c = new Color(parseFloat(params[0]), parseFloat(params[1]), parseFloat(params[2]), parseFloat(params[3]));
		return new ColorCommand(c, charpoint, single);
	}
	private StyleCommand createFadeCommand(String[] params, int charpoint) {
		if (params.length != 2) {
			throw new RadicalFishException("Number of Parameters for fade (fd) command must be 2 (duration, type)");
		}
		
		float duration = parseFloat(params[0]);
		if (!params[1].equals("in") && !params[1].equals("out")) {
			throw new RadicalFishException("second parameter of fade command is not valid (must be in or out) given: " + params[1]);
		}
		boolean type = params[1].equals("in");
		
		return new FadeCommand(type ? FADE.IN : FADE.OUT, duration, charpoint);
	}
	private StyleCommand createResetCommand(String[] params, int charpoint) {
		if (params.length != 1) {
			throw new RadicalFishException("Number of Parameters for reset (x) command must be 1 (type)");
		}
		RESET res = null;
		try {
			res = RESET.valueOf(params[0].toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new RadicalFishException("the given reset type is not in the enum: '" + params[0] + "'");
		}
		return new ResetCommand(res, charpoint);
		
	}
	private StyleCommand createGroupCommand(String[] params, int charpoint) {
		if (params.length != 3) {
			throw new RadicalFishException("Number of Parameters for group command must be 3 (command, delay, number of characters)");
		}
		
		String name;
		String command = params[0].substring(1, params[0].length() - 1);
		if (command.contains(":")) {
			name = command.substring(0, command.indexOf(":"));
			command = command.substring(command.indexOf(":") + 1, command.length()).replace(" ", "");
		} else {
			name = command;
			command = "";
		}
		
		float delay = parseFloat(params[1]);
		int times = parseInt(params[2]);
		
		Array<StyleCommand> c = new Array<StyleCommand>();
		for (int i = 0; i < times; i++) {
			c.add(createCommand(name, command, charpoint));
		}
		
		return new GroupCommand(c, charpoint, delay);
	}
	private StyleCommand createRepeatCommand(String[] params, int charpoint) {
		if (params.length != 2) {
			throw new RadicalFishException("Number of Parameters for repeat (rp) command must be 2 (command, number of characters)");
		}
		
		String name;
		String command = params[0].substring(1, params[0].length() - 1);
		if (command.contains(":")) {
			name = command.substring(0, command.indexOf(":"));
			command = command.substring(command.indexOf(":") + 1, command.length()).replace(" ", "");
		} else {
			name = command;
			command = "";
		}
		
		int times = parseInt(params[1]);
		
		return new RepeatCommand(createCommand(name, command, charpoint), charpoint, times);
	}
	private StyleCommand createMoveCommand(String[] params, int charpoint, boolean single) {
		if (params.length != 3) {
			throw new RadicalFishException("Number of Parameters for move/smove (mv/sm) command must be 3 (x, y, time)");
		}
		
		float x = parseFloat(params[0]);
		float y = parseFloat(params[1]);
		float time = parseFloat(params[2]);
		return new MoveCommand(x, y, charpoint, time, single);
	}
	
	private int parseInt(String val) {
		try {
			return Integer.parseInt(val);
		} catch (NumberFormatException e) {
			throw new RadicalFishException("Could not parse int value parameter (Maybe a parameter is not a int where it should be).");
		}
	}
	private float parseFloat(String val) {
		try {
			return Float.parseFloat(val);
		} catch (NumberFormatException e) {
			throw new RadicalFishException("Could not parse float value parameter (Maybe a parameter is not a float where it should be).");
		}
	}
	
}
