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
package de.radicalfish.font.commands;
import com.badlogic.gdx.utils.Array;
import de.radicalfish.GameContainer;
import de.radicalfish.font.StyleInfo;

/**
 * A command which executes all commands added in the parallel group. Note that only the <code>charpoint</code> set in
 * this class will be used. All added commands will be executed when the charpoint as been reached.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 13.09.2012
 */
public class ParallelCommand extends StyleCommand {
	
	/** The commands to execute parallel. */
	protected final Array<StyleCommand> commands = new Array<StyleCommand>();
	
	/**
	 * Creates a new {@link ParallelCommand}.
	 * 
	 * @param charpoint
	 *            the point at chich to start the parallel group.
	 */
	public ParallelCommand(int charpoint) {
		super(charpoint);
	}
	
	// OVERRIDE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void update(GameContainer container, float delta) {
		for (int i = 0; i < commands.size; i++) {
			commands.get(i).update(container, delta);
		}
	}
	public void execute(GameContainer container, StyleInfo style) {
		for (int i = 0; i < commands.size; i++) {
			commands.get(i).execute(container, style);
		}
	}
	public void finish(GameContainer container, StyleInfo style) {
		for (int i = 0; i < commands.size; i++) {
			commands.get(i).finish(container, style);
		}
	}
	public void reset() {
		for (int i = 0; i < commands.size; i++) {
			commands.get(i).reset();
		}
		
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Adds a command to the parallel group.
	 */
	public void addCommand(StyleCommand command) {
		commands.add(command);
	}
	/**
	 * Removes a command from the parallel group.
	 */
	public void removeCommand(int index) {
		commands.removeIndex(index);
	}
	/**
	 * Removes a command from the parallel group.
	 */
	public void removeCommand(StyleCommand command) {
		commands.removeValue(command, true);
	}
}
