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
import de.radicalfish.test.collisionnew.blocks.SimpleTile;
import de.radicalfish.test.world.Ball;
import de.radicalfish.util.MathUtil;
import de.radicalfish.world.World;
import de.radicalfish.world.map.EntityLayer;
import de.radicalfish.world.map.Layer;
import de.radicalfish.world.map.Map;
import de.radicalfish.world.map.MapListener;
import de.radicalfish.world.map.Tile;

public class SimpleMap implements Map {
	
	public List<Layer> layers;
	
	public Layer collision;
	public EntityLayer eLayer;
	
	public int width, height;
	public int tileSize;
	
	public String name = "simple";
	
	public SimpleMap() {
		
	}
	public SimpleMap(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void init(GameContext context, World world) {
		
		// used to create content, when loading a map this will not be called so no overhead.
		layers = new ArrayList<Layer>();
		layers.add(new SimpleLayer(width, height, "test1"));
		layers.add(new SimpleLayer(width, height, "test2"));
		layers.add(new SimpleLayer(width, height, "test3"));
		collision = new SimpleLayer(width, height, "collision");
		eLayer = new SimpleEntityLayer("entities");
		tileSize = 16;
		
		SimpleLayer.load();
		
		for(Layer layer : layers) {
			layer.setTileSet(new SimpleTileSet("test", SimpleLayer.sheet));
			for (int i = 0; i < layer.getTiles().length; i++) {
				for (int j = 0; j < layer.getTiles()[0].length; j++) {
					if (i == 0 && j == 0) {
						layer.getTiles()[i][j] = new AnimatedTileImpl(new int[] { 1000, 1000 }, new int[] { 0, 1 }, true);
					} else {
						layer.getTiles()[i][j] = new SimpleTile(MathUtil.random(0, 10));
					}
					
				}
			}
		}
		
		for (int i = 0; i < collision.getTiles().length; i++) {
			for (int j = 0; j < collision.getTiles()[0].length; j++) {
				if (i == 0 && j == 0) {
					collision.getTiles()[i][j] = new AnimatedTileImpl(new int[] { 1000, 1000 }, new int[] { 0, 1 }, true);
				} else {
					collision.getTiles()[i][j] = new SimpleTile(MathUtil.random(0, 10));
				}
				
			}
		}
		
		eLayer.getEntites().add(new Ball());
		eLayer.getEntites().add(new Ball());
		
	}
	
	@Override
	public void update(GameContext context, World world, GameDelta delta) throws SlickException {
		for(Layer layer : layers) {
			layer.update(context, world, delta);
		}
	}
	
	@Override
	public void render(GameContext context, World world, Graphics g) throws SlickException {
		for(Layer layer : layers) {
			layer.render(context, world, g);
		}
	}
	
	@Override
	public void destroy(GameContext context, World world) {}
	
	@Override
	public void addMapListener(MapListener listener) {}
	
	@Override
	public void removeMapListener(MapListener listener) {}
	
	public void removeAllListener() {}
	
	public String getName() {
		return name;
	}
	
	@Override
	public int getTileWidth() {
		return width;
	}
	
	@Override
	public int getTileHeight() {
		return height;
	}
	
	@Override
	public int getWidth() {
		return width * tileSize;
	}
	
	@Override
	public int getHeight() {
		return height * tileSize;
	}
	
	@Override
	public int getTileSize() {
		return tileSize;
	}
	
	@Override
	public Tile[][] getTiles(int layer) {
		return layers.get(layer).getTiles();
	}
	
	@Override
	public Tile getTileAt(int x, int y, int layer) {
		return layers.get(layer).getTileAt(x, y);
	}
	
	@Override
	public List<Layer> getLayers() {
		return layers;
	}
	
	@Override
	public Layer getLayer(int layer) {
		return layers.get(layer);
	}
	
	@Override
	public Layer getCollisionLayer() {
		return collision;
	}
	
	@Override
	public Tile getCollisionTileAt(int x, int y) {
		return collision.getTileAt(x, y);
	}
	
	@Override
	public EntityLayer getEntityLayer() {
		return eLayer;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public void setTileSize(int size) {
		tileSize = size;
	}
	
	@Override
	public void setSize(int tileWidth, int tileHeight) {
		width = tileWidth;
		height = tileHeight;
	}
	
	@Override
	public void setTileAt(int x, int y, int id, int layer) {
		layers.get(layer).setTileAt(x, y, id);
	}
	
	@Override
	public void setTileAt(int x, int y, Tile tile, int layer) {
		layers.get(layer).setTileAt(x, y, tile);
	}
	
	@Override
	public void setLayer(Layer layer, int layerIndex) {
		layers.set(layerIndex, layer);
	}
	
	@Override
	public void setLayers(List<Layer> layers) {
		this.layers = layers;
	}
	
	@Override
	public void setCollisionTileAt(int x, int y, int id) {
		collision.setTileAt(x, y, id);
	}
	
	@Override
	public void setCollisionTileAt(int x, int y, Tile tile) {
		collision.setTileAt(x, y, tile);
	}
	
	@Override
	public void setCollisionLayer(Layer layer) {
		collision = layer;
	}
	
	@Override
	public void setEntityLayer(EntityLayer layer) {
		eLayer = layer;
	}
	
}
