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
package de.radicalfish.font.commands;

import de.radicalfish.GameContainer;
import de.radicalfish.font.StyleInfo;
import de.radicalfish.state.transitions.FadeTransition.FADE;

/**
 * A command which fades in a single character with a given time.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 13.09.2012
 */
public class FadeCommand extends StyleCommand {
	
	protected final FADE type;
	
	protected final float duration;
	
	private float time, alpha;
	
	private float a1, a2, a3, a4;
	
	/**
	 * Creates a new {@link FadeCommand} with the given parameters.
	 * 
	 * @param type
	 *            the type of the fade (in or out)
	 * @param duration
	 *            the duration of the fade in seconds.
	 * @param charpoint
	 *            the charpoint to start
	 */
	public FadeCommand(FADE type, float duration, int charpoint) {
		super(charpoint);
		
		this.type = type;
		this.duration = duration;
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void update(GameContainer container, float delta) {
		if (time < duration) {
			alpha = time / duration;
			time += delta;
		} else {
			alpha = 1f;
		}
	}
	public void execute(GameContainer container, StyleInfo style) {
		a1 = style.colorTopLeft.a;
		a2 = style.colorTopRight.a;
		a3 = style.colorBottomLeft.a;
		a4 = style.colorBottomRight.a;
		
		style.colorTopLeft.a = alpha;
		style.colorBottomRight.a = alpha;
		style.colorBottomLeft.a = alpha;
		style.colorBottomRight.a = alpha;
	}
	public void finish(GameContainer container, StyleInfo style) {
		style.colorTopLeft.a = a1;
		style.colorBottomRight.a = a2;
		style.colorBottomLeft.a = a3;
		style.colorBottomRight.a = a4;
	}
	public void reset() {
		time = 0.0f;
	}
	
}
