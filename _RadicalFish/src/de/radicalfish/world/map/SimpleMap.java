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
package de.radicalfish.world.map;
import java.util.Arrays;
import java.util.List;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.util.Log;
import de.radicalfish.context.GameContext;
import de.radicalfish.context.GameDelta;
import de.radicalfish.context.Settings;
import de.radicalfish.world.World;

/**
 * A simple map which contains the tiles with id's where an id is the index in a {@link SpriteSheet}. For collision this
 * map uses the <code>collisionmap.png</code> found in <code>de/radicalfish/assets/collisionmap.png</code>.
 * 
 * @author Stefan Lange
 * @version 0.0.0
 * @since 15.06.2012
 */
public class SimpleMap implements Map {
	
	private String name = "not loaded...";
	private int widthP = 0, heightP = 0, widthT = 0, heightT = 0;
	private int tileSize = 0;
	
	private Layer[] layers;
	
	
	
	/**
	 * Creates a new {@link SimpleMap} but does not load any data. use the <code>loadMap</code> method to do so.
	 */
	public SimpleMap() {}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void load(GameContext context, World world, String path) {
		
	}
	public void destroy(GameContext context, World world) {
		
	}
	public void update(GameContext context, World world, GameDelta delta) throws SlickException {
		if(!isLoaded(context.getSettings())) {
			return;
		}
	}
	public void render(GameContext context, World world, Graphics g) throws SlickException {
		if(!isLoaded(context.getSettings())) {
			return;
		}
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private boolean isLoaded(Settings settings) {
		if(layers == null && settings.getProperty("debug.map.checklayers", false)) {
			Log.info("No Data was loaded!");
			return false;
		}
		return true;
	}
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public String getName() {
		return name;
	}
	public int getWidth() {
		return widthP;
	}
	public int getHeight() {
		return heightP;
	}
	public int getTileWidth() {
		return widthT;
	}
	public int getTileHeight() {
		return heightT;
	}
	public int getTileSize() {
		return tileSize;
	}
	
	public Tile[][] getTiles(int layer) {
		if(layers == null)
			return null;
		return layers[layer].getTiles();
	}
	public Tile getTileAt(int x, int y, int layer) {
		if(layers == null)
			return null;
		return layers[layer].getTileAt(x, y);
	}
	
	public List<Layer> getLayers() {
		return Arrays.asList(layers);
	}
	public Layer getLayer(int layer) {
		if(layers == null)
			return null;
		return layers[layer];
	}
	
	// SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void setTileAt(int x, int y, int id, int layer) {
		
	}
	public void setTileAt(int x, int y, Tile tile, int layer) {
		
	}
	public void setLayer(Layer layer, int layerIndex) {
		
	}
	
}
