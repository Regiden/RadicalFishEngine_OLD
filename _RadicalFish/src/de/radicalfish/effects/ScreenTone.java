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
package de.radicalfish.effects;
import org.newdawn.slick.Color;

/**
 * A class which can be used to apply a Color effect to set a screen tone.
 * 
 * @author Stefan Lange
 * @version 0.0.0
 * @since 11.03.2012
 */
public class ScreenTone {
	
	// the basic tone of the world and a temp color for mixing
	private Color tone, temp;
	
	/**
	 * Creates a new ScreeTone object. The tone is set to default (white).
	 */
	public ScreenTone() {
		this(Color.white);
	}
	/**
	 * Creates a new ScreenTone object with a specific start color.
	 * 
	 * @param color
	 */
	public ScreenTone(Color color) {
		tone = new Color(color);
		temp = new Color(Color.white);
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Changes the tone. Note there is not Range checking so check the parameters because there must be in the range of
	 * 0 - 1!
	 * 
	 * @param r
	 *            the red component
	 * @param g
	 *            the green component
	 * @param b
	 *            the blue component
	 */
	public void changeTone(float r, float g, float b) {
		tone.r = r;
		tone.g = g;
		tone.b = b;
		// alpha should generally always be used by getTone(float alpha)!
		tone.a = 1f;
	}
	/**
	 * Changes the tone base on <code>color</code>. Note that alpha will not be taken into account! Use
	 * <code>getTone(float alpha)</code> while drawing to apply alpha!
	 * 
	 * @param color
	 *            the color to set the tone to
	 */
	public void changeTone(Color color) {
		changeTone(color.r, color.g, color.b);
	}
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return the tone.
	 */
	public Color getTone() {
		return getTone(1f);
	}
	/**
	 * @param alpha
	 *            the alpha to apply
	 * @return the tone with with alpha set to <code>alpha</code>.
	 */
	public Color getTone(float alpha) {
		tone.a = alpha;
		return tone;
	}
	/**
	 * Mixes the tone with a certain color (by multiplying the rgb's). Alpha will be taken from <code>micColor</code>.
	 * 
	 * @param mixColor
	 *            the color to mix with.
	 * @return the tone mixed with <code>color</code>.
	 */
	public Color getTone(Color mixColor) {
		temp.r = tone.r * mixColor.r;
		temp.g = tone.g * mixColor.g;
		temp.b = tone.b * mixColor.b;
		temp.a = mixColor.a;
		return temp;
	}
	
	// SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Changes the red component.
	 * 
	 * @param red
	 *            the value to apply
	 */
	public void setRed(float red) {
		tone.r = red;
	}
	/**
	 * Changes the green component.
	 * 
	 * @param green
	 *            the value to apply
	 */
	public void setGreen(float green) {
		tone.g = green;
	}
	/**
	 * Changes the blue component.
	 * 
	 * @param blue
	 *            the value to apply
	 */
	public void setBlue(float blue) {
		tone.b = blue;
	}
}
