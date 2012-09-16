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

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.radicalfish.GameContainer;
import de.radicalfish.font.StyleInfo;

/**
 * This abstract class adds a duration to a style command which tells us hoe long the command needs to change it's
 * state. The update, execute and finish methods have new parameters.
 * <p>
 * The methods include the passed time since the command has been started (reset will of course reset the timer) in a
 * range of 0 - 1. 0 means the start of the command and 1 means the command is done. If the command is done the update
 * method will not be called anymore (use the {@link TimeCommand#TimeCommand(int, float, boolean)} C'Tor to still call
 * the update method if you need it.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 16.09.2012
 */
public abstract class TimeCommand extends StyleCommand {
	
	/** The duration of this {@link TimeCommand}. */
	public float duration;
	
	/** True if we are done. set it to true if your command is done sooner then the duration. */
	public boolean done;
	
	private float timer;
	private float alpha;
	
	/**
	 * Creates a new {@link TimeCommand}. This C'Tor makes sure that the update methode will not be called when the
	 * command is done
	 * 
	 * @param charpoint
	 *            the character point at which the command should be executed
	 * @param duration
	 *            the time this command needs to change it's state (in seconds)
	 */
	public TimeCommand(int charpoint, float duration) {
		super(charpoint);
		this.duration = duration;
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Updates this {@link StyleCommand}.
	 * 
	 * @param container
	 *            the container the game runs in
	 * @param delta
	 *            the time since the last frame
	 * @param alpha
	 *            the time passed in a range of (0 - 1)
	 */
	public abstract void update(GameContainer container, float delta, float alpha);
	/**
	 * Will be called to apply the command to the {@link StyleInfo} given as parameter. After this the
	 * {@link TextureRegion} will be drawn based in the {@link StyleInfo}.
	 * 
	 * @param container
	 *            the container the game runs in
	 * @param style
	 *            the {@link StyleInfo} to change. will be used to draw the character
	 * @param alpha
	 *            the time passed in a range of (0 - 1)
	 */
	public abstract void execute(GameContainer container, StyleInfo style, float alpha);
	/**
	 * Releases any resources used by this command (eg. you use a custom shader). Can also be used to reset values on
	 * the style info.
	 * 
	 * @param container
	 *            the container the game runs in
	 * @param style
	 *            the {@link StyleInfo} to change. will be used to draw the character
	 * @param alpha
	 *            the time passed in a range of (0 - 1)
	 */
	public abstract void finish(GameContainer container, StyleInfo style, float alpha);
	
	/**
	 * Set to true if this command is done.
	 */
	public void setDone(boolean done) {
		this.done = done;
	}
	/**
	 * @return true if {@link TimeCommand} is done.
	 */
	public boolean isDone() {
		return done;
	}
	
	// OVERRIDE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public final void update(GameContainer container, float delta) {
		if (!done) {
			update(container, delta, alpha);
		}
		if (!done) {
			if (timer >= duration) {
				done = true;
			}
			timer += delta;
			if (timer > duration) {
				timer = duration;
			}
			alpha = duration == 0 ? 1 : timer / duration;
		}
		
	}
	public final void execute(GameContainer container, StyleInfo style) {
		execute(container, style, alpha);
	}
	public final void finish(GameContainer container, StyleInfo style) {
		finish(container, style, alpha);
	}
	
	/**
	 * MUST call super.reset() on a sub-class.
	 */
	public void reset() {
		timer = 0;
		alpha = 0;
		done = false;
	}
	
}
