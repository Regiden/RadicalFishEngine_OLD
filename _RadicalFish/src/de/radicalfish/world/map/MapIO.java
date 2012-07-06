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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;
import de.radicalfish.world.Entity;

/**
 * Writes and Reads Maps. MapIO will attempt to write all data provided by the Interfaces {@link Map}, {@link Layer},
 * {@link TileSet}, {@link Tile} and {@link EntityLayer}. For Entities MapIO will write all values provided by the
 * abstract class {@link Entity} (excluding all abstract methods, those will not be saved).
 * <p>
 * When reading the {@link MapIOListener} can be used to provided instances of the Interfaces (or the abstract Entity
 * class). This makes it possible to save the map in one Application and load in another with a complete different
 * Implementation.
 * <p>
 * All data provided via the getter in the Interfaces and the Entity class should never return a null object. This could
 * lead to errors while writing/reading maps.
 * <p>
 * MapIO can use GZIP to compress the file. For this {@link GZIPInputStream} and {@link GZIPOutputStream} are used. Use
 * the boolean parameters in the write/read methods to enable GZIP.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 04.07.2012
 */
public class MapIO {
	
	private static final int VERSION = 101;
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public static void writeMap(String path, Map map, boolean zip) throws SlickException {
		try {
			writeMap(new FileOutputStream(path), map, zip);
		} catch (Exception e) {
			throw new SlickException(e.getMessage(), e.getCause());
		}
	}
	public static void writeMap(OutputStream stream, Map map, boolean zip) throws SlickException {
		try {
			DataOutputStream dos = null;
			if (zip) {
				dos = new DataOutputStream(new GZIPOutputStream(stream));
			} else {
				dos = new DataOutputStream(new BufferedOutputStream(stream));
			}
			
			// version and time stamp.
			dos.writeInt(VERSION);
			dos.writeLong(new Date().getTime()); // time-stamp
			
			// write map
			writeMap(dos, map);
			
			dos.flush();
			dos.close();
			
		} catch (Exception e) {
			throw new SlickException(e.getMessage(), e.getCause());
		}
	}
	
	public static void readMap(String path, MapIOListener callback, boolean unzip) throws SlickException {
		readMap(ResourceLoader.getResourceAsStream(path), callback, unzip);
	}
	public static void readMap(InputStream stream, MapIOListener callback, boolean unzip) throws SlickException {
		try {
			DataInputStream dis = null;
			if (unzip) {
				dis = new DataInputStream(new GZIPInputStream(stream));
			} else {
				dis = new DataInputStream(new BufferedInputStream(stream));
			}
			
			int version = dis.readInt();
			
			if (version != VERSION) {
				Log.info("Version Mismatch! Current: " + VERSION + ", Read: " + version);
			}
			
			dis.close();
			
		} catch (Exception e) {
			throw new SlickException(e.getMessage(), e.getCause());
		}
	}
	
	// INTERN WRITE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private static void writeMap(DataOutputStream dos, Map map) throws SlickException {
		try {
			// class name for loading
			String className = map.getClass().getSimpleName();
			dos.writeInt(className.length());
			dos.writeBytes(className);
			
			// map name
			dos.writeInt(map.getName().length());
			dos.writeBytes(map.getName());
			
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
			EntityLayer el = map.getEntityLayer();
			int entities = el.getEntites().size();
			dos.writeInt(entities);
			for (int i = 0; i < entities; i++) {
				writeEntity(dos, el.getEntity(i));
			}
			
		} catch (Exception e) {
			throw new SlickException(e.getMessage(), e.getCause());
		}
	}
	private static void writeLayer(DataOutputStream dos, Layer layer) throws SlickException {
		try {
			// class name for loading
			String className = layer.getClass().getSimpleName();
			dos.writeInt(className.length());
			dos.writeBytes(className);
			
			// TileSet
			writeTileSet(dos, layer.getTileSet());
			
			// write tiles
			Tile[][] tiles = layer.getTiles();
			int numTiles = 0;
			if (tiles != null) {
				numTiles = tiles.length * tiles[0].length;
			}
			
			dos.writeLong(numTiles);
			if (numTiles > 0) {
				for (int i = 0; i < tiles.length; i++) {
					for (int j = 0; j < tiles[0].length; j++) {
						writeTile(dos, tiles[i][j]);
					}
				}
			}
			
		} catch (Exception e) {
			throw new SlickException(e.getMessage(), e.getCause());
		}
	}
	private static void writeTileSet(DataOutputStream dos, TileSet tileSet) throws SlickException {
		try {
			// class name for loading
			String className = tileSet.getClass().getSimpleName();
			dos.writeInt(className.length());
			dos.writeBytes(className);
			
			// resource name
			String resName = tileSet.getResourceName();
			dos.writeInt(resName.length());
			dos.writeBytes(resName);
			
			// resource location
			String resloc = tileSet.getResourceLocation();
			dos.writeInt(resloc.length());
			dos.writeBytes(resloc);
			
		} catch (Exception e) {
			throw new SlickException(e.getMessage(), e.getCause());
		}
	}
	private static void writeTile(DataOutputStream dos, Tile tile) throws SlickException {
		try {
			// class name for loading
			String className = tile.getClass().getSimpleName();
			dos.writeInt(className.length());
			dos.writeBytes(className);
			
			// write sub type later this can be changed to some custom saving and so on
			dos.writeInt(tile instanceof AnimatedTile ? 2 : 1);
			
			// write down the id.
			dos.writeInt(tile.getTileID());
			
			// is this tile has AnimatedTile as interface, save this extra infos
			if (tile instanceof AnimatedTile) {
				writeAnimatedTile(dos, (AnimatedTile) tile);
			}
		} catch (Exception e) {
			throw new SlickException(e.getMessage(), e.getCause());
		}
	}
	private static void writeAnimatedTile(DataOutputStream dos, AnimatedTile tile) throws SlickException {
		try {
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
			
		} catch (Exception e) {
			throw new SlickException(e.getMessage(), e.getCause());
		}
	}
	
	private static void writeEntity(DataOutputStream dos, Entity entity) throws SlickException {
		try {
			// class name for loading
			String className = entity.getClass().getSimpleName();
			dos.writeInt(className.length());
			dos.writeBytes(className);
			
			
			
		} catch (Exception e) {
			throw new SlickException(e.getMessage(), e.getCause());
		}
		
	}
	
	// INTERN READ
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	
}
