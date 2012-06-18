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
package de.radicalfish.world;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import de.radicalfish.context.GameContext;
import de.radicalfish.context.GameDelta;

/**
 * A Interface for cameras with some default methods a camera should have.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 03.06.2012
 */
public interface Camera {
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Updates the camera.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param world
	 *            the world the game play in
	 * @param delta
	 *            the {@link GameDelta} object holding the delta value.
	 */
	public void update(GameContext context, World world, GameDelta delta);
	/**
	 * Translate the context to the camera point if needed.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param world
	 *            the world the game play in
	 * @param g
	 *            the graphics context to apply the translation to
	 */
	public void translate(GameContext context, World world, Graphics g);
	/**
	 * Translate the context to the camera point if needed for tiled maps.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param world
	 *            the world the game play in
	 * @param g
	 *            the graphics context to apply the translation to
	 */
	public void translateMap(GameContext context, World world, Graphics g);
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return the current position of the camera.
	 */
	public Vector2f getCurrent();
	/**
	 * @return the current x position of the camera.
	 */
	public float getCurrentX();
	/**
	 * @return the current y position of the camera.
	 */
	public float getCurrentY();
	/**
	 * @return the target position of the camera.
	 */
	public Vector2f getTarget();
	/**
	 * @return the target x position of the camera.
	 */
	public float getTargetX();
	/**
	 * @return the target y position of the camera.
	 */
	public float getTargetY();
	
	/**
	 * @return the speed with which the camera moves.
	 */
	public float getSpeed();
	
	// SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @param world
	 *            the world the game runs in, can be used to check the bounds if needed
	 * @param x
	 *            the x position
	 * @param y
	 *            the y position
	 */
	public void setCurrent(World world, float x, float y);
	/**
	 * @param world
	 *            the world the game runs in, can be used to check the bounds if needed
	 * @param x
	 *            the x position
	 * @param y
	 *            the y position
	 * @param time
	 *            the time to move. can be <= 0 to use linear interpolation
	 */
	public void setTarget(World world, float x, float y, int time);
	/**
	 * @param speed
	 *            the speed to move the camera.
	 */
	public void setSpeed(float speed);
	
	/**
	 * Centers the camera to the entity.
	 * 
	 * @param world
	 *            the world the game runs in, can be used to check the bounds if needed
	 * @param x
	 *            the x position of the point to center to
	 * @param y
	 *            the y position of the point to center to
	 */
	public void centerCurrent(World world, float x, float y);
	/**
	 * Centers the camera to the entity.
	 * 
	 * @param world
	 *            the world the game runs in, can be used to check the bounds if needed
	 * @param x
	 *            the x position of the point to center to
	 * @param y
	 *            the y position of the point to center to
	 */
	public void centerTarget(World world, float x, float y, int time);
	
}
