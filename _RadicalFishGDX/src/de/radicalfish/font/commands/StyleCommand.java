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
package de.radicalfish.font.commands;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.radicalfish.GameContainer;
import de.radicalfish.font.Font;
import de.radicalfish.font.StyleInfo;

/**
 * This class is the base of all {@link StyleCommand}s. It offers an charpoint from which the command should be
 * executed. of course the {@link Font} must offer the feature to use {@link StyleCommand}s.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 08.12.2011
 */
public abstract class StyleCommand {
	
	public int charpoint;
	
	/**
	 * Creates a new {@link StyleCommand} with the point on the text line to execute the command.
	 * 
	 * @param charpoint
	 *            the character point at which the command should be executed
	 */
	public StyleCommand(int charpoint) {
		this.charpoint = charpoint;
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Updates this {@link StyleCommand}.
	 * 
	 * @param container
	 *            the container the game runs in
	 * @param delta
	 *            the time since the last frame
	 */
	public abstract void update(GameContainer container, float delta) ;
	/**
	 * Will be called to apply the command to the {@link StyleInfo} given as parameter. After this the
	 * {@link TextureRegion} will be drawn based in the {@link StyleInfo}.
	 * 
	 * @param container
	 *            the container the game runs in
	 * @param style
	 *            the {@link StyleInfo} to change. will be used to draw the character
	 */
	public abstract void execute(GameContainer container, StyleInfo style) ;
	/**
	 * Releases any resources used by this command (eg. you use a custom shader). Can also be used to reset values on
	 * the style info.
	 * 
	 * @param container
	 *            the container the game runs in
	 * @param style
	 *            the {@link StyleInfo} to change. will be used to draw the character
	 */
	public abstract void finish(GameContainer container, StyleInfo style) ;
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
}
