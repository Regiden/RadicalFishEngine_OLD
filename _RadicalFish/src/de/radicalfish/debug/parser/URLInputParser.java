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
package de.radicalfish.debug.parser;
import java.util.ArrayList;
import java.util.regex.Pattern;
import de.radicalfish.debug.DevConsole;
import de.radicalfish.debug.InputParser;

/**
 * URL parser for the {@link DevConsole}. the parser uses the key word <b>url</b>. The URL must fit the common url
 * style. supported protocolls are:
 * <p>
 * <li>http/https (e.g. http://radicalfish.de</li>
 * <li>www (e.g. www.radicalfish.de)</li>
 * <li>ftp (e.g. ftp://files.com</li>
 * <li>file (e.g. file://path/to/file</li>
 * <pre></pre>
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 14.05.2012
 */
public class URLInputParser implements InputParser {
	
	private static Pattern split = Pattern.compile(" ");
	
	private static Pattern url = Pattern.compile("\\b(https?://|ftp://|file://|www)[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
	
	// INTERFACE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public String parseInput(String message) {
		if (message.startsWith("url")) {
			String pair[] = split.split(message);
			if (pair.length == 2) {
				if (url.matcher(pair[1]).matches()) {
					return makeLingMessage(pair[1]);
				} else {
					return makeErrorMessage("Invalid URL format: " + pair[1] + " !");
				}
				
			} else {
				return makeErrorMessage("Invalid parameter length. Use the format: \"url someurl \" !");
			}
		}
		return message;
	}
	public ArrayList<String> getAutoCompletionContent() {
		return null;
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private String makeErrorMessage(String error) {
		return "<div style=\"font:error\" >" + error + "</div>";
	}
	private String makeLingMessage(String link) {
		return "<a style=\"font: link\" href=\"" + link + "\" >" + link + "</a>";
	}
	
}