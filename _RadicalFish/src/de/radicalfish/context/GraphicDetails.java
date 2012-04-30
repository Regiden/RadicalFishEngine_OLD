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
package de.radicalfish.context;

/**
 * Some default graphic details getter and setter.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 23.03.2012
 */
public interface GraphicDetails {
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return true if the system can handle FBO's.
	 */
	public boolean isFBOSupported();
	/**
	 * @return true if the game should use Post Processing.
	 */
	public boolean usePostProcessing();
	/**
	 * @return true if Shader are supported.
	 */
	public boolean isShaderSupported();
	/**
	 * @return true if the game should use Shader.
	 */
	public boolean useShaders();
	/**
	 * @return true if the game should use animations.
	 */
	public boolean useAnimations();
	/**
	 * @return true if the game should use effects.
	 */
	public boolean useEffects();
	
	// SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Set if FBO is supported.
	 */
	public void setFBOSupported(boolean value);
	/**
	 * Set if Shader are supported.
	 */
	public void setShaderSupported(boolean value);
	/**
	 * If the system does not support Shader this should always return false.
	 */
	public void setUseShader(boolean value);
	/**
	 * if the system does not support FBO's this should always return false.
	 */
	public void setUsePostProcessing(boolean value);
	/**
	 * true if animations should be used in the game. TODO change to int for a finer tuning?
	 */
	public void setUseAnimations(boolean value);
	/**
	 * true if effects should be used in the game. TODO change to int for a finer tuning?
	 */
	public void setUseEffects(boolean value);
}
