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
import java.util.Map;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Preferences;
import de.radicalfish.context.defaults.DefaultSettings;

/**
 * This interfaces manages settings. This can be a wrapper around {@link Preferences} like the {@link DefaultSettings}
 * use. For first start the game should load settings from an internal file and then use the Settings file saved on some
 * local storage for further runs.
 * <p>
 * To check of the file is already created you can get all key-value pairs with {@link Settings#getAllSettings()} and
 * check the number of keys. If they equal 0 this means the file does not has any settings and thus the settings should
 * be set.
 * <p>
 * The setter should not only set a value but add it when the value does not exits.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 11.03.2012
 */
public interface Settings {
	
	/**
	 * The possible OS's for a game
	 */
	public enum OperatingSystem {
		WINDOWS, LINUX, MAC, SUN_OS, ANDROID, IOS, WEB, APPLET, OTHER
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Can be used to print out the settings.
	 */
	public void printSettings();
	/**
	 * @return true if the given <code>key</code> is in the settings file.
	 */
	public boolean contains(String key);
	/**
	 * Removes the key-value pair from the settings.
	 */
	public void remove(String key);
	
	// SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Adds or Sets all the key-values pairs to the settings.
	 */
	public void setAllSettings(Map<String, ?> map);
	
	/**
	 * Adds or sets a property dynamically to the settings.
	 * 
	 * @param key
	 *            the key of the property
	 * @param value
	 *            the value of the property
	 */
	public void setProperty(String key, String value);
	/**
	 * Adds or sets a property dynamically to the settings.
	 * 
	 * @param key
	 *            the key of the property
	 * @param value
	 *            the value of the property
	 */
	public void setProperty(String key, float value);
	/**
	 * Adds or sets a property dynamically to the settings.
	 * 
	 * @param key
	 *            the key of the property
	 * @param value
	 *            the value of the property
	 */
	public void setProperty(String key, int value);
	/**
	 * Adds or sets a property dynamically to the settings.
	 * 
	 * @param key
	 *            the key of the property
	 * @param value
	 *            the value of the property
	 */
	public void setProperty(String key, long value);
	/**
	 * Adds or sets a property dynamically to the settings.
	 * 
	 * @param key
	 *            the key of the property
	 * @param value
	 *            the value of the property
	 */
	public void setProperty(String key, boolean value);
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return a map containing all mapped keys/fields.
	 */
	public Map<String, ?> getAllSettings();
	
	/**
	 * @return the local storage {@link Files#getLocalStoragePath()}.
	 */
	public String getLocalStorage();
	/**
	 * @return the external storage {@link Files#getExternalStoragePath()}.
	 */
	public String getExternalStorage();
	
	/**
	 * @return the system the games runs on.
	 */
	public OperatingSystem getSystem();
	/**
	 * @return the property associated with the key or the default if the key does not exist.
	 */
	public String getProperty(String key, String defaultValue);
	/**
	 * @return the property associated with the key casted to boolean or the default if the key does not exist.
	 */
	public boolean getProperty(String key, boolean defaultValue);
	/**
	 * @return the property associated with the key casted to integer or the default if the key does not exist.
	 */
	public int getProperty(String key, int defaultValue);
	/**
	 * @return the property associated with the key casted to long or the default if the key does not exist.
	 */
	public long getProperty(String key, long defaultValue);
	/**
	 * @return the property associated with the key casted to float or the default if the key does not exist.
	 */
	public float getProperty(String key, float defaultValue);
	
}
