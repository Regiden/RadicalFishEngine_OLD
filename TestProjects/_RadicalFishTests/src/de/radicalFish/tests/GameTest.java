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
package de.radicalfish.tests;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.radicalfish.Game;
import de.radicalfish.GameContainer;
import de.radicalfish.context.Settings;
import de.radicalfish.context.defaults.DefaultSettings;
import de.radicalfish.graphics.Graphics;
import de.radicalfish.state.transitions.FadeTransition;
import de.radicalfish.state.transitions.FadeTransition.FADETYPE;
import de.radicalfish.tests.utils.RadicalFishTest;
import de.radicalfish.util.RadicalFishException;

/**
 * My testing ground.
 */
public class GameTest implements Game, RadicalFishTest {
	
	private Sprite sprite;
	private Texture part;
	
	private float scale;
	private Vector2 position, velocity;
	
	private FadeTransition fade;
	
	// GAME METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void init(GameContainer container) throws RadicalFishException {
		part = new Texture(Gdx.files.internal("data/particle.png"));
		sprite = new Sprite(part);
		sprite.flip(false, true);
		sprite.setOrigin(16, 16);
		
		position = new Vector2();
		velocity = new Vector2();
		scale = 1.0f;
		
		sprite.setColor(1, 0, 0, 1);
		fade = new FadeTransition(Color.RED, FADETYPE.FADE_OUT, 2.0f);
		
		Settings s = new DefaultSettings();
		s.printSettings();
		
	}
	
	public void update(GameContainer container, float delta) throws RadicalFishException {
		handleInput(delta);
		fade.update(container, delta);
	}
	public void render(GameContainer container, Graphics g) throws RadicalFishException {
		SpriteBatch batch = g.getSpriteBatch();
		
		sprite.setPosition(position.x, position.y);
		sprite.setScale(scale);
		
		g.translate(10, 10);
		g.apply();
		
		batch.begin();
		sprite.draw(batch);
		batch.end();
		
		fade.postRender(container, g);
	}
	
	private void handleInput(float delta) {
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			scale += 0.02f;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			scale -= 0.02f;
		}
		
		boolean lrpress = false;
		boolean udpress = false;
		
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			velocity.x -= 400f * delta;
			lrpress = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			velocity.x += 400f * delta;
			lrpress = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			velocity.y += 400f * delta;
			udpress = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			velocity.y -= 400f * delta;
			udpress = true;
		}
		
		if(velocity.x >= 2000f) {
			velocity.x = 2000f;
		}
		if(velocity.y >= 2000f) {
			velocity.y = 2000f;
		}
		
		position.x += velocity.x * delta;
		position.y += velocity.y * delta;
		
		if (!lrpress) {
			if(velocity.x > 0) {
				velocity.x -= 400f * delta;
				if(velocity.x < 0) {
					velocity.x = 0;
				}
			} else {
				velocity.x += 400f * delta;
				if(velocity.x > 0) {
					velocity.x = 0;
				}
			}
			
		}
		if (!udpress) {
			if(velocity.y > 0) {
				velocity.y -= 400f * delta;
				if(velocity.y < 0) {
					velocity.y = 0;
				}
			} else {
				velocity.y += 400f * delta;
				if(velocity.y > 0) {
					velocity.y = 0;
				}
			}
			
		}
		
		if(position.x < 0) {
			position.x = 0;
			velocity.x *= -0.7;
		} else if(position.x >= 800 - 32) {
			position.x = 800 - 32;
			velocity.x *= -0.7;
		}
		if(position.y < 0) {
			position.y = 0;
			velocity.y *= -0.7;
		} else if(position.y >= 600 - 32) {
			position.y = 600 - 32;
			velocity.y *= -0.7;
		}
		
	}
	
	// OTHER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void pause(GameContainer container) {}
	public void resume(GameContainer container) {}
	public void dispose() {}
	
	// TEST METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void initContainer(GameContainer container) {
		container.setSmoothDelta(true);
	}
	
	public String getTitle() {
		return "Simple Game Test";
	}
	public int getWidth() {
		return 800;
	}
	public int getHeight() {
		return 600;
	}
	public boolean needsGL20() {
		return true;
	}
	
}
