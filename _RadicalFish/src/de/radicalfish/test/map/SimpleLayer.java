package de.radicalfish.test.map;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import de.radicalfish.context.GameContext;
import de.radicalfish.context.GameDelta;
import de.radicalfish.test.collisionnew.blocks.SimpleTile;
import de.radicalfish.util.FastMath;
import de.radicalfish.world.World;
import de.radicalfish.world.map.Layer;
import de.radicalfish.world.map.Tile;
import de.radicalfish.world.map.TileSet;

public class SimpleLayer implements Layer {
	
	public static transient SpriteSheet sheet;
	
	private Tile[][] tiles;
	
	private String name;
	private TileSet tileset;
	
	public SimpleLayer(int width, int height, String name) {
		tiles = new Tile[width][height];
		this.name = name;
		
		load();
		tileset = new SimpleTileSet("test", sheet);
		
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				if (i == 0 && j == 0) {
					tiles[i][j] = new AnimatedTileImpl(new int[] { 1000, 1000 }, new int[] { 0, 1 }, true);
				} else {
					tiles[i][j] = new SimpleTile(FastMath.random(0, 10));
				}
				
			}
		}
	}
	
	public void update(GameContext context, World world, GameDelta delta) throws SlickException {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				tiles[i][j].update(context, world, delta);
			}
		}
	}
	public void render(GameContext context, World world, Graphics g) throws SlickException {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				tiles[i][j].render(context, world, g);
			}
		}
	}
	
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
		return name;
	}
	public TileSet getTileSet() {
		return tileset;
	}
	
	public static void load() {
		if (sheet == null) {
			try {
				sheet = new SpriteSheet("de/radicalfish/assets/collisionmap.png", 16, 16);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}
	
}