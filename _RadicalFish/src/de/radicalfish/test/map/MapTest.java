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
import org.newdawn.slick.SpriteSheet;
import de.radicalfish.context.DefaultGameDelta;
import de.radicalfish.context.GameDelta;
import de.radicalfish.world.map.Layer;
import de.radicalfish.world.map.Map;
import de.radicalfish.world.map.MapIO;
import de.radicalfish.world.map.MapIOListener;

public class MapTest extends BasicGame {
	
	private GameDelta delta;
	private Map map;
	
	public MapTest() {
		super("Map Test");
	}
	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new MapTest(), 800, 600, false);
		app.start();
	}
	
	public void init(GameContainer container) throws SlickException {
		
		delta = new DefaultGameDelta();
		
		map = new SimpleMap(30, 30);
		map.init(null, null);
		
		MapIO.saveMap("test.map", map);
		
		System.out.println(map.getLayer(0).getTileSet().getResourceLocation());
		System.out.println(map.getLayer(0).getTileSet().getSheet());
		
		Map map2 = MapIO.loadMap("test.map", new MapIOListener() {
			public SpriteSheet readTileSet(Layer layer, String resourceName, String resourceLocation) {
				System.out.println(resourceName);
				System.out.println(resourceLocation);
				
				SimpleLayer.load();
				return SimpleLayer.sheet;
			}
		});
		
		System.out.println(map2.getLayer(0).getTileSet().getResourceLocation());
		System.out.println(map2.getLayer(0).getTileSet().getSheet());
		
	}
	public void update(GameContainer container, int delta) throws SlickException {
		this.delta.update(null, null, delta);
		
		map.update(null, null, this.delta);
	}
	public void render(GameContainer container, Graphics g) throws SlickException {
		map.render(null, null, g);
	}
	
	
	
}