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
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import com.badlogic.gdx.math.Vector2;
import de.radicalfish.Grid;
import de.radicalfish.animation.Animator;
import de.radicalfish.debug.Logger;
import de.radicalfish.util.RadicalFishException;
import de.radicalfish.util.ResourceLoader;
import de.radicalfish.util.Utils;
import de.radicalfish.world.Entity;

/**
 * Writes and Reads Maps. MapIO will attempt to write all data provided by the Interfaces {@link Map}, {@link Layer},
 * {@link TileSet}, {@link Tile} and {@link EntityLayer}. For Entities MapIO will write all values provided by the
 * abstract class {@link Entity} (excluding all abstract methods, the {@link Animator} and the collision box. Those will
 * not be saved. all graphics should be loaded in the <code>init</code> method of the {@link Entity} class).
 * <p>
 * When reading the {@link MapIOReader} can be used to provided instances of the Interfaces (or the abstract Entity
 * class). This makes it possible to save the map in one Application and load in another with a complete different
 * Implementation.
 * <p>
 * All data provided via the getter in the Interfaces and the Entity class should never return a null object. This could
 * lead to errors while writing/reading maps.
 * <p>
 * MapIO can use GZIP to compress the file. For this {@link GZIPInputStream} and {@link GZIPOutputStream} are used. Use
 * the boolean parameters in the write/read methods to enable GZIP. Not using GZIP will leave the format quite open to
 * read for other applications (in other languages too of course).
 * <p>
 * Note: A TileSet can be null, in case a TileSet is null, "null" will be written into the file for the null TilSet.
 * When reading the {@link MapIOReader#getTileSetIntance(String, String, String)} method will not be invoked! This is
 * done so you can use a {@link Layer} as collision layer.
 * <p>
 * String will be written without UTF-8 (Could be a feature later...), so make sure all Strings are ASCII or something.
 * <p>
 * <hr>
 * Changelog:
 * <li>0.5 - revamped from Serialization to own writing.</li>
 * <li>0.6 - added null checks for writing</li>
 * <li>0.7 - added writing entities</li>
 * <li>1.0 - added reading maps</li>
 * <hr>
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 04.07.2012
 */
public class MapIO {
	
	/** If true, the methods will print logs while writing/reading the maps. */
	public static final boolean LOG = true;
	
