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
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.NumberUtils;
import de.radicalfish.GameContainer;
import de.radicalfish.util.RadicalFishException;

/**
 * A {@link Font} implementation which uses the {@link FontSheet}. This class supports styled fonts. Multi line texts
 * must be separated by a <code>\n</code> separator!
 * <p>
 * It uses ASCII encoding so the font file should have all characters in the ASCII order, eg like this:
 * 
 * <pre>
 *   !"#$%&'()*+,-./
 *  0123456789:;<=>?
 *  &#0064;ABCDEFGHIJKLMNO
 * PQRSTUVWXYZ[\]^_
 * 
 * <pre>
 * This would only display upper case characters and the starting character would be ' '.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 04.09.2012
 */
public class SpriteFont implements Font {
	
	private static final Pattern split = Pattern.compile("\n");
	
	private final TextBounds bounds = new TextBounds();
	private final StyleInfo info = new StyleInfo();
	
	private Color tempColor = Color.WHITE.cpy();
	
	/** The {@link FontSheet} the {@link SpriteFont} uses. */
	public final FontSheet font;
	
	/** The starting character in the ASCII. */
	public final char startChar;
	
	private float color = Color.WHITE.toFloatBits();
	
	/** The space between two characters. */
	public final int charakterSpace;
	/** The total number of characters in the file. */
	public final int totalChars;
	
	private boolean alphaFlag = false;
	
	// C'TOR
	// ��������������������������������������������������������������������������������������������
	/**
	 * Creates a new {@link SpriteFont}.
	 * 
	 * @param font
	 *            the {@link FontSheet} used to draw the characters
	 * @param charakterSpace
	 *            the space between two characters (can be negative)
	 * @param startChar
	 *            the starting char from the ASCII
	 */
	public SpriteFont(FontSheet font, int charakterSpace, char startChar) {
		this.startChar = startChar;
		this.font = font;
		this.charakterSpace = charakterSpace;
		totalChars = font.tilesAcross * font.tilesDown;
	}
	
	// METHODS
	// ��������������������������������������������������������������������������������������������
	public void draw(SpriteBatch batch, String text, float x, float y) throws RadicalFishException {
		draw(batch, text, x, y, 0, text.length(), null, null);
	}
	public void draw(SpriteBatch batch, String text, float x, float y, int startIndex, int endIndex) throws RadicalFishException {
		draw(batch, text, x, y, startIndex, endIndex, null, null);
	}
	
	public void draw(SpriteBatch batch, String text, float x, float y, GameContainer c, StyledLine style) throws RadicalFishException {
		draw(batch, text, x, y, 0, text.length(), c, style);
	}
	public void draw(SpriteBatch batch, String text, float x, float y, int startIndex, int endIndex, GameContainer c, StyledLine style)
			throws RadicalFishException {
		float oldColor = batch.getColor().toFloatBits();
		batch.setColor(color);
		info.setColor(color);
		drawLine(batch, text, x, y, startIndex, endIndex, c, style);
		batch.setColor(oldColor);
	}
	
	public void drawMultiLine(SpriteBatch batch, String text, float x, float y) throws RadicalFishException {
		
	}
	public void drawMultiLine(SpriteBatch batch, String text, float x, float y, float alignWidth, HAlignment alignment)
			throws RadicalFishException {
		
	}
	
	public void drawMultiLine(SpriteBatch batch, String text, float x, float y, GameContainer c, StyledText style) throws RadicalFishException {
		
	}
	public void drawMultiLine(SpriteBatch batch, String text, float x, float y, float alignWidth, HAlignment alignment, GameContainer c,
			StyledText style) throws RadicalFishException {
		
	}
	
	public void drawNumbers(SpriteBatch batch, String text, float x, float y) throws RadicalFishException {
		
	}
	public void drawNumbers(SpriteBatch batch, String text, float x, float y, GameContainer c, StyledLine style) throws RadicalFishException {
		
	}
	
	public void dispose() {
		font.dispose();
	}
	
	// INTERN
	// ��������������������������������������������������������������������������������������������
	private void drawLine(SpriteBatch batch, String str, float x, float y, int start, int end, GameContainer c, StyledLine s)
			throws RadicalFishException {
		try {
			byte[] data = str.getBytes("US-ASCII");
			int count = 0;
			int width = 0;
			for (int i = 0; i < data.length; i++) {
				int index = data[i] - startChar;
				if (index < totalChars) {
					int xPos = (index % font.tilesAcross);
					int yPos = (index / font.tilesAcross);
					if ((i >= start) || (i <= end)) {
						if(s != null) {
							width = font.getTileWidthAt(xPos, yPos);
							info.size.set(width, font.getTileHeight());
							info.origin.set(width / 2, font.getTileHeight() / 2);
							s.execute(c, info, i);
							float[] array = info.createVertices(font.getSubImage(xPos, yPos), x + count, y);
							batch.draw(font.base, array, 0, array.length);
							s.finish(c, info);
							info.resetGeom();
						} else {
							batch.draw(font.getSubImage(xPos, yPos), x + count, y);
						}
					}
					count += font.getTileWidthAt(xPos, yPos) + charakterSpace;
				}
			}
		} catch (UnsupportedEncodingException e) {
			// Should never happen, ASCII is supported pretty much anywhere
			throw new RadicalFishException(e.getMessage());
		}
	}
	
	// SETTER
	// ��������������������������������������������������������������������������������������������
	public void setColor(Color color) {
		this.color = color.toFloatBits();
		if (color.a == 1.0f) {
			alphaFlag = true;
		}
	}
	public void setColor(float r, float g, float b, float a) {
		this.color = Color.toFloatBits(r, g, b, a);
		if (a == 1.0f) {
			alphaFlag = true;
		}
	}
	public void setColor(float color) {
		this.color = color;
	}
	
	// GETTER
	// ��������������������������������������������������������������������������������������������
	public Color getColor() {
		int intBits = NumberUtils.floatToIntColor(color);
		tempColor.r = (intBits & 0xff) / 255f;
		tempColor.g = ((intBits >>> 8) & 0xff) / 255f;
		tempColor.b = ((intBits >>> 16) & 0xff) / 255f;
		tempColor.a = ((intBits >>> 24) & 0xff) / 255f;
		if (alphaFlag) {
			tempColor.a = 1.0f;
			alphaFlag = false;
		}
		return tempColor;
	}
	public TextBounds getBounds(String text) {
		bounds.height = getHeight(text);
		bounds.width = getWidth(text);
		return null;
	}
	public float getWidth(String text) {
		return 0;
	}
	public float getHeight(String text) {
		if (text.contains("\n")) {
			return font.getTileHeight() * split.split("\n").length;
		} else {
			return font.getTileHeight();
		}
	}
	public float getLineHeight() {
		return font.getTileHeight();
	}
	
	public boolean supportsStyledCommand() {
		return true;
	}
	
}