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
package de.radicalfish.test.world;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import de.radicalfish.Rectangle2D;
import de.radicalfish.context.GameContext;
import de.radicalfish.context.GameDelta;
import de.radicalfish.util.Utils;
import de.radicalfish.world.Entity;
import de.radicalfish.world.World;

public class Player extends Entity {
	
	private static final long serialVersionUID = -1475660341697080622L;
	
	private Image sprite;
	
	private Rectangle2D collsionbox;
	
	private float friction = 100f / 1000f;
	
	// GAME METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void init(GameContext context, World world) throws SlickException {
		animator.loadAnimations(context, "de/radicalfish/assets/ani.xml", null);
		setOffset(43, 48);
		setOffScreenRanges(64, 90);
	}
	public void doUpdate(GameContext context, World world, GameDelta delta) throws SlickException {
		handleInput(context.getInput(), delta.getDelta());
		
	}
	public void doRender(GameContext context, World world, Graphics g) throws SlickException {
		sprite = Entity.checkMissing(animator.getCurrentImage());
		
		Utils.pushMatrix();
		g.scale(2, 2);
		
		sprite.draw(position.x, position.y);
		
		Utils.popMatrix();
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void handleInput(Input input, float delta) {
		if (input.isKeyDown(Input.KEY_RIGHT)) {
			position.x += friction * delta;
		} else if (input.isKeyDown(Input.KEY_LEFT)) {
			position.x -= friction * delta;
		}
		if (input.isKeyDown(Input.KEY_DOWN)) {
			position.y += friction * delta;
		} else if (input.isKeyDown(Input.KEY_UP)) {
			position.y -= friction * delta;
		}
	}
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public Rectangle2D getCollisionBox(Rectangle2D target) {
		return calculateCollisionBox(collsionbox);
	}
	public int getCollisionWidth() {
		return 11;
	}
	public int getCollisionHeight() {
		return 32;
	}
	
	public boolean canCollide() {
		return true;
	}
	public int getLayer() {
		return 3;
	}
	
}
