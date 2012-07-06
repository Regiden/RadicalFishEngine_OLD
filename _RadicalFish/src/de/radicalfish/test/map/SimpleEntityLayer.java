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
package de.radicalfish.test.map;
import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import de.radicalfish.context.GameContext;
import de.radicalfish.context.GameDelta;
import de.radicalfish.test.world.Ball;
import de.radicalfish.world.Entity;
import de.radicalfish.world.World;
import de.radicalfish.world.map.EntityLayer;

public class SimpleEntityLayer implements EntityLayer {
	
	private List<Entity> entities;
	private String name;
	
	public SimpleEntityLayer(String name) {
		this.name = name;
		entities = new ArrayList<Entity>();
		
		entities.add(new Ball());
		entities.add(new Ball());
	}
	
	@Override
	public void update(GameContext context, World world, GameDelta delta) throws SlickException {
		
	}
	@Override
	public void render(GameContext context, World world, Graphics g) throws SlickException {
		
	}
	
	@Override
	public String getName() {
		return name;
	}
	@Override
	public List<Entity> getEntites() {
		return entities;
	}
	@Override
	public Entity getEntity(int index) {
		return entities.get(index);
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}
	@Override
	public void setEntity(int index, Entity entity) {
		entities.set(index, entity);
	}
	
}
