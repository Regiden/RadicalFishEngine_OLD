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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import de.radicalfish.context.Settings;
import de.radicalfish.debug.Logger;

/**
 * Simple implementation of {@link Settings}. The {@link Preferences} interface from libgdx will be used here.
 * <p>
 * If you set a property which contains the string 'logging' the {@link Logger#setLogging(boolean)} method will be
 * called with the value (the value should equal true or false. If not nothing happens).
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 15.03.2012
 */
public class DefaultSettings implements Settings {
	
	private Preferences prefs;
	private String localStore, externalStore;
	
	private OperatingSystem system;
	
	/**
	 * Creates a new {@link Settings} implementation from a {@link Preferences} instance.
	 */
	public DefaultSettings(Preferences prefs) {
		create(prefs);
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void printSettings() {
		Logger.none("Operating System:  " + system);
		Logger.none("Local Storage:     " + localStore);
		Logger.none("External Strorage: " + externalStore);
		Logger.none("Properties: ");
		printProperties();
	}
	
	public boolean contains(String key) {
		return prefs.contains(key);
	}
	public void remove(String key) {
		prefs.remove(key);
	}
	
	// PRIVATE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void create(Preferences prefs) {
		this.prefs = prefs;
		loadOS();
		loadPaths();
	}
	
	private void loadOS() {
		ApplicationType type = Gdx.app.getType();
		switch (type) {
			case Android:
				system = OperatingSystem.ANDROID;
			case Desktop:
				String os = System.getProperty("os.name").toLowerCase();
				if (os.contains("win"))
					system = OperatingSystem.WINDOWS;
				else if (os.contains("linux"))
					system = OperatingSystem.LINUX;
				else if (os.contains("unix"))
					system = OperatingSystem.LINUX;
				else if (os.contains("mac"))
					system = OperatingSystem.MAC;
				else if (os.contains("solaris"))
					system = OperatingSystem.SUN_OS;
				else if (os.contains("sunos"))
					system = OperatingSystem.SUN_OS;
				break;
			case Applet:
				system = OperatingSystem.APPLET;
				break;
			case iOS:
				system = OperatingSystem.IOS;
				break;
			case WebGL:
				system = OperatingSystem.WEB;
				break;
			default:
				system = OperatingSystem.OTHER;
		}
	}
	private void loadPaths() {
		localStore = Gdx.files.getLocalStoragePath();
		externalStore = Gdx.files.getExternalStoragePath();
	}
	
	private void printProperties() {
		Iterator<String> keys = prefs.get().keySet().iterator();
		String temp;
		int longestKey = 0;
		
		List<String> list = new ArrayList<String>();
		while (keys.hasNext()) {
			temp = keys.next();
			if (temp.contains(".")) {
				list.add(temp);
				if (temp.length() > longestKey) {
					longestKey = temp.length();
				}
			}
		}
		
		int length = 0;
		Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
		for (String s : list) {
			length = longestKey - s.length();
			Logger.none("\t" + s + ": " + fillEmptySpace(length) + prefs.getString(s));
		}
	}
	private String fillEmptySpace(int length) {
		String s = "";
		for (int i = 0; i < length; i++) {
			s += " ";
		}
		return s;
	}
	
	// SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void setAllSettings(Map<String, ?> map) {
		prefs.put(map);
	}
	
	public void setProperty(String key, String value) {
		prefs.putString(key, value);
		if (key.contains("logging")) {
			if (value.equals("true") || value.equals("false")) {
				Logger.setLogging(value.equals("true"));
			}
		}
	}
	public void setProperty(String key, boolean value) {
		prefs.putBoolean(key, value);
		if (key.contains("logging")) {
			Logger.setLogging(value);
		}
	}
	public void setProperty(String key, float value) {
		prefs.putFloat(key, value);
	}
	public void setProperty(String key, int value) {
		prefs.putInteger(key, value);
	}
	public void setProperty(String key, long value) {
		prefs.putLong(key, value);
	}
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public Map<String, ?> getAllSettings() {
		return prefs.get();
	}
	
	public OperatingSystem getSystem() {
		return system;
	}
	
	public String getLocalStorage() {
		return localStore;
	}
	public String getExternalStorage() {
		return externalStore;
	}
	
	public String getProperty(String key, String defaultValue) {
		return prefs.getString(key, defaultValue);
	}
	public boolean getProperty(String key, boolean defaultValue) {
		return prefs.getBoolean(key, defaultValue);
	}
	public float getProperty(String key, float defaultValue) {
		return prefs.getFloat(key, defaultValue);
	}
	public int getProperty(String key, int defaultValue) {
		return prefs.getInteger(key, defaultValue);
	}
	public long getProperty(String key, long defaultValue) {
		return prefs.getLong(key, defaultValue);
	}
	
}
