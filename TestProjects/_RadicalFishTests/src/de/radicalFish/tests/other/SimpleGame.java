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
package de.radicalfish.tests.other;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.radicalfish.BasicGame;
import de.radicalfish.GameContainer;
import de.radicalfish.GameInput;
import de.radicalfish.graphics.Graphics;

/**
 * This is the example used in the README.md of the main package.
 */
public class SimpleGame extends BasicGame {
	
	private Texture texture;
	private Sprite sprite;
	
	private Vector2 position;
	
	public void init(GameContainer container) {
		position = new Vector2(100, 100);
		
		texture = new Texture("data/block.png");
		sprite = new Sprite(texture);
		sprite.setPosition(position.x, position.y);
		
		// don't forget to flip since we are on y-down by default!
		sprite.flip(false, true);
	}
	public void update(GameContainer container, float delta) {
		GameInput input = container.getInput();
		
		// we check if a key is down and move our sprite by a friction of 100 pixels per frame
		// thus we multiply with delta to get constant movement no matter the frame rate
		if(input.isKeyDown(Keys.LEFT)) {
			position.x -= 100f * delta;
		} else if(input.isKeyDown(Keys.RIGHT)) {
			position.x += 100f * delta;
		}
		if(input.isKeyDown(Keys.UP)) {
			position.y -= 100f * delta;
		} else if(input.isKeyDown(Keys.DOWN)) {
			position.y += 100f * delta;
		}
		
		sprite.setPosition(position.x, position.y);
	}
	
	
	public void render(GameContainer container, Graphics g) {
		SpriteBatch batch = g.getSpriteBatch();
		
		
		batch.begin();
		{
			sprite.draw(batch);
		}
		batch.end();
	}
	
	// lets override some of the method BasicGame helpfully hides for us
	// lets first override the dispose method and dispose our stuff!
	public void dispose() {
		texture.dispose();
	}
	// maybe the keyDown from the InputProcessor?
	public boolean keyDown(int keycode) {
		System.out.println("Pressed: " + GameInput.getKeyNameAsString(keycode) + " | code: " + keycode);
		return false; 
	}
	
}
