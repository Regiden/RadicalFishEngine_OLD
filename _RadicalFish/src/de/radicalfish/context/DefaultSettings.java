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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;
import de.radicalfish.util.Logger;

/**
 * Simple implementation of {@link Settings} and {@link GraphicDetails}. Loads settings via {@link Properties}.
 * 
 * Note that values a loaded as soon as <code>loadSettings</code> is called. The C'tor just loads the properties without
 * setting any values.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 15.03.2012
 */
public class DefaultSettings implements Settings, GraphicDetails {
	
	private Properties properties;
	private String gamePath, userPath;
	
	private OperatingSystem system;
	
	private int textSpeed;
	private float musicVolume, soundVolume;
	private boolean debug, logging, fullscreen, sound3D, vsync, smoothDelta;
	private boolean postprocessing, animations, effects, shaders;
	private static boolean shaderSupported, fboSupported;
	
	/**
	 * Loads the properties file.
	 */
	public DefaultSettings(String path) throws SlickException {
		loadSettings(path);
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void loadSettings(String path) throws SlickException {
		loadProperties(path);
		
		Log.setVerbose(logging);
		Log.setLogSystem(new Logger(logging));
	}
	public void saveSettings(String path) {
		
	}
	
	public String toString() {
		Logger.none("------------------------ Settings ------------------------");
		Logger.none("Operating System: " + system);
		Logger.none("Game Path:        " + gamePath);
		Logger.none("User Path:        " + userPath);
		Logger.none("Properties:       " + properties.size());
		Logger.none("Common:");
		Logger.none("\tDebug:        " + debug);
		Logger.none("\tLogging:      " + logging);
		Logger.none("\tFullscreen:   " + fullscreen);
		Logger.none("\t3D Sound:     " + sound3D);
		Logger.none("\tVsync:        " + vsync);
		Logger.none("\tSmooth Delta: " + smoothDelta);
		Logger.none("\tText Speed:   " + textSpeed);
		Logger.none("\tMusic Volume: " + musicVolume);
		Logger.none("\tSound Volume: " + soundVolume);
		
		Logger.none("Graphics:");
		Logger.none("\tSupport Shader: " + shaderSupported);
		Logger.none("\tSupport FBO:    " + fboSupported);
		Logger.none("\tShader:         " + shaders);
		Logger.none("\tFBO:            " + postprocessing);
		Logger.none("\tEffects:        " + effects);
		Logger.none("\tAnimations:     " + animations);
		if (properties.size() > 11) {
			Logger.none("Others:");
			printAllUncommonProperties();
		}
		return "------------------------ Settings ------------------------";
	}
	
	// PRIVATE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void loadOS() {
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
		else
			system = OperatingSystem.OTHER;
	}
	private void loadProperties(String path) throws SlickException {
		loadOS();
		properties = new Properties();
		try {
			properties.load(ResourceLoader.getResourceAsStream(path));
		} catch (Exception e) {
			throw new SlickException("Could load settings file: " + path);
		}
		if (!properties.isEmpty()) {
			loadGraphicDetails();
			loadCommonSettings();
		} else {
			Log.info("Settings File is empty! Using Defaults!");
			loadDefaults();
		}
		
	}
	private void loadDefaults() {
		postprocessing = false;
		animations = true;
		shaders = false;
		effects = true;
		
		fullscreen = false;
		musicVolume = 1.0f;
		soundVolume = 1.0f;
		textSpeed = 1;
		sound3D = false;
	}
	private void loadGraphicDetails() {
		postprocessing = properties.getProperty("postprocessing", "false").equals("true");
		animations = properties.getProperty("animations", "false").equals("true");
		shaders = properties.getProperty("shaders", "false").equals("true");
		effects = properties.getProperty("effects", "false").equals("true");
	}
	private void loadCommonSettings() {
		fullscreen = properties.getProperty("fullscreen", "false").equals("true");
		debug = properties.getProperty("debug", "false").equals("true");
		logging = properties.getProperty("logging", "false").equals("true");
		logging = properties.getProperty("sound3D", "false").equals("true");
		vsync = properties.getProperty("vsync", "false").equals("true");
		smoothDelta = properties.getProperty("smoothdelta", "false").equals("true");
		
		musicVolume = castToFloat(properties.getProperty("music", "1.0"), 1.0f);
		soundVolume = castToFloat(properties.getProperty("sound", "1.0"), 1.0f);
		textSpeed = castToInteger(properties.getProperty("textspeed", "1"), 1);
		
	}
	