	private static final int VERSION = 101;
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Saves a map to the specific <code>path</code>.
	 * 
	 * @param path
	 *            the path to save the map to.
	 * @param map
	 *            the map to save, cannot be null.
	 * @param zip
	 *            true if we want to use GZIP to compress the content of the file.
	 */
	public static void writeMap(String path, Map map, boolean zip) {
		Utils.notNull("path", path);
		Utils.notNull("map", map);
		
		try {
			writeMap(new FileOutputStream(path), map, zip);
		} catch (Exception e) {
			throw new RadicalFishException(e.getMessage(), e.getCause());
		}
	}
	/**
	 * Saves a map to the specific <code>stream</code>.
	 * 
	 * @param stream
	 *            the stream to use for saving
	 * @param map
	 *            the map to save, cannot be null
	 * @param zip
	 *            true if we want to use GZIP to compress the content of the file.
	 */
	public static void writeMap(OutputStream stream, Map map, boolean zip) {
		Utils.notNull("stream", stream);
		Utils.notNull("map", map);
		
		try {
			DataOutputStream dos = null;
			if (zip) {
				dos = new DataOutputStream(new GZIPOutputStream(stream));
			} else {
				dos = new DataOutputStream(new BufferedOutputStream(stream));
			}
			
			if (LOG) {
				Logger.info("Writing Map...");
			}
			
			log("Version: " + VERSION);
			
			// version and time stamp.
			dos.writeInt(VERSION);
			
			// write map
			writeMap(dos, map);
			
			dos.flush();
			dos.close();
			
			if (LOG) {
				Logger.info("Wrote Map: " + map.getName() + " (Layers: " + map.getLayers().size() + ", Entities: "
						+ map.getEntityLayer().getEntites().size() + ")");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RadicalFishException(e.getMessage());
		}
	}
	
	public static <T extends Map> T readMap(String path, boolean unzip, MapIOReader callback) {
		Utils.notNull("path", path);
		return readMap(ResourceLoader.getResourceAsStream(path), unzip, callback);
	}
	@SuppressWarnings("unchecked")
	public static <T extends Map> T readMap(InputStream stream, boolean unzip, MapIOReader callback) {
		Utils.notNull("stream", stream);
		Utils.notNull("callback", callback);
		
		try {
			DataInputStream dis = null;
			if (unzip) {
				dis = new DataInputStream(new GZIPInputStream(stream));
			} else {
				dis = new DataInputStream(new BufferedInputStream(stream));
			}
			
			if (LOG) {
				Logger.info("Loading Map...");
			}
			
			int version = dis.readInt();
			if (version != VERSION) {
				Logger.info("Version Mismatch! Current: " + VERSION + ", Read: " + version);
				Logger.info("Will try to load map anyway...");
			}
			log("Version: " + version);
			
			Map map = readMap(dis, callback);
			
			dis.close();
			
			return (T) map;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RadicalFishException(e.getMessage(), e.getCause());
		}
	}
	
	// INTERN READ
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private static Map readMap(DataInputStream dis, MapIOReader callback) throws IOException {
		Map map = callback.getMapInstance(readString(dis));
		Utils.notNull("map instance", map);
		
		// read infos
		map.setName(readString(dis));
		map.setTileSize(dis.readInt());
		map.setSize(dis.readInt(), dis.readInt());
		
		// read layers
		List<Layer> layers = new ArrayList<Layer>();
		int numLayers = dis.readInt();
		for (int i = 0; i < numLayers; i++) {
			layers.add(readLayer(dis, callback));
		}
		map.setLayers(layers);
		
		// read collision
		map.setCollisionLayer(readLayer(dis, callback));
		
		// read entity layer
		map.setEntityLayer(readEntityLayer(dis, callback));
		
		return map;
	}
	private static Layer readLayer(DataInputStream dis, MapIOReader callback) throws IOException {
		Layer layer = callback.getLayerIntance(readString(dis));
		Utils.notNull("layer instance", layer);
		
		layer.setName(readString(dis));
		layer.setTileSet(readTileSet(dis, callback));
		
		long numTiles = dis.readLong();
		int width = dis.readInt();
		int height = dis.readInt();
		
		Tile[][] tiles = new Tile[width][height];
		if (numTiles > 0) {
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					tiles[i][j] = readTile(dis, callback);
				}
			}
		}
		
		layer.setTiles(tiles);
		
		log("Read Layer: " + layer.getName() + " (Tiles: " + numTiles + ")");
		
		return layer;
		
	}
	private static TileSet readTileSet(DataInputStream dis, MapIOReader callback) throws IOException {
		String classname = readString(dis);
		if (classname.equals("null")) {
			return null;
		}
		
		TileSet set = callback.getTileSetIntance(classname, readString(dis), readString(dis));
		Utils.notNull("tileset instance", set);
		
		set.setName(readString(dis));
		
		log("Read TileSet: " + set.getName());
		return set;
	}
	private static Tile readTile(DataInputStream dis, MapIOReader callback) throws IOException {
		Tile tile = callback.getTileInstance(readString(dis), dis.readInt() == 1 ? "normal" : "animated");
		Utils.notNull("tile instance", tile);
		
		tile.setTileID(dis.readInt());
		
		if (tile instanceof AnimatedTile) {
			readAnimatedTile(dis, (AnimatedTile) tile);
		}
		
		return tile;
		
	}
	private static void readAnimatedTile(DataInputStream dis, AnimatedTile tile) throws IOException {
		int num = dis.readInt();
		int[] times = new int[num];
		for (int i = 0; i < times.length; i++) {
			times[i] = dis.readInt();
		}
		tile.setFrameTimes(times);
		
		num = dis.readInt();
		int[] indexes = new int[num];
		for (int i = 0; i < indexes.length; i++) {
			indexes[i] = dis.readInt();
		}
		tile.setIndexes(indexes);
		
	}
	private static EntityLayer readEntityLayer(DataInputStream dis, MapIOReader callback) throws IOException {
		EntityLayer layer = callback.getEntityLayerInstance(readString(dis));
		Utils.notNull("entity layer", layer);
		
		// set the name
		layer.setName(readString(dis));
		
		// read entities
		List<Entity> entities = new ArrayList<Entity>();
		int numEn = dis.readInt();
		for (int i = 0; i < numEn; i++) {
			entities.add(readEntity(dis, callback));
		}
		layer.setEntities(entities);
		
		log("Read Entity Layer: " + layer.getName() + " (Entities: " + numEn + ")");
		
		return layer;
	}
	private static Entity readEntity(DataInputStream dis, MapIOReader callback) throws IOException {
		Entity entity = callback.getEntityInstance(readString(dis));
		Utils.notNull("entity instance", entity);
		
		entity.setName(readString(dis));
		entity.setID(dis.readInt());
		entity.setGridPosition(dis.readFloat(), dis.readFloat());
		entity.setPosition(dis.readFloat(), dis.readFloat());
		entity.setOldPosition(dis.readFloat(), dis.readFloat());
		entity.setScreenPosition(dis.readFloat(), dis.readFloat());
		entity.setOffset(dis.readFloat(), dis.readFloat());
		entity.setOffScreenRanges(dis.readFloat(), dis.readFloat());
		entity.setDirection(dis.readFloat(), dis.readFloat());
		entity.setVelocity(dis.readFloat(), dis.readFloat());
		entity.setAcceleration(dis.readFloat(), dis.readFloat());
		entity.setYSortOffset(dis.readFloat());
		
		entity.setActive(dis.readBoolean());
		entity.setVisible(dis.readBoolean());
		entity.setAlive(dis.readBoolean());
		
		return entity;
		
	}
	
	// INTERN WRITE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private static void writeMap(DataOutputStream dos, Map map) throws IOException {
		Utils.notNull("map", map.getName());
		Utils.notNull("layers", map.getLayers());
		Utils.notNull("collision layer", map.getCollisionLayer());
		Utils.notNull("entity layer", map.getEntityLayer());
		
		// class name for loading
		writeString(dos, map.getClass().getSimpleName());
		
		// map name
		writeString(dos, map.getName());
		
		// information about the map
		dos.writeInt(map.getTileSize());
		dos.writeInt(map.getTileWidth());
		dos.writeInt(map.getTileHeight());
		
		// layers
		int layers = map.getLayers().size();
		dos.writeInt(layers);
		for (int i = 0; i < layers; i++) {
			writeLayer(dos, map.getLayer(i));
		}
		
		// write collision
		writeLayer(dos, map.getCollisionLayer());
		
		// write entities
		writeEntityLayer(dos, map.getEntityLayer());
	}
	private static void writeLayer(DataOutputStream dos, Layer layer) throws IOException {
		Utils.notNull("layer", layer);
		Utils.notNull("layer name", layer.getName());
		Utils.notNull("layer tiles", layer.getTiles());
		
		// class name for loading
		writeString(dos, layer.getClass().getSimpleName());
		
		// write layer name
		writeString(dos, layer.getName());
		
		// TileSet
		writeTileSet(dos, layer.getTileSet());
		
		// write tiles
		Tile[][] tiles = layer.getTiles();
		int numTiles = 0;
		if (tiles != null) {
			numTiles = tiles.length * tiles[0].length;
		}
		
		dos.writeLong(numTiles);
		dos.writeInt(tiles != null ? tiles.length : 0);
		dos.writeInt(tiles != null ? tiles[0].length : 0);
		if (numTiles > 0) {
			for (int i = 0; i < tiles.length; i++) {
				for (int j = 0; j < tiles[0].length; j++) {
					writeTile(dos, tiles[i][j]);
				}
			}
			
		}
		
		log("Wrote Layer: " + layer.getName() + " (Tiles: " + numTiles + ")");
		
	}
	private static void writeTileSet(DataOutputStream dos, TileSet tileSet) throws IOException {
		if (tileSet != null) {
			Utils.notNull("tilset name", tileSet.getName());
			Utils.notNull("tilset resource name", tileSet.getResourceName());
			Utils.notNull("tilset resource location", tileSet.getResourceLocation());
		}
		if (tileSet == null) {
			writeString(dos, "null");
			return;
		}
		
		// class name for loading
		writeString(dos, tileSet.getClass().getSimpleName());
		
		// resource name
		writeString(dos, tileSet.getResourceName());
		
		// resource location
		writeString(dos, tileSet.getResourceLocation());
		
		// write tileset name
		writeString(dos, tileSet.getName());
		
		log("Wrote TileSet: " + tileSet.getName());
	}
	private static void writeTile(DataOutputStream dos, Tile tile) throws IOException {
		Utils.notNull("tile", tile);
		
		// class name for loading
		writeString(dos, tile.getClass().getSimpleName());
		
		// write sub type later this can be changed to some custom saving and so on
		dos.writeInt(tile instanceof AnimatedTile ? 2 : 1);
		
		// write down the id.
		dos.writeInt(tile.getTileID());
		
		// is this tile has AnimatedTile as interface, save this extra infos
		if (tile instanceof AnimatedTile) {
			writeAnimatedTile(dos, (AnimatedTile) tile);
		}
	}
	private static void writeAnimatedTile(DataOutputStream dos, AnimatedTile tile) throws IOException {
		Utils.notNull("animated tile", tile);
		Utils.notNull("animated tile times array", tile.getFrameTimes());
		Utils.notNull("animated tile index array", tile.getIndexes());
		
		// times
		int[] times = tile.getFrameTimes();
		int num = 0;
		if (times != null) {
			num = times.length;
		}
		
		dos.writeInt(num);
		for (int i = 0; i < times.length; i++) {
			dos.writeInt(times[i]);
		}
		
		// indexes
		int[] indexes = tile.getIndexes();
		num = 0;
		if (indexes != null) {
			num = indexes.length;
		}
		
		dos.writeInt(num);
		for (int i = 0; i < indexes.length; i++) {
			dos.writeInt(indexes[i]);
		}
		
	}
	private static void writeEntityLayer(DataOutputStream dos, EntityLayer layer) throws IOException {
		// class name for loading
		writeString(dos, layer.getClass().getSimpleName());
		
		// name
		writeString(dos, layer.getName());
		
		// write entities
		int entities = layer.getEntites().size();
		dos.writeInt(entities);
		for (int i = 0; i < entities; i++) {
			writeEntity(dos, layer.getEntity(i));
		}
		
		log("Wrote Entity Layer: " + layer.getName() + " (Entities: " + entities + ")");
		
	}
	private static void writeEntity(DataOutputStream dos, Entity entity) throws IOException {
		Utils.notNull("entity", entity);
		// we don't need to do checks for the rest, since there get initialized by the abstract class anyway
		// is someone sets them to null he did something very wrong
		
		// class name for loading
		writeString(dos, entity.getClass().getSimpleName());
		
		// write name and id
		writeString(dos, entity.getName());
		dos.writeInt(entity.getID());
		
		// write positions, velocity, acceleration
		writeGrid(dos, entity.getGridPosition());
		writeVector(dos, entity.getPosition());
		writeVector(dos, entity.getOldPosition());
		writeVector(dos, entity.getScreenPosition());
		writeVector(dos, entity.getOffset());
		writeVector(dos, entity.getOffscreenRanges());
		writeVector(dos, entity.getDirection());
		writeVector(dos, entity.getVelocity());
		writeVector(dos, entity.getAcceleration());
		dos.writeFloat(entity.getYSortOffset());
		
		// write booleans
		dos.writeBoolean(entity.isActive());
		dos.writeBoolean(entity.isVisible());
		dos.writeBoolean(entity.isAlive());
		
	}
	
	// INTERN OTHER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private static void log(String log) {
		if (LOG) {
			Logger.none("\t" + log);
		}
	}
	
	private static String readString(DataInputStream dis) throws IOException {
		int size = dis.readInt();
		byte[] bytes = new byte[size];
		dis.read(bytes);
		return new String(bytes);
	}
	
	private static void writeString(DataOutputStream dos, String string) throws IOException {
		dos.writeInt(string.length());
		dos.writeBytes(string);
	}
	private static void writeGrid(DataOutputStream dos, Grid grid) throws IOException {
		dos.writeFloat(grid.x);
		dos.writeFloat(grid.y);
	}
	private static void writeVector(DataOutputStream dos, Vector2 vector) throws IOException {
		dos.writeFloat(vector.x);
		dos.writeFloat(vector.y);
	}
}
