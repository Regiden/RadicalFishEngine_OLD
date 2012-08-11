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
import de.radicalfish.world.Entity;
import de.radicalfish.world.map.Tile;

// if this is called there is a collision so just return true. and postion the entity.
public class SolidBlock implements BlockCheck {
	
	public boolean checkAndPosition(Tile[][] tiles, Entity entity, Rectangle2D tile, Grid position, int direction, int tileSize) {
		tile.setPosition((position.x / tileSize) * tileSize, (position.y / tileSize) * tileSize);
		
		if (direction == 0) {
			entity.setPositionX(tile.getRight() - entity.getOffsetX());
		} else if (direction == 1) {
			entity.setPositionX(tile.getX() - entity.getCollisionWidth() - entity.getOffsetX());
		} else if (direction == 2) {
			entity.setPositionY(tile.getBottom() - entity.getOffsetY());
		} else if (direction == 3) {
			entity.setPositionY(tile.getY() - entity.getCollisionHeight() -entity.getOffsetY());
		}
		
		
		return true;
	}
	
}
