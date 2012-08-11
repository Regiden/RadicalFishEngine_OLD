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
import org.newdawn.slick.Image;
import de.radicalfish.context.Resources;

/**
 * A TileSet is a sheet which contains all tiles on a layer.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 04.07.2012
 */
public interface TileSet {
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return the name of the TileSet.
	 */
	public String getName();
	
	/**
	 * @return the resource name of the class if the {@link Resources} class is used. This will be used by the
	 *         {@link MapIO} class. cannot be null.
	 */
	public String getResourceName();
	/**
	 * @return the location where the TileSet is on the file system. this will be used by the {@link MapIO} class. But
	 *         it will not be set when loading a map. For Image classes from Slick just use
	 *         {@link Image#getResourceReference()}. cannot be null.
	 */
	public String getResourceLocation();
	
	/**
	 * Utility for Slick2D, use {@link TileSet#draw(int)} or {@link TileSet#draw(int, int)} for direct drawing.
	 * 
	 * @return the sub image of the TileSet at <code>index</code>.
	 */
	public Image getTileAt(int index);
	/**
	 * Utility for Slick2D, use {@link TileSet#draw(int)} or {@link TileSet#draw(int, int)} for direct drawing.
	 * 
	 * @return the sub image of the TileSet at <code>x</code>, <code>y</code>.
	 */
	public Image getTileAt(int x, int y);
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Sets the name of the TileSet.
	 */
	public void setName(String name);
	/**
	 * Sets the resource name to use.
	 * 
	 * @param name
	 *            the resource name if any
	 */
	public void setResourceName(String name);
	
}
