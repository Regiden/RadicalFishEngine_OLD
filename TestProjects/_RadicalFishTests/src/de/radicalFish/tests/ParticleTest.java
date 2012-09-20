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
import java.util.Random;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import de.radicalfish.BasicGame;
import de.radicalfish.GameContainer;
import de.radicalfish.GameInput;
import de.radicalfish.effects.Rumble;
import de.radicalfish.effects.Rumble.RUMBLE_AXIS;
import de.radicalfish.effects.Rumble.RUMBLE_POWER;
import de.radicalfish.effects.Rumble.RUMBLE_SPEED;
import de.radicalfish.effects.Rumble.RumbleHandle;
import de.radicalfish.effects.ToneModel;
import de.radicalfish.effects.ToneShader;
import de.radicalfish.font.Font;
import de.radicalfish.graphics.BlendMode;
import de.radicalfish.graphics.Graphics;
import de.radicalfish.tests.utils.RadicalFishTest;
import de.radicalfish.util.RadicalFishException;
import de.radicalfish.util.VectorUtil;

/**
 * A quick test which shows the performance of the engine. Use the Mouse (or Fingers) to drag the particles towards the
 * pointer (mouse/finger). If you hold the right mouse button you can spin the particles around the pointer. holding
 * both the left and right will make the partciles go crazy.
 * 
 * 'D' shows the options or hides them
 * 
 * @author Stefan Lange
 * @version 0.5.0
 * @since 21.08.2012
 */
public class ParticleTest extends BasicGame implements RadicalFishTest {
	
	private Particle[] particles;
	
	private Random RND = new Random();
	
	private Texture particle;
	private Vector2 temp = new Vector2();
	private Color loopyColorShit;
	private Color transClear = new Color(0, 0, 0, 0.5f);
	
	private Rumble rumble = new Rumble();
	
	private float lastx, lasty;
	private float mouseX, mouseY;
	private float minColor = 0.1f, maxColor = 0.6f, colorSpeed = 0.05f;
	
	private long updateTime, renderTime, localTime, runs;
	
	private int width, height;
	private int size;
	private int pixelWidth = 4;
	private int rendercalls;
	
	private ToneModel tone = new ToneModel(1, 1, 1, 1);
	private ToneShader shader;
	
	private int state = 0, pressState;
	
	private boolean clearScreen = true;
	private boolean h4c7orzBool = true;;
	private boolean gravityRandom = false;
	private boolean showOptions = false;
	
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void init(GameContainer container) throws RadicalFishException {
		container.setClipViewport(false);
		width = container.getDisplayWidth();
		height = container.getDisplayHeight();
		
		
		loopyColorShit = new Color(maxColor, minColor, minColor, 1.0f);
		
		particles = new Particle[100000];
		
		size = 10000;
		
		for (int i = 0; i < particles.length; i++) {
			particles[i] = new Particle();
			
			particles[i].px = RND.nextFloat() * (width - pixelWidth);
			particles[i].py = RND.nextFloat() * (height - pixelWidth);
			
			particles[i].vx = 0;
			particles[i].vy = 0;
		}
		
		particle = new Texture("data/particle.png");
		
		showOptions = container.isShowDebug();
		ShaderProgram.pedantic = false;
		shader = new ToneShader(tone);
		
	}
	public void update(GameContainer container, float delta) throws RadicalFishException {
		GameInput in = container.getInput();
		width = container.getDisplayWidth();
		height = container.getDisplayHeight();
		runs++;
		
		rumble.update(delta);
		
		localTime = TimeUtils.nanoTime();
		
		handleInput(container, delta);
		if (in.isKeyDown(Keys.O)) {
			tone.setRed(tone.getRed() + 1 * delta);
		}
		if (in.isKeyDown(Keys.I)) {
			tone.setRed(tone.getRed() - 1 * delta);
		}
		if (in.isKeyDown(Keys.L)) {
			tone.setGreen(tone.getGreen() + 1 * delta);
		}
		if (in.isKeyDown(Keys.K)) {
			tone.setGreen(tone.getGreen() - 1 * delta);
		}
		if (in.isKeyDown(Keys.M)) {
			tone.setBlue(tone.getBlue() + 1 * delta);
		}
		if (in.isKeyDown(Keys.N)) {
			tone.setBlue(tone.getBlue() - 1 * delta);
		}
		
		cycleColor();
		
		for (int i = 0; i < size; i++) {
			particles[i].update(delta);
			
			if (pressState == 1) {
				suckHard(i, delta, mouseX, mouseY);
			} else if (pressState == 2) {
				suckHard(i, delta, mouseX, mouseY);
				spinHard(i, delta, mouseX, mouseY);
			} else if (pressState == 3) {
				spinHard(i, delta, mouseX, mouseY);
			}
		}
		updateTime += TimeUtils.nanoTime() - localTime;
	}
	public void render(GameContainer container, Graphics g) throws RadicalFishException {
		SpriteBatch batch = g.getSpriteBatch();
		Font font = container.getFont();
		
		localTime = TimeUtils.nanoTime();
		
		batch.setShader(shader.getShader());
		
		g.translate(rumble.getOffsetX(), rumble.getOffsetY());
		g.apply();
		
		if (!clearScreen) {
			batch.begin();
			g.setColor(transClear);
			g.fillRect(0, 0, container.getDisplayWidth(), container.getDisplayHeight());
			batch.end();
		}
		
		g.setBlendMode(BlendMode.ADD);
		batch.setColor(loopyColorShit);
		batch.begin();
		
		shader.setUniforms();
		
		{
			for (int i = 0; i < size; i++) {
				batch.draw(particle, particles[i].px, particles[i].py, pixelWidth, pixelWidth);
			}
			
			batch.setShader(null);
			g.resetTransform(true);
			if (showOptions) {
				rendercalls = batch.renderCalls;
				g.setBlendMode(BlendMode.NORMAL);
				batch.setColor(1, 1, 1, 1);
				font.draw(batch, "Particles: " + size + " (left/right resize)", 5, 35);
				font.draw(batch, "Calls/Max: " + rendercalls + "/" + batch.maxSpritesInBatch, 5, 50);
				font.draw(batch, "Particle Size: " + pixelWidth + " (+/- resize)", 5, 65);
				font.draw(batch, "Clear Screen (C): " + clearScreen, 5, 85);
				font.draw(batch, "Random Offset (S): " + gravityRandom, 5, 100);
				font.draw(batch, "Update: " + ((updateTime / runs) / 1000) + "us", 5, 120);
				font.draw(batch, "Render: " + ((renderTime / runs) / 1000) + "us", 5, 135);
			}
		}
		batch.end();
		
		renderTime += TimeUtils.nanoTime() - localTime;
		
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void cycleColor() {
		switch (state) {
			case 0:
				loopyColorShit.g += colorSpeed * 1.0f / 255f;
				if (loopyColorShit.g >= maxColor) {
					state = 1;
					loopyColorShit.g = maxColor;
				}
				break;
			case 1:
				loopyColorShit.r -= colorSpeed * 1.0f / 255f;
				if (loopyColorShit.r <= minColor) {
					state = 2;
					loopyColorShit.r = minColor;
				}
				break;
			case 2:
				loopyColorShit.b += colorSpeed * 1.0f / 255f;
				if (loopyColorShit.b >= maxColor) {
					state = 3;
					loopyColorShit.b = maxColor;
				}
				break;
			case 3:
				loopyColorShit.g -= colorSpeed * 1.0f / 255f;
				if (loopyColorShit.g <= minColor) {
					state = 4;
					loopyColorShit.g = minColor;
				}
				break;
			case 4:
				loopyColorShit.r += colorSpeed * 1.0f / 255f;
				if (loopyColorShit.r >= maxColor) {
					state = 5;
					loopyColorShit.r = maxColor;
				}
				break;
			case 5:
				loopyColorShit.b -= colorSpeed * 1.0f / 255f;
				if (loopyColorShit.b <= minColor) {
					state = 0;
					loopyColorShit.b = minColor;
				}
				break;
		}
	}
	private void suckHard(int i, float delta, float x, float y) {
		
		temp.x = (x - particles[i].px);
		temp.y = (y - particles[i].py);
		temp.nor();
		
		if (gravityRandom) {
			particles[i].vx += temp.x * delta * 10f * RND.nextDouble();
			particles[i].vy += temp.y * delta * 10f * RND.nextDouble();
		} else {
			particles[i].vx += temp.x * delta * 10f;
			particles[i].vy += temp.y * delta * 10f;
		}
	}
	private void spinHard(int i, float delta, float x, float y) {
		temp.x = (x - particles[i].px);
		temp.y = (y - particles[i].py);
		temp.nor();
		temp = VectorUtil.makePerpendicular(temp);
		
		if (gravityRandom) {
			particles[i].vx += temp.x * delta * 4f * RND.nextFloat();
			particles[i].vy += temp.y * delta * 4f * RND.nextFloat();
		} else {
			particles[i].vx += temp.x * delta * 4f;
			particles[i].vy += temp.y * delta * 4f;
		}
	}
	
	private void handleInput(GameContainer container, float delta) {
		GameInput in = container.getInput();
		
		mouseX = in.getX();
		mouseY = in.getY();
		
		// hack for the particles to check of they should get slower when no bttons press was made
		h4c7orzBool = true;
		pressState = 0;
		
		if (in.isButtonDown(Buttons.LEFT) && in.isButtonDown(Buttons.RIGHT)) {
			pressState = 3;
		} else if (in.isButtonDown(Buttons.LEFT)) {
			pressState = 1;
		} else if (in.isButtonDown(Buttons.RIGHT)) {
			pressState = 2;
		}
		
		// check for keys (need to find a way to make it work for android)
		if (in.isKeyDown(Keys.LEFT)) {
			size -= 100 * delta;
		}
		if (in.isKeyDown(Keys.RIGHT)) {
			size += 100 * delta;
		}
		if (in.isKeyPressed(Keys.PLUS)) {
			pixelWidth++;
		}
		if (in.isKeyPressed(Keys.MINUS)) {
			pixelWidth--;
			if (pixelWidth <= 0) {
				pixelWidth = 1;
			}
		}
		if (in.isKeyPressed(Keys.C)) {
			clearScreen = !clearScreen;
			container.setClearEachFrame(clearScreen);
		}
		if (in.isKeyPressed(Keys.S)) {
			gravityRandom = !gravityRandom;
		}
		if (in.isKeyPressed(Keys.D)) {
			showOptions = !showOptions;
			container.setShowDebug(!container.isShowDebug());
		}
		if (in.isKeyPressed(Keys.R)) {
			RumbleHandle rh = new RumbleHandle(RUMBLE_AXIS.BOTH, RUMBLE_POWER.EXTREME, RUMBLE_SPEED.FAST, 5.0f, false, true);
			rumble.addRumble(rh);
		}
		
		if (in.isKeyPressed(Keys.ESCAPE)) {
			container.exit();
		}
	}
	
	// TEST METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void initContainer(GameContainer container) {
		container.setSmoothDeltas(true);
		container.setBatchSize(10000);
	}
	
