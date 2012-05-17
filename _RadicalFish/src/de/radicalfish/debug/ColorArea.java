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
package de.radicalfish.debug;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.renderer.DynamicImage;

/**
 * A color area which previews color changes. It also supports a chroma value in the range of 0.0 - 2.0. If the chroma
 * value is under 1.0 the area gets grayer. if the value if higher the 1.0 the area gets flooded with color.... some
 * sort of :D
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 17.05.2012
 */
public class ColorArea extends Widget {
	
	private DynamicImage image;
	private final ByteBuffer byteBuffer;
	private final IntBuffer colorBuffer; // the byte buffer as int buffer.
	
	private int samples = 360;
	
	private float red, green, blue, chroma;
	private float oldred, oldgreen, oldblue, oldchroma;
	boolean needsUpdate;
	
	/**
	 * Default with 512 samples.
	 */
	public ColorArea() {
		this(512);
	}
	/**
	 * @param samples
	 *            the number of samples to take. more will make the area less widespread on taller widgets.
	 */
	public ColorArea(int samples) {
		this.samples = samples;
		byteBuffer = ByteBuffer.allocateDirect(samples * 1 * 4); // we only need 1 height pixel
		byteBuffer.order(ByteOrder.BIG_ENDIAN);
		colorBuffer = byteBuffer.asIntBuffer();
		
		red = 1.0f;
		green = 1.0f;
		blue = 1.0f;
		chroma = 1.0f;
		
		oldred = red;
		oldgreen = green;
		oldblue = blue;
		oldchroma = chroma;
		
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public boolean needsUpdate() {
		return oldred != red || oldgreen != green || oldblue != blue || oldchroma != chroma;
	}
	
	// OVERRIDE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	@Override
	public void destroy() {
		super.destroy();
		if (image != null) {
			image.destroy();
			image = null;
		}
	}
	@Override
	protected void paintWidget(GUI gui) {
		if (image == null) {
			makeImage(gui);
			needsUpdate = true;
		}
		if (image != null) {
			if (needsUpdate() || needsUpdate) {
				updateImage();
			}
			// draw it across the complete widget.
			image.draw(getAnimationState(), getInnerX(), getInnerY(), getInnerWidth(), getInnerHeight());
		}
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void makeImage(GUI gui) {
		image = gui.getRenderer().createDynamicImage(samples, 1);
	}
	private void updateImage() {
		int length = samples / 6; // we need 6 steps
		int index = 0;
		float lerp;
		
		// Yes, I'am well aware that I can display a color space easier
		// BUT that would remove the ability to have the preview like you can see it now
		
		// red -> yellow
		for (int i = 0; i < length; i++) {
			lerp = lerp(0, green, i / ((float) length));
			colorBuffer.put(index++, (toRGBChroma(red, lerp, 0, 1f, chroma) << 8) | 0xFF);
		}
		// yellow -> green
		for (int i = 0; i < length; i++) {
			lerp = lerp(red, 0, i / ((float) length));
			colorBuffer.put(index++, (toRGBChroma(lerp, green, 0, 1f, chroma) << 8) | 0xFF);
		}
		// green -> cyan
		for (int i = 0; i < length; i++) {
			lerp = lerp(0, blue, i / ((float) length));
			colorBuffer.put(index++, (toRGBChroma(0, green, lerp, 1f, chroma) << 8) | 0xFF);
		}
		
		// cyan -> blue
		for (int i = 0; i < length; i++) {
			lerp = lerp(green, 0, i / ((float) length));
			colorBuffer.put(index++, (toRGBChroma(0, lerp, blue, 1f, chroma) << 8) | 0xFF);
		}
		
		// blue -> violet
		for (int i = 0; i < length; i++) {
			lerp = lerp(0, red, i / ((float) length));
			colorBuffer.put(index++, (toRGBChroma(lerp, 0, blue, 1f, chroma) << 8) | 0xFF);
		}
		
		// fill empty space
		int dif = Math.max(samples - (index + length), 0);
		// blue -> violet
		for (int i = 0; i < length + dif; i++) {
			lerp = lerp(blue, 0, i / ((float) (length + dif)));
			colorBuffer.put(index++, (toRGBChroma(red, 0, lerp, 1f, chroma) << 8) | 0xFF);
		}
		
		image.update(byteBuffer, DynamicImage.Format.RGBA);
		
		oldred = red;
		oldgreen = green;
		oldblue = blue;
		oldchroma = chroma;
		needsUpdate = false;
	}
	
	private float lerp(float current, float target, float ratio) {
		if (current == target)
			return current;
		return current * (1f - ratio) + target * ratio;
	}
	
	private int toRGBChroma(float r, float g, float b, float a, float chroma) {
		float gray = r * 0.30f + g * 0.59f + b * 0.11f;
		
		float chromalow = Math.min(1, chroma);
		float chromahigh = Math.max(chroma - 1, 0) + 1;
		
		float nr = (r - gray) * chromalow + gray;
		float ng = (g - gray) * chromalow + gray;
		float nb = (b - gray) * chromalow + gray;
		
		nr = Math.min(1f, nr * chromahigh);
		ng = Math.min(1f, ng * chromahigh);
		nb = Math.min(1f, nb * chromahigh);
		
		return toColorInt(nr, ng, nb, a);
	}
	private int toColorInt(float r, float g, float b, float a) {
		int r2 = (int) (r * 255 + 0.5);
		int g2 = (int) (g * 255 + 0.5);
		int b2 = (int) (b * 255 + 0.5);
		int a2 = (int) (a * 255 + 0.5);
		int value = ((a2 & 0xFF) << 24) | ((r2 & 0xFF) << 16) | ((g2 & 0xFF) << 8) | ((b2 & 0xFF) << 0);
		return value;
	}
	
	// GETTER & SETTERS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return the samples
	 */
	public int getSamples() {
		return samples;
	}
	/**
	 * @return the red
	 */
	public float getRed() {
		return red;
	}
	/**
	 * @return the green
	 */
	public float getGreen() {
		return green;
	}
	/**
	 * @return the blue
	 */
	public float getBlue() {
		return blue;
	}
	/**
	 * @return the chroma
	 */
	public float getChroma() {
		return chroma;
	}
	
	/**
	 * @param samples
	 *            the samples to set
	 */
	public void setSamples(int samples) {
		this.samples = samples;
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
	 * @param chroma
	 *            the chroma to set (kept in the range of 0 - 1)
	 */
	public void setChroma(float chroma) {
		if (chroma < 0)
			chroma = 0;
		if (chroma > 2)
			chroma = 2;
		this.chroma = chroma;
	}
	
}