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
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import org.newdawn.slick.util.LogSystem;

/**
 * This is a LogSystem which adds some methods to the normal logger.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 05.10.2011
 */
public class Logger implements LogSystem {
	
	/** The types a log can have. */
	public enum LOGTYPE {
		NONE, LOAD, ERROR, WARN, INFO, DEBUG, INIT
	}
	
	/** Standard {@link System} out stream. */
	public static PrintStream OUT = System.out;
	/** Standard {@link System} err stream. */
	public static PrintStream ERR = System.err;
	/** A log containing everything called by a {@link Logger}. */
	public static ArrayList<String> LOG = new ArrayList<String>();
	
	private static ArrayList<LogListener> listerner = new ArrayList<LogListener>();
	private static boolean isVerbose = true;
	private static int maxLogLines = 1000;
	
	// STATIC METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Adds a listener which gets called whenever this logger logs output. an initial call to the
	 * <code>logChanged()</code> will be made to initiate the logging for the listener. the <code>lastAdded</code>
	 * parameter will be empty for this call!
	 */
	public static void addLogListener(LogListener listener) {
		if (listener == null) {
			throw new NullPointerException("listener is null!");
		}
		Logger.listerner.add(listener);
		listener.logChanged(LOG, "", LOGTYPE.INIT);
	}
	/**
	 * Removes a listener from this logger.
	 */
	public static void removeLogListener(LogListener listener) {
		if (listener == null) {
			throw new NullPointerException("listener is null!");
		}
		Logger.listerner.remove(listener);
	}
	
	public static void load(String message) {
		if (isVerbose) {
			OUT.println("LOADING: " + message);
			appendLog(LOGTYPE.LOAD, message);
		}
	}
	public static void none(String message) {
		if (isVerbose) {
			OUT.println(message);
			appendLog(LOGTYPE.NONE, message);
		}
	}
	
	
	public int getMaxLogLines() {
		return maxLogLines;
	}
	/**
	 * @param lines
	 *            max number of lines
	 * @return a String containing the log with a max of <code>lines</code>.
	 */
	public static String getLogAsString(int lines) {
		StringBuilder sb = new StringBuilder();
		
		int min = LOG.size() - lines;
		if (min < 0)
			min = 0;
		for (int i = min; i < LOG.size(); i++)
			sb.append(LOG.get(i)).append("\n");
		
		return sb.toString();
	}
	
	
	
	public static void setMaxLogLines(int value) {
		maxLogLines = value;
		if(LOG.size() > maxLogLines) {
			int rem = LOG.size() - maxLogLines;
			for(int i = 0; i < rem; i++) {
				LOG.remove(maxLogLines + i);
			}
		}
		
	}
	public static void setLogging(boolean value) {
		isVerbose = value;
	}
	
	// INTERN STATIC METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private static void appendLog(LOGTYPE type, String message) {
		LOG.add(message);
		if (LOG.size() > maxLogLines)
			LOG.remove(0);
		
		for (LogListener lis : listerner) {
			lis.logChanged(LOG, message, type);
		}
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void error(String message, Throwable e) {
		error(message);
		error(e);
		if (isVerbose)
			appendLog(LOGTYPE.ERROR, message);
	}
	public void error(Throwable e) {
		error("");
		e.printStackTrace(ERR);
		
	}
	public void error(String message) {
		ERR.println(new Date() + " ERROR: " + message);
		if (isVerbose)
			appendLog(LOGTYPE.ERROR, message);
	}
	public void warn(String message) {
		OUT.println("WARNING: " + message);
		if (isVerbose)
			appendLog(LOGTYPE.WARN, message);
	}
	public void warn(String message, Throwable e) {
		OUT.println("WARNING: " + message);
		e.printStackTrace(OUT);
		if (isVerbose)
			appendLog(LOGTYPE.WARN, message);
	}
	public void info(String message) {
		OUT.println("INFO: " + message);
		if (isVerbose)
			appendLog(LOGTYPE.INFO, message);
	}
	public void debug(String message) {
		OUT.println("DEBUG: " + message);
		if (isVerbose)
			appendLog(LOGTYPE.DEBUG, message);
	}
	
}
