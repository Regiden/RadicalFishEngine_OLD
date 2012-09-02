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
import java.util.List;
import de.radicalfish.context.GameContext;
import de.radicalfish.context.GameDelta;
import de.radicalfish.util.RadicalFishException;

/**
 * System to manage entities in the world
 * 
 * @author Stefan Lange
 * @version 0.0.0
 * @since 14.06.2012
 */
public interface EntitySystem {
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Initiate the entity system. this should call initiate on all entities.
	 * 
	 * @param context
	 *            the context the games runs in
	 * @param world
	 *            the world the game plays in
	 */
	public void init(GameContext context, World world) throws RadicalFishException;
	/**
	 * Updates all entities in this system.
	 * 
	 * @param context
	 *            the context the games runs in
	 * @param world
	 *            the world the game plays in
	 * @param delta
	 *            the {@link GameDelta} object holding the delta value
	 */
	public void update(GameContext context, World world, GameDelta delta) throws RadicalFishException;
	
	// ADDING & CHECKING
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Adds an entity to the world. This can call <code>init</code> on an entity.
	 * 
	 * @param context
	 *            the context the games runs in
	 * @param world
	 *            the world the game plays in
	 * 
	 * @return the entity added.
	 */
	public Entity addEntity(Entity e, GameContext context, World world) throws RadicalFishException;
	/**
	 * Removes an Entity from the world by it's object.
	 * 
	 * @return the entity removed.
	 */
	public Entity removeEntity(Entity e) throws RadicalFishException;
	/**
	 * Removes an Entity from the world by it's id (If ID's are used).
	 * 
	 * @return the entity removed.
	 */
	public Entity removeEntity(int id) throws RadicalFishException;
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return all entities contained in this system.
	 */
	public List<Entity> getEntities() throws RadicalFishException;
	/**
	 * @return an Entity by ID. ID's should be unique, meaning that only one entity should have a specific id.
	 */
	public Entity getEntity(int id) throws RadicalFishException;
	/**
	 * @return the entity by it's name. Can be more then one hence a lsit will be returned.
	 */
	public List<Entity> getEntities(String name) throws RadicalFishException;
	
}
