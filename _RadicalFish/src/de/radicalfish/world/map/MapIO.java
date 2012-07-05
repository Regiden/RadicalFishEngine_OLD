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

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.ResourceLoader;
import de.radicalfish.context.GameContext;
import de.radicalfish.context.GameDelta;
import de.radicalfish.util.Utils;
import de.radicalfish.world.World;

/**
 * Serves as saver and loader for maps which include the interface map. MapIO will try to save the map via the getters
 * from the interface. The RadicalFishEngine uses Java Serialization with some proxy objects to load or save the data.
 * Not that no image data will be saved. if a map was loaded, the <code>load</code> method of the
 * 
 * @author Stefan Lange
 * @version 0.0.0
 * @since 04.07.2012
 */
public class MapIO {
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public static void saveMap(String path, Map map) {
		
	}
	
	/**
	 * Loads a map frim a specific <code>path</code>. The path will be loaded via the {@link ResourceLoader}.
	 * 
	 * @param listener
	 *            the listener for loading, cannot be null
	 * @return the map as casted object.
	 * @throws SlickException
	 */
	public static <T extends Map> T loadMap(String path, MapIOListener listener) throws SlickException {
		Utils.notNull("path", path);
		return loadMap(ResourceLoader.getResourceAsStream(path), listener);
	}
	
	/**
	 * Loads a map from a specific <code>stream</code>.
	 * 
	 * @param listener
	 *            the listener for loading, cannot be null
	 * @return the map as casted object.
	 * @throws SlickException
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Map> T loadMap(InputStream stream, MapIOListener listener) throws SlickException {
		Utils.notNull("listener", listener);
		Utils.notNull("stream", stream);
		
		Map map = listener.getLoadableMap();
		
		try {
			// DataInputStream dis = new DataInputStream(stream);
			
		} catch (Exception e) {
			throw new SlickException(e.getMessage(), e.getCause());
		}
		
		return (T) map;
	}
	
	// PROXY
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public static class MapProxy implements Serializable, Map {
		private static final long serialVersionUID = 101L;
		
		private List<Layer> layers = new ArrayList<Layer>();
		private Layer collisionLayer;
		
		private String name;
		private int tileWidth, tileHeight, tileSize, width, height;
		
		public void load(GameContext context, World world, String path) {}
		public void update(GameContext context, World world, GameDelta delta) throws SlickException {}
		public void render(GameContext context, World world, Graphics g) throws SlickException {}
		public void destroy(GameContext context, World world) {}
		
		public void addMapListener(MapListener listener) {}
		public void removeMapListener(MapListener listener) {}
		
		public String getName() {
			return name;
		}
		public int getTileWidth() {
			return tileWidth;
		}
		public int getTileHeight() {
			return tileHeight;
		}
		public int getWidth() {
			return width;
		}
		public int getHeight() {
			return height;
		}
		public int getTileSize() {
			return tileSize;
		}
		public Tile[][] getTiles(int layer) {
			return layers.get(layer).getTiles();
		}
		public Tile getTileAt(int x, int y, int layer) {
			return layers.get(layer).getTileAt(x, y);
		}
		public List<Layer> getLayers() {
			return layers;
		}
		public Layer getLayer(int layer) {
			return layers.get(layer);
		}
		public Layer getCollisionLayer() {
			return collisionLayer;
		}
		public Tile getCollisionTileAt(int x, int y) {
			return collisionLayer.getTileAt(x, y);
		}
		
		public void setName(String name) {
			Utils.notNull("name", name);
			this.name = name;
		}
		public void setTileSize(int size) {
			tileSize = size;
		}
		public void setTileAt(int x, int y, int id, int layer) {
			layers.get(layer).setTileAt(x, y, id);
		}
		public void setTileAt(int x, int y, Tile tile, int layer) {
			Utils.notNull("tile", tile);
			layers.get(layer).setTileAt(x, y, tile);
		}
		public void setLayer(Layer layer, int layerIndex) {
			Utils.notNull("layer", layer);
			layers.set(layerIndex, layer);
		}
		public void setLayers(List<Layer> layers) {
			Utils.notNull("layers", layers);
			this.layers = layers;
		}
		public void setCollisionTileAt(int x, int y, int id) {
			collisionLayer.setTileAt(x, y, id);
		}
		public void setCollisionTileAt(int x, int y, Tile tile) {
			Utils.notNull("tile", tile);
			collisionLayer.setTileAt(x, y, tile);
		}
		public void setCollisionLayer(Layer layer) {
			Utils.notNull("layer", layer);
			collisionLayer = layer;
		}
		
	}
	public static class LayerProxy implements Serializable, Layer {
		private static final long serialVersionUID = 101L;
		
		public String getName() {
			return null;
		}
		
		public TileSet getTileSet() {
			return null;
		}
		
		public Tile[][] getTiles() {
			return null;
		}
		
		public Tile getTileAt(int x, int y) {
			return null;
		}
		
		public void setTileAt(int x, int y, int id) {}
		
		public void setTileAt(int x, int y, Tile tile) {}
		
	}
	public static class TileSetProxy implements Serializable, TileSet {
		private static final long serialVersionUID = 101L;
		
		public String getResourceName() {
			return null;
		}
		
		public String getResourceLocation() {
			return null;
		}
		
		public Image getTileAt(int index) {
			return null;
		}
		
		public Image getTileAt(int x, int y) {
			return null;
		}
		
	}
	public static class TileProxy implements Serializable, Tile {
		private static final long serialVersionUID = 101L;
		
		public void update(GameContext context, World world, GameDelta delta) throws SlickException {}
		
		public void render(GameContext context, World world, Graphics g) throws SlickException {}
		
		public int getTileID() {
			return 0;
		}
		
		public void setTileID(int id) {}
		
	}
	
}
