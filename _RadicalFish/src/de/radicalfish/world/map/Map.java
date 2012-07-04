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
import java.util.List;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import de.radicalfish.context.GameContext;
import de.radicalfish.context.GameDelta;
import de.radicalfish.world.EntitySystem;
import de.radicalfish.world.World;

/**
 * Interface for maps returned by the {@link World}. A map should render and update all entities it contains. All
 * entities a map has should be mapped to {@link EntitySystem}s. So there is no need to update the systems manually. But
 * you call also handle that by yourself, e.g. updating everything in the world object and only update the object in the
 * gameplay state or something.
 * 
 * @author Stefan Lange
 * @version 0.0.0
 * @since 15.06.2012
 */
public interface Map {
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Loads this map. This should also set up {@link EntitySystem}s used by maps. Note you can use the
	 * <code>destroy</code> method to unload another map before to save memory.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param world
	 *            the world the game plays in
	 * @param path
	 *            the path from which the map should be loaded.
	 */
	public void load(GameContext context, World world, String path);
	/**
	 * Updates the map. This should also update all entities on the map.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param world
	 *            the world the game plays in
	 * @param delta
	 *            the {@link GameDelta} object holding the delta value
	 */
	public void update(GameContext context, World world, GameDelta delta) throws SlickException;
	/**
	 * Renders the map. This should also render all the entities. You can use the {@link EntitySystem}s to store
	 * entities.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param world
	 *            the world the game plays in
	 * @param g
	 *            the graphics context to draw to
	 * @throws SlickException
	 */
	public void render(GameContext context, World world, Graphics g) throws SlickException;
	/**
	 * Unloads all content. This can be used to flush {@link EntitySystem}s from entities and make one map object for all
	 * maps to save memory.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param world
	 *            the world the game plays in
	 */
	public void destroy(GameContext context, World world);
	
	/**
	 * Adds an {@link MapListener} to the map.
	 */
	public void addMapListener(MapListener listener);
	/**
	 * Removes and {@link MapListener} from the map.
	 */
	public void removeMapListener(MapListener listener);
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return name of the map.
	 */
	public String getName();
	/**
	 * @return the width of the map in tiles.
	 */
	public int getTileWidth();
	/**
	 * @return the height of the map in tiles.
	 */
	public int getTileHeight();
	/**
	 * @return the width of the map in pixels.
	 */
	public int getWidth();
	/**
	 * @return the height of the map in pixels.
	 */
	public int getHeight();
	
	/**
	 * @return the size of a tile.
	 */
	public int getTileSize();
	
	/**
	 * @return the tile data.
	 */
	public Tile[][] getTiles(int layer);
	/**
	 * @return the tile id at <code>x</code>, <code>y</code>.
	 */
	public Tile getTileAt(int x, int y, int layer);
	
	/**
	 * @return all layer contained in the map.
	 */
	public List<Layer> getLayers();
	/**
	 * @return the layer at the id <code>layer</code>.
	 */
	public Layer getLayer(int layer);
	
	/**
	 * @return the layer which contains the collision IDs for the map.
	 */
	public Layer getCollisionLayer();
	/**
	 * @return the collision tile at <code>x</code>, <code>y</code>.
	 */
	public Tile getCollisionTileAt(int x, int y);
	
	// SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Sets the name of the map.
	 */
	public void setName(String name);
	/**
	 * Sets the size of one tile;
	 */
	public void setTileSize(int size);
	
	/**
	 * Sets the id of a tile at <code>x</code>, <code>y</code> at the index <code>layer</code>.
	 */
	public void setTileAt(int x, int y, int id, int layer);
	/**
	 * Changes the tile at <code>x</code>, <code>y</code> at the index <code>layer</code>.
	 */
	public void setTileAt(int x, int y, Tile tile, int layer);
	/**
	 * Changes a whole layer at the index <code>layer</code>.
	 */
	public void setLayer(Layer layer, int layerIndex);
	/**
	 * Sets all the layers.
	 */
	public void setLayers(List<Layer> layers);
	
	/**
	 * Sets the id of a tile at <code>x</code>, <code>y</code> at the index <code>layer</code> in the collision layer.
	 */
	public void setCollisionTileAt(int x, int y, int id);
	/**
	 * Changes the tile at <code>x</code>, <code>y</code> at the index <code>layer</code> in the collision layer.
	 */
	public void setCollisionTileAt(int x, int y, Tile tile);
	/**
	 * Changes the collision layer to <code>layer</code>.
	 */
	public void setCollisionLayer(Layer layer);
	
}
