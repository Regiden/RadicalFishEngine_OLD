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
package de.radicalfish.test.world;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import de.radicalfish.GameState;
import de.radicalfish.context.GameContext;
import de.radicalfish.context.GameDelta;
import de.radicalfish.debug.Logger;
import de.radicalfish.debug.PerformanceListener;
import de.radicalfish.extern.Easing;
import de.radicalfish.extern.SimpleFX;
import de.radicalfish.world.World;

public class TestGameState extends GameState implements PerformanceListener {
	
	private Image image, scene;
	private SimpleFX effect, effect2;
	private Player player;
	
	private long utime, rtime;
	
	public TestGameState(GameContext context, World world, int id) {
		super(context, world, id);
	}
	
	// GAME METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void init(GameContext context, World world) throws SlickException {
		image = new Image("de/radicalfish/testdata/TESTBLOCK.png");
		scene = new Image("de/radicalfish/testdata/scene.png", false, Image.FILTER_NEAREST);
		
		effect = new SimpleFX(30, 400, 3 * 1000, Easing.QUAD_IN_OUT);
		effect2 = new SimpleFX(30, 400, 3 * 1000, Easing.LINEAR);
		
		player = new Player();
		player.init(context, world);
		
		
	}
	public void update(GameContext context, World world, GameDelta delta) throws SlickException {
		long locTime = System.nanoTime();
		Input in = context.getContainer().getInput();
		
		GameDelta deltor = context.getGameDelta();
		
		player.update(context, world, delta);
		
		
		if (in.isKeyPressed(Input.KEY_A)) {
			Logger.none("test");
			deltor.slowDown(0.5f, 2000, Easing.LINEAR);
		}
		if (in.isKeyPressed(Input.KEY_D)) {
			deltor.slowDown(1.0f, 2000, Easing.LINEAR);
		}
		
		effect.update(delta.getNormalDelta());
		if (effect.finished()) {
			effect.flip();
			effect.restart();
		}
		
		effect2.update(delta.getDelta());
		if (effect2.finished()) {
			effect2.flip();
			effect2.restart();
		}
		
		utime = (System.nanoTime() - locTime) / 1000;
		
	}
	public void render(GameContext context, World world, Graphics g) throws SlickException {
		long locTime = System.nanoTime();
		
		g.scale(2, 2);
		scene.draw();
		g.resetTransform();
		
		image.draw(effect.getValue(), 50);
		image.draw(effect2.getValue(), 100);
		
		image.draw(20, 20);
		GL11.glPushMatrix();
		g.scale(2, 2);
		GL11.glPushMatrix();
		image.draw(20, 20);
		GL11.glPopMatrix();
		image.draw(40, 40);
		GL11.glPopMatrix();
		image.draw(60, 60);
		
		player.render(context, world, g);
		
		image.draw(100, 100);
		
		rtime = (System.nanoTime() - locTime) / 1000;
		
	}
	
	// INTERFACE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	@Override
	public long getMessuredTime() {
		return (int) (utime + rtime);
	}
	
}
