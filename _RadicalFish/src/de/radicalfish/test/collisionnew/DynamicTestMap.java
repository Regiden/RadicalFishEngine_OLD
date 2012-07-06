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
import java.util.Arrays;
import java.util.List;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import de.radicalfish.Grid;
import de.radicalfish.context.GameContext;
import de.radicalfish.context.GameDelta;
import de.radicalfish.test.collisionnew.blocks.SimpleTile;
import de.radicalfish.util.Utils;
import de.radicalfish.world.Camera;
import de.radicalfish.world.World;
import de.radicalfish.world.map.EntityLayer;
import de.radicalfish.world.map.Layer;
import de.radicalfish.world.map.Map;
import de.radicalfish.world.map.MapListener;
import de.radicalfish.world.map.Tile;
import de.radicalfish.world.map.TileSet;

/**
 * A test map which can me changed dynamically for testing. It has only one layer.
 * 
 * @author Stefan Lange
 * @version 0.0.0
 * @since 15.06.2012
 */
public class DynamicTestMap implements Map {
	private static final long serialVersionUID = 1L;

	private SpriteSheet sheet;
	
	private ArrayList<MapListener> listener;
	
	private Layer[] layers;
	private Layer collision;
	private Grid index, current;
	
	private String name = "not loaded...";
	private int widthP = 0, heightP = 0, widthT = 0, heightT = 0;
	private int tileSize = 0;
	
