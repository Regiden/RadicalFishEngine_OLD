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
package de.radicalfish.util;

import org.newdawn.slick.Color;

/**
 * Class with some useful methods for manipulating colors.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 09.05.2012
 */
public class ColorUtil {
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Converts an Color to HSL value and puts into a float array were:
	 * 
	 * <pre>
	 * float[0] = hue 
	 * float[1] = saturation 
	 * float[2] = value
	 * </pre>
	 * 
	 * @param r
	 *            the red value
	 * @param g
	 *            the green value
	 * @param b
	 *            the blue value
	 * @return float array containing the HSL values.
	 */
	public static float[] convertRGBToHSL(float r, float g, float b) {
		float max = Math.max(Math.max(r, g), b);
		float min = Math.min(Math.min(r, g), b);
		
		float summe = max + min;
		float saturation = max - min;
		
		if (saturation > 0.0f) {
			saturation /= (summe > 1.0f) ? 2.0f - summe : summe;
		}
		return new float[] { 360f * getHue(r, g, b, max, min), 100f * saturation, 50f * summe };
	}
	/**
	 * Converts an Color to HSL value and puts into a float array were:
	 * 
	 * <pre>
	 * float[0] = hue 
	 * float[1] = saturation 
	 * float[2] = value
	 * </pre>
	 * 
	 * @param color
	 *            the color to transform
	 * @return float array containing the HSL values.
	 */
	public static float[] convertRGBToHSL(Color color) {
		return convertRGBToHSL(color.r, color.g, color.b);
	}

	/**
	 * Transforms a HSL to RGB.
	 * 
	 * @param color
	 *            the color to transform, must have a size of 3 were hue is the first index, saturation the second and
	 *            lightness the third.
	 * 
	 *            <pre>
	 * hue range = 0 - 360
	 * saturation range = 0 - 100
	 * lightness range = 0 - 100
	 * </pre>
	 * @return a new Color object holding the rgb values. alpha is set to 1.0f
	 */
	public static Color toRGB(float[] color) {
		float hue = color[0] / 360f;
		float saturation = color[1] / 100f;
		float lightness = color[2] / 100f;
		
		float r, g, b;
		
		if (saturation > 0.0f) {
			hue = (hue < 1.0f) ? hue * 6.0f : 0.0f;
			float q = lightness + saturation * ((lightness > 0.5f) ? 1.0f - lightness : lightness);
			float p = 2.0f * lightness - q;
			r = normalize(q, p, (hue < 4.0f) ? (hue + 2.0f) : (hue - 4.0f));
			g = normalize(q, p, hue);
			b = normalize(q, p, (hue < 2.0f) ? (hue + 4.0f) : (hue - 2.0f));
		} else {
			r = g = b = lightness;
		}
		
		System.out.println(r);
		System.out.println(g);
		System.out.println(b);
		
		return new Color(r, g, b, 1.0f);
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	// taken from TWL ColorSpaceHSL class
	private static float normalize(float q, float p, float color) {
		if (color < 1.0f) {
			return p + (q - p) * color;
		}
		if (color < 3.0f) {
			return q;
		}
		if (color < 4.0f) {
			return p + (q - p) * (4.0f - color);
		}
		return p;
	}
	// taken from TWL ColorSpaceHSL class
	private static float getHue(float red, float green, float blue, float max, float min) {
		float hue = max - min;
		if (hue > 0.0f) {
			if (max == red) {
				hue = (green - blue) / hue;
				if (hue < 0.0f) {
					hue += 6.0f;
				}
			} else if (max == green) {
				hue = 2.0f + (blue - red) / hue;
			} else /* max == blue */{
				hue = 4.0f + (red - green) / hue;
			}
			hue /= 6.0f;
		}
		return hue;
	}
}
