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


/**
 * Some Utilities for useful for developing.
 * 
 * @author Stefan Lange
 * @version 0.2.0
 * @since 15.06.2012
 */
public class Utils {
	
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
