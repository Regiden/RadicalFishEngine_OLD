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
package de.radicalfish.tests.effects;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import de.radicalfish.effects.ToneModel;
import de.radicalfish.util.Utils;

/**
 * A simple effect which manipulates the tone of the sprite via the {@link ToneModel}. It uses an {@link ShaderProgram}
 * so you can use it in a {@link SpriteBatch}.
 * <p>
 * 2 files are needed for that in your internal path in a folder named "shaders":
 * <li>simple.vert: the vertex shader, you can fine the file in the android test project under assets!</li>
 * <li>toner.frag: the fragment shader, you can fine the file in the android test project under assets!</li>
 * <hr>
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 07.06.2012
 */
public class ToneShader {
	
	private ShaderProgram shader;
	private ToneModel tone;
	
	/**
	 * Creates the {@link ToneShader}. the {@link ToneModel} can't be null
	 */
	public ToneShader(ToneModel tone) {
		Utils.notNull("tone", tone);
		shader = new ShaderProgram(Gdx.files.internal("shaders/simple.vert"), Gdx.files.internal("shaders/toner.frag"));
		this.tone = tone;
	}
	
	/**
	 * Call this to set the uniforms on the shader.
	 */
	public void setUniforms() {
		shader.setUniformf("rgbc", tone.getRed(), tone.getGreen(), tone.getBlue(), tone.getChroma());
		shader.setUniformf("rgbcOver", tone.getRedOvershoot(), tone.getGreenOvershoot(), tone.getBlueOvershoot(), tone.getChromaOvershoot());
	}
	
	/**
	 * The shader uses for this effect.
	 * 
	 * @return
	 */
	public ShaderProgram getShader() {
		return shader;
	}
}
