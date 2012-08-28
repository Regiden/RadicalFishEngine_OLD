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
package de.radicalfish.state.transitions;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.radicalfish.GameContainer;
import de.radicalfish.graphics.Graphics;
import de.radicalfish.state.GameState;
import de.radicalfish.util.RadicalFishException;

/**
 * A transition which blends the scene in or out base on the {@link FADETYPE}.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 28.08.2012
 */
public class FadeTransition implements Transition {
	
	private static Color temp = new Color();
	
	/** The type of the fade. */
	public enum FADETYPE {
		FADE_IN, FADE_OUT
	}
	
	private Color color;
	private FADETYPE type;
	private float fadeTime;
	
	public FadeTransition(Color color, FADETYPE type, float fadeTime) {
		this.color = color;
		this.color.a = (type == FADETYPE.FADE_IN ? 1f : 0f);
		this.type = type;
		this.fadeTime = fadeTime;
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void update(GameContainer container, float delta) throws RadicalFishException {
		if (type == FADETYPE.FADE_IN) {
			color.a -= delta / fadeTime;
			if (color.a < 0) {
				color.a = 0;
			}
		} else {
			color.a += delta / fadeTime;
			if (color.a > 1) {
				color.a = 1;
			}
		}
		
	}
	public void postRender(GameContainer container, Graphics g) throws RadicalFishException {
		g.pushTransform();
		g.setOrigin(0, 0);
		g.resetTransform(true);
		
		SpriteBatch batch = g.getSpriteBatch();
		temp.set(g.getColor());
		
		batch.begin();
		g.setColor(color);
		g.fillRect(0, 0, container.getDisplayWidth(), container.getDisplayHeight());
		batch.end();
		
		g.setColor(temp);
		g.popTransform(true);
	}
	
	public boolean isFinished() {
		if (type == FADETYPE.FADE_IN) {
			return (color.a <= 0);
		} else {
			return (color.a >= 1);
		}
	}
	
	
	// UNUSED
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void init(GameContainer container, GameState from, GameState to) throws RadicalFishException {}
	public void preRender(GameContainer container, Graphics g) throws RadicalFishException {}
}
