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
package de.radicalfish.text;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import de.radicalfish.GameContainer;
import de.radicalfish.util.RadicalFishException;
import de.radicalfish.util.Utils;

/**
 * Implementation of the {@link BitmapFont} for this engine. Does not support styled text.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 03.09.2012
 */
public class BMFont implements Font {
	
	private BitmapFont font;
	
	public BMFont(BitmapFont font) {
		Utils.notNull("font", font);
		this.font = font;
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void draw(SpriteBatch batch, CharSequence text, float x, float y) throws RadicalFishException {
		font.draw(batch, text, x, y);
	}
	public void draw(SpriteBatch batch, CharSequence text, float x, float y, int startIndex, int endIndex) throws RadicalFishException {
		font.draw(batch, text, x, y, startIndex, endIndex);
	}
	
	public void draw(SpriteBatch batch, CharSequence text, float x, float y, GameContainer c, StyledLine style) throws RadicalFishException {
		font.draw(batch, text, x, y);
	}
	public void draw(SpriteBatch batch, CharSequence text, float x, float y, int startIndex, int endIndex, GameContainer c, StyledLine style)
			throws RadicalFishException {
		font.draw(batch, text, x, y, startIndex, endIndex);
	}
	
	public void drawMultiLine(SpriteBatch batch, CharSequence text, float x, float y) throws RadicalFishException {
		font.drawMultiLine(batch, text, x, y);
	}
	public void drawMultiLine(SpriteBatch batch, CharSequence text, float x, float y, float alignWidth, HAlignment alignment)
			throws RadicalFishException {
		font.drawMultiLine(batch, text, x, y, alignWidth, alignment);
	}
	
	public void drawMultiLine(SpriteBatch batch, CharSequence text, float x, float y, GameContainer c, StyledText style)
			throws RadicalFishException {
		font.drawMultiLine(batch, text, x, y);
	}
	public void drawMultiLine(SpriteBatch batch, CharSequence text, float x, float y, float alignWidth, HAlignment alignment, GameContainer c,
			StyledText style) throws RadicalFishException {
		font.drawMultiLine(batch, text, x, y, alignWidth, alignment);
	}
	
	public void drawNumbers(SpriteBatch batch, CharSequence text, float x, float y) throws RadicalFishException {
		font.draw(batch, text, x, y);
	}
	public void drawNumbers(SpriteBatch batch, CharSequence text, float x, float y, GameContainer c, StyledLine style)
			throws RadicalFishException {
		font.draw(batch, text, x, y);
	}
	
	public void dispose() {
		font.dispose();
	}
	
	// SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void setColor(Color color) {
		font.setColor(color);
	}
	public void setColor(float r, float g, float b, float a) {
		font.setColor(r, g, b, a);
	}
	public void setColor(float color) {
		font.setColor(color);
	}
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public Color getColor() {
		return font.getColor();
	}
	
	public TextBounds getBounds(CharSequence text) {
		return font.getBounds(text);
	}
	public float getWidth(CharSequence text) {
		return font.getBounds(text).width;
	}
	public float getHeight(CharSequence text) {
		return font.getBounds(text).height;
	}
	public float getLineHeight() {
		return font.getLineHeight();
	}
	
	public boolean supportsStyledCommand() {
		return false;
	}
	
}
