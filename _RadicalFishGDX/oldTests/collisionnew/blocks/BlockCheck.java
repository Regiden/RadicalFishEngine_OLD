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
package de.radicalfish.test.collisionnew.blocks;
import de.radicalfish.Grid;
import de.radicalfish.Rectangle2D;
import de.radicalfish.test.collisionnew.TileCollisionManager;
import de.radicalfish.world.Entity;
import de.radicalfish.world.map.Tile;

/**
 * Used in the {@link TileCollisionManager}.
 * 
 * @author Stefan Lange
 * @version 0.0.0
 * @since 18.06.2012
 */
public interface BlockCheck {
	
	/**
	 * Called to check collision in a specific tile and position the entity if a collision was found.
	 * 
	 * @param tiles
	 *            the tiles of the collision layer
	 * @param entity
	 *            the entity to check
	 * @param tile
	 *            the tile to check as rectangle. can be used to position the entity along the tile.
	 * @param position
	 *            the position at which the collision manager found the collision
	 * @param direction
	 *            the direction
	 * @param tileSize
	 *            the size if a tile
	 * @return true if the entity really collides with the tile (e.g. for slopes your need extra checks
	 */
	public boolean checkAndPosition(Tile[][] tiles, Entity entity, Rectangle2D tile, Grid position, int direction, int tileSize);
	
}
