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
package de.radicalfish.util;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Some Utilities for useful for developing.
 * 
 * @author Stefan Lange
 * @version 0.2.0
 * @since 15.06.2012
 */
public class Utils {
	
	private static long timer = 0;
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Check is the object is null and if so throws an NPE.
	 */
	public static void notNull(String name, Object o) {
		if (o == null) {
			throw new NullPointerException(name + " is null!");
		}
	}
	
	/**
	 * Formats the given <code>time</code> to a string based on it's size. If the size of the time is to larger it will
	 * be push into the next time unit. ns -> us -> ms e.g.
	 * 
	 * <pre>
	 *  1.) time = 123456789 nano seconds
	 *  2.) time /= 1000
	 *  3.) check if time is still to large and repeat second step
	 *  return as string
	 * </pre>
	 * 
	 * @param time
	 * @return
	 */
	public static String formatTime(long time) {
		int steps = 0;
		while (time >= 1000) {
			time /= 1000;
			steps++;
		}
		return time + getTimeUnit(steps);
	}
	/**
	 * Starts a single nano timer, ended with {@link Utils#endNanoTimer()}. the value of the timmer will be overriden if
	 * this is called twice without a {@link Utils#endNanoTimer()} call.
	 */
	public static void startNanoTimer() {
		timer = TimeUtils.nanoTime();
	}
	/**
	 * Ends the nano timer.
	 * 
	 * @return the time between {@link Utils#startNanoTimer()} and this method call.
	 */
	public static long endNanoTimer() {
		return TimeUtils.nanoTime() - timer;
	}
	
	/**
	 * @param gdxKeyCode
	 *            the key to map
	 * @return maps a GDX key code to the LWJGL key code. If unsure it returns 0 and for AnyKey it returns -1.
	 */
	public static int mapGDXKeyToLWJGL(int gdxKeyCode) {
		switch (gdxKeyCode) {
			case -1:
				return -1;
			case 3:
				return 199;
			case 7:
				return 11;
			case 8:
				return 2;
			case 9:
				return 3;
			case 10:
				return 4;
			case 11:
				return 5;
			case 12:
				return 6;
			case 13:
				return 7;
			case 14:
				return 8;
			case 15:
				return 9;
			case 16:
				return 10;
			case 19:
				return 200;
			case 20:
				return 208;
			case 21:
				return 203;
			case 22:
				return 205;
			case 26:
				return 222;
			case 29:
				return 30;
			case 30:
				return 48;
			case 31:
				return 46;
			case 32:
				return 32;
			case 33:
				return 18;
			case 34:
				return 33;
			case 35:
				return 34;
			case 36:
				return 35;
			case 37:
				return 23;
			case 38:
				return 36;
			case 39:
				return 37;
			case 40:
				return 38;
			case 41:
				return 50;
			case 42:
				return 49;
			case 43:
				return 24;
			case 44:
				return 25;
			case 45:
				return 16;
			case 46:
				return 19;
			case 47:
				return 31;
			case 48:
				return 20;
			case 49:
				return 22;
			case 50:
				return 47;
			case 51:
				return 17;
			case 52:
				return 45;
			case 53:
				return 21;
			case 54:
				return 44;
			case 55:
				return 51;
			case 56:
				return 52;
			case 57:
				return 56;
			case 58:
				return 184;
			case 59:
				return 42;
			case 60:
				return 54;
			case 61:
				return 15;
			case 62:
				return 57;
			case 66:
				return 28;
			case 67:
				return 14;
			case 68:
				return 41;
			case 69:
				return 12;
			case 70:
				return 13;
			case 71:
				return 26;
			case 72:
				return 27;
			case 73:
				return 43;
			case 74:
				return 39;
			case 75:
				return 40;
			case 76:
				return 53;
			case 81:
				return 78;
			case 92:
				return 201;
			case 93:
				return 209;
			case 112:
				return 211;
			case 129:
				return 29;
			case 130:
				return 157;
			case 131:
				return 1;
			case 132:
				return 207;
			case 133:
				return 210;
			case 243:
				return 146;
			case 244:
				return 59;
			case 245:
				return 60;
			case 246:
				return 61;
			case 247:
				return 62;
			case 248:
				return 63;
			case 249:
				return 64;
			case 250:
				return 65;
			case 251:
				return 66;
			case 252:
				return 67;
			case 253:
				return 68;
			case 254:
				return 87;
			case 255:
				return 88;
		}
		return 0;
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private static String getTimeUnit(int steps) {
		switch (steps) {
			case 0:
				return "ns";
			case 1:
				return "us";
			case 2:
				return "ms";
			case 3:
				return "s";
			case 4:
				return "m";
			case 5:
				return "h";
			case 6:
				return "days";
			case 7:
				return "months";
			case 8:
				return "years";
			default:
				return "d (WTF dude check you calculation!)";
		}
	}
}
