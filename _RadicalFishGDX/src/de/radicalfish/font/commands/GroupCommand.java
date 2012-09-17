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

// TODO fix doc for changes from repeat command to group command

/**
 * A command that executes a group of commands starting from the given charpoint. the listm ust be ordered which means
 * that
 * 
 * <pre>
 * repeat.command.get(0)
 * </pre>
 * 
 * returns the command that should be executed as first. This useful if you want to fade a set of characters or do
 * anything over a set of characters.
 * <p>
 * Also you can set a delay. Every command will start the next command in line after the delay time has passed.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 14.09.2012
 */
public class GroupCommand extends StyleCommand {
	
	/**
	 * The commands to repeat. This has the size of 1 if the {@link GroupCommand#RepeatCommand(StyleCommand, int)} was
	 * used.
	 */
	protected final Array<Container> commands;
	
	/** The time to wait to execute the next command. */
	protected final float delay;
	/** The number of characters to apply the effect. */
	protected final int characters;
	/** The current character we proceed. */
	protected int currentpoint;
	
	/**
	 * Creates a new {@link GroupCommand}.
	 * 
	 * @param commands
	 *            the commands to repeat
	 * @param charpoint
	 *            the charpoint to start
	 * @param delay
	 *            the time to wait until the next command will be started. When the first command starts the next will
	 *            take <code>delay</code> seconds before it really starts.
	 */
	public GroupCommand(final Array<StyleCommand> commands, int charpoint, float delay) {
		super(charpoint);
		
		this.commands = new Array<Container>();
		for (int i = 0; i < commands.size; i++) {
			this.commands.add(new Container(commands.get(i)));
		}
		for (int i = 0; i < this.commands.size; i++) {
			if (i + 1 < commands.size) {
				this.commands.get(i).next = this.commands.get(i + 1);
			}
		}
		this.commands.get(0).started = true;
		characters = commands.size;
		currentpoint = 0;
		this.delay = delay;
	}
	
	// OVERRIDE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void update(GameContainer container, float delta) {
		for (int i = 0; i < commands.size; i++) {
			commands.get(i).update(container, delta);
		}
		currentpoint = 0;
		
	}
	public void execute(GameContainer container, StyleInfo style) {
		commands.get(currentpoint).execute(container, style);
	}
	public void finish(GameContainer container, StyleInfo style) {
		commands.get(currentpoint).finish(container, style);
		currentpoint = (currentpoint + 1) % characters;
		
	}
	public void reset() {
		for (int i = 0; i < commands.size; i++) {
			commands.get(i).reset();
		}
		commands.get(0).started = true;
		currentpoint = 0;
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Adds a command to the end of the array.
	 */
	public void addStyleCommand(StyleCommand command) {
		commands.add(new Container(command));
		reset();
	}
	/**
	 * Removes an command from the list.
	 */
	public void remove(int index) {
		commands.removeIndex(index);
		reset();
	}
	
	public int getCharPoint() {
		return charpoint + currentpoint;
	}
	
	// INTERN CLASSES
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private class Container {
		StyleCommand command;
		Container next;
		
		boolean started = false;
		
		float time = 0;
		
		public Container(StyleCommand command) {
			this.command = command;
		}
		
		void update(GameContainer container, float delta) {
			if (!started) {
				return;
			}
			command.update(container, delta);
			
			if (next != null && !next.started) {
				time += delta;
				if (time >= delay) {
					next.started = true;
				}
			}
			
		}
		void execute(GameContainer container, StyleInfo style) {
			if (started) {
				command.execute(container, style);
			}
		}
		public void finish(GameContainer container, StyleInfo style) {
			if (started) {
				command.finish(container, style);
			}
		}
		
		public void reset() {
			command.reset();
			started = false;
			time = 0;
		}
		
	}
	
}
