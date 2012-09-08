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
package de.radicalfish.animation;
import java.util.ArrayList;
import com.badlogic.gdx.graphics.g2d.Sprite;
import de.radicalfish.util.RadicalFishException;

/**
 * An animation class with extends features compared to {@link Animation}.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 22.11.2011
 */
public class Animation {
	
	private ArrayList<Frame> frames;
	
	private int currentFrame;
	private int direction;
	private float nextStep;
	
	private boolean loops;
	private boolean pingPong;
	private boolean stopped;
	private boolean loopsAt;
	private int loopAtFrame;
	
	public Animation() {
		frames = new ArrayList<Frame>();
		currentFrame = -1;
		
		loops = false;
		pingPong = false;
		stopped = false;
		loopsAt = false;
		loopAtFrame = -1;
		
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Starts the animation or restarts if it is stopped.
	 */
	public void start() {
		if (!stopped) {
			return;
		}
		if (frames.size() == 0) {
			return;
		}
		stopped = false;
		currentFrame = 0;
		nextStep = frames.get(0).duration;
	}
	/**
	 * Stops the animation.
	 */
	public void stop() {
		if (frames.size() == 0)
			return;
		stopped = true;
	}
	/**
	 * Resumes the animation.
	 */
	public void resume() {
		if (frames.size() == 0)
			return;
		stopped = false;
	}
	/**
	 * Updates this animation.
	 * 
	 * @param delta
	 *            the time since the last update in ms.
	 */
	public void update(float delta) {
		if (stopped)
			return;
		if (frames.size() == 0)
			return;
		
		nextStep -= delta;
		
		// code taken from slick2D author kevin glass
		while (nextStep < 0 && (!stopped)) {
			if (loopsAt) {
				if (currentFrame == frames.size() - 1) {
					currentFrame = loopAtFrame;
				} else
					currentFrame = (currentFrame + direction) % frames.size();
			} else {
				if ((currentFrame == frames.size() - 1) && (!loops) && (!pingPong)) {
					stopped = true;
					break;
				}
				currentFrame = (currentFrame + direction) % frames.size();
				if (pingPong) {
					if (currentFrame <= 0) {
						currentFrame = 0;
						direction = 1;
						if (!loops) {
							stopped = true;
							break;
						}
					} else if (currentFrame >= frames.size() - 1) {
						currentFrame = frames.size() - 1;
						direction = -1;
					}
				}
			}
			nextStep += frames.get(currentFrame).duration;
		}
	}
	/**
	 * Adds a new frame to the animation.
	 * 
	 * @param image
	 *            the image for the frame
	 * @param duration
	 *            the duration of this frame.
	 */
	public void addFrame(Sprite image, int duration) {
		if (frames.isEmpty())
			nextStep = duration;
		
		frames.add(new Frame(image, duration));
		
		currentFrame = 0;
		direction = 1;
	}
	
	// GETTER AND SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return the currentFrame
	 */
	public int getCurrentFrame() {
		return currentFrame;
	}
	/**
	 * @return the current sprite
	 */
	public Sprite getCurrentSprite() {
		return frames.get(currentFrame).sprite;
	}
	/**
	 * @return true if the animation loops
	 */
	public boolean isLoops() {
		return loops;
	}
	/**
	 * @return the true if the animation run forwards and backwards.
	 */
	public boolean isPingPong() {
		return pingPong;
	}
	/**
	 * @return the number of frames.
	 */
	public int getTotalFrames() {
		return frames.size();
	}
	
	/**
	 * Sets a point to start a new loop from, turn of by setting <code>loopAt</code> to false. This will override the
	 * ping pong mode!
	 * 
	 * @param loopAt
	 *            true if we want to loop at a certain point
	 * @param loopPos
	 *            the loop point to set.
	 */
	public void setLoopsAt(boolean loopAt, int loopPos) {
		if (loopPos < frames.size()) {
			if (loopAt) {
				loopsAt = true;
				loopAtFrame = loopPos;
			} else {
				loopsAt = false;
				loopPos = -1;
			}
		} else
			throw new RadicalFishException("loopPos ist greater then frames number: " + loopPos);
	}
	/**
	 * @param currentFrame
	 *            the current frame
	 */
	public void setCurrentFrame(int currentFrame) {
		if (currentFrame < frames.size())
			this.currentFrame = currentFrame;
		else
			throw new RadicalFishException("currentFrames ist greater then frames number: " + currentFrame);
	}
	/**
	 * @param loops
	 *            true if the animation should loop
	 */
	public void setLoops(boolean loops) {
		this.loops = loops;
	}
	/**
	 * @param pingPong
	 *            true if the animation should run backwards too
	 */
	public void setPingPong(boolean pingPong) {
		this.pingPong = pingPong;
	}
	
	// INNER CLASS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private class Frame {
		/** The image of this frame. */
		public Sprite sprite;
		/** The duration of this frame. */
		public int duration;
		
		public Frame(Sprite image, int duration) {
			this.sprite = image;
			this.duration = duration;
		}
	}
}
