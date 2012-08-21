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
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.InternalTextureLoader;

public class ColorSpaceTest extends BasicGame {
	private DynamicColorSpace space;
	
	private float speed = 0.0005f;
	
	public ColorSpaceTest() {
		super("Color Space Test");
	}
	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new ColorSpaceTest());
		app.setTargetFrameRate(60);
		app.setVSync(true);
		app.start();
	}
	
	public void init(GameContainer container) throws SlickException {
		
		space = new DynamicColorSpace(360, 10);
		
	}
	public void update(GameContainer container, int delta) throws SlickException {
		Input in = container.getInput();
		
		if (in.isKeyDown(Input.KEY_Q)) {
			space.red -= speed * delta;
		}
		if (in.isKeyDown(Input.KEY_W)) {
			space.red += speed * delta;
		}
		if (in.isKeyDown(Input.KEY_A)) {
			space.green -= speed * delta;
		}
		if (in.isKeyDown(Input.KEY_S)) {
			space.green += speed * delta;
		}
		if (in.isKeyDown(Input.KEY_Y)) {
			space.blue -= speed * delta;
		}
		if (in.isKeyDown(Input.KEY_X)) {
			space.blue += speed * delta;
		}
		
		if (space.red < 0) {
			space.red = 0;
		}
		if (space.red > 1) {
			space.red = 1;
		}
		if (space.green < 0) {
			space.green = 0;
		}
		if (space.green > 1) {
			space.green = 1;
		}
		if (space.blue < 0) {
			space.blue = 0;
		}
		if (space.chroma > 1) {
			space.chroma = 1;
		}
		
		if (space.needsUpdate()) {
			space.updateImage();
		}
	}
	public void render(GameContainer container, Graphics g) throws SlickException {
		g.setColor(Color.white);
		g.drawRect(149, 149, 361, 11);
		space.getImage().draw(150, 150);
		
		g.drawString("red:   " + (int) (space.red * 255), 10, 40);
		g.drawString("green: " + (int) (space.green * 255), 10, 55);
		g.drawString("blue:  " + (int) (space.blue * 255), 10, 70);
		g.drawString("chroma:  " + (int) (space.blue * 255), 10, 85);
		
		g.drawString("Texture Count: " + InternalTextureLoader.getTextureCount(), 10, 25);
	}
	
	private static class DynamicColorSpace {
		
		private ImageBuffer buffer;
		private Image image;
		
		public float red, green, blue, chroma;
		private float oldred, oldgreen, oldblue, oldchroma;
		private boolean needsUpdate;
		
		public DynamicColorSpace(int width, int height) {
			buffer = new ImageBuffer(width, height);
			
			red = 1.0f;
			green = 1.0f;
			blue = 1.0f;
			chroma = 1.0f;
			
			oldred = red;
			oldgreen = green;
			oldblue = blue;
			oldchroma = chroma;
			
			needsUpdate = true;
			updateImage();
		}
		
		public boolean needsUpdate() {
			return oldred != red || oldgreen != green || oldblue != blue || oldchroma != chroma;
		}
		public void updateImage() {
			int length = buffer.getWidth() / 6;
			int offset = 0, lerp;
			
			needsUpdate = true;
			oldred = red;
			oldgreen = green;
			oldblue = blue;
			
			// red -> yellow
			for (int i = 0; i < length; i++) {
				lerp = (int) (lerp(0, getColorAsInt(green), i / ((float) length)));
				makeVGradient(offset, i, getColorAsInt(red), lerp, 0);
			}
			
			// yellow -> green
			offset += length;
			for (int i = 0; i < length; i++) {
				lerp = (int) (lerp(getColorAsInt(red), 0, i / ((float) length)));
				makeVGradient(offset, i, lerp, getColorAsInt(green), 0);
			}
			
			// green -> cyan
			offset += length;
			for (int i = 0; i < length; i++) {
				lerp = (int) (lerp(0, getColorAsInt(blue), i / ((float) length)));
				makeVGradient(offset, i, 0, getColorAsInt(green), lerp);
			}
			
			// cyan -> blue
			offset += length;
			for (int i = 0; i < length; i++) {
				lerp = (int) (lerp(getColorAsInt(green), 0, i / ((float) length)));
				makeVGradient(offset, i, 0, lerp, getColorAsInt(blue));
			}
			
			// blue -> violet
			offset += length;
			for (int i = 0; i < length; i++) {
				lerp = (int) (lerp(0, getColorAsInt(red), i / ((float) length)));
				makeVGradient(offset, i, lerp, 0, getColorAsInt(blue));
			}
			
			// violet -> red
			offset += length;
			for (int i = 0; i < length; i++) {
				lerp = (int) (lerp(getColorAsInt(blue), 0, i / ((float) length)));
				makeVGradient(offset, i, getColorAsInt(red), 0, lerp);
			}
			
		}
		
		public Image getImage() throws SlickException {
			if (needsUpdate) {
				if (image != null) {
					image.destroy();
				}
				image = buffer.getImage();
				needsUpdate = false;
			}
			return image;
		}
		
		private int getColorAsInt(float value) {
			return (int) (value * 255);
		}
		private void makeVGradient(int offset, int i, int red, int green, int blue) {
			for (int j = 0; j < buffer.getHeight(); j++) {
				buffer.setRGBA(i + offset, j, red, green, blue, 255);
			}
		}
		private float lerp(float current, float target, float ratio) {
			if (current == target)
				return current;
			return current * (1f - ratio) + target * ratio;
		}
		
	}
	
}
