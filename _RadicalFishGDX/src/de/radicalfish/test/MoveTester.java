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
package de.radicalfish.test;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class MoveTester {
	
	public Image block;
	public Vector2f position, velocity, acceleration;
	public float friction_a, friction_s, maxSpeed;
	
	public MoveTester() throws SlickException {
		friction_a = 0.001f;
		friction_s = 0.005f;
		maxSpeed = 0.25f;
		
		block = new Image("data/TESTBLOCK.png", false, Image.FILTER_NEAREST);
		position = new Vector2f();
		velocity = new Vector2f();
		acceleration = new Vector2f();
	}
	
	public void update(GameContainer container, int delta) {
		boolean pressedUpDown = false, pressedLeftRight = false;
		
		Input in = container.getInput();
		
		acceleration.scale(0);
		
		if (in.isKeyDown(Input.KEY_UP) || in.isKeyDown(Input.KEY_DOWN))
			pressedUpDown = true;
		if (in.isKeyDown(Input.KEY_LEFT) || in.isKeyDown(Input.KEY_RIGHT))
			pressedLeftRight = true;
		
		if (in.isKeyDown(Input.KEY_UP))
			acceleration.y -= friction_a * delta;
		if (in.isKeyDown(Input.KEY_DOWN))
			acceleration.y += friction_a * delta;
		if (in.isKeyDown(Input.KEY_LEFT))
			acceleration.x -= friction_a * delta;
		if (in.isKeyDown(Input.KEY_RIGHT))
			acceleration.x += friction_a * delta;
		
		velocity.x += acceleration.x * delta;
		velocity.y += acceleration.y * delta;
		if (Math.abs(velocity.x) > maxSpeed)
			velocity.x = maxSpeed * Math.signum(velocity.x);
		if (Math.abs(velocity.y) > maxSpeed)
			velocity.y = maxSpeed * Math.signum(velocity.y);
		
		position.x += velocity.x * delta;
		position.y += velocity.y * delta;
		
		if (!pressedLeftRight)
			velocity.x *= (1 - friction_s * delta);
		if (!pressedUpDown)
			velocity.y *= 1 - friction_s * delta;
	}
	public void render(GameContainer container, Graphics g, Color apply) {
		block.draw(position.x, position.y, apply);
	}
}
