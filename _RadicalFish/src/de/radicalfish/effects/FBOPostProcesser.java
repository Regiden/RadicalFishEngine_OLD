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
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;
import de.radicalfish.context.GameContext;
import de.radicalfish.extern.FBO;

/**
 * A {@link PostProcesser} which uses an FrameBufferObject and an {@link Image} to draw to.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 07.06.2012
 */
public class FBOPostProcesser implements PostProcesser {
	
	private PostProcessingEffect effect;
	private FBO sceneBuffer;
	private Image sceneImage;
	
	private boolean usesEffect;
	
	/**
	 * Creates {@link FBO} object and an {@link Image} for the use and sets <code>isInUse</code> to true.
	 * 
	 * @throws SlickException
	 */
	public FBOPostProcesser(GameContext context) throws SlickException {
		if (!FBO.isSupported()) {
			throw new SlickException("FBO is not supported on this System");
		}
		
		sceneBuffer = new FBO(context.getContainerWidth(), context.getContainerHeight());
		sceneImage = new Image(sceneBuffer.getTexture());
	
		usesEffect = true;
		
		if(context.getSettings().isDebugging()) {
			Log.info("Created FBO for Post Processing with the size: [" + sceneBuffer.getWidth() + ", " + sceneBuffer.getHeight() + "]");
		}
	
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void bind(GameContext context, Graphics g) throws SlickException {
		sceneBuffer.bind();
	}
	public void unbind(GameContext context, Graphics g) throws SlickException {
		sceneBuffer.unbind();
	}
	public void flush(GameContext context, Graphics g) throws SlickException {
		unbind(context, g);
		renderScene(context, g);
	}
	public void renderScene(GameContext context, Graphics g) throws SlickException {
		if (sceneBuffer.isInUse()) {
			throw new SlickException("FBO was not unbind!");
		}
		
		if (isUseEffect() && effect != null) {
			effect.begin(context, g);
			sceneImage.draw();
			effect.end(context, g);
		} else {
			sceneImage.draw();
		}
		
	}
	public void updateSize(int width, int height) throws SlickException {
		Log.info("Updating FBO size...");
		
		sceneBuffer.release();
		sceneBuffer.getTexture().release();
		
		sceneBuffer = new FBO(width, height);
		sceneImage = new Image(sceneBuffer.getTexture());
		
		System.gc();
	}
	
	// GETTER & SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	@Override
	public boolean isUseEffect() {
		return usesEffect;
	}
	
	public void setEffect(PostProcessingEffect effect) {
		if (effect == null) {
			throw new NullPointerException("effect is null!");
		}
		this.effect = effect;
	}
	public void setUseEffect(boolean value) {
		usesEffect = value;
	}
	
}
