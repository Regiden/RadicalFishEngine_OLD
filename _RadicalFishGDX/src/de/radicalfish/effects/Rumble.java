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
package de.radicalfish.effects;
import java.util.HashMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import de.radicalfish.util.VectorUtil;
import de.radicalfish.util.Utils;

/**
 * Rumble is a utility class you can use to offset your camera and make it look like the screen shakes. The class is not
 * meant to be sub-classes but if you need further customization, you a free to do it.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 25.08.2012
 */
public class Rumble {
	
	protected Vector2 offset;
	
	protected Array<RumbleHandle> handles;
	protected HashMap<String, RumbleHandle> namedHandles;
	
	/**
	 * Creates a new {@link Rumble}.
	 */
	public Rumble() {
		handles = new Array<RumbleHandle>();
		namedHandles = new HashMap<String, RumbleHandle>();
		offset = new Vector2(0, 0);
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Updates all {@link RumbleHandle}s.
	 */
	public void update(float delta) {
		RumbleHandle temp = null;
		offset.set(0, 0);
		int i = handles.size;
		while (i-- > 0) {
			temp = handles.get(i);
			
			temp.update(delta);
			if (temp.isDone()) {
				if (temp.name != null) {
					namedHandles.remove(temp.name);
				}
				handles.removeIndex(i);
				continue;
			}
			offset.add(temp.offset);
		}
	}
	
	/**
	 * Adds a new {@link RumbleHandle} to the list of handles.
	 * 
	 * @param handle
	 */
	public void addRumble(RumbleHandle handle) {
		Utils.notNull("handle", handle);
		if (handle.name != null) {
			this.namedHandles.put(handle.name, handle);
		} else {
			handle.continues = false;
		}
		handles.add(handle);
	}
	/**
	 * Removes a {@link RumbleHandle} from the list of handles if this handle has a name. if the name does not exist
	 * nothing happens.
	 */
	public void removeRumble(String key) {
		Utils.notNull("key", key);
		if (namedHandles.containsKey(key)) {
			handles.removeValue(namedHandles.remove(key), true);
		}
	}
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return a {@link RumbleHandle} that had a has a name or null if no handle was found with this name.
	 */
	public RumbleHandle getRumble(String key) {
		return namedHandles.get(key);
	}
	
	/**
	 * @return the offset calculated by every {@link RumbleHandle}.
	 */
	public Vector2 getOffset() {
		return offset;
	}
	/**
	 * @return the offset's x values.
	 */
	public float getOffsetX() {
		return offset.x;
	}
	/**
	 * @return the offset's y values.
	 */
	public float getOffsetY() {
		return offset.y;
	}
	
	// INTERN CLASSES
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * This handle will be used to add rumble effects to the {@link Rumble} class.
	 */
	public static class RumbleHandle {
		protected Vector2 offset = new Vector2(0, 0);
		private Vector2 target = new Vector2(0, 0);
		private Vector2 start = new Vector2(0, 0);
		
		private String name;
		
		private RUMBLE_AXIS axis;
		
		private float power;
		private float totalTime;
		private float totalDuration;
		private float singleDuration;
		private float singleTimer;
		
		private boolean continues;
		private boolean fade;
		
		/**
		 * Creates a new {@link RumbleHandle} without a name.
		 * 
		 * @param axis
		 *            the axis over which the handle should rumble.
		 * @param power
		 *            the power of the rumble effect
		 * @param speed
		 *            the speed a single shake from one point to another takes.
		 * @param duration
		 *            the duration over which the handle should rumble
		 * @param continues
		 *            true if this rumble effect won't stop
		 * @param fade
		 *            true if this rumble should fade out the more time passes.
		 */
		public RumbleHandle(RUMBLE_AXIS axis, RUMBLE_POWER power, RUMBLE_SPEED speed, float duration, boolean continues, boolean fade) {
			this(null, axis, power, speed, duration, continues, fade);
		}
		/**
		 * Creates a new {@link RumbleHandle}.
		 * 
		 * @param name
		 *            the name of the handle. there can be no handles with the same name.
		 * @param axis
		 *            the axis over which the handle should rumble.
		 * @param power
		 *            the power of the rumble effect
		 * @param speed
		 *            the speed a single shake from one point to another takes.
		 * @param duration
		 *            the duration over which the handle should rumble
		 * @param continues
		 *            true if this rumble effect won't stop
		 * @param fade
		 *            true if this rumble should fade out the more time passes.
		 */
		public RumbleHandle(String name, RUMBLE_AXIS axis, RUMBLE_POWER power, RUMBLE_SPEED speed, float duration, boolean continues,
				boolean fade) {
			this.name = name;
			set(axis, power, speed, duration, continues, fade);
		}
		
		// METHODS
		// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
		
		/**
		 * Updates the handle.
		 */
		public void update(float delta) {
			if ((totalTime <= totalDuration) || continues) {
				float locRum = 0.0f;
				if (fade && !continues) {
					locRum = power * ((totalDuration - totalTime) / totalDuration);
				} else {
					locRum = power;
				}
				updatePosition(locRum, power, delta);
				totalTime += delta;
			} else {
				stop();
			}
		}
		/**
		 * Stops this handle. this means it will be removed from the list of handles.
		 */
		public void stop() {
			totalTime = totalDuration;
			offset.set(0, 0);
			continues = false;
		}
		/**
		 * True if the time exceeded the duration and the handle is not continues.
		 */
		public boolean isDone() {
			return totalTime >= totalDuration && !continues;
		}
		
		/**
		 * Sets the name if any.
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * Sets the {@link RumbleHandle} values.
		 */
		public void set(RUMBLE_AXIS axis, RUMBLE_POWER power, RUMBLE_SPEED speed, float duration, boolean continues, boolean fade) {
			this.power = power.val;
			totalDuration = duration;
			this.continues = continues;
			this.fade = fade;
			this.axis = axis;
			if (this.continues) {
				totalTime = totalDuration;
			}
			
			offset.set(0, 0);
			target.set(0, this.power);
			start.set(0, 0);
			
			totalTime = 0;
			singleTimer = 0;
			singleDuration = speed.val;
		}
		
		private void updatePosition(float power, float originPower, float delta) {
			float t = (float) Math.min(1.0, singleTimer / singleDuration);
			singleTimer += delta;
			
			if (axis == RUMBLE_AXIS.BOTH) {
				offset.x = lerp(this.target.x, this.start.x, t);
				offset.y = lerp(this.target.y, this.start.y, t);
			} else if (axis == RUMBLE_AXIS.X_ONLY) {
				offset.x = lerp(this.target.x, this.start.x, t);
			} else if (axis == RUMBLE_AXIS.Y_ONLY) {
				offset.y = lerp(this.target.y, this.start.y, t);
			}
			
			if (singleTimer >= singleDuration) {
				singleTimer = 0;
				start.set(target);
				VectorUtil.rotate(target, Math.PI / 2 + Math.random() * Math.PI);
				VectorUtil.setLength(target, power);
			}
		}
		private float lerp(float target, float start, float alpha) {
			return target * (alpha) + (1 - alpha) * start;
		}
		
		
		
	}
	
	// ENUMS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * The axis on which a single {@link RumbleHande} should rumble.
	 */
	public static enum RUMBLE_AXIS {
		BOTH, X_ONLY, Y_ONLY
	}
	/**
	 * The power a rumble effect can have.
	 */
	public static enum RUMBLE_POWER {
		WEAKEST(1), WEAKER(2), WEAK(3), MEDIUM(4), STRONG(5), STRONGER(6), STRONGEST(7), MEGA(8), ULTRA(10), EXTREME(15);
		private int val;
		
		private RUMBLE_POWER(int val) {
			this.val = val;
		}
		public int value() {
			return val;
		}
	}
	/**
	 * The speed a single 'rumble' from one position to another can have. The val method returns a accumulated time the
	 * rumble takes.
	 */
	public static enum RUMBLE_SPEED {
		SUPERSLOW(0.4f), SLOWEST(0.2f), SLOWER(0.1f), SLOW(0.075f), NORMAL(0.050f), FAST(0.040f), FASTER(0.033f), FASTEST(0.020f);
		private float val;
		
		private RUMBLE_SPEED(float val) {
			this.val = val;
		}
		public float value() {
			return val;
		}
	}
}
