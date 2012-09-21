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
package de.radicalfish.context;
import com.badlogic.gdx.utils.Array;

/**
 * Getter and Setter for primitive values.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 09.04.2012
 */
public interface GameVariables {
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return the number of string primitives.
	 */
	public int getNumberOfStrings();
	/**
	 * @return the number of boolean primitives.
	 */
	public int getNumberOfBooleans();
	/**
	 * @return the number of integer primitives.
	 */
	public int getNumberOfInts();
	/**
	 * @return the number of float primitives.
	 */
	public int getNumberOfFloats();
	
	/**
	 * @return an array with all string keys.
	 */
	public Array<String> getStringKeys();
	/**
	 * @return an array with all boolean keys.
	 */
	public Array<String> getBooleanKeys();
	/**
	 * @return an array with all integer keys.
	 */
	public Array<String> getIntKeys();
	/**
	 * @return an array with all float keys.
	 */
	public Array<String> getFloatKeys();
	
	/**
	 * @return a String saved in the context of the game.
	 */
	public String getString(String key, String defaultValue);
	/**
	 * @return a boolean saved in the context of the game.
	 */
	public boolean getBoolean(String key, boolean defaltValue);
	/**
	 * @return an integer saved in the context of the game.
	 */
	public int getInt(String key, int defaultValue);
	/**
	 * @return a float saved in the context of the game.
	 */
	public float getFloat(String key, float defaultValue);
	
	// SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Sets a String to a value.
	 */
	public void putString(String key, String value);
	/**
	 * Sets a boolean to a value.
	 */
	public void putBoolean(String key, boolean value);
	/**
	 * Sets an integer to a value.
	 */
	public void putInt(String key, int value);
	/**
	 * Sets a float to a value.
	 */
	public void putFloat(String key, float value);
}