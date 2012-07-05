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
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.ResourceLoader;
import de.radicalfish.util.IO_Object;
import de.radicalfish.util.Utils;

/**
 * Loads and saves maps. All Implementations of the {@link Map} Interface must be serializable. if some fields are not
 * serializable this method will throw an exception. When loading the Callback {@link MapIOListener} makes it possible
 * to load Tilesets be resource name or resource location.
 * 
 * @author Stefan Lange
 * @version 0.0.0
 * @since 04.07.2012
 */
public class MapIO {
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public static void saveMap(String path, Map map) throws SlickException {
		IO_Object.save(map, path);
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
		
		Map map = IO_Object.loadAs(stream);
		
		for (int i = 0; i < map.getLayers().size(); i++) {
			TileSet set = map.getLayer(i).getTileSet();
			set.setSheet(set.getResourceName(), listener.readTileSet(map.getLayer(i), set.getResourceName(), set.getResourceLocation()));
		}
		
		return (T) map;
	}
	
}
