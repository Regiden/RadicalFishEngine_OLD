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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import de.radicalfish.GameContainer;
import de.radicalfish.graphics.Graphics;
import de.radicalfish.graphics.SpriteSheet;
import de.radicalfish.tests.utils.SimpleTest;
import de.radicalfish.util.RadicalFishException;

public class SpriteSheetTest extends SimpleTest {
	
	private SpriteSheet sheet;
	private float[] rot = new float[4];
	private float[] sca = new float[4];
	
	private boolean rotate = true;
	private boolean scale = true;
	
	private float time;
	
	public SpriteSheetTest() {
		super("SpriteSheet Test");
	}
	
	public void init(GameContainer container) throws RadicalFishException {
		sheet = new SpriteSheet("data/block.png", 16, 16);
		for (int i = 0; i < rot.length; i++) {
			rot[i] = MathUtils.random(0, 360);
		}
		for (int i = 0; i < sca.length; i++) {
			sca[i] = MathUtils.random(0, 2.0f);
		}
		container.getGraphics().setClearColor(0.7f, 0.1f, 0.2f);
	}
	public void update(GameContainer container, float delta) throws RadicalFishException {
		if(rotate) {
			for (int i = 0; i < rot.length; i++) {
				rot[i] = (rot[i] + (100 * delta)) % 360;
			}
		}
		if(scale) {
			time += delta;
			for (int i = 0; i < sca.length; i++) {
				sca[i] = Math.min(1.6f, Math.max(0.4f, MathUtils.sin(time) + 1.0f));
			}
		}
	}
	public void render(GameContainer container, Graphics g) throws RadicalFishException {
		SpriteBatch batch = g.getSpriteBatch();
		
		batch.begin();
		{
			g.setColor(0.5f, 0.5f, 0.5f, 1.0f);
			g.fillRect(0, 0, 220, 70);
			g.setColor(Color.BLACK);
			g.drawRect(0, 0, 220, 70);
			g.setColor(Color.WHITE);
			
			container.getFont().draw(batch, "Press R to start/stop the rotation", 5, 35);
			container.getFont().draw(batch, "Press S to start/stop the scaling", 5, 50);
			container.getFont().draw(batch, "This is a 2x2 SpriteSheet with tw and th set to 16", 100, 160);
			container.getFont().draw(batch, "The texture is 32x32 pixel. cached is: " + sheet.isCached(), 100, 296);
			
			
			batch.draw(sheet.base, 100, 100, 32, 32, 0, 0, 32, 32, false, g.isYDown());
			
			g.scale(2);
			g.apply();
			
			batch.draw(sheet.getSubImage(0, 0), 100, 100, 8, 8, 16, 16, sca[0], sca[0], rot[0]);
			batch.draw(sheet.getSubImage(1, 0), 117, 100, 8, 8, 16, 16, sca[1], sca[1], rot[1]);
			batch.draw(sheet.getSubImage(1, 1), 117, 117, 8, 8, 16, 16, sca[2], sca[2], rot[2]);
			batch.draw(sheet.getSubImage(0, 1), 100, 117, 8, 8, 16, 16, sca[3], sca[3], rot[3]);
			
			g.scale(2);
			g.apply();
			
			batch.draw(sheet.getSubImage(0, 0), 100, 100, 8, 8, 16, 16, sca[0], sca[0], -rot[0]);
			batch.draw(sheet.getSubImage(1, 0), 117, 100, 8, 8, 16, 16, sca[1], sca[1], -rot[1]);
			batch.draw(sheet.getSubImage(1, 1), 117, 117, 8, 8, 16, 16, sca[2], sca[2], -rot[2]);
			batch.draw(sheet.getSubImage(0, 1), 100, 117, 8, 8, 16, 16, sca[3], sca[3], -rot[3]);
			
		}
		batch.end();
	}
	
	public boolean keyDown(int keycode) {
		if (keycode == Keys.R) {
			rotate = !rotate;
		}
		if (keycode == Keys.S) {
			scale = !scale;
		}
		return false;
	}
	
}
