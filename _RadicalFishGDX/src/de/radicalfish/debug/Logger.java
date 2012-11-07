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
package de.radicalfish.debug;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;

/**
 * This Logger is used to have a global logger for this engine. With this you can log in all classes and just turn on or
 * turn off logging. You can still use the {@link com.badlogic.gdx.utils.Logger} from libgdx.
 * 
 * @author Stefan Lange
 * @version 1.2.0
 * @since 05.10.2011
 */
public class Logger {
	
	private static boolean logging = true;
	
	// STATIC METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public static void none(String message) {
		if (logging) {
			if (Gdx.app == null || Gdx.app.getType() == ApplicationType.Desktop) {
				System.out.println(message);
			} else {
				log("", message);
			}
		}
	}
	public static void load(String message) {
		if (logging) {
			log("LOADING: ", message);
		}
	}
	public static void error(String message) {
		if (logging) {
			logError("", message);
		}
	}
	public static void error(String message, Throwable e) {
		if (logging) {
			logError("ERROR", message, e);
		}
	}
	public static void warn(String message) {
		if (logging) {
			log("WARN", message);
		}
	}
	public static void info(String message) {
		if (logging) {
			log("INFO", message);
		}
	}
	public static void debug(String message) {
		if (logging) {
			log("DEBUG", message);
		}
	}
	
	// INTERN STATIC METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private static void log(String tag, String message) {
		if (Gdx.app != null) {
			Gdx.app.log(tag, message);
		} else {
			System.out.println(tag + ": " + message);
		}
	}
	private static void logError(String tag, String message) {
		if (Gdx.app != null) {
			Gdx.app.error(tag, message);
		} else {
			System.err.println(tag + ": " + message);
		}
	}
	private static void logError(String tag, String message, Throwable e) {
		if (Gdx.app != null) {
			Gdx.app.error(tag, message, e);
		} else {
			System.err.println(tag + ": " + message);
			e.printStackTrace();
		}
	}
	
	// SETTER & GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * True if we want logging or not.
	 */
	public static void setLogging(boolean value) {
		logging = value;
	}
	/**
	 * 
	 * @return true if we are logging.
	 */
	public static boolean isLogging() {
		return logging;
	}
}
