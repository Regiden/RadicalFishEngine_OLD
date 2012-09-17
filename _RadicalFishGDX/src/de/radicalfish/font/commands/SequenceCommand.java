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
import de.radicalfish.util.Utils;

/**
 * A command which executes a number of {@link TimeCommand} one after another. the <code>charpoint</code> of this class
 * will be ignored.
 * <p>
 * You can loop are sequence and even ping-pong it (Set the behavior in the C'Tor).
 * <p>
 * Note that the last command will not be updated anymore if it's done. It will still be executed tho :D
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 17.09.2012
 */
public class SequenceCommand extends StyleCommand {
	
	/** The commands we execute one after another. */
	protected final Array<TimeCommand> commands = new Array<TimeCommand>();
	
	/** The index of the current command. */
	protected int current;
	
	/** True if we loop the sequence, pingpong overrides this behavior. */
	public boolean loops;
	/** True if the sequence should be "pingpong"-ed. */
	public boolean pingpong;
	
	private int direction;
	
	private boolean stopped = false;
	
	/**
	 * Creates a new {@link SequenceCommand} from a list of commands (may contain nothing).
	 */
	public SequenceCommand(Array<TimeCommand> commands, int charpoint) {
		this(commands, charpoint, false, false);
	}
	/**
	 * Creates a new {@link SequenceCommand} from a list of commands (may contain nothing).
	 */
	public SequenceCommand(Array<TimeCommand> commands, int charpoint, boolean loops, boolean pingpong) {
		super(charpoint);
		Utils.notNull("commands", commands);
		commands.addAll(commands, 0, commands.size);
		this.loops = loops;
		this.pingpong = pingpong;
	}
	
	// OVERRIDE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void update(GameContainer container, float delta) {
		if (commands.size == 0 || stopped) {
			return;
		}
		commands.get(current).update(container, delta);
		while (commands.get(current).isDone() && !stopped) {
			if ((current == commands.size - 1) && (!loops) && (!pingpong)) {
				stopped = true;
				break;
			}
			current = (current + direction) % commands.size;
			if (pingpong) {
				if (current <= 0) {
					current = 0;
					direction = 1;
					if (!loops) {
						stopped = true;
						break;
					}
				} else if (current >= commands.size - 1) {
					current = commands.size - 1;
					direction = -1;
				}
			}
		}
	}
	public void execute(GameContainer container, StyleInfo style) {
		if (commands.size == 0) {
			return;
		}
		commands.get(current).execute(container, style);
	}
	public void finish(GameContainer container, StyleInfo style) {
		if (commands.size == 0) {
			return;
		}
		commands.get(current).finish(container, style);
	}
	public void reset() {
		stopped = false;
		for (int i = 0; i < commands.size; i++) {
			commands.get(i).reset();
		}
		current = 0;
	}
	
	public int getCharPoint() {
		if (commands.size == 0) {
			return super.getCharPoint();
		}
		return commands.get(current).getCharPoint();
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Adds a {@link TimeCommand} to the sequence.
	 */
	public void addCommand(TimeCommand command) {
		commands.add(command);
	}
	/**
	 * Removes a {@link TimeCommand} from the sequence.
	 */
	public void removeCommand(int index) {
		commands.removeIndex(index);
	}
	
	/**
	 * @return True if the sequence has been stopped, falser if loops or pingpong is true
	 */
	public boolean isStopped() {
		return stopped;
	}
	
}
