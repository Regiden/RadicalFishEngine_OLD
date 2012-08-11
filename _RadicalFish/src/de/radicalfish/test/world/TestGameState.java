/*
 * Copyright (c) 2011, Stefan Lange
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
package de.radicalfish.test.world;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import de.radicalfish.Rectangle2D;
import de.radicalfish.context.GameContext;
import de.radicalfish.context.GameDelta;
import de.radicalfish.context.Settings;
import de.radicalfish.debug.PerformanceListener;
import de.radicalfish.state.GameState;
import de.radicalfish.util.GraphicUtils;
import de.radicalfish.world.Entity;
import de.radicalfish.world.EntitySystem;
import de.radicalfish.world.World;
import de.radicalfish.world.map.MapListener;

public class TestGameState extends GameState implements MapListener, PerformanceListener {
	
	private static Rectangle2D DEBUG_BOUNDS = new Rectangle2D();
	
	private enum MODE {
		PLAY, EDIT
	}
	
	private SpriteSheet chip;
	private Color bg = new Color(0, 153, 153);
	
	private Player player;
	private Rectangle2D chiprect;
	
	private MODE mode;
	
	private int index;
	private int gridX, gridY;
	private int mouseX, mouseY;
	
	private long time;
	
	public TestGameState(GameContext context, World world, int id) {
		super(context, world, id);
	}
	
	// GAME METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void init(GameContext context, World world) throws SlickException {
		player = new Player();
		
		world.getEntitySystem("map-all").addEntity(player, context, world);
		world.getMap().addMapListener(this);
		mode = MODE.PLAY;
		
		chip = context.getResources().getSpriteSheet("test-chipset");
		index = 0;
		
		chiprect = new Rectangle2D(40, 384, chip.getWidth(), chip.getHeight());
		
	}
	public void update(GameContext context, World world, GameDelta delta) throws SlickException {
		if (context.getInput().isKeyPressed(Input.KEY_F10)) {
			mode = MODE.PLAY;
		} else if (context.getInput().isKeyPressed(Input.KEY_F11)) {
			mode = MODE.EDIT;
		}
		
		if (mode == MODE.PLAY) {
			updateGame(context, world, delta);
		} else if (mode == MODE.EDIT) {
			updateEditor(context, world, delta);
		}
	}
	public void render(GameContext context, World world, Graphics g) throws SlickException {
		if (mode == MODE.PLAY) {
			renderGame(context, world, g);
		} else if (mode == MODE.EDIT) {
			renderEditor(context, world, g);
		}
		debug_draw_sprite_rectangle(world, context.getSettings(), g);
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void updateGame(GameContext context, World world, GameDelta delta) throws SlickException {
		world.update(context, delta);
		
		if (context.getInput().isKeyDown(Input.KEY_P)) {
			world.getEntitySystem("map-all").addEntity(new Ball(), context, world);
		}
	}
	private void renderGame(GameContext context, World world, Graphics g) throws SlickException {
		g.setColor(bg);
		g.resetTransform();
		g.scale(2, 2);
		g.fillRect(0, 0, context.getGameWidth(), context.getGameHeight());
		g.resetTransform();
		world.render(context, g);
	}
	
	private void updateEditor(GameContext context, World world, GameDelta delta) {
		gridX = context.getInput().getMouseX() / world.getTileSize() - 2;
		gridY = context.getInput().getMouseY() / world.getTileSize() - 2;
		
		mouseX = context.getInput().getMouseX();
		mouseY = context.getInput().getMouseY();
		
		Input in = context.getInput();
		
		if (in.isKeyDown(Input.KEY_LEFT)) {
			player.getPosition().x -= 0.1f * delta.getDelta();
		} else if (in.isKeyDown(Input.KEY_RIGHT)) {
			player.getPosition().x += 0.1f * delta.getDelta();
		}
		if (in.isKeyDown(Input.KEY_UP)) {
			player.getPosition().y -= 0.1f * delta.getDelta();
		} else if (in.isKeyDown(Input.KEY_DOWN)) {
			player.getPosition().y += 0.1f * delta.getDelta();
		}
		
		if (in.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			if (gridX >= 0 && gridY >= 0 && gridX < world.getMap().getTileWidth() && gridY < world.getMap().getTileHeight()) {
				world.getMap().setTileAt(gridX, gridY, index, 0);
				world.getMap().getCollisionLayer().setTileAt(gridX, gridY, index);
			}
			if (chiprect.contains(mouseX, mouseY)) {
				index = ((mouseY - chiprect.getY()) / world.getTileSize()) * chip.getHorizontalCount()
						+ ((mouseX - chiprect.getX()) / world.getTileSize());
			}
			
		}
		if (in.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
			if (gridX >= 0 && gridY >= 0 && gridX < world.getMap().getTileWidth() && gridY < world.getMap().getTileHeight()) {
				world.getMap().setTileAt(gridX, gridY, -1, 0);
				world.getMap().getCollisionLayer().setTileAt(gridX, gridY, -1);
			}
		}
		
		if (in.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			if (gridX >= 0 && gridY >= 0 && gridX < world.getMap().getTileWidth() && gridY < world.getMap().getTileHeight()) {
				world.getMap().setTileAt(gridX, gridY, index, 0);
				world.getMap().getCollisionLayer().setTileAt(gridX, gridY, index);
			}
		}
		if (in.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) {
			if (gridX >= 0 && gridY >= 0 && gridX < world.getMap().getTileWidth() && gridY < world.getMap().getTileHeight()) {
				world.getMap().setTileAt(gridX, gridY, -1, 0);
				world.getMap().getCollisionLayer().setTileAt(gridX, gridY, -1);
			}
		}
		
	}
	private void renderEditor(GameContext context, World world, Graphics g) throws SlickException {
		g.setColor(bg);
		g.fillRect(32, 32, world.getMap().getWidth(), world.getMap().getHeight());
		g.resetTransform();
		g.translate(32, 32);
		g.scale(0.5f, 0.5f);
		world.render(context, g);
		
		g.resetTransform();
		g.translate(32, 32);
		g.setColor(new Color(0, 0.5f, 1, 0.5f));
		if (gridX >= 0 && gridY >= 0 && gridX < world.getMap().getTileWidth() && gridY < world.getMap().getTileHeight()) {
			g.fillRect(gridX * world.getTileSize(), gridY * world.getTileSize(), world.getTileSize(), world.getTileSize());
		}
		
		g.resetTransform();
		g.setColor(Color.white);
		g.drawString("TILE: (" + gridX + ", " + gridY + ")", 440, 32);
		
		g.translate(20, 360);
		g.drawString("CURRENT TILE: ", 10, 10);
		
		g.setColor(bg);
		g.translate(20, 24);
		g.fillRect(0, 0, chip.getWidth(), chip.getHeight());
		chip.draw(0, 0);
		
		int sheetX = index % chip.getHorizontalCount();
		int sheetY = index / chip.getHorizontalCount();
		g.setColor(Color.white);
		g.drawRect(sheetX * world.getTileSize(), sheetY * world.getTileSize(), world.getTileSize(), world.getTileSize());
		
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void debug_draw_sprite_rectangle(World world, Settings settings, Graphics g) throws SlickException {
		// draw ALL rectangles
		if (settings.isDebugging() && settings.getProperty("debug.rect.collision", false)) {
			g.pushTransform();
			g.resetTransform();
			if (mode == MODE.PLAY) {
				g.scale(2, 2);
			} 
			for (EntitySystem es : world.getEntitySystems()) {
				for (Entity e : es.getEntities()) {
					e.calculateCollisionBox(DEBUG_BOUNDS);
					if(mode == MODE.EDIT) {
						GraphicUtils.drawRect(DEBUG_BOUNDS.getX() + 32, DEBUG_BOUNDS.getY() +32, DEBUG_BOUNDS.getWidth(), DEBUG_BOUNDS.getHeight(),
								Color.white, 1f);
					} else {
						GraphicUtils.drawRect(DEBUG_BOUNDS.getX(), DEBUG_BOUNDS.getY(), DEBUG_BOUNDS.getWidth(), DEBUG_BOUNDS.getHeight(),
								Color.white, 0.5f);
					}
					
				}
			}
			g.popTransform();
		}
	}
	
	// INTERFACE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public long getMessuredTime() {
		return time;
	}
	public void onLayerComplete(GameContext context, World world, Graphics g, int layer) throws SlickException {
		long time = System.nanoTime();
		for (EntitySystem es : world.getEntitySystems()) {
			for (Entity e : es.getEntities()) {
				g.resetTransform();
				if (mode == MODE.EDIT) {
					g.scale(0.5f, 0.5f);
					g.translate(64, 64);
				}
				
				e.render(context, world, g);
				
				g.resetTransform();
				if (mode == MODE.EDIT) {
					g.scale(0.5f, 0.5f);
				}
			}
		}
		this.time = (System.nanoTime() - time) / 1000;
		
	}
	
}
