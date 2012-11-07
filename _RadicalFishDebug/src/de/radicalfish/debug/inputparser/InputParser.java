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
package de.radicalfish.debug.inputparser;
import java.util.ArrayList;
import de.matthiasmann.twl.textarea.HTMLTextAreaModel;
import de.radicalfish.debug.DeveloperConsole;

/**
 * Callback for user based handling of console input. A parser should always start with a keyword.
 * <p>
 * {@link InputParser#parseInput(String, DeveloperConsole)} will be called once enter is hit in the
 * {@link DeveloperConsole}. If the input matches the parser it should return the text to display (as HMTL if you want
 * see {@link HTMLTextAreaModel} for a list of supported tags). If not simply return the input string.
 * <p>
 * {@link InputParser#getAutoCompletionContent(String)} should return a list which matches the current input. This way
 * the user can select from the list directly to shorten the input time.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 14.05.2012
 */
public interface InputParser {
	
	/**
	 * Get's called when hitting the submit button or pressing return/enter on the edit field. Use this message to
	 * proceed the input with your own parser.
	 * 
	 * @param message
	 *            the input of the text field
	 * @param console
	 *            the console from which the message comes
	 * @return the message to show on the text area or <code>message</code> to forward the input to the
	 *         {@link DeveloperConsole} parser.
	 */
	public String parseInput(String message, DeveloperConsole console);
	
	/**
	 * @param input
	 *            the current typed input
	 * @return a list of possible commands for auto completion. can be null
	 */
	public ArrayList<String> getAutoCompletionContent(String input);
	
}
