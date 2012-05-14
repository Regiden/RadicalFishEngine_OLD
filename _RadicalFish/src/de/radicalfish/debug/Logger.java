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
	
	/** Standard {@link System} out stream. */
	public static PrintStream OUT = System.out;
	/** Standard {@link System} err stream. */
	public static PrintStream ERR = System.err;
	/** A log containing everything called by a {@link Logger}.*/
	public static ArrayList<String> LOG = new ArrayList<String>();
	
	private static boolean isVerbose;
	
	public Logger(boolean verbose) {
		isVerbose = verbose;
	}
	
	// STATIC METHODS
	// these are the ones which are new and must be used static (this is the reason we need an extra
	// boolean here)
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public static void load(String message) {
		if (isVerbose) {
			OUT.println("LOADING: " + message);
			appendLog(message);
		}
	}
	public static void none(String message) {
		if (isVerbose) {
			OUT.println(message);
			appendLog(message);
		}
	}
	public static String getLog(int lines) {
		StringBuilder sb = new StringBuilder();
		
		int min = LOG.size() - lines;
		if(min < 0)
			min = 0;
		for(int i = min; i < LOG.size(); i++) 
			sb.append(LOG.get(i)).append("\n");
		
		return sb.toString();
	}
	
	private static void appendLog(String message) {
		LOG.add(message);
		if(LOG.size() > 100)
			LOG.remove(0);
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void error(String message, Throwable e) {
		error(message);
		error(e);
		if(isVerbose)
			appendLog(message);
	}
	public void error(Throwable e) {
		error("");
		e.printStackTrace(ERR);
		
	}
	public void error(String message) {
		ERR.println(new Date() + " ERROR: " + message);
		if(isVerbose)
			appendLog(message);
	}
	public void warn(String message) {
		OUT.println("WARNING: " + message);
		if(isVerbose)
			appendLog(message);
	}
	public void warn(String message, Throwable e) {
		OUT.println("WARNING: " + message);
		e.printStackTrace(OUT);
		if(isVerbose)
			appendLog(message);
	}
	public void info(String message) {
		OUT.println("INFO: " + message);
		if(isVerbose)
			appendLog(message);
	}
	public void debug(String message) {
		OUT.println("DEBUG: " + message);
		if(isVerbose)
			appendLog(message);
	}

}
