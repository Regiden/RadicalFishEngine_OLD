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
import de.radicalfish.GameContainer;
import de.radicalfish.font.StyleInfo;

/**
 * A command which repeats a command of a set of characters. This can be used to eg. only fade one word and not more.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 16.09.2012
 */
public class RepeatCommand extends StyleCommand {
	
	/** The {@link StyleCommand} to use. */
	protected final StyleCommand repeat;
	
	/** he number of characters to apply the command on. */
	protected final int characters;
	
	/** the current point (0 for the first and <code>characters</code> -1 for the max)*/
	protected int currentpoint;
	
	/**
	 * Creates a new {@link RepeatCommand}.
	 * 
	 * @param repeat
	 *            the {@link StyleCommand} to use
	 * @param charpoint
	 *            the charpoint to start.
	 * @param characters
	 *            the number of characters to apply this command on
	 */
	public RepeatCommand(StyleCommand repeat, int charpoint, int characters) {
		super(charpoint);
		this.repeat = repeat;
		this.characters = characters;
	}
	
	// OVERRIDE
	// ��������������������������������������������������������������������������������������������
	public void update(GameContainer container, float delta) {
		repeat.update(container, delta);
	}
	public void execute(GameContainer container, StyleInfo style) {
		repeat.execute(container, style);
	}
	public void finish(GameContainer container, StyleInfo style) {
		repeat.finish(container, style);
		currentpoint = (currentpoint + 1) % characters;
	}
	public void reset() {
		repeat.reset();
	}
	
	public int getCharPoint() {
		return charpoint + currentpoint;
	}
	
	
}