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
package de.radicalfish.font;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import de.radicalfish.GameContainer;
import de.radicalfish.font.commands.StyleCommand;

/**
 * Base interface for all fonts used in this engine.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 03.09.2012
 */
public interface Font extends Disposable {
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Draws the given <code>text</code> at <code>x</code>, <code>y</code>.
	 */
	public void draw(SpriteBatch batch, String text, float x, float y);
	/**
	 * Draws the given <code>text</code> at <code>x</code>, <code>y</code>.
	 */
	public void draw(SpriteBatch batch, String text, float x, float y, int startIndex, int endIndex);
	
	/**
	 * Draws the given <code>text</code> at <code>x</code>, <code>y</code> with a styled feature.
	 * 
	 * @param style
	 *            the list of commands to style the text in
	 */
	public void draw(SpriteBatch batch, String text, float x, float y, GameContainer c, StyledLine style);
	/**
	 * Draws the given <code>text</code> at <code>x</code>, <code>y</code> with a styled feature.
	 * 
	 * @param style
	 *            the list of commands to style the text in
	 */
	public void draw(SpriteBatch batch, String text, float x, float y, int startIndex, int endIndex, GameContainer c, StyledLine style);
	
	/**
	 * Draws the given multi line <code>text</code> at <code>x</code>, <code>y</code>.
	 */
	public void drawMultiLine(SpriteBatch batch, String text, float x, float y);
	/**
	 * Draws the given multi line <code>text</code> at <code>x</code>, <code>y</code>.
	 * 
	 */
	public void drawMultiLine(SpriteBatch batch, String text, float x, float y, float alignWidth, HAlignment alignment);
	
	/**
	 * Draws the given <code>text</code> at <code>x</code>, <code>y</code> with a styled feature.
	 * 
	 * @param style
	 *            the list of commands to style the text in
	 * 
	 */
	public void drawMultiLine(SpriteBatch batch, String text, float x, float y, GameContainer c, StyledText style);
	/**
	 * Draws the given <code>text</code> at <code>x</code>, <code>y</code> with a styled feature.
	 * 
	 * @param style
	 *            the list of commands to style the text in
	 */
	public void drawMultiLine(SpriteBatch batch, String text, float x, float y, float alignWidth, HAlignment alignment, GameContainer c,
			StyledText style);
	
	/**
	 * Draws the <code>text</code> at <code>x</code>, <code>y</code>. This method can be used if you want special
	 * handling for number.
	 */
	public void drawNumbers(SpriteBatch batch, String text, float x, float y);
	/**
	 * Draws the <code>text</code> at <code>x</code>, <code>y</code> with a styled feature. This method can be used if
	 * you want special handling for number.
	 * 
	 * @param style
	 *            the list of commands to style the text in @
	 */
	public void drawNumbers(SpriteBatch batch, String text, float x, float y, GameContainer c, StyledLine style);
	
	// SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * The color to tint the text in. Default should be white. With no {@link StyleCommand}s this should be the default
	 * tint color.
	 */
	public void setColor(Color color);
	/**
	 * The color to tint the text in. Default should be white. With no {@link StyleCommand}s this should be the default
	 * tint color.
	 */
	public void setColor(float r, float g, float b, float a);
	/**
	 * The color to tint the text in. Default should be white. With no {@link StyleCommand}s this should be the default
	 * tint color.
	 */
	public void setColor(float color);
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return the {@link Color} to tint this font.
	 */
	public Color getColor();
	/**
	 * @return the bounds of the given <code>text</code>. this method should respect multi line texts.
	 */
	public TextBounds getBounds(String text);
	
	/**
	 * @return the width of the given <code>text</code>. This should respect multi line texts.
	 */
	public float getWidth(String text);
	/**
	 * 
	 * @return the height of the given <code>text</code>. This should respect multi line texts.
	 */
	public float getHeight(String text);
	/**
	 * @return the maximum height a single line can have.
	 */
	public float getLineHeight();
	
	/**
	 * @return true if this font supports {@link StyleCommand}s.
	 */
	public boolean supportsStyledCommand();
	
}
