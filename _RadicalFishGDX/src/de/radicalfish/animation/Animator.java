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
package de.radicalfish.animation;
import java.io.Serializable;
import java.util.HashMap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import de.radicalfish.util.RadicalFishException;

/**
 * Handles animations for entities.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 11.06.2012
 */
public class Animator implements Serializable {
	
	private static final long serialVersionUID = 8460918241921046816L;
	
	// private static long time;
	
	private HashMap<String, Animation> animations;
	private Animation current;
	
	public Animator() {
		animations = new HashMap<String, Animation>();
		current = null;
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Updates the current animation if any. Checks the stack if any animation should be played after the current has
	 * been stopped.
	 * 
	 * @param delta
	 *            the time since the last update call.
	 */
	public void update(float delta) {
		if (current != null) {
			current.update(delta);
		}
	}
	
	/**
	 * Adds a new Animation to the Animator.
	 * 
	 * @param key
	 *            the name for the animation.
	 */
	public void addAnimation(String key, Animation animation) {
		if (animations.containsKey(key)) {
			throw new RadicalFishException("key already exits: " + key);
		}
		if (animation == null) {
			throw new NullPointerException("animation is null");
		}
		
		animations.put(key, animation);
		if (current == null) {
			current = animation;
		}
	}
	public void removeAnimation(String key) {
		if (!animations.containsKey(key)) {
			throw new RadicalFishException("no such animation found: " + key);
		}
		
	}
	
	/**
	 * Plays a specific animation mapped by the animator. It will make the animation mapped by <code>key</code> the
	 * current animation. This calls start on the animation which means it restarts the animation.
	 * 
	 */
	public void playAnimation(String key) {
		playAnimation(key, true);
	}
	/**
	 * Plays a specific animation mapped by the animator. It will make the animation mapped by <code>key</code> the
	 * current animation. It will also stop the current animation if any.
	 * 
	 * @param restart
	 *            false if you want the new animation to be resumed instead of restarted
	 */
	public void playAnimation(String key, boolean restart) {
		if (!animations.containsKey(key)) {
			throw new RadicalFishException("no such animation found: " + key);
		}
		
		if (current != null) {
			current.stop();
		}
		current = animations.get(key);
		if (restart) {
			current.start();
		} else {
			current.resume();
		}
	}
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return a animation handle by the Animator. or null if no animations was mapped for the <code>key</code>.
	 */
	public Animation getAnimation(String key) {
		return animations.get(key);
	}
	/**
	 * @return the current used animation. can be null!
	 */
	public Animation getCurrentAnimation() {
		return current;
	}
	/**
	 * @return the current image (frame) in the current animation if any.
	 */
	public Sprite getCurrentImage() {
		if (current != null) {
			return current.getCurrentSprite();
		}
		return null;
	}
	
}