	/**
	 * Creates a new {@link DynamicTestMap} but does not load any data. use the <code>load</code> method to do so.
	 * 
	 * @param width
	 *            the width in tiles.
	 * @param height
	 *            the height in tiles.
	 * @throws SlickException
	 */
	public DynamicTestMap(int width, int height, int tileSize) throws SlickException {
		widthT = width;
		heightT = height;
		widthP = width * tileSize;
		heightP = height * tileSize;
		this.tileSize = tileSize;
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void init(GameContext context, World world) {
		sheet = context.getResources().getSpriteSheet("test-chipset");
		layers = new Layer[1];
		layers[0] = new SimpleLayer(widthT, heightT);
		collision = new SimpleLayer(widthT, heightT);
		listener = new ArrayList<MapListener>();
		index = new Grid();
		current = new Grid();
		fillFirst();
	}
	public void destroy(GameContext context, World world) {
		
	}
	public void update(GameContext context, World world, GameDelta delta) throws SlickException {
	
	}
	public void render(GameContext context, World world, Graphics g) throws SlickException {
		
		Camera cam = world.getCamera();
		cam.translateMap(context, world, g);
		current.set((int) cam.getCurrentX() / tileSize, (int) cam.getCurrentY() / tileSize);
		renderMap(-32, -32, current.x - 2, current.y - 2, 31, 24, context, world, g);
		
	}
	
	public void addMapListener(MapListener listener) {
		Utils.notNull("listener", listener);
		this.listener.add(listener);
	}
	public void removeMapListener(MapListener listener) {
		Utils.notNull("listener", listener);
		this.listener.remove(listener);
	}
	public void removeAllListener() {
		listener.clear();
	}
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void renderMap(float x, float y, int sx, int sy, int width, int height, GameContext context, World world, Graphics g)
			throws SlickException {
		
		for (int i = 0; i < layers.length; i++) {
			renderLayer(x, y, sx, sy, width, height, i);
			
			invokeOnLayerComplete(context, world, g, i);
			
		}
		
	}
	private void renderLayer(float x, float y, int sx, int sy, int width, int height, int layer) {
		sheet.startUse();
		for (int ty = 0; ty < height; ty++) {
			for (int tx = 0; tx < width; tx++) {
				if ((sx + tx < 0) || (sy + ty < 0))
					continue;
				if ((sx + tx >= this.widthT) || (sy + ty >= this.heightT))
					continue;
				if (layers[layer].getTileAt(sx + tx, sy + ty).getTileID() < 0)
					continue;
				
				indexToPosition(layers[layer].getTileAt(sx + tx, sy + ty).getTileID(), index);
				
				sheet.getSubImage(index.x, index.y).drawEmbedded(x + (tx * tileSize), y + (ty * tileSize), tileSize, tileSize);
			}
		}
		sheet.endUse();
	}
	
	private Grid indexToPosition(int index, Grid grid) {
		grid.x = index % sheet.getHorizontalCount();
		grid.y = index / sheet.getVerticalCount();
		return grid;
	}
	private void invokeOnLayerComplete(GameContext context, World world, Graphics g, int layer) throws SlickException {
		for (MapListener listener : this.listener) {
			listener.onLayerComplete(context, world, g, layer);
		}
	}
	private void fillFirst() {
		for (int i = 0; i < layers.length; i++) {
			Layer layer = layers[i];
			for (int x = 0; x < layer.getTiles().length; x++) {
				for (int y = 0; y < layer.getTiles()[0].length; y++) {
					if(y == layer.getTiles()[0].length -1) {
						layer.setTileAt(x, y, new SimpleTile(0));
					} else {
						layer.setTileAt(x, y, new SimpleTile(-1));
					}
				}
			}
			
		}
		for (int x = 0; x < collision.getTiles().length; x++) {
			for (int y = 0; y < collision.getTiles()[0].length; y++) {
				if(y == collision.getTiles()[0].length -1) {
					collision.setTileAt(x, y, new SimpleTile(0));
				} else {
					collision.setTileAt(x, y, new SimpleTile(-1));
				}
				
			}
		}
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
		if (layers == null)
			return null;
		return layers[layer].getTiles();
	}
	public Tile getTileAt(int x, int y, int layer) {
		if (layers == null)
			return null;
		return layers[layer].getTileAt(x, y);
	}
	
	public List<Layer> getLayers() {
		return Arrays.asList(layers);
	}
	public Layer getLayer(int layer) {
		if (layers == null)
			return null;
		return layers[layer];
	}
	
	public Layer getCollisionLayer() {
		return collision;
	}
	public Tile getCollisionTileAt(int x, int y) {
		if (collision == null)
			return null;
		return collision.getTileAt(x, y);
	}
	
	@Override
	public EntityLayer getEntityLayer() {
		return null;
	}
	
	// SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void setName(String name) {
		this.name = name;
	}
	public void setTileSize(int size) {}
	public void setTileAt(int x, int y, int id, int layer) {
		layers[layer].setTileAt(x, y, id);
	}
	public void setTileAt(int x, int y, Tile tile, int layer) {
		
	}
	public void setLayer(Layer layer, int layerIndex) {
		
	}
	@Override
	public void setLayers(List<Layer> layers) {}
	
	public void setCollisionTileAt(int x, int y, int id) {}
	public void setCollisionTileAt(int x, int y, Tile tile) {}
	public void setCollisionLayer(Layer layer) {}
	
	@Override
	public void setEntityLayer(EntityLayer layer) {}
	
	// INTERN CLASSES
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public static class SimpleLayer implements Layer {
		private static final long serialVersionUID = 1L;
		private Tile[][] tiles;
		
		public SimpleLayer(int width, int height) {
			tiles = new Tile[width][height];
		}
		
		public void update(GameContext context, World world, GameDelta delta) throws SlickException {}
		public void render(GameContext context, World world, Graphics g) throws SlickException {}
		
		
		public Tile[][] getTiles() {
			return tiles;
		}
		public Tile getTileAt(int x, int y) {
			return tiles[x][y];
		}
		
		public void setTileAt(int x, int y, int id) {
			tiles[x][y].setTileID(id);
		}
		public void setTileAt(int x, int y, Tile tile) {
			tiles[x][y] = tile;
		}
		
		public String getName() {
			return null;
		}
		public TileSet getTileSet() {
			return null;
		}
		
	}
	
}
