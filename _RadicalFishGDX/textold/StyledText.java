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
package de.radicalfish.text;
import java.util.ArrayList;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * Holds a list of message commands for a single line and wraps methods.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 08.12.2011
 */
public class StyledText {
	
	/** the list of commands. */
	public ArrayList<StyleCommand> commands;
	
	private ArrayList<StyleCommand> reverseCommands;
	
	/**
	 * Creates a new {@link StyledText} for holding commands.
	 */
	public StyledText() {
		commands = new ArrayList<StyleCommand>();
		reverseCommands = new ArrayList<StyleCommand>();
	}
	
	/**
	 * Adds a command to the list.
	 * 
	 * @param command
	 *            the command to add
	 */
	public void addCommand(StyleCommand command) {
		commands.add(command);
	}
	/**
	 * Updates the commands in this line
	 * 
	 * @param delta
	 *            the since the last frame.
	 * @throws SlickException
	 */
	public void update(int delta) throws SlickException {
		for (StyleCommand command : commands)
			command.update(delta);
	}
	/**
	 * execute the commands at <code>charPoint</code> if there are any commands to execute.
	 * 
	 * @param charPoint
	 *            the point to check
	 * @param g
	 *            the graphic context on which the commands should be applied
	 * @throws SlickException
	 */
	public void execute(int charPoint, Graphics g) throws SlickException {
		for (StyleCommand command : commands) {
			if (command.getCharPoint() == charPoint) {
				command.execute(g);
				reverseCommands.add(command);
			}
			
		}
	}
	/**
	 * Calls reverse() on the last executed commands.
	 * 
	 * @param g
	 *            the graphic context on which the command should be reversed
	 * @throws SlickException
	 */
	public void reverse(Graphics g) throws SlickException {
		for (StyleCommand command : reverseCommands) {
			command.reverse(g);
		}
		reverseCommands.clear();
	}
	/**
	 * Call reset() on all commands.
	 */
	public void reset() {
		for (StyleCommand command : commands)
			command.reset();
	}

}
