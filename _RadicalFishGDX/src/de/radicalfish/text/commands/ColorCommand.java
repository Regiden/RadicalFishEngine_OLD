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
package de.radicalfish.text.commands;
import com.badlogic.gdx.graphics.Color;
import de.radicalfish.GameContainer;
import de.radicalfish.text.SpriteFont;
import de.radicalfish.text.StyleCommand;
import de.radicalfish.text.StyleInfo;
import de.radicalfish.util.RadicalFishException;

/**
 * A command which will change the color to tint the text in. If the <code>preCharacter</code> parameter in the C'Tor is
 * true, only the character at the given charpoint will be tinted. If not all following characters will be tinted in
 * that color (At least thats how {@link SpriteFont} handles it).
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 04.09.2012
 */
public class ColorCommand extends StyleCommand {
	
	protected Color color;
	
	protected float c1, c2, c3, c4;
	protected boolean perCharacter;
	
	/**
	 * Creates a new {@link ColorCommand}.
	 * 
	 * @param color
	 *            the color to tint the character/text in
	 * @param charpoint
	 *            the charpoint to start
	 * @param perCharacter
	 *            true if only the character at <code>charpoint</code>
	 */
	public ColorCommand(Color color, int charpoint, boolean perCharacter) {
		super(charpoint);
		this.color = color;
		this.perCharacter = perCharacter;
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void execute(GameContainer container, StyleInfo style) throws RadicalFishException {
		c1 = style.colorTopLeft;
		c2 = style.colorTopRight;
		c3 = style.colorBottomLeft;
		c4 = style.colorBottomRight;
		style.setColor(color);
	}
	public void finish(GameContainer container, StyleInfo style) throws RadicalFishException {
		if (perCharacter) {
			style.colorTopLeft = c1;
			style.colorTopRight = c2;
			style.colorBottomLeft = c3;
			style.colorBottomRight = c4;
		}
	}
	
	// UNUSED
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void update(GameContainer container, float delta) throws RadicalFishException {}
	public void reset() {}
	
}
