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
package de.radicalfish.test.collisionnew;
import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.SlickException;
import de.radicalfish.context.GameContext;
import de.radicalfish.context.GameDelta;
import de.radicalfish.debug.PerformanceListener;
import de.radicalfish.util.Utils;
import de.radicalfish.world.Entity;
import de.radicalfish.world.EntitySystem;
import de.radicalfish.world.World;

/**
 * And entity system which invokes the makes a call to the maps collision manager after an entity was updated.
 * 
 * @author Stefan Lange
 * @version 0.0.0
 * @since 16.06.2012
 */
public class MapEntitySystem implements EntitySystem, PerformanceListener {
	
	private ArrayList<Entity> entities;
	private ArrayList<Entity> checkList;
	private ArrayList<Entity> nameList;
	
	private long time;
	
	public MapEntitySystem() {
		entities = new ArrayList<Entity>();
		checkList = new ArrayList<Entity>();
		nameList = new ArrayList<Entity>();
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void init(GameContext context, World world) throws SlickException {
		for (Entity e : entities) {
			e.init(context, world);
		}
	}
	public void update(GameContext context, World world, GameDelta delta) throws SlickException {
		long locTime = System.nanoTime();
		
		// idea from princec
		// no iterator because we check the list dynamically
		// one entity could add a new one and so on
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e.isAlive()) {
				e.update(context, world, delta);
				world.getMap().getCollisionManager().checkCollision(context, world, e);
				if (e.isAlive()) {
					checkList.add(e);
				}
			}
		}
		
		ArrayList<Entity> temp = entities;
		entities = checkList;
		checkList = temp;
		checkList.clear();
		
		time = (System.nanoTime() - locTime) / 1000;
	}
	
	public Entity addEntity(Entity e, GameContext context, World world) throws SlickException {
		Utils.notNull("entity", e);
		entities.add(e);
		e.init(context, world);
		return e;
	}
	public Entity removeEntity(Entity e) throws SlickException {
		Utils.notNull("entity", e);
		int index = entities.indexOf(e);
		if (index < 0) {
			throw new SlickException("Noch such Entity in the System: " + e);
		}
		return entities.remove(index);
	}
	public Entity removeEntity(int id) throws SlickException {
		for (Entity e : entities) {
			if (e.getID() == id) {
				entities.remove(e);
				return e;
			}
		}
		throw new SlickException("No such Entity with ID: " + id);
	}
	
	
	
	// PERFOMANCE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public long getMessuredTime() {
		return time;
	}
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public List<Entity> getEntities() throws SlickException {
		return entities;
	}
	public List<Entity> getEntities(String name) throws SlickException {
		Utils.notNull("name", name);
		nameList.clear();
		for (Entity e : entities) {
			if (e.getName().equals(name)) {
				nameList.add(e);
			}
		}
		return nameList;
	}
	public Entity getEntity(int id) throws SlickException {
		for (Entity e : entities) {
			if (e.getID() == id) {
				return e;
			}
		}
		throw new SlickException("No such Entity with ID: " + id);
	}
	
}
