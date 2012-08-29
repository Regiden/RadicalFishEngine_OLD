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
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpriteBatchError implements ApplicationListener {
	
	private SpriteBatch batch;
	private BitmapFont font;
	private Texture quad;
	
	private OrthographicCamera cam;
	
	private Color color = Color.CYAN.cpy();
	
	private String text = "This is a test!";
	
	private int width = 800, height = 600;
	
	public SpriteBatchError(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void create() {
		cam = new OrthographicCamera(width, height);
		
		batch = new SpriteBatch();
		font = new BitmapFont();
		
		Pixmap map = new Pixmap(1, 1, Format.RGBA8888);
		map.setColor(1, 1, 1, 1);
		map.drawPixel(0, 0);
		quad = new Texture(map);
		map.dispose();
	}
	
	@Override
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
		cam.setToOrtho(false, width, height);
		cam.update();
		batch.setProjectionMatrix(cam.combined);
	}
	
	@Override
	public void render() {
		batch.begin();
		
		float w = font.getBounds(text).width;
		font.draw(batch, text, width / 2 - w / 2, height / 2 - font.getLineHeight() / 2);

		// just try a small quad, it's the same...
		batch.flush();
		batch.setColor(Color.BLUE);
		batch.draw(quad, width / 2 - w / 2, height / 2 - font.getLineHeight() / 2 - 20, 40, 80);
		
		// put in any color. you will still see the text through (not much but still)
		// open a graphic program and fill the color with another color (tolerance should be set to 0) and
		// bamm you see the text :(
		
		batch.setColor(color);
		batch.draw(quad, 0, 0, width, height);
		
		batch.end();
	}
	
	@Override
	public void pause() {
		
	}
	@Override
	public void resume() {
		
	}
	
	@Override
	public void dispose() {
		font.dispose();
		quad.dispose();
		batch.dispose();
	}
	
}
