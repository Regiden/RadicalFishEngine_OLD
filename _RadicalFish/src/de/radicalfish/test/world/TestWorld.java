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
package de.radicalfish.test.world;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import de.radicalfish.context.GameContext;
import de.radicalfish.context.GameDelta;
import de.radicalfish.test.collisionnew.DynamicTestMap;
import de.radicalfish.util.Utils;
import de.radicalfish.world.Camera;
import de.radicalfish.world.EntitySystem;
import de.radicalfish.world.World;
import de.radicalfish.world.map.Map;

public class TestWorld implements World {
	
	private HashMap<String, EntitySystem> systems;
	private ArrayList<EntitySystem> backing;
	
	private Camera camera;
	private Map map;
	
	public TestWorld() throws SlickException {
		camera = new TestCamera();
		systems = new HashMap<String, EntitySystem>();
		backing = new ArrayList<EntitySystem>();
		
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void init(GameContext context) throws SlickException {
		map = new DynamicTestMap(25, 19, 16);
		map.load(context, this, null);
	};
	public void update(GameContext context, GameDelta delta) throws SlickException {
		for(EntitySystem es : backing) {
			es.update(context, this, delta);
		}
		camera.update(context, this, delta);
	}
	public void render(GameContext context, Graphics g) throws SlickException {
		map.render(context, this, g);
	}

	public void addEntitySystem(String name, EntitySystem system) throws SlickException {
		Utils.notNull("name", name);
		Utils.notNull("system", system);
		if(systems.containsKey(name)) {
			throw new SlickException("EntitySystem with the name: " + name + " already exists!");
		}
		systems.put(name, system);
		backing.add(system);
	}
	public void removeEntitySystem(String name) throws SlickException {
		Utils.notNull("name", name);
		if(!systems.containsKey(name)) {
			throw new SlickException("EntitySystem with the name: " + name + " does not exist");
		}
		backing.remove(systems.remove(name));
	}
	
	
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public List<EntitySystem> getEntitySystems() {
		return backing;
	}
	public EntitySystem getEntitySystem(String name) {
		Utils.notNull("name", name);
		return systems.get(name);
	}
	public Camera getCamera() {
		return camera;
	}
	public Map getMap() {
		return map;
	}
	public int getTileSize() {
		return 16;
	}
	
}
