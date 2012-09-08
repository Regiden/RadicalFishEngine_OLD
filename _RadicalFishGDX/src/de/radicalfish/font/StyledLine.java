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
import com.badlogic.gdx.utils.Array;
import de.radicalfish.GameContainer;

/**
 * A wrapper for a list of {@link StyleCommand}s in a single line of text.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 03.09.2012
 */
public class StyledLine {
	
	/** The array containing all commands for this line. */
	public final Array<StyleCommand> commands = new Array<StyleCommand>();
	
	private final Array<StyleCommand> finish = new Array<StyleCommand>();
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Updates all commands in this list.
	 * 
	 * @param container
	 *            the container the game runs in
	 * @param delta
	 *            the time since the last frame in seconds
	 */
	public void update(GameContainer container, float delta)  {
		for (int i = 0; i < commands.size; i++) {
			commands.get(i).update(container, delta);
		}
	}
	/**
	 * Executes every {@link StyleCommand} where the given <code>charpoint</code> is equal to the charpoint of the
	 * {@link StyleCommand}.
	 * 
	 * @param container
	 *            the container the game runs in
	 * @param style
	 *            the {@link StyleInfo} to change.
	 * @param charpoint
	 *            the current character
	 */
	public void execute(GameContainer container, StyleInfo style, int charpoint)  {
		for (int i = 0; i < commands.size; i++) {
			StyleCommand sc = commands.get(i);
			if (sc.charpoint == charpoint) {
				sc.execute(container, style);
				finish.add(sc);
			}
		}
	}
	/**
	 * Calls {@link StyleCommand#finish(GameContainer)} on every command that was executed.
	 * 
	 * @param container
	 *            the container the game runs in
	 * @param style
	 *            the {@link StyleInfo} to change.
	 */
	public void finish(GameContainer container, StyleInfo style)  {
		for (int i = 0; i < finish.size; i++) {
			finish.get(i).finish(container, style);
		}
		finish.clear();
	}
	
	/**
	 * Resets all {@link StyleCommand}.
	 */
	public void reset() {
		for (int i = 0; i < commands.size; i++) {
			commands.get(i).reset();
		}
	}
	
	/**
	 * Adds the given {@link StyleCommand} to the list of commands.
	 */
	public void add(StyleCommand command) {
		commands.add(command);
	}
	/**
	 * Removes the given {@link StyleCommand} from the list of commands.
	 */
	public void remove(StyleCommand command) {
		commands.removeValue(command, true);
	}
	
}
