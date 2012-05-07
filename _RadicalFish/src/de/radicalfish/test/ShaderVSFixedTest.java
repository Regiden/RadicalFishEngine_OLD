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
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.shader.ShaderProgram;

public class ShaderVSFixedTest extends BasicGame {
	
	String path = "de/radicalfish/testdata/";
	
	ShaderProgram flatShader;
	Image sprite;
	
	long timeShaderTotal, timeShaderAvg;
	long timeFixedTotal, timeFixedAvg;
	
	long runs = 1, localTime;
	
	public ShaderVSFixedTest() {
		super("Test");
	}
	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new ShaderVSFixedTest(), 800, 600, false);
		app.start();
		
	}
	
	public void init(GameContainer container) throws SlickException {
		sprite = new Image(path + "TESTBLOCK.png");
		flatShader = ShaderProgram.loadProgram(path + "shader/simple.vert", path + "shader/simple.frag");
	}
	public void update(GameContainer container, int delta) throws SlickException {
		runs++;
	}
	public void render(GameContainer container, Graphics g) throws SlickException {
		doSomething(g);
		renderShaderBased(g);
		renderFixedBased(g);
		
	}
	
	private void renderShaderBased(Graphics g) {
		localTime = System.nanoTime();
		
		flatShader.bind();
		for (int i = 0; i < 10; i++) {
			sprite.draw(60 + (i * 20), 60 + (20 * i));
		}
		ShaderProgram.unbind();
		
		timeShaderTotal += System.nanoTime() - localTime;
		timeShaderAvg = timeShaderTotal / runs;
		
		g.drawString("Shader: " + timeShaderAvg, 50, 40);
	}
	private void renderFixedBased(Graphics g) {
		g.translate(200, 0);
		localTime = System.nanoTime();
		
		for (int i = 0; i < 10; i++) {
			sprite.draw(60 + (i * 20), 60 + (20 * i));
		}
		
		timeFixedTotal += System.nanoTime() - localTime;
		timeFixedAvg = timeFixedTotal / runs;
		
		g.drawString("Fixed: " + timeFixedAvg, 50, 40);
		g.resetTransform();
	}
	private void doSomething(Graphics g) {
		g.translate(2000, 0);
		long localTime = System.nanoTime();
		
		for (int i = 0; i < 10; i++) {
			sprite.draw(60 + (i * 20), 60 + (20 * i));
		}
		
		long timeFixedTotal = System.nanoTime() - localTime;
		long timeFixedAvg = timeFixedTotal / runs;
		
		g.drawString("Fixed: " + timeFixedAvg, 50, 40);
		g.resetTransform();
	}
	
}
