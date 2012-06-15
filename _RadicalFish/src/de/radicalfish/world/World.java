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
package de.radicalfish.world;
import java.util.List;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import de.radicalfish.context.GameContext;
import de.radicalfish.context.GameDelta;

/**
 * Describes a world the game plays in. A world will give the map the game currently plays on and all EntitySystems that
 * are used by the game. A world will <b>not be automatically updated and rendered!</b>. You must call update and render
 * somewhere on your own and also decided what would be rendered if you make a call. e.g. what should be rendered if
 * you call render and so on.
 * 
 * @author Stefan Lange
 * @version 0.0.0
 * @since 11.03.2012
 */
public interface World {
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Updates the world. No parameter for World here since we call this on the world anyway.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param delta
	 *            the {@link GameDelta} object holding the delta value
	 */
	public void update(GameContext context, GameDelta delta) throws SlickException;
	/**
	 * Renders the world. No parameter for World here since we call this on the world anyway.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param g
	 *            the graphics context to draw to
	 */
	public void render(GameContext context, Graphics g) throws SlickException;
	
	/**
	 * Adds an {@link EntitySystem} to the world.
	 * 
	 * @param name
	 *            the name of the system
	 * @throws SlickException
	 */
	public void addEntitySystem(String name, EntitySystem system) throws SlickException;
	/**
	 * Removes and {@link EntitySystem} for the world.
	 * 
	 * @param name
	 *            the name of the system
	 */
	public void removeEntitySystem(String name) throws SlickException;
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return a list of all system in use.
	 */
	public List<EntitySystem> getEntitySystems();
	/**
	 * @return the EntitySystem defined by <code>name</code>.
	 */
	public EntitySystem getEntitySystem(String name);
	/**
	 * @return the camera in use. Should never be null!
	 */
	public Camera getCamera();
	
	/**
	 * @return the size of a tile. can be ignored if the game does not requires tiles.
	 */
	public int getTileSize();
}