	private void printAllUncommonProperties() {
		Enumeration<Object> keys = properties.keys();
		String temp;
		int longestKey = 0;
		
		List<String> list = new ArrayList<String>();
		
		while (keys.hasMoreElements()) {
			temp = keys.nextElement().toString();
			// hacky way. assuming every special property has at least one "." (dot).
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
			Logger.none("\t" + s + ": " + fillEmptySpace(length) + properties.getProperty(s));
		}
	}
	private String fillEmptySpace(int length) {
		String s = "";
		for (int i = 0; i < length; i++) {
			s += " ";
		}
		return s;
	}
	private float castToFloat(String value, float defaultValue) {
		try {
			return Float.valueOf(value);
		} catch (NumberFormatException e) {}
		return defaultValue;
	}
	private int castToInteger(String value, int defaultValue) {
		try {
			return Integer.valueOf(value);
		} catch (NumberFormatException e) {}
		return defaultValue;
	}
	
	// GETTER GRAPHICS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public boolean isFBOSupported() {
		return fboSupported;
	};
	public boolean usePostProcessing() {
		return postprocessing && fboSupported;
	}
	public boolean useAnimations() {
		return animations;
	}
	public boolean useEffects() {
		return effects;
	}
	public boolean isShaderSupported() {
		return shaderSupported;
	}
	public boolean useShaders() {
		return shaders && shaderSupported;
	}
	
	// SETTER GRAPHICS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void setFBOSupported(boolean value) {
		fboSupported = value;
	};
	public void setShaderSupported(boolean value) {
		shaderSupported = value;
	}
	public void setUseShader(boolean value) {
		if (isShaderSupported()) {
			shaders = value;
		} else {
			shaders = false;
		}
	};
	public void setUsePostProcessing(boolean value) {
		if (isFBOSupported()) {
			postprocessing = value;
		} else {
			postprocessing = false;
		}
	}
	public void setUseAnimations(boolean value) {
		animations = value;
	}
	public void setUseEffects(boolean value) {
		effects = value;
	}
	
	// GETTER SETTINGS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public GraphicDetails getGraphicDetails() {
		return this;
	};
	public OperatingSystem getSystem() {
		return system;
	}
	
	public String getUserPath() {
		return userPath;
	}
	public String getGamePath() {
		return gamePath;
	}
	public String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}
	public boolean getProperty(String key, boolean defaultValue) {
		return getProperty(key, "" + defaultValue).equals("true");
	}
	public float getProperty(String key, float defaultValue) {
		try {
			return Float.valueOf(properties.getProperty(key));
		} catch (NumberFormatException e) {}
		return defaultValue;
	}
	public int getProperty(String key, int defaultValue) {
		try {
			return Integer.valueOf(properties.getProperty(key));
		} catch (NumberFormatException e) {}
		return defaultValue;
	}
	
	public float getSoundVolume() {
		return soundVolume;
	}
	public float getMusicVolume() {
		return musicVolume;
	}
	
	public int getTextSpeed() {
		return textSpeed;
	}
	
	public boolean isDebugging() {
		return debug;
	}
	public boolean isLogging() {
		return logging;
	}
	public boolean isFullscreen() {
		return fullscreen;
	}
	public boolean is3DSound() {
		return sound3D;
	}
	public boolean isVSync() {
		return vsync;
	}
	public boolean isSmoothDelta() {
		return smoothDelta;
	}
	
	// SETTER SETTINGS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void setFullscreen(boolean value) {
		fullscreen = value;
	}
	public void setDebugging(boolean value) {
		debug = value;
	}
	public void setLogging(boolean value) {
		logging = value;
	}
	public void setVSync(boolean value) {
		vsync = value;
	}
	public void setSmoothDelta(boolean value) {
		smoothDelta = value;
	}
	
	public void set3DSound(boolean value) {
		sound3D = value;
	}
	public void setMusicVolume(float value) {
		musicVolume = value;
	}
	public void setSoundVolume(float value) {
		soundVolume = value;
	}
	
	public void setTextSpeed(int value) {
		textSpeed = value;
	}
	
	public void setProperty(String key, String value) {
		properties.setProperty(key, value);
	}
}
