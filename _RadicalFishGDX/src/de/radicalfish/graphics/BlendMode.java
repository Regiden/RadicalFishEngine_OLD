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
package de.radicalfish.graphics;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;

/**
 * Simple enum for blend modes with method apply a blend mode. Note that {@link BlendMode#SUB} only works for GL20!
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 21.08.2012
 */
public enum BlendMode {
	
	/** This can be used if you want to use pre-multiplied alpha */
	PRE_MULTIPLY(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA),
	/** Normal drawing with alpha support. */
	NORMAL(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA),
	/** Draw adding the existing color to the new color. */
	ADD(GL10.GL_SRC_ALPHA, GL10.GL_ONE),
	/**
	 * Draw subtracting the existing color to the new color. Note for this the {@link GL20#glBlendEquation(int)} must be
	 * changed (which only works when using GL20
	 */
	SUB(GL10.GL_SRC_ALPHA, GL10.GL_ONE),
	/** Draws blending the new image into the old one by a factor of it's color. */
	SCREEN(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_COLOR),
	/** Draws blending the new image into the old one by a factor of it's color including the alpha of the source */
	SCREEN_ALPHA(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_COLOR),
	/** Draws by multiplying the source and destination color. */
	MULTIPLY(GL10.GL_ONE_MINUS_SRC_COLOR, GL10.GL_ONE_MINUS_SRC_ALPHA),
	/** Draws to using an alpha map, this disables blending and leaves only the alpha channel open for drawing. */
	ALPHA_MAP(-1, -1),
	/** Draws using alpha blending. */
	ALPHA_BLEND(GL10.GL_DST_ALPHA, GL10.GL_ONE_MINUS_DST_ALPHA);
	
	private final int src, dst;
	
	BlendMode(int src, int dst) {
		this.src = src;
		this.dst = dst;
	}
	
	public int src() {
		return src;
	}
	public int dst() {
		return dst;
	}
}
