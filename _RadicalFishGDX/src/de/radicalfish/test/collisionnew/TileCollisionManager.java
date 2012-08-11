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
package de.radicalfish.test.collisionnew;
import de.radicalfish.Grid;
import de.radicalfish.Rectangle2D;
import de.radicalfish.context.GameContext;
import de.radicalfish.extern.Bresenham;
import de.radicalfish.test.collisionnew.blocks.BlockCheck;
import de.radicalfish.test.collisionnew.blocks.SolidBlock;
import de.radicalfish.world.CollisionManager;
import de.radicalfish.world.Entity;
import de.radicalfish.world.World;
import de.radicalfish.world.map.Tile;

/**
 * A collision Manager used for maps and other tile- or grid-based systems.
 * 
 * @author Stefan Lange
 * @version 0.0.0
 * @since 16.06.2012
 */
public class TileCollisionManager implements CollisionManager {
	
	private static Bresenham breseham = new Bresenham();
	private static Grid foundPosition = new Grid();
	private static int foundID = 0;
	
	private Rectangle2D tile;
	private BlockCheck[] tilechecker;
	
	private int tileSize;
	private int[] plots;
	
	public TileCollisionManager(int tileSize) {
		this.tileSize = tileSize;
		plots = new int[4];
		tile = new Rectangle2D(0, 0, tileSize, tileSize);
		
		// init all tile checks. the index is equal to the id
		tilechecker = new BlockCheck[60];
		tilechecker[0] = new SolidBlock();
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public boolean checkCollision(GameContext context, World world, Entity entity) {
		return checkCollision(context, world, entity, true);
	}
	public boolean checkCollision(GameContext context, World world, Entity entity, boolean invokeCallbacks) {
		if (!entity.canCollide()) {
			return false;
		}
		
		Tile[][] tiles = world.getMap().getCollisionLayer().getTiles();
		boolean foundCollision = false;
		Rectangle2D rect = entity.getCollisionBox();
		
		// left and right
		rect.setSize(entity.getCollisionWidth(), entity.getCollisionHeight());
		rect.setX(entity.getPositionX() + entity.getOffsetX());
		if (hasEntityMoved(entity.getDirectionX())) {
			int dir = entity.getDirection().x > 0 ? 1 : 0;
			foundCollision = _checkCollision(tiles, rect, entity, dir, invokeCallbacks);
		}
		rect.setX(entity.getPositionX() + entity.getOffsetX());
		
		// up and down
		rect.setY(entity.getPositionY() + entity.getOffsetY());
		
		if (hasEntityMoved(entity.getDirectionY())) {
			int dir = entity.getDirection().y > 0 ? 3 : 2;
			foundCollision = _checkCollision(tiles, rect,entity, dir, invokeCallbacks);
			
		}
		rect.setY(entity.getPositionY() + entity.getOffsetY());
		
		// calback
		if (foundCollision && invokeCallbacks) {
			entity.onMapCollision(foundID, (int) foundPosition.x / tileSize, (int) foundPosition.y / tileSize);
		}
		
		return foundCollision;
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private boolean _checkCollision(Tile[][] tiles, Rectangle2D rect, Entity entity, int direction, boolean invokeCallbacks) {
		createPlotLine(rect, direction);
		int id = getFirstCollidingID(tiles);
		if (id >= 0) {
			return tilechecker[id].checkAndPosition(tiles, entity, tile, foundPosition, direction, tileSize);
		}
		return false;
	}
	private boolean hasEntityMoved(float direction) {
		return direction != 0.0f;
	}
	private void createPlotLine(Rectangle2D rect, int direction) {
		if (direction == 0) { // left
			plots[0] = rect.getX();
			plots[1] = rect.getY();
			plots[2] = rect.getX();
			plots[3] = rect.getBottom() - 1;
		} else if (direction == 1) { // right
			plots[0] = rect.getRight();
			plots[1] = rect.getY();
			plots[2] = rect.getRight();
			plots[3] = rect.getBottom() - 1;
		} else if (direction == 2) { // up
			plots[0] = rect.getX();
			plots[1] = rect.getY() - 1;
			plots[2] = rect.getRight() - 1;
			plots[3] = rect.getY() - 1;
		} else if (direction == 3) { // down
			plots[0] = rect.getX();
			plots[1] = rect.getBottom();
			plots[2] = rect.getRight() - 1;
			plots[3] = rect.getBottom();
			
		}
		breseham.plot(plots[0], plots[1], plots[2], plots[3]);
		
	}
	
	private int getFirstCollidingID(Tile[][] tiles) {
		int id = -1;
		while (breseham.next()) {
			if (breseham.getX() / tileSize < 0 || breseham.getY() / tileSize < 0) {
				continue;
			}
			id = tiles[breseham.getX() / tileSize][breseham.getY() / tileSize].getTileID();
			
			if (id >= 0) {
				foundID = id;
				foundPosition.set(breseham.getX(), breseham.getY());
				return id;
			}
		}
		return id;
	}
	
	// GETTER & SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public int getTileSize() {
		return tileSize;
	}
	public void setTileSize(int tileSize) {
		this.tileSize = tileSize;
	}
	
}
