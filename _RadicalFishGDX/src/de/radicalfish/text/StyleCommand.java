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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.radicalfish.GameContainer;
import de.radicalfish.graphics.Graphics;
import de.radicalfish.util.RadicalFishException;

/**
 * This class is the base of all {@link StyleCommand}s. It offers an charpoint from which the command should be
 * executed. of course the {@link Font} must offer the feature to use {@link StyleCommand}s.
 * <p>
 * A command can 'override' the drawing of a single character if required. for this the C'tor
 * {@link StyleCommand#StyleCommand(int, boolean)} should be used. A {@link Font} should check for the boolean via
 * {@link StyleCommand#isOverrideExecute()} and call the
 * {@link StyleCommand#execute(GameContainer, Graphics, TextureRegion, Font)} if it's true.
 * 
 * @author Stefan Lange
 * @version 0.5.0
 * @since 08.12.2011
 */
public abstract class StyleCommand {
	
	public int charpoint;
	public boolean overrideExecute;
	
	/**
	 * Creates a new {@link StyleCommand} with the point on the text line to execute the command.
	 * 
	 * @param charpoint
	 *            the character point at which the command should be executed
	 */
	public StyleCommand(int charpoint) {
		this(charpoint, false);
	}
	/**
	 * Creates a new {@link StyleCommand} with the point on the text line to execute the command.
	 * 
	 * @param charpoint
	 *            the character point at which the command should be executed
	 * @param overrideExecute
	 *            true if the the {@link StyleCommand#execute(GameContainer, Graphics, TextureRegion)} method should be
	 *            called. if false the character will be drawn with the default {@link SpriteBatch}.
	 */
	public StyleCommand(int charpoint, boolean overrideExecute) {
		this.charpoint = charpoint;
		this.overrideExecute = overrideExecute;
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Updates this {@link StyleCommand}.
	 * 
	 * @param container
	 *            the container the game runs in
	 * @param delta
	 *            the time since the last frame.
	 * @throws RadicalFishException
	 */
	public abstract void update(GameContainer container, float delta) throws RadicalFishException;
	
	/**
	 * Will be called to apply the command to the {@link StyleInfo} given as parameter.
	 * 
	 * @param container
	 *            the container the game runs in.
	 * @param style
	 *            the {@link StyleInfo} to change. will be used to draw the character.
	 * @throws RadicalFishException
	 */
	public abstract void execute(GameContainer container, StyleInfo style) throws RadicalFishException;
	
	/**
	 * Releases any resources used by this command (eg. you use a custom shader).
	 * 
	 * @param container
	 *            the container the game runs in.
	 * @throws RadicalFishException
	 */
	public abstract void finish(GameContainer container) throws RadicalFishException;
	
	/**
	 * Resets the {@link StyleCommand}.
	 */
	public abstract void reset();
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return the character point at which the command should be executed.
	 */
	public int getCharPoint() {
		return charpoint;
	}
	/**
	 * @return true the the {@link StyleCommand#execute(GameContainer, Graphics, TextureRegion)} will be called instead
	 *         of a simple draw via the {@link SpriteBatch}.
	 */
	public boolean isOverrideExecute() {
		return overrideExecute;
	}
}
