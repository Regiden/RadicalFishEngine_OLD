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
package de.radicalfish.text;
import com.badlogic.gdx.utils.Array;
import de.radicalfish.GameContainer;
import de.radicalfish.util.RadicalFishException;

/**
 * A wrapper for a list of {@link StyledLine}s in a multi line text. the {@link StyledLine}s must be added in the order
 * of the lines.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 03.09.2012
 */
public class StyledText {
	/** The array containing all commands for this line. */
	public final Array<StyledLine> commands = new Array<StyledLine>();
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Updates the current line.
	 * 
	 * @param container
	 *            the container the game runs in
	 * @param delta
	 *            the time since the last frame in seconds
	 * @param line
	 *            the line index of the current line
	 * @throws RadicalFishException
	 */
	public void update(GameContainer container, float delta, int line) throws RadicalFishException {
		commands.get(line).update(container, delta);
	}
	/**
	 * Calls {@link StyledLine#execute(GameContainer, StyleInfo, int)} on the current line.
	 * 
	 * @param container
	 *            the container the game runs in
	 * @param style
	 *            the {@link StyleInfo} to change.
	 * @param line
	 *            the current line index.
	 * @param charpoint
	 *            the current character
	 * @throws RadicalFishException
	 */
	public void execute(GameContainer container, StyleInfo style, int line, int charpoint) throws RadicalFishException {
		commands.get(line).execute(container, style, charpoint);
	}
	/**
	 * Calls {@link StyledLine#finish(GameContainer)} on the current line.
	 * 
	 * @param container
	 *            the container the game runs in
	 * @param style
	 *            the {@link StyleInfo} to change.
	 * @param line
	 *            the index of the current line.
	 * @throws RadicalFishException
	 */
	public void finish(GameContainer container, StyleInfo style, int line) throws RadicalFishException {
		commands.get(line).finish(container, style);
	}
	
	/**
	 * Resets all {@link StyledLine}.
	 */
	public void reset() {
		for (int i = 0; i < commands.size; i++) {
			commands.get(i).reset();
		}
	}
	
	/**
	 * Adds the given {@link StyledLine} to the list of commands.
	 */
	public void add(StyledLine command) {
		commands.add(command);
	}
	/**
	 * Removes the given {@link StyledLine} from the list of commands.
	 */
	public void remove(StyledLine command) {
		commands.removeValue(command, true);
	}
	
}
