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
package de.radicalfish.effects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.shader.ShaderProgram;
import de.radicalfish.context.GameContext;

/**
 * A simple effect which use the <code>getGameTone</code> method from the {@link GameContext} to manipulate the tone of
 * the scene. This is a nice effect to make the scene look like it is afternoon or night. the files for the shader can be
 * found under;
 * <p>
 * <code>de/radicalfish/assests/shader</code>.
 * 
 * The shader will not run if the graphic details does not support shader are disabled.
 * 
 * @author Stefan Lange
 * @version 0.0.0
 * @since 07.06.2012
 */
public class ToneShaderEffect implements PostProcessingEffect {
	
	private ShaderProgram toneShader;
	
	private boolean usedShader;
	
	public ToneShaderEffect() throws SlickException {
		toneShader = ShaderProgram.loadProgram("de/radicalfish/assets/shader/simple.vert", "de/radicalfish/assets/shader/toner.frag");
	}
	
	// INTERFACE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void begin(GameContext context, Graphics g) {
		if(context.getSettings().getGraphicDetails().useShader()) {
			toneShader.bind();
			ToneModel t = context.getGameTone();
			toneShader.setUniform4f("rgbc", t.getRed(), t.getGreen(), t.getBlue(), t.getChroma());
			toneShader.setUniform4f("rgbcOver", t.getRedOvershoot(), t.getGreenOvershoot(), t.getBlueOvershoot(), t.getChromaOvershoot());
			usedShader = true;
		}
		
	}
	public void end(GameContext context, Graphics g) {
		if(context.getSettings().getGraphicDetails().useShader() && usedShader) {
			toneShader.unbind();
		}
	}
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return the shader use fot the toning.
	 */
	public ShaderProgram getShader() {
		return toneShader;
	}
	
}
