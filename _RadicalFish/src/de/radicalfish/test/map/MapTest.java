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
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;
import de.radicalfish.context.DefaultGameDelta;
import de.radicalfish.context.GameDelta;
import de.radicalfish.debug.Logger;
import de.radicalfish.test.collisionnew.blocks.SimpleTile;
import de.radicalfish.test.world.Ball;
import de.radicalfish.world.Entity;
import de.radicalfish.world.map.EntityLayer;
import de.radicalfish.world.map.Layer;
import de.radicalfish.world.map.Map;
import de.radicalfish.world.map.MapIO;
import de.radicalfish.world.map.MapIOReader;
import de.radicalfish.world.map.Tile;
import de.radicalfish.world.map.TileSet;

public class MapTest extends BasicGame {
	
	private GameDelta delta;
	
	public MapTest() {
		super("Map Test");
	}
	public static void main(String[] args) throws SlickException {
		Logger.setLogging(true);
		Log.setVerbose(true);
		Log.setLogSystem(new Logger());
		
		AppGameContainer app = new AppGameContainer(new MapTest(), 800, 600, false);
		app.start();
	}
	
	public void init(GameContainer container) throws SlickException {
		delta = new DefaultGameDelta();

		Map map3 = new SimpleMap(50, 50);
		map3.init(null, null);
		map3.setName("lol-bot");
		
		MapIO.writeMap("test2.map", map3, false);
		SimpleMap map = MapIO.readMap("test2.map", false, new MapIOReader() {
			public TileSet getTileSetIntance(String classname, String resourceName, String resoureLocation) {
				return new SimpleTileSet(resourceName, SimpleLayer.sheet);
			}
			public Tile getTileInstance(String classname, String type) {
				if(type.equals("normal")) {
					return new SimpleTile();
				}
				return new AnimatedTileImpl();
			}
			public Map getMapInstance(String classname) {
				return new SimpleMap();
			}
			public Layer getLayerIntance(String classname) {
				return new SimpleLayer();
			}
			public EntityLayer getEntityLayerInstance(String classname) {
				return new SimpleEntityLayer();
			}
			public Entity getEntityInstance(String classname) {
				if(classname.equals("Ball")) {
					return new Ball();
				}
				return null;
			}
			
		});
		System.out.println(map.getName());
		
	}
	public void update(GameContainer container, int delta) throws SlickException {
		this.delta.update(null, null, delta);
		
	}
	public void render(GameContainer container, Graphics g) throws SlickException {
		
	}
	
}
