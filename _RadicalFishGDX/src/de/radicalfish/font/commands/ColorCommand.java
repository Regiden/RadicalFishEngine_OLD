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
import com.badlogic.gdx.graphics.Color;
import de.radicalfish.GameContainer;
import de.radicalfish.font.SpriteFont;
import de.radicalfish.font.StyleInfo;

/**
 * A command which will change the color to tint the text in. If the <code>preCharacter</code> parameter in the C'Tor is
 * true, only the character at the given charpoint will be tinted. If not all following characters will be tinted in
 * that color (At least thats how {@link SpriteFont} handles it).
 * <p>
 * Note this also changes the alpha value. any alpha related commands should be applied after a color command.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 04.09.2012
 */
public class ColorCommand extends StyleCommand {
	
	protected final Color color;
	protected final boolean singleChar;
	
	private Color c1 = new Color(), c2 = new Color(), c3 = new Color(), c4 = new Color();
	
	/**
	 * Creates a new {@link ColorCommand}.
	 * 
	 * @param color
	 *            the color to tint the character/text in
	 * @param charpoint
	 *            the charpoint to start
	 * @param singleChar
	 *            true if only the character at <code>charpoint</code> should be tinted
	 */
	public ColorCommand(Color color, int charpoint, boolean singleChar) {
		super(charpoint);
		this.color = color;
		this.singleChar = singleChar;
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void execute(GameContainer container, StyleInfo style) {
		c1.set(style.colorTopLeft);
		c2.set(style.colorTopRight);
		c3.set(style.colorBottomLeft);
		c4.set(style.colorBottomRight);
		style.setColor(color);
	}
	public void finish(GameContainer container, StyleInfo style) {
		if (singleChar) {
			style.colorTopLeft.set(c1);
			style.colorTopRight.set(c2);
			style.colorBottomLeft.set(c3);
			style.colorBottomRight.set(c4);
		}
	}
	
	// UNUSED
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void update(GameContainer container, float delta) {}
	public void reset() {}
	
}
