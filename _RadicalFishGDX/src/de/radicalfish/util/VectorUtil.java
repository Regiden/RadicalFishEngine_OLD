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
package de.radicalfish.util;
import com.badlogic.gdx.math.Vector2;

/**
 * Some functions helpful add-ons to the Math class.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 14.07.2011
 */
public final class VectorUtil {
	
	// NO C'Tor
	private VectorUtil() {}
	
	// VECTOR
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Makes the given {@link Vector2} to the perpendicular to this vector.
	 * 
	 * @return the same object but with the new x = -y and y = x.
	 */
	public static Vector2 makePerpendicular(Vector2 vec) {
		return vec.set(-vec.y, vec.x);
	}
	/**
	 * Rotates the given {@link Vector2} by the given <code>radians</code>. the rotation is counter-clockwise.
	 * 
	 * @param radians
	 *            the radians in the scope of 0 - Math.PI * 2.
	 * @return the rotated vector.
	 */
	public static Vector2 rotate(Vector2 vec, double radians) {
		float x = vec.x;
		vec.x = (float) (Math.cos(radians) * x + Math.sin(radians) * vec.y);
		vec.y = (float) (Math.sin(-radians) * x + Math.cos(radians) * vec.y);
		return vec;
	}
	/**
	 * Sets the length of the vector.
	 * 
	 * @param length
	 *            the length the vector should have.
	 * @return the vector with the new length.
	 */
	public static Vector2 setLength(Vector2 vec, float length) {
		float cur = vec.len();
		if (cur == 0.0f) {
			vec.x = length;
			vec.y = 0;
		} else {
			float f = length / cur;
			vec.x *= f;
			vec.y *= f;
		}
		return vec;
	}
}
