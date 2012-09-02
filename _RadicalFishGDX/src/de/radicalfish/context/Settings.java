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
import de.radicalfish.util.RadicalFishException;

/**
 * small interfaces for some simple settings.
 * 
 * @author Stefan Lange
 * @version 0.0.0
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
	 * Loads the settings.
	 */
	public void loadSettings(String name) throws RadicalFishException;
	/**
	 * Saves the settings.
	 */
	public void saveSettings() throws RadicalFishException;
	/**
	 * Can be used to print out the settings.
	 */
	public void printSettings();
	
	/**
	 * @return true if the given <code>key</code> is in the settings file.
	 */
	public boolean contains(String key);
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return a map containing all mapped keys/fields.
	 */
	public Map<String, ?> getAll();
	
	/**
	 * @return the objects which holds the details about the game's graphics.
	 */
	public GraphicDetails getGraphicDetails();
	/**
	 * @return the local storage {@link Files#getLocalStoragePath()}.
	 */
	public String getLocalStorage();
	/**
	 * @return the external storage {@link Files#getExternalStoragePath()}.
	 */
	public String getExternalStorage();
	/**
	 * @return true if the game runs in debug mode.
	 */
	public boolean isDebugging();
	/**
	 * @return true if logging is turned on
	 */
	public boolean isLogging();
	/**
	 * @return true if the game runs in fullscreen or should run in fullscreen.
	 */
	public boolean isFullscreen();
	/**
	 * @return true if the game should use 3D sound.
	 */
	public boolean isSound3D();
	/**
	 * @return true if the game should sync the frame rate at the monitor frame rate
	 */
	public boolean isVSync();
	/**
	 * @return true if the deltas should be smoothed (A Slick2D Feature, works best with vsync enabled);
	 */
	public boolean isSmoothDelta();
	/**
	 * @return the volume of the sound.
	 */
	public float getSoundVolume();
	/**
	 * @return the volume of the music.
	 */
	public float getMusicVolume();
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
	 * @return the property associated with the key casted to float or the default if the key does not exist.
	 */
	public float getProperty(String key, float defaultValue);
	
	// SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @param value
	 *            true for fullscreen, false otherwise
	 */
	public void setFullscreen(boolean value);
	/**
	 * @param value
	 *            true for global debugging, false otherwise (ideally it disables all sub debugging)
	 */
	public void setDebugging(boolean value);
	/**
	 * @param value
	 *            true if the game should log (to a file or whatever)
	 */
	public void setLogging(boolean value);
	/**
	 * @param value
	 *            true if the game should use 3Dsound (ignore if not wanted)
	 */
	public void setSound3D(boolean value);
	/**
	 * @param value
	 *            true if the game should use vertical synchronization
	 */
	public void setVSync(boolean value);
	/**
	 * @param value
	 *            true if the game should smooth the deltas
	 */
	public void setSmoothDelta(boolean value);
	/**
	 * @param value
	 *            the volume of the sound (range should be 0.0 - 1.0)
	 */
	public void setSoundVolume(float value);
	/**
	 * @param value
	 *            the volume of the music (range should be 0.0 - 1.0)
	 */
	public void setMusicVolume(float value);
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
	public void setProperty(String key, boolean value);
}
