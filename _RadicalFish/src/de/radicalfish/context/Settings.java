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
import org.newdawn.slick.SlickException;

// TODO add setter

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
		WINDOWS, LINUX, MAC, SUN_OS, OTHER
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Loads the settings.
	 */
	public void loadSettings(String path) throws SlickException;
	/**
	 * Saves the settings.
	 */
	public void saveSettings(String path) throws SlickException;
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return the objects which holds the details about the game's graphics.
	 */
	public GraphicDetails getGraphicDetails();
	/**
	 * @return the path where the game saves its state.
	 */
	public String getUserPath();
	/**
	 * @return the path which the game runs from.
	 */
	public String getGamePath();
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
	 * @return the volume of the sound.
	 */
	public float getSoundVolume();
	/**
	 * @return the volume of the music.
	 */
	public float getMusicVolume();
	/**
	 * @return true if the game should use 3D sound.
	 */
	public boolean is3DSound();
	/**
	 * @return the speed of the text.
	 */
	public int getTextSpeed();
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
	
}
