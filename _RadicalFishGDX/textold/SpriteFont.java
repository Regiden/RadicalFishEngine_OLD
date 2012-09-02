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
import java.io.UnsupportedEncodingException;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

/**
 * This is a similar implementation to the SpriteSheetFont class Slick2D use. But instead of using the SpriteSheet class
 * this class uses the {@link FontSheet}. It also allows to set the space between to characters. This can also be used
 * to minimize the space between 2 by setting a negative value! It is also capable of taking a message command list into
 * consideration.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 19.10.2011
 */
public class SpriteFont implements StyledFont {
	
	private FontSheet sheet;
	private int charakterSpace;
	private int tilesAcross, tilesDown, totalChars;
	private char startChar;
	
	/**
	 * Creates a new {@link SpriteFont} from an {@link FontSheet} with set character space. The sheet should have the
	 * characters set in ASCII-Order! Taken form the SpriteSheet class of Slick2D: <blockquote> To only get upper-case
	 * characters working you would usually set up a SpriteSheet with characters for these values:
	 * 
	 * <pre>
	 *   !"#$%&'()*+,-./
	 *  0123456789:;<=>?
	 *  &#0064;ABCDEFGHIJKLMNO
	 * PQRSTUVWXYZ[\]^_`
	 * 
	 * <pre>
	 * In this set, ' ' (SPACE) would be the startingCharacter of your characterSet. </blockquote>
	 * 
	 * @param sheet the sheet to use
	 * @param charakterSpace the space between two characters
	 * @param start the first character in the sheet
	 */
	public SpriteFont(FontSheet sheet, int charakterSpace, char startChar) throws SlickException {
		this.sheet = sheet;
		this.charakterSpace = charakterSpace;
		this.startChar = startChar;
		tilesAcross = sheet.getHorizontalCount();
		tilesDown = sheet.getVerticalCount();
		totalChars = tilesAcross * tilesDown;
	}
	/**
	 * 
	 * Creates a new {@link SpriteFont} with set character space. The sheet should have the characters set in
	 * ASCII-Order! Taken form the SpriteSheet class of Slick2D: <blockquote> To only get upper-case characters working
	 * you would usually set up a SpriteSheet with characters for these values:
	 * 
	 * <pre>
	 *   !"#$%&'()*+,-./
	 *  0123456789:;<=>?
	 *  &#0064;ABCDEFGHIJKLMNO
	 * PQRSTUVWXYZ[\]^_`
	 * 
	 * <pre>
	 * In this set, ' ' (SPACE) would be the startingCharacter of your characterSet. </blockquote>
	 * 
	 * @param ref the path to the image used as sheet
	 * @param widths the widths of each tile
	 * @param height the height of the tiles
	 * @param charakterSpace the space between two characters
	 * @param startChar the first character in the sheet
	 */
	public SpriteFont(String ref, int[][] widths, int height, int charakterSpace, char startChar) throws SlickException {
		this(new FontSheet(ref, widths, height), charakterSpace, startChar);
	}
	
	// INTERFACE METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void drawString(float x, float y, String text) {
		drawString(x, y, text, Color.white);
	}
	public void drawString(float x, float y, String text, Color col) {
		drawString(x, y, text, col, 0, text.length() - 1);
	}
	public void drawString(float x, float y, String text, Color col, int startIndex, int endIndex) {
		// this is obviously taken from Slick2D's SpriteSheetFont class, but it uses
		// renderInUse and the charakterSpace and of course the width per tile thingi.
		try {
			byte[] data = text.getBytes("US-ASCII");
			int count = 0;
			sheet.startUse();
			col.bind();
			for (int i = 0; i < data.length; i++) {
				int index = data[i] - startChar;
				if (index < totalChars) {
					int xPos = (index % tilesAcross);
					int yPos = (index / tilesAcross);
					if ((i >= startIndex) || (i <= endIndex))
						sheet.renderInUse(x + count, y, xPos, yPos);
					count += sheet.getTileWidthAt(xPos, yPos) + charakterSpace;
				}
			}
			sheet.endUse();
		} catch (UnsupportedEncodingException e) {
			// Should never happen, ASCII is supported pretty much anywhere
			Log.error(e);
		}
	}
	
	public void drawString(String text, float x, float y, StyledText commands, Graphics g) throws SlickException {
		int startIndex = 0;
		int endIndex = text.length() - 1;
		try {
			byte[] data = text.getBytes("US-ASCII");
			int count = 0;
			g.setColor(Color.white);
			for (int i = 0; i < data.length; i++) {
				int index = data[i] - startChar;
				if (index < totalChars) {
					int xPos = (index % tilesAcross);
					int yPos = (index / tilesAcross);
					if ((i >= startIndex) || (i <= endIndex)) {
						commands.execute(i, g);
						sheet.getSprite(xPos, yPos).draw(x + count, y, g.getColor());
						commands.reverse(g);
					}
					count += sheet.getTileWidthAt(xPos, yPos) + charakterSpace;
				}
			}
		} catch (UnsupportedEncodingException e) {
			Log.error(e);
		}
	}
	public void drawNumber(float x, float y, String numbers) throws SlickException{
		drawNumbers(x, y, numbers, Color.white);
	}
	public void drawNumbers(float x, float y, String numbers, Color filter) throws SlickException {
		try {
			byte[] data = numbers.getBytes("US-ASCII");
			int count = 0;
			sheet.startUse();
			filter.bind();
			for (int i = 0; i < data.length; i++) {
				int index = data[i] - startChar;
				if (index < totalChars) {
					int xPos = (index % tilesAcross);
					int yPos = (index / tilesAcross);
					if ((i >= 0) || (i <= numbers.length() - 1)) {
						if (numbers.charAt(i) == '1')
							sheet.renderInUse(x + count + 1, y, xPos, yPos);
						else
							sheet.renderInUse(x + count, y, xPos, yPos);
					}
					if (numbers.charAt(i) == '1' || numbers.charAt(i) == ' ')
						count += 5 + charakterSpace;
					else
						count += sheet.getTileWidthAt(xPos, yPos) + charakterSpace;
				}
			}
			sheet.endUse();
		} catch (UnsupportedEncodingException e) {
			// Should never happen, ASCII is supported pretty much anywhere
			Log.error(e);
		}
	}
	
	public int getLineHeight() {
		return sheet.getMaxTileHeight();
	}
	public int getWidth(String str) {
		int count = 0;
		
		try {
			byte[] data = str.getBytes("US-ASCII");
			for (int i = 0; i < data.length; i++) {
				int index = data[i] - startChar;
				if (index < totalChars)
					count += sheet.getTileWidthAt(index % tilesAcross, index / tilesAcross) + charakterSpace;
				
			}
			
		} catch (UnsupportedEncodingException e) {
			// Should never happen, ASCII is supported pretty much anywhere
			Log.error(e);
		}
		return count - charakterSpace;
	}
	public int getHeight(String str) {
		return sheet.getMaxTileHeight();
	}
	
	public boolean supportsStyledText() {
		return true;
	}
	
}
