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
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.shader.ShaderProgram;

public class GrayShaderTest extends BasicGame {
	
	private ShaderProgram gray;
	private String PATH = "de/radicalfish/testdata/";
	
	private Image image;
	
	private float scale = 1.0f;
	
	public GrayShaderTest() {
		super("Shader Test");
	}
	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new GrayShaderTest());
		app.start();
	}
	
	public void init(GameContainer container) throws SlickException {
		gray = ShaderProgram.loadProgram(PATH + "shader/simple.vert", PATH + "shader/gray.frag");
		image = new Image(PATH + "scene.png", false, Image.FILTER_NEAREST);
	}
	public void update(GameContainer container, int delta) throws SlickException {
		if(container.getInput().isKeyDown(Input.KEY_DOWN)) {
			scale -= 0.0005f * delta;
		}
		if(container.getInput().isKeyDown(Input.KEY_UP)) {
			scale += 0.0005f * delta;
		}
		
		if(scale < 0) {
			scale = 0;
		}
		if(scale > 1) {
			scale = 1;
		}
	}
	public void render(GameContainer container, Graphics g) throws SlickException {
		g.scale(2, 2);
		
		gray.bind();
		gray.setUniform1f("scale", scale);
		
		image.draw(0, 0);
		
		ShaderProgram.unbind();
		
		g.resetTransform();
		g.drawString("Scale: " + scale, 10, 20);
	}
}
