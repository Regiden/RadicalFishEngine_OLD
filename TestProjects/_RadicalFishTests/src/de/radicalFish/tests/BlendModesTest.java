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
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import de.radicalfish.GameContainer;
import de.radicalfish.GameInput;
import de.radicalfish.graphics.BlendMode;
import de.radicalfish.graphics.Graphics;
import de.radicalfish.tests.utils.SimpleTest;
import de.radicalfish.util.RadicalFishException;

/**
 * tests blend modes.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 02.09.2012
 */
public class BlendModesTest extends SimpleTest {
	
	public BlendModesTest() {
		super("Blend Modes Test");
		
	}
	
	private Sprite logo, particle;
	private Vector2 position = new Vector2(0, 0);
	
	private Vector2[] pPos;
	
	private BlendMode mode = BlendMode.NORMAL;
	
	private float speed = 250;
	private float rotation = 0.0f;
	
	// GAME METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void init(GameContainer container) throws RadicalFishException {
		logo = new Sprite(new Texture("data/logo.png"));
		logo.flip(false, true);
		particle = new Sprite(new Texture("data/particle.png"));
		particle.flip(false, true);
		
		
		pPos = new Vector2[100];
		
		for (int i = 0; i < pPos.length; i++) {
			pPos[i] = new Vector2(MathUtils.random(0, container.getWidth() - 32), MathUtils.random(0, container.getHeight() - 32));
		}
	}
	public void update(GameContainer container, float delta) throws RadicalFishException {
		rotation = (rotation + (100 * delta)) % 360;
		handleInput(container.getInput(), delta);
	}
	public void render(GameContainer container, Graphics g) throws RadicalFishException {
		g.setBlendMode(mode);
		SpriteBatch batch = g.getSpriteBatch();
		batch.begin();
		
		logo.setPosition(container.getWidth() / 2 - logo.getWidth() / 2, container.getHeight() / 2 - logo.getHeight() / 2);
		logo.draw(batch);
		
		for (int i = 0; i < pPos.length; i++) {
			particle.setPosition(pPos[i].x, pPos[i].y);
			particle.draw(batch);
		}
		
		
		logo.setPosition(position.x, position.y);
		logo.draw(batch);
		
		g.setBlendMode(BlendMode.NORMAL);
		
		container.getFont().draw(batch, "Blend Mode (A/D): " + mode.name(), 5, 35);
		
		batch.end();
	}
	
	private void handleInput(GameInput input, float delta) {
		if (input.isKeyDown(Keys.LEFT)) {
			position.x -= speed * delta;
		} else if (input.isKeyDown(Keys.RIGHT)) {
			position.x += speed * delta;
		}
		if (input.isKeyDown(Keys.UP)) {
			position.y -= speed * delta;
		} else if (input.isKeyDown(Keys.DOWN)) {
			position.y += speed * delta;
		}
	}
	
	public boolean keyDown(int keycode) {
		int t = mode.ordinal();
		if(keycode == Keys.A) {
			t--;
		} else if(keycode == Keys.D) {
			t++;
		}
		if(t < 0) {
			t = BlendMode.values().length -1;
		} else if(t >= BlendMode.values().length) {
			t = 0;
		}
		mode = BlendMode.values()[t];
		return false;
	}
}
