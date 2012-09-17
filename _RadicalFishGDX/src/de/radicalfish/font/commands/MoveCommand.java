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
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import de.radicalfish.GameContainer;
import de.radicalfish.font.StyleInfo;

/**
 * Offsets the character at charpoint by a given offset. This is a {@link TimeCommand} which means you must supply a
 * time the move takes.
 * <p>
 * You can set an {@link Interpolation} (default is {@link Interpolation#linear} to modify the style of the move.
 * <p>
 * Like the {@link ColorCommand} the command can be applied on single character or all upcoming character.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 16.09.2012
 */
public class MoveCommand extends TimeCommand {
	
	/** The {@link Interpolation} used to move from one point to another. */
	protected Interpolation tween;
	
	/** The offset to add to the character position. */
	protected final Vector2 offset = new Vector2(0, 0);
	/** The current position. */
	protected final Vector2 current = new Vector2(0, 0);
	
	/** True if the effect will only be applied on one character */
	protected final boolean singleChar;
	
	/**
	 * Creates a new {@link MoveCommand}.
	 * 
	 * @param offsetx
	 *            the offset to move to. 0 equals the relative x position of the character
	 * @param offsety
	 *            the offset to move to. 0 equals the relative y position of the character
	 * @param charpoint
	 *            the character point at which the command should be executed
	 * @param duration
	 *            the time this command needs to change it's state (in seconds)
	 * @param singleChar
	 *            true if only the character at <code>charpoint</code> should be moved
	 * 
	 */
	public MoveCommand(float offsetx, float offsety, int charpoint, float duration, boolean singleChar) {
		this(Interpolation.linear, offsetx, offsety, charpoint, duration, singleChar);
	}
	/**
	 * Creates a new {@link MoveCommand}.
	 * 
	 * @param tween
	 *            The {@link Interpolation} to use.
	 * @param offsetx
	 *            the offset to move to. 0 equals the relative x position of the character
	 * @param offsety
	 *            the offset to move to. 0 equals the relative y position of the character
	 * @param charpoint
	 *            the character point at which the command should be executed
	 * @param duration
	 *            the time this command needs to change it's state (in seconds)
	 * @param singleChar
	 *            true if only the character at <code>charpoint</code> should be tinted
	 */
	public MoveCommand(Interpolation tween, float offsetx, float offsety, int charpoint, float duration, boolean singleChar) {
		super(charpoint, duration);
		this.tween = tween;
		offset.set(offsetx, offsety);
		this.singleChar = singleChar;
	}
	
	// OVERRIDE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void update(GameContainer container, float delta, float alpha) {
		current.x = tween.apply(0, offset.x, alpha);
		current.y = tween.apply(0, offset.y, alpha);
	}
	public void execute(GameContainer container, StyleInfo style, float alpha) {
		style.offset.add(current);
	}
	public void finish(GameContainer container, StyleInfo style, float alpha) {
		if (singleChar) {
			style.offset.sub(current);
		}
	}
	
	public void reset() {
		super.reset();
		current.set(0, 0);
	}
	
}
