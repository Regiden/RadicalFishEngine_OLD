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
package de.radicalfish.tests.states;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import de.radicalfish.GameInput;
import de.radicalfish.context.GameContext;
import de.radicalfish.context.GameDelta;
import de.radicalfish.graphics.Graphics;
import de.radicalfish.state.BasicGameState;
import de.radicalfish.state.transitions.FadeTransition;
import de.radicalfish.state.transitions.FadeTransition.FADETYPE;
import de.radicalfish.util.RadicalFishException;
import de.radicalfish.world.World;

public class SimpleState extends BasicGameState {
	
	private String text;
	
	public SimpleState(int ID) {
		super(ID);
		text = "This is the state with the ID: " + ID;
	}
	
	public void init(GameContext context, World world) throws RadicalFishException {
		
	}
	public void update(GameContext context, World world, GameDelta delta) throws RadicalFishException {
		GameInput input = context.getInput();
		
		if (input.isKeyPressed(Keys.ENTER)) {
			context.getGame().enterState((getID() + 1) % 2, new FadeTransition(Color.BLACK, FADETYPE.FADE_OUT, 2.0f, 1.0f),
					new FadeTransition(Color.BLACK, FADETYPE.FADE_IN, 2.0f));
		}
	}
	public void render(GameContext context, World world, Graphics g) throws RadicalFishException {
		BitmapFont font = context.getFont();
		
		float width = font.getBounds(text).width;
		
		g.getSpriteBatch().begin();
		font.draw(g.getSpriteBatch(), text, 800 / 2 - width / 2, 600 / 2 - font.getLineHeight() / 2);
		g.getSpriteBatch().end();
	}
	
}