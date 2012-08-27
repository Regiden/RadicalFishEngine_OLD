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

/**
 * This is a color model for toning. Instead of just using red, green and blue this model uses overshoot value for each
 * color. This can be used to flood a image or whatever with a certain color. There is also a chroma value. This value
 * should make the area grayer the smaller the value is. Chroma has an overshoot value too which should make the overall
 * color brighter. This feature can be used for shader to manipulate the screen tone or the tone of an image. Every
 * value has a range of 0.0 - 1.0. There will be no Exception if a setter detects a value out of range. instead it sets
 * the min/max value.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 19.05.2012
 */
public class ToneModel {
	
	private float red, green, blue, chroma, redOvershoot, greenOvershoot, blueOvershoot, chromaOvershoot;
	
	/**
	 * Default, all value set to 1.0f. Overshoots are set to 0.
	 */
	public ToneModel() {
		this(1, 1, 1, 1f);
	}
	/**
	 * Tone model with pre-set colors and chroma. Overshoots are set to 0.
	 */
	public ToneModel(float r, float g, float b, float chroma) {
		setRed(r);
		setGreen(g);
		setBlue(b);
		setChroma(chroma);
		
		redOvershoot = 0f;
		greenOvershoot = 0f;
		blueOvershoot = 0f;
		chromaOvershoot = 0f;
	}
	
	// GETTER & SETTERS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return the red
	 */
	public float getRed() {
		return red;
	}
	/**
	 * @return the red overshoot
	 */
	public float getRedOvershoot() {
		return redOvershoot;
	}
	/**
	 * @return the green
	 */
	public float getGreen() {
		return green;
	}
	/**
	 * @return the green overshoot
	 */
	public float getGreenOvershoot() {
		return greenOvershoot;
	}
	/**
	 * @return the blue
	 */
	public float getBlue() {
		return blue;
	}
	/**
	 * @return the blue overshoot
	 */
	public float getBlueOvershoot() {
		return blueOvershoot;
	}
	/**
	 * @return the chroma
	 */
	public float getChroma() {
		return chroma;
	}
	/**
	 * @return the chroma overshoot
	 */
	public float getChromaOvershoot() {
		return chromaOvershoot;
	}
	
	/**
	 * @param red
	 *            the red to set (kept in the range of 0 - 1)
	 */
	public void setRed(float red) {
		if (red < 0)
			red = 0;
		if (red > 1)
			red = 1;
		this.red = red;
	}
	/**
	 * @param redOvershoot
	 *            the red overshoot to set (kept in the range of 0 - 1)
	 */
	public void setRedOvershoot(float redOvershoot) {
		if (redOvershoot < 0)
			redOvershoot = 0;
		if (redOvershoot > 1)
			redOvershoot = 1;
		this.redOvershoot = redOvershoot;
	}
	/**
	 * @param green
	 *            the green to set (kept in the range of 0 - 1)
	 */
	public void setGreen(float green) {
		if (green < 0)
			green = 0;
		if (green > 1)
			green = 1;
		this.green = green;
	}
	/**
	 * @param greenOvershoot
	 *            the red overshoot to set (kept in the range of 0 - 1)
	 */
	public void setGreenOvershoot(float greenOvershoot) {
		if (greenOvershoot < 0)
			greenOvershoot = 0;
		if (greenOvershoot > 1)
			greenOvershoot = 1;
		this.greenOvershoot = greenOvershoot;
	}
	/**
	 * @param blue
	 *            the blue to set (kept in the range of 0 - 1)
	 */
	public void setBlue(float blue) {
		if (blue < 0)
			blue = 0;
		if (blue > 1)
			blue = 1;
		this.blue = blue;
	}
	/**
	 * @param blueOvershoot
	 *            the blue overshoot to set (kept in the range of 0 - 1)
	 */
	public void setBlueOvershoot(float blueOvershoot) {
		if (blueOvershoot < 0)
			blueOvershoot = 0;
		if (blueOvershoot > 1)
			blueOvershoot = 1;
		this.blueOvershoot = blueOvershoot;
	}
	/**
	 * @param chroma
	 *            the chroma to set (kept in the range of 0 - 1)
	 */
	public void setChroma(float chroma) {
		if (chroma < 0)
			chroma = 0;
		if (chroma > 1)
			chroma = 1;
		this.chroma = chroma;
	}
	/**
	 * @param chromaOvershoot
	 *            the chroma overshoot to set (kept in the range of 0 - 1)
	 */
	public void setChromaOvershoot(float chromaOvershoot) {
		if (chromaOvershoot < 0)
			chromaOvershoot = 0;
		if (chromaOvershoot > 1)
			chromaOvershoot = 1;
		this.chromaOvershoot = chromaOvershoot;
	}
	
	public String toString() {
		return "[(r:" + red + ", g:" + green + ", b:" + blue + ", c:" + chroma + ")] (ro:" + redOvershoot + ", go:" + greenOvershoot + ", bo:"
				+ blueOvershoot + ", co:" + chromaOvershoot + ")";
	}
	
}