	public String getTitle() {
		return "Performance Test with particles";
	}
	public int getWidth() {
		return 800; // init width
	}
	public int getHeight() {
		return 600; // init height
	}
	public boolean needsGL20() {
		return true;
	}
	
	// INNER CLASS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private class Particle {
		public float px, py, vx, vy, opx, opy;
		
		public void update(float delta) {
			opx = px;
			opy = py;
			
			px += vx * delta * 100f;
			py += vy * delta * 100f;
			
			if (h4c7orzBool) {
				vx -= vx * delta * 0.3f;
				vy -= vy * delta * 0.3f;
			} else {
				vx -= vx * delta * 0.1f;
				vy -= vy * delta * 0.1f;
			}
			if (px < 0.0f && vx < 0.0f) {
				px = 0.0f;
				vx *= -0.7f;
			} else if (px > width - pixelWidth && vx > 0.0f) {
				px = width - pixelWidth;
				vx *= -0.7f;
			}
			if (py < 0.0f && vy < 0.0f) {
				py = 0.0f;
				vy *= -0.7f;
			} else if (py > height - pixelWidth && vy > 0.0f) {
				py = height - pixelWidth;
				vy *= -0.7f;
			}
			
			lastx = px - opx;
			lasty = py - opy;
			
			if (lastx <= 0)
				lastx *= -1;
			if (lastx <= 1)
				opx = (int) (opx + 1);
			if (lasty <= 0)
				lasty *= -1;
			if (lasty <= 1)
				opy = (int) (opy + 1);
			
		}
	}
}
