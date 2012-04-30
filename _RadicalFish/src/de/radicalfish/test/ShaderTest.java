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
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.opengl.InternalTextureLoader;
import org.newdawn.slick.opengl.shader.ShaderProgram;
import de.radicalfish.context.DefaultSettings;
import de.radicalfish.context.Settings;
import de.radicalfish.extern.Easing;
import de.radicalfish.extern.FBO;
import de.radicalfish.extern.SimpleFX;

public class ShaderTest extends BasicGame {
	
	// pixture is a 640x480 image | object is 32x32
	public Image picture, object, fboImage;
	public Color toneColor;
	public Font gameFont;
	
	public Settings settings;
	public ShaderProgram toner, wave, add, spherewave;
	public FBO fbo;
	public SimpleFX effect;
	
	public static int width = 640, height = 480;
	public float time, value, friction;
	
	public ShaderTest() {
		super("Shader Tests");
	}
	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new ShaderTest(), width, height, false);
		app.start();
	}
	
	// GAME METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	@Override
	public void init(GameContainer container) throws SlickException {
		ShaderProgram.setStrictMode(false);
		toner = ShaderProgram.loadProgram("data/shader/simplevert.vert", "data/shader/screentone.frag");
		wave = ShaderProgram.loadProgram("data/shader/simplevert.vert", "data/shader/wave.frag");
		add = ShaderProgram.loadProgram("data/shader/simplevert.vert", "data/shader/add.frag");
		spherewave = ShaderProgram.loadProgram("data/shader/simplevert.vert", "data/shader/spherewave.frag");
		
		time = 0.0f;
		value = 1.0f;
		friction = 0.0001f;
		
		effect = new SimpleFX(50, 400, 5000, Easing.QUARTIC_OUT);
		
		picture = new Image("data/NPCs.png", false, Image.FILTER_NEAREST);
		object = new Image("data/TESTBLOCK.png", false, Image.FILTER_NEAREST);
		toneColor = new Color(Color.white);
		
		fbo = new FBO(width + 100, height + 100);
		fboImage = new Image(fbo.getTexture());
		
		gameFont = loadFont("data/PIXEARG_.TTF", 8, false, false);
		
		settings = new DefaultSettings("options.txt");
		System.out.println(settings);
		
		container.setDefaultFont(gameFont);
		
		
	}
	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		time += delta;
		effect.update(delta);
		if (effect.finished()) {
			effect.flip();
			effect.restart();
		}
		
		Input in = container.getInput();
		if (in.isKeyDown(Input.KEY_Q))
			toneColor.r -= friction * delta;
		if (in.isKeyDown(Input.KEY_W))
			toneColor.r += friction * delta;
		if (in.isKeyDown(Input.KEY_A))
			toneColor.g -= friction * delta;
		if (in.isKeyDown(Input.KEY_S))
			toneColor.g += friction * delta;
		if (in.isKeyDown(Input.KEY_Y))
			toneColor.b -= friction * delta;
		if (in.isKeyDown(Input.KEY_X))
			toneColor.b += friction * delta;
		if (in.isKeyDown(Input.KEY_UP))
			value += 0.005f * delta;
		if (in.isKeyDown(Input.KEY_DOWN))
			value -= 0.005f * delta;
		
	}
	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		g.setFont(gameFont);
		
		fbo.bind();
		g.clear();
		// g.scale(2, 2);
		
		g.translate(50, 50);
		picture.draw(container.getWidth() / 2f - picture.getWidth() / 2f, container.getHeight() / 2f - picture.getHeight() / 2f);
		
		g.resetTransform();
		g.translate(50, 50);
		object.draw(80, 10);
		object.draw(effect.getValue(), 100);
		object.draw(100, effect.getValue());
		
		fbo.unbind();
		
		// at last the wave shader
		wave.bind();
		wave.setUniform1f("time", time / 1000f);
		fboImage.draw(-50, -50);
		ShaderProgram.unbind();
	
//		spherewave.bind();
//		spherewave.setUniform1f("time", time / 1000f);
//		fboImage.draw(-50, -50);
//		ShaderProgram.unbind();

		
		g.resetTransform();
		g.setColor(Color.white);
		g.drawString("add value UP/DOWN", 480, 10);
		g.drawString("value: " + value, 480, 25);
		
		g.drawString("Red = Q/W | Green = A/S | Blue = Y/X", 10, 70);
		g.drawString("Red: ", 10, 25);
		g.drawString("Green: ", 10, 40);
		g.drawString("Blue: ", 10, 55);
		
		g.setColor(Color.red);
		g.fillRect(50, 25, (toneColor.r * 255f), 10);
		g.setColor(Color.green);
		g.fillRect(50, 40, (toneColor.g * 255f), 10);
		g.setColor(Color.blue);
		g.fillRect(50, 55, (toneColor.b * 255f), 10);
		
		g.setColor(Color.white);
		g.drawRect(50, 25, 255, 10);
		g.drawRect(50, 40, 255, 10);
		g.drawRect(50, 55, 255, 10);
		
		g.drawString("" + (toneColor.r * 100), 310, 25);
		g.drawString("" + (toneColor.g * 100), 310, 40);
		g.drawString("" + (toneColor.b * 100), 310, 55);
		
		if(settings.getProperty("debug.texture.count", "").equals("true")) {
			g.drawString("(Texture Count: " + InternalTextureLoader.getTextureCount() + ")", 60, 10);
		}
		
		
	}
	
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	@SuppressWarnings("unchecked")
	private Font loadFont(String path, int size, boolean bold, boolean italic) {
		UnicodeFont font = null;
		
		try {
			font = new UnicodeFont(path, size, bold, italic);
			font.addAsciiGlyphs();
			font.addGlyphs(400, 600);
			font.getEffects().add(new ColorEffect(java.awt.Color.white));
			font.loadGlyphs();
			
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		return font;
	}
	public void drawWave(Graphics g, Image image, float x, float y) {
		fbo.bind();
		g.clear();
		image.draw(x, y);
		fbo.unbind();
		wave.bind();
		wave.setUniform1f("time", time / 500f);
		fboImage.draw();
		ShaderProgram.unbind();
	}
	
}
