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
package de.radicalfish.context.defaults;
import java.util.HashMap;
import com.badlogic.gdx.utils.ObjectIntMap;
import de.radicalfish.context.GameVariables;
import de.radicalfish.util.collection.ObjectBooleanMap;
import de.radicalfish.util.collection.ObjectFloatMap;

/**
 * Simple implementation of {@link GameVariables}.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 09.04.2012
 */
public class DefaultGameVariables implements GameVariables {
	
	private HashMap<String, String> strings;
	private ObjectBooleanMap<String> bools;
	private ObjectIntMap<String> ints;
	private ObjectFloatMap<String> floats;
	
	public DefaultGameVariables() {
		strings = new HashMap<String, String>();
		bools = new ObjectBooleanMap<String>();
		ints = new ObjectIntMap<String>();
		floats = new ObjectFloatMap<String>();
	}
	
	// PRIVATE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void checkKey(String key) {
		if (!bools.containsKey(key))
			bools.put(key, false);
	}
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * If the key does not exist if will be created with the default value false. This means the parameter
	 * <code>defaultValue</code> in this implementation is ignored.
	 */
	public boolean getBoolean(String key, boolean defaltValue) {
		checkKey(key);
		return bools.get(key, defaltValue);
	}
	public String getString(String key, String defaultValue) {
		if (strings.containsKey(key)) {
			return strings.get(key);
		} else {
			return defaultValue;
		}
	}
	public int getInt(String key, int defaultValue) {
		return ints.get(key, defaultValue);
	}
	public float getFloat(String key, float defaultValue) {
		return floats.get(key, defaultValue);
	}
	
	// SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void putBoolean(String key, boolean value) {
		bools.put(key, value);
	}
	public void putString(String key, String value) {
		strings.put(key, value);
	}
	public void putInt(String key, int value) {
		ints.put(key, value);
	}
	public void putFloat(String key, float value) {
		floats.put(key, value);
	}
	
}
