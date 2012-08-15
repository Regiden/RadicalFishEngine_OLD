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
				return 0;
			case 58:
				return 0;
			case 59:
				return 0;
			case 60:
				return 0;
			case 61:
				return 0;
			case 62:
				return 0;
			case 63:
				return 0;
			case 64:
				return 0;
			case 65:
				return 0;
			case 66:
				return 0;
			case 67:
				return 0;
			case 68:
				return 0;
			case 69:
				return 0;
			case 70:
				return 0;
			case 71:
				return 0;
			case 72:
				return 0;
			case 73:
				return 0;
			case 74:
				return 0;
			case 75:
				return 0;
			case 76:
				return 0;
			case 77:
				return 0;
			case 78:
				return 0;
			case 79:
				return 0;
			case 80:
				return 0;
			case 81:
				return 0;
			case 82:
				return 0;
			case 83:
				return 0;
			case 84:
				return 0;
			case 85:
				return 0;
			case 86:
				return 0;
			case 87:
				return 0;
			case 88:
				return 0;
			case 89:
				return 0;
			case 90:
				return 0;
			case 91:
				return 0;
			case 92:
				return 0;
			case 93:
				return 0;
			case 94:
				return 0;
			case 95:
				return 0;
			case 96:
				return 0;
			case 97:
				return 0;
			case 98:
				return 0;
			case 99:
				return 0;
			case 100:
				return 0;
			case 101:
				return 0;
			case 102:
				return 0;
			case 103:
				return 0;
			case 104:
				return 0;
			case 105:
				return 0;
			case 106:
				return 0;
			case 107:
				return 0;
			case 108:
				return 0;
			case 109:
				return 0;
			case 110:
				return 0;
			case 111:
				return 0;
			case 112:
				return 0;
			case 113:
				return 0;
			case 114:
				return 0;
			case 115:
				return 0;
			case 116:
				return 0;
			case 117:
				return 0;
			case 118:
				return 0;
			case 119:
				return 0;
			case 120:
				return 0;
			case 121:
				return 0;
			case 122:
				return 0;
			case 123:
				return 0;
			case 124:
				return 0;
			case 125:
				return 0;
			case 126:
				return 0;
			case 127:
				return 0;
			case 128:
				return 0;
			case 129:
				return 0;
			case 130:
				return 0;
			case 131:
				return 0;
			case 132:
				return 0;
			case 133:
				return 0;
			case 134:
				return 0;
			case 135:
				return 0;
			case 136:
				return 0;
			case 137:
				return 0;
			case 138:
				return 0;
			case 139:
				return 0;
			case 140:
				return 0;
			case 141:
				return 0;
			case 142:
				return 0;
			case 143:
				return 0;
			case 144:
				return 0;
			case 145:
				return 0;
			case 146:
				return 0;
			case 147:
				return 0;
			case 148:
				return 0;
			case 149:
				return 0;
			case 150:
				return 0;
			case 151:
				return 0;
			case 152:
				return 0;
			case 153:
				return 0;
			case 154:
				return 0;
			case 155:
				return 0;
			case 156:
				return 0;
			case 157:
				return 0;
			case 158:
				return 0;
			case 159:
				return 0;
			case 160:
				return 0;
			case 161:
				return 0;
			case 162:
				return 0;
			case 163:
				return 0;
			case 164:
				return 0;
			case 165:
				return 0;
			case 166:
				return 0;
			case 167:
				return 0;
			case 168:
				return 0;
			case 169:
				return 0;
			case 170:
				return 0;
			case 171:
				return 0;
			case 172:
				return 0;
			case 173:
				return 0;
			case 174:
				return 0;
			case 175:
				return 0;
			case 176:
				return 0;
			case 177:
				return 0;
			case 178:
				return 0;
			case 179:
				return 0;
			case 180:
				return 0;
			case 181:
				return 0;
			case 182:
				return 0;
			case 183:
				return 0;
			case 184:
				return 0;
			case 185:
				return 0;
			case 186:
				return 0;
			case 187:
				return 0;
			case 188:
				return 0;
			case 189:
				return 0;
			case 190:
				return 0;
			case 191:
				return 0;
			case 192:
				return 0;
			case 193:
				return 0;
			case 194:
				return 0;
			case 195:
				return 0;
			case 196:
				return 0;
			case 197:
				return 0;
			case 198:
				return 0;
			case 199:
				return 0;
			case 200:
				return 0;
			case 201:
				return 0;
			case 202:
				return 0;
			case 203:
				return 0;
			case 204:
				return 0;
			case 205:
				return 0;
			case 206:
				return 0;
			case 207:
				return 0;
			case 208:
				return 0;
			case 209:
				return 0;
			case 210:
				return 0;
			case 211:
				return 0;
			case 212:
				return 0;
			case 213:
				return 0;
			case 214:
				return 0;
			case 215:
				return 0;
			case 216:
				return 0;
			case 217:
				return 0;
			case 218:
				return 0;
			case 219:
				return 0;
			case 220:
				return 0;
			case 221:
				return 0;
			case 222:
				return 0;
			case 223:
				return 0;
			case 224:
				return 0;
			case 225:
				return 0;
			case 226:
				return 0;
			case 227:
				return 0;
			case 228:
				return 0;
			case 229:
				return 0;
			case 230:
				return 0;
			case 231:
				return 0;
			case 232:
				return 0;
			case 233:
				return 0;
			case 234:
				return 0;
			case 235:
				return 0;
			case 236:
				return 0;
			case 237:
				return 0;
			case 238:
				return 0;
			case 239:
				return 0;
			case 240:
				return 0;
			case 241:
				return 0;
			case 242:
				return 0;
			case 243:
				return 0;
			case 244:
				return 0;
			case 245:
				return 0;
			case 246:
				return 0;
			case 247:
				return 0;
			case 248:
				return 0;
			case 249:
				return 0;
			case 250:
				return 0;
			case 251:
				return 0;
			case 252:
				return 0;
			case 253:
				return 0;
			case 254:
				return 0;
			case 255:
				return 0;
		}
		return -2;
	}
}
