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
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * a extended version of Slick2D's font with support for styled text. It also has specific methods for drawing number
 * because you sometimes want numbers do be rendered differently.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 15.04.2012
 */
public interface StyledFont extends org.newdawn.slick.Font {
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Draws the string with a list of commands to style the text. See {@link StyleCommand} and {@link StyledText}.
	 * 
	 * @param text
	 *            the text to draw (duh :o)
	 * @param x
	 *            the x position
	 * @param y
	 *            the y position
	 * @param styling
	 *            the list of style commands
	 * @param g
	 *            the graphic context do draw to
	 */
	public void drawString(String text, float x, float y, StyledText styling, Graphics g) throws SlickException;
	/**
	 * Draws numbers.
	 * 
	 * @param numbers
	 *            the numbers
	 * @param x
	 *            the x position
	 * @param y
	 *            the y position
	 */
	public void drawNumber(float x, float y, String numbers) throws SlickException;
	/**
	 * Draws numbers.
	 * 
	 * @param numbers
	 *            the numbers
	 * @param x
	 *            the x position
	 * @param y
	 *            the y position
	 * @param filter
	 *            the color to apply.
	 */
	public void drawNumbers(float x, float y, String numbers, Color filter) throws SlickException;
	

}
