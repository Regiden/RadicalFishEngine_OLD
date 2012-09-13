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

/**
 * A transition which blends the scene in or out base on the {@link FADE}.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 28.08.2012
 */
public class FadeTransition implements Transition {
	
	private static Color temp = new Color();
	
	/** The type of the fade. */
	public enum FADE {
		IN, OUT
	}
	
	private Color color;
	private FADE type;
	private float fadeTime, waitTime, timer;
	
	/**
	 * Creates a new {@link FadeTransition}. Note that this will make a copy of the color object.
	 * 
	 * @param color
	 *            the color to fade in or to
	 * @param type
	 *            the type of the fade
	 * @param fadeTime
	 *            the time of the fade in seconds
	 */
	public FadeTransition(Color color, FADE type, float fadeTime) {
		this(color, type, fadeTime, 0.0f);
	}
	/**
	 * Creates a new {@link FadeTransition}. Note that this will make a copy of the color object.
	 * 
	 * @param color
	 *            the color to fade in or to
	 * @param type
	 *            the type of the fade
	 * @param fadeTime
	 *            the time of the fade in seconds
	 * @param waitTime
	 *            the time to wait after the basic transition is done (Used to wait some time before going to the next
	 *            transition)
	 */
	public FadeTransition(Color color, FADE type, float fadeTime, float waitTime) {
		this.color = color.cpy();
		this.color.a = (type == FADE.IN ? 1f : 0f);
		this.type = type;
		this.fadeTime = fadeTime;
		this.waitTime = waitTime;
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void update(GameContainer container, float delta) {
		if (type == FADE.IN) {
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
		if (done()) {
			timer += delta;
			if (timer >= waitTime) {
				timer = waitTime;
			}
		}
	}
	public void postRender(GameContainer container, Graphics g) {
		g.pushTransform();
		g.setOrigin(0, 0);
		g.resetTransform(true);
		
		SpriteBatch batch = g.getSpriteBatch();
		temp.set(g.getColor());
		
		batch.begin();
		g.setColor(color);
		
		g.fillRect(0, 0, container.getDisplayWidth(), container.getDisplayHeight());
		if (color.a >= 1.0f) {
			g.fillRect(0, 0, container.getDisplayWidth(), container.getDisplayHeight());
		}
		batch.end();
		
		g.setColor(temp);
		g.popTransform(true);
	}
	
	public boolean isFinished() {
		if (type == FADE.IN) {
			return (color.a <= 0) && (timer >= waitTime);
		} else {
			return (color.a >= 1) && (timer >= waitTime);
		}
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private boolean done() {
		if (type == FADE.IN) {
			return (color.a <= 0);
		} else {
			return (color.a >= 1);
		}
	}
	
	// UNUSED
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void init(GameContainer container, GameState from, GameState to) {}
	public void preRender(GameContainer container, Graphics g) {}
}
