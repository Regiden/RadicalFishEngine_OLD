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
import java.util.HashMap;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * Implementation of the {@link FontRenderer} for {@link SpriteFont}.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 15.04.2012
 */
public class SpriteFontRenderer implements FontRenderer {
	
	private HashMap<String, StyledFont> fonts;
	private StyledFont currentFont;
	
	/**
	 * Creates a SpriteFontRenderer with no font set.
	 */
	public SpriteFontRenderer() throws SlickException {
		fonts = new HashMap<String, StyledFont>();
		currentFont = null;
	}
	/**
	 * Creates a SpriteFontRenderer with a default font to start with.
	 * 
	 * @param key
	 *            the key for the font
	 * @param defaultFont
	 *            the font.
	 */
	public SpriteFontRenderer(String key, StyledFont defaultFont) throws SlickException {
		this();
		fonts.put(key, defaultFont);
		currentFont = defaultFont;
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void drawString(String text, float x, float y) throws SlickException {
		drawString(text, x, y, Color.white);
	}
	public void drawString(String text, float x, float y, Color filter) throws SlickException {
		if (currentFont == null)
			throw new SlickException("No font is set for the font renderer!");
		currentFont.drawString(x, y, text, filter);
	}
	public void drawString(String text, float x, float y, StyledText styling, Graphics g) throws SlickException {
		if (currentFont == null)
			throw new SlickException("No font is set for the font renderer!");
		currentFont.drawString(text, x, y, styling, g);
	}
	public void drawNumber(float x, float y, String numbers) throws SlickException {
		drawNumbers(x, y, numbers, Color.white);
	}
	public void drawNumbers(float x, float y, String numbers, Color filter) throws SlickException {
		if (currentFont == null)
			throw new SlickException("No font is set for the font renderer!");
		
		currentFont.drawNumbers(x, y, numbers, filter);
	}
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public StyledFont getFont(String key) throws SlickException {
		if (!fonts.containsKey(key))
			throw new SlickException("Font does not exist: " + key);
		return fonts.get(key);
	}
	public StyledFont getCurrentFont() {
		return currentFont;
	}
	
	// SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void addFont(String key, StyledFont font) throws SlickException {
		if (fonts.containsKey(key))
			throw new SlickException("Font already exist: " + key);
		
		fonts.put(key, font);
		
		if (currentFont == null)
			currentFont = font;
	}
	public void setFont(String key) throws SlickException {
		if (!fonts.containsKey(key))
			throw new SlickException("Font does not exist: " + key);
		currentFont = fonts.get(key);
	}
}
