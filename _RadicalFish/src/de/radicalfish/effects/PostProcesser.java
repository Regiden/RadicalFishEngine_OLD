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
import de.radicalfish.context.GameContext;

/**
 * Used to process an effect on the complete scene. Must be instanced when Slick2D was created. Can be enabled/disabled
 * via the <code>bind/unbind</code> methods. So if you use some sort of frame buffer you must unbind the PostProcesser
 * first and enable it after you done with your rendering on anothoer frame buffer.
 * <p>
 * The scene should have the size of the display. To update the size if the window size changes use
 * <code>updateSize()</code>.
 * <p>
 * To set the effect the PostProcesser should apply to the scene use the <code>setEffect</code> method. There is no
 * getter since you should handle the effect by a subclass of the {@link PostProcessingEffect} interface.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 07.06.2012
 */
public interface PostProcesser {
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Starts the use of the PostProcesser.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param g
	 *            graphics context. after this call the graphics context should be equal to the {@link PostProcesser}
	 *            graphics.
	 * @throws SlickException
	 */
	public void bind(GameContext context, Graphics g) throws SlickException;
	/**
	 * Ends the use of the PostProcesser. this methods does not draw the complete scene! use <code>flush</code>.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param g
	 *            the graphics context. after this call the graphics context will be returned to the standard.
	 * @throws SlickException
	 */
	public void unbind(GameContext context, Graphics g) throws SlickException;
	/**
	 * Ends the use of the PostProcesser and draw the scene to the screen.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param g
	 *            the graphics context. after this call the graphics context should be equal to the
	 *            {@link PostProcesser} graphics.
	 * @throws SlickException
	 */
	public void flush(GameContext context, Graphics g) throws SlickException;
	/**
	 * Renders the scene, can be used after unbind.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param g
	 *            the graphics context to render the scene to.
	 * @throws SlickException
	 */
	public void renderScene(GameContext context, Graphics g) throws SlickException;
	/**
	 * Updates the size of the scene.
	 * 
	 * @throws SlickException
	 */
	public void updateSize(int width, int height) throws SlickException;
	
	// GETTER & SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return true if the effect should be used.
	 */
	public boolean isUseEffect();
	
	/**
	 * Sets the effect to use. Can be null to ignore the effect.
	 */
	public void setEffect(PostProcessingEffect effect);
	/**
	 * Sets of the effect should be applied (if any is set).
	 */
	public void setUseEffect(boolean value);
	
}
