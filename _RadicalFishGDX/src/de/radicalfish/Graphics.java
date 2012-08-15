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
package de.radicalfish;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

/**
 * A wrapper for translating the context, drawing sprites with the {@link SpriteBatch} and displaying primitives.
 * It offers a Slick2D like drawing but also allows to batch sprites.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 08.08.2012
 */
public class Graphics {
	
	private SpriteBatch spriteBatch;
	private GraphicsContext gContext;
	
	private Vector3 origin;
	
	public Graphics(SpriteBatch spriteBatch, GraphicsContext gContext) {
		this.gContext = gContext;
		this.spriteBatch = spriteBatch;
		
		origin = new Vector3(gContext.position);
	}
	
	// METHODS
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	/**
	 * Resets the translation and only the translation!
	 */
	public void resetTranslate() {
		gContext.position.set(origin);
	}
	/**
	 * Translate the context to <code>x</code>, <code>y</code>.
	 */
	public void translate(float x, float y) {
		gContext.translate(-x, -y);
	}
	
	/**
	 * Resets the scale and only the scale!
	 */
	public void resetScale() {
		gContext.setScale(1f);
	}
	/**
	 * scales the context.
	 */
	public void scale(float scale) {
		gContext.scale(scale);
	}
	/**
	 * scales the context.
	 */
	public void scale(float x, float y) {
		gContext.scale(x, y);
	}
	/**
	 * sets the scale of the context.
	 */
	public void setScale(float scale) {
		gContext.setScale(scale);
	}
	/**
	 * sets the scale of the context.
	 */
	public void setScale(float x, float y) {
		gContext.setScale(x, y);
	}
	
	/**
	 * Resets the translation and the scale. 
	 * 
	 * @param apply
	 *            true if we want to apply the changes directly (on the batch too).
	 */
	public void resetTransform(boolean apply) {
		resetTranslate();
		resetScale();
		
		if(apply) {
			apply();
		}
		
	}
	/**
	 * Applies changes to both the context and the batch.
	 */
	public void apply() {
		applyContext();
		applyBatch();
	}
	/**
	 * Applies the Translations on the context.
	 */
	public void applyContext() {
		gContext.update();
	}
	/**
	 * Applies the context changes to the batch. This should be called as less as possible to avoid flushing the batch while batching.
	 */
	public void applyBatch() {
		spriteBatch.setProjectionMatrix(gContext.combined);

	}
	
	// GETTER
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}
	
	/**
	 * @return the current scale value.
	 */
	public float getScaleX() {
		return gContext.getScaleX();
	}
	public float getScaleY() {
		return gContext.getScaleY();
	}
}
