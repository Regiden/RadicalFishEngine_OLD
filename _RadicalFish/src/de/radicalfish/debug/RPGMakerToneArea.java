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
import de.radicalfish.effects.ToneModel;

/**
 * A color area which previews color changes like the RPG Maker 2000. See {@link ToneModel} for more information on how
 * the area gets created.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 17.05.2012
 */
public class RPGMakerToneArea extends Widget {
	
	private DynamicImage image;
	private final ByteBuffer byteBuffer;
	private final IntBuffer colorBuffer; // the byte buffer as int buffer.
	
	private int width, height;
	
	private ToneModel toneModel;
	private float oldred, oldgreen, oldblue, oldchroma, oldredo, oldgreeno, oldblueo, oldchromo;
	
	// C'TOR
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Default with a width of 36 and a height of 16. a new {@link ToneModel} will created.
	 */
	public RPGMakerToneArea() {
		this(36, 16, new ToneModel());
	}
	/**
	 * Width and height are set to 36 an 16.
	 * 
	 * @param model
	 *            the ToneModel to use.
	 */
	public RPGMakerToneArea(ToneModel model) {
		this(36, 16, model);
	}
	/**
	 * A new {@link ToneModel} will be created.
	 * 
	 * @param width
	 *            the width of the texture. higher values <b>might</b> make the color area less stretched on taller
	 *            widgets. The minimum is 6 (for each color step)
	 * @param height
	 *            the height of the texture. The minimum is 8 (smaller values will not display brighter or darker
	 *            colors)
	 */
	public RPGMakerToneArea(int width, int height) {
		this(width, height, new ToneModel());
	}
	/**
	 * 
	 * @param width
	 *            the width of the texture. higher values <b>might</b> make the color area less stretched on taller
	 *            widgets. The minimum is 6 (for each color step)
	 * @param height
	 *            the height of the texture. The minimum is 8 (smaller values will not display brighter or darker
	 *            colors)
	 * @param model
	 *            the ToneModel to use.
	 */
	public RPGMakerToneArea(int width, int height, ToneModel model) {
		if (width < 6) {
			throw new IllegalArgumentException("widht must be greater or equal to 6: " + width);
		}
		if (model == null) {
			throw new NullPointerException("model is null!");
		}
		setTheme("colorarea");
		
		toneModel = model;
		this.width = width;
		this.height = height;
		byteBuffer = ByteBuffer.allocateDirect(width * height * 4);
		byteBuffer.order(ByteOrder.BIG_ENDIAN);
		colorBuffer = byteBuffer.asIntBuffer();
		
		oldred = 0.1f; // force initial update with this
		oldgreen = toneModel.getGreen();
		oldblue = toneModel.getBlue();
		oldchroma = toneModel.getChroma();
		oldredo = toneModel.getRedOvershoot();
		oldgreeno = toneModel.getGreenOvershoot();
		oldblueo = toneModel.getBlueOvershoot();
		oldchromo = toneModel.getChromaOvershoot();
		
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public boolean needsUpdate() {
		return oldred != toneModel.getRed() || oldgreen != toneModel.getGreen() || oldblue != toneModel.getBlue()
				|| oldchroma != toneModel.getChroma() || toneModel.getRedOvershoot() != oldredo || toneModel.getGreenOvershoot() != oldgreeno
				|| toneModel.getBlueOvershoot() != oldblueo || toneModel.getChromaOvershoot() != oldchromo;
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
		}
		if (image != null) {
			if (needsUpdate()) {
				updateImage();
			}
			// draw it across the complete widget.
			image.draw(getAnimationState(), getInnerX(), getInnerY(), getInnerWidth(), getInnerHeight());
		}
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void makeImage(GUI gui) {
		image = gui.getRenderer().createDynamicImage(width, height);
	}
	private void updateImage() {
		int length = width / 6; // we need 6 steps
		int index = 0;
		float lerp;
		
		// If anyone has a better idea of how to do this, just tell me ;)
		// What this basically does is lerping from one color to another while adding the overshoots and calculating the
		// chroma value.
		
		// red -> yellow
		for (int i = 0; i < length; i++) {
			lerp = lerp(0, toneModel.getGreen(), i / ((float) length));
			index = loopVertical(toneModel.getRed(), getRangedValue(lerp, toneModel.getGreenOvershoot()), toneModel.getBlueOvershoot(), index);
		}
		// yellow -> green
		for (int i = 0; i < length; i++) {
			lerp = lerp(toneModel.getRed(), 0, i / ((float) length));
			index = loopVertical(getRangedValue(lerp, toneModel.getRedOvershoot()), toneModel.getGreen(), toneModel.getBlueOvershoot(), index);
		}
		// green -> cyan
		for (int i = 0; i < length; i++) {
			lerp = lerp(0, toneModel.getBlue(), i / ((float) length));
			index = loopVertical(toneModel.getRedOvershoot(), toneModel.getGreen(), getRangedValue(lerp, toneModel.getBlueOvershoot()), index);
		}
		
		// cyan -> blue
		for (int i = 0; i < length; i++) {
			lerp = lerp(toneModel.getGreen(), 0, i / ((float) length));
			index = loopVertical(toneModel.getRedOvershoot(), getRangedValue(lerp, toneModel.getGreenOvershoot()), toneModel.getBlue(), index);
		}
		
		// blue -> violet
		for (int i = 0; i < length; i++) {
			lerp = lerp(0, toneModel.getRed(), i / ((float) length));
			index = loopVertical(getRangedValue(lerp, toneModel.getRedOvershoot()), toneModel.getGreenOvershoot(), toneModel.getBlue(), index);
		}
		
		// blue -> violet + adding remaining pixels
		int dif = Math.max(width - (index + length), 0);
		for (int i = 0; i < length + dif; i++) {
			lerp = lerp(toneModel.getBlue(), 0, i / ((float) (length + dif)));
			index = loopVertical(toneModel.getRed(), toneModel.getGreenOvershoot(), getRangedValue(lerp, toneModel.getBlueOvershoot()), index);
		}
		
		image.update(byteBuffer, DynamicImage.Format.RGBA);
		
		oldred = toneModel.getRed();
		oldgreen = toneModel.getGreen();
		oldblue = toneModel.getBlue();
		oldchroma = toneModel.getChroma();
		oldredo = toneModel.getRedOvershoot();
		oldgreeno = toneModel.getGreenOvershoot();
		oldblueo = toneModel.getBlueOvershoot();
		oldchromo = toneModel.getChromaOvershoot();
	}
	
	private float getRangedValue(float normal, float add) {
		return Math.max(Math.min(normal + add, 1f), 0f);
	}
	private float lerp(float current, float target, float ratio) {
		if (current == target)
			return current;
		return current * (1f - ratio) + target * ratio;
	}
	
	private int loopVertical(float r, float g, float b, int index) {
		if (height <= 8) {
			colorBuffer.put(index++, (toRGBChroma(r, g, b, 1f) << 8) | 0xFF);
		} else {
			float lerp;
			int split = height / 2;
			for (int i = 0; i < split; i++) {
				lerp = 1 - (i / ((float) split));
				colorBuffer.put(index + (i * width), (toRGBChromaLerpLight(r, g, b, 1f, lerp / 1.4f, 0.1f) << 8) | 0xFF);
			}
			for (int i = split; i < height; i++) {
				lerp = ((i - split) / ((float) split));
				colorBuffer.put(index + (i * width), (toRGBChromaLerpLight(r, g, b, 1f, lerp / 1.5f, 0.9f) << 8) | 0xFF);
			}
			index++;
		}
		
		return index;
	}
	
	private int toRGBChromaLerpLight(float r, float g, float b, float a, float ratio, float target) {
		// hack to remove the white gradient when chroma overshoot gets higher
		if (target >= 0.9f) {
			ratio = lerp(ratio, 0, toneModel.getChromaOvershoot());
		}
		r = getRangedValue(lerp(r, target, ratio), toneModel.getRedOvershoot());
		g = getRangedValue(lerp(g, target, ratio), toneModel.getGreenOvershoot());
		b = getRangedValue(lerp(b, target, ratio), toneModel.getBlueOvershoot());
		
		r = Math.min(toneModel.getRed(), r);
		g = Math.min(toneModel.getGreen(), g);
		b = Math.min(toneModel.getBlue(), b);
		return toRGBChroma(r, g, b, a);
	}
	private int toRGBChroma(float r, float g, float b, float a) {
		float gray = r * 0.30f + g * 0.59f + b * 0.11f;
		
		float nr = (r - gray) * toneModel.getChroma() + gray;
		float ng = (g - gray) * toneModel.getChroma() + gray;
		float nb = (b - gray) * toneModel.getChroma() + gray;
		
		nr = Math.min(1f, nr * (toneModel.getChromaOvershoot() + 1));
		ng = Math.min(1f, ng * (toneModel.getChromaOvershoot() + 1));
		nb = Math.min(1f, nb * (toneModel.getChromaOvershoot() + 1));
		
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
	 * @return the tone models used for this area
	 */
	public ToneModel getToneModel() {
		return toneModel;
	}
	/**
	 * @return the width of the color area
	 */
	public int getColorAreaWidth() {
		return width;
	}
	/**
	 * @return the height of the color area
	 */
	public int getColorAreaHeight() {
		return height;
	}
	/**
	 * @return the red
	 */
	public float getRed() {
		return toneModel.getRed();
	}
	/**
	 * @return the red overshoot
	 */
	public float getRedOverShoot() {
		return toneModel.getRedOvershoot();
	}
	/**
	 * @return the green
	 */
	public float getGreen() {
		return toneModel.getGreen();
	}
	/**
	 * @return the green overshoot
	 */
	public float getGreenOverShoot() {
		return toneModel.getGreenOvershoot();
	}
	/**
	 * @return the blue
	 */
	public float getBlue() {
		return toneModel.getBlue();
	}
	/**
	 * @return the blue overshoot
	 */
	public float getBlueOvershoot() {
		return toneModel.getBlueOvershoot();
	}
	/**
	 * @return the chroma
	 */
	public float getChroma() {
		return toneModel.getChroma();
	}
	/**
	 * @return the chroma overshoot
	 */
	public float getChromaOvershoot() {
		return toneModel.getChromaOvershoot();
	}
	
	/**
	 * @param model
	 *            the model to set (Can't be null)
	 */
	public void setToneModel(ToneModel model) {
		if (model == null) {
			throw new NullPointerException("model is null!");
		}
		toneModel = model;
	}
	/**
	 * @param red
	 *            the red to set (kept in the range of 0 - 1)
	 */
	public void setRed(float red) {
		toneModel.setRed(red);
	}
	/**
	 * @param redOvershoot
	 *            the red overshoot to set (kept in the range of 0 - 1)
	 */
	public void setRedOvershoot(float redOvershoot) {
		toneModel.setRedOvershoot(redOvershoot);
	}
	/**
	 * @param green
	 *            the green to set (kept in the range of 0 - 1)
	 */
	public void setGreen(float green) {
		toneModel.setGreen(green);
	}
	/**
	 * @param greenOvershoot
	 *            the green overshoot to set (kept in the range of 0 - 1)
	 */
	public void setGreenOvershoot(float greenOvershoot) {
		toneModel.setGreenOvershoot(greenOvershoot);
	}
	/**
	 * @param blue
	 *            the blue to set (kept in the range of 0 - 1)
	 */
	public void setBlue(float blue) {
		toneModel.setBlue(blue);
	}
	/**
	 * @param blueOvershoot
	 *            the blue overshoot to set (kept in the range of 0 - 1)
	 */
	public void setBlueOvershoot(float blueOvershoot) {
		toneModel.setBlueOvershoot(blueOvershoot);
	}
	/**
	 * @param chroma
	 *            the chroma to set (kept in the range of 0 - 1)
	 */
	public void setChroma(float chroma) {
		toneModel.setChroma(chroma);
	}
	/**
	 * @param chromaOvershoot
	 *            the chroma overshoot to set (kept in the range of 0 - 1)
	 */
	public void setChromaOvershoot(float chromaOvershoot) {
		toneModel.setChromaOvershoot(chromaOvershoot);
	}
}