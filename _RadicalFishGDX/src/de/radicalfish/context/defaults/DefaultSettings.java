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
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import org.newdawn.slick.SlickException;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import de.radicalfish.context.GraphicDetails;
import de.radicalfish.context.Settings;
import de.radicalfish.debug.Logger;
import de.radicalfish.util.ResourceLoader;

/**
 * Simple implementation of {@link Settings} and {@link GraphicDetails}. Loads settings via {@link Properties}.
 * <p>
 * Note that values are loaded as soon as <code>loadSettings</code> is called. The C'Tor just loads the properties
 * without setting any values. Use the C'Tor without a parameter to create an empty settings implementation. all values
 * will be set to defaults (false for boolean and 1.0 for floats).
 * <p>
 * Common values will be auto set. For this all common properties have the "pre" value of <code>common</code>.property.
 * The same goes for graphics with the <code>graphics</code>.property.
 * <p>
 * FBO support and Shader support works is GL20 is available.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 15.03.2012
 */
public class DefaultSettings implements Settings, GraphicDetails {
	
	private static boolean shaderSupported, fboSupported;
	
	private Properties properties;
	private String localStore, externalStore;
	
	private OperatingSystem system;
	
	private float musicVolume, soundVolume;
	private boolean debug, logging, fullscreen, sound3D, vsync, smoothDelta;
	private boolean postprocessing, animations, effects, shaders;
	
	/**
	 * Creates an empty settings object, with no set properties. the default properties will get default values. Logging
	 * will be set to true, assuming you use this C'Tor only for testing. A real game should load settings form
	 * somewhere.
	 */
	public DefaultSettings() {
		loadOS();
		properties = new Properties();
		loadDefaults();
		loadPaths();
		checkGraphicDetails();
		logging = true;
	}
	/**
	 * Loads the properties file. the path must be internal. calls {@link Settings#loadSettings(String)}.
	 */
	public DefaultSettings(String path) throws SlickException {
		loadSettings(path);
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void loadSettings(String path) throws SlickException {
		loadProperties(path);
		Logger.setLogging(logging);
	}
	public void saveSettings(String path) {
		
	}
	public void printSettings() {
		Logger.none("Operating System:  " + system);
		Logger.none("Local Storage:     " + localStore);
		Logger.none("External Strorage: " + externalStore);
		Logger.none("Properties:        " + properties.size());
		Logger.none("Common:");
		Logger.none("\tDebug:        " + debug);
		Logger.none("\tLogging:      " + logging);
		Logger.none("\tFullscreen:   " + fullscreen);
		Logger.none("\t3D Sound:     " + sound3D);
		Logger.none("\tVsync:        " + vsync);
		Logger.none("\tSmooth Delta: " + smoothDelta);
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
	}
	
	// PRIVATE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
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
			Logger.info("Settings File is empty! Using Defaults!");
			loadDefaults();
		}
		loadPaths();
		checkGraphicDetails();
	}
	private void loadDefaults() {
		postprocessing = false;
		animations = false;
		shaders = false;
		effects = false;
		
		musicVolume = 1.0f;
		soundVolume = 1.0f;
		fullscreen = false;
		sound3D = false;
		vsync = false;
		debug = false;
		logging = false;
	}
	private void loadGraphicDetails() {
		postprocessing = properties.getProperty("graphics.postprocessing", "false").equals("true");
		animations = properties.getProperty("graphics.animations", "false").equals("true");
		shaders = properties.getProperty("graphics.shaders", "false").equals("true");
		effects = properties.getProperty("graphics.effects", "false").equals("true");
	}
	private void loadCommonSettings() {
		fullscreen = properties.getProperty("common.fullscreen", "false").equals("true");
		debug = properties.getProperty("common.debug", "false").equals("true");
		logging = properties.getProperty("common.logging", "false").equals("true");
		logging = properties.getProperty("common.sound3D", "false").equals("true");
		vsync = properties.getProperty("common.vsync", "false").equals("true");
		smoothDelta = properties.getProperty("common.smoothdelta", "false").equals("true");
		
		musicVolume = castToFloat(properties.getProperty("common.music", "1.0"), 1.0f);
		soundVolume = castToFloat(properties.getProperty("common.sound", "1.0"), 1.0f);
	}
	private void loadPaths() {
		localStore = Gdx.files.getLocalStoragePath();
		externalStore = Gdx.files.getExternalStoragePath();
	}
	private void checkGraphicDetails() {
		fboSupported = Gdx.graphics.isGL20Available();
		shaderSupported = Gdx.graphics.isGL20Available();
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
	
	private void checkForSimpleSettings(String key) {
		checkForCommon(key);
		checkForGraphics(key);
	}
	private void checkForCommon(String key) {
		if (key.equals("common.debug")) {
			setDebugging(getProperty(key, false));
		} else if (key.equals("common.fullscreen")) {
			setFullscreen(getProperty(key, false));
		} else if (key.equals("common.sound3D")) {
			setSound3D(getProperty(key, false));
		} else if (key.equals("common.logging")) {
			setLogging(getProperty(key, false));
		} else if (key.equals("common.vsync")) {
			setVSync(getProperty(key, false));
		} else if (key.equals("common.smoothdelta")) {
			setSmoothDelta(getProperty(key, false));
		} else if (key.equals("common.music")) {
			setMusicVolume(getProperty(key, 0.0f));
		} else if (key.equals("common.sound")) {
			setSoundVolume(getProperty(key, 0.0f));
		}
	}
	private void checkForGraphics(String key) {
		if (key.equals("graphics.effect")) {
			setUseEffects(getProperty(key, false));
		} else if (key.equals("graphics.animation")) {
			setUseAnimations(getProperty(key, false));
		} else if (key.equals("graphics.shaders")) {
			setUseShader(getProperty(key, false));
		} else if (key.equals("graphics.postprocessing")) {
			setUsePostProcessing(getProperty(key, false));
		}
	}
	
	private void _setProperty(String key, String value) {
		properties.setProperty(key, value);
	}
	
	// GETTER GRAPHICS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public boolean isFBOSupported() {
		return fboSupported;
	}
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
	public boolean useShader() {
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
			_setProperty("graphics.shaders", "" + value);
		} else {
			shaders = false;
			_setProperty("graphics.shaders", "" + false);
		}
	};
	public void setUsePostProcessing(boolean value) {
		if (isFBOSupported()) {
			postprocessing = value;
			_setProperty("graphics.postprocessing", "" + value);
		} else {
			postprocessing = false;
			_setProperty("graphics.postprocessing", "" + false);
		}
	}
	public void setUseAnimations(boolean value) {
		animations = value;
		_setProperty("graphics.animations", "" + value);
	}
	public void setUseEffects(boolean value) {
		effects = value;
		_setProperty("graphics.effects", "" + value);
	}
	
	// GETTER SETTINGS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public GraphicDetails getGraphicDetails() {
		return this;
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
	
	public boolean isDebugging() {
		return debug;
	}
	public boolean isLogging() {
		return logging;
	}
	public boolean isFullscreen() {
		return fullscreen;
	}
	public boolean isSound3D() {
		return sound3D;
	}
	public boolean isVSync() {
		return vsync;
	}
	public boolean isSmoothDelta() {
		return smoothDelta;
	}
	
	public Properties getProperties() {
		return properties;
	}
	
	// SETTER SETTINGS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void setFullscreen(boolean value) {
		fullscreen = value;
		_setProperty("common.fullscreen", "" + value);
	}
	public void setDebugging(boolean value) {
		debug = value;
		_setProperty("common.debug", "" + value);
	}
	public void setLogging(boolean value) {
		logging = value;
		_setProperty("common.logging", "" + value);
		Logger.setLogging(logging);
	}
	public void setVSync(boolean value) {
		vsync = value;
		_setProperty("common.vsync", "" + value);
	}
	public void setSmoothDelta(boolean value) {
		smoothDelta = value;
		_setProperty("common.smoothdelta", "" + value);
	}
	
	public void setSound3D(boolean value) {
		sound3D = value;
		_setProperty("common.sound3D", "" + value);
	}
	public void setMusicVolume(float value) {
		musicVolume = value;
		_setProperty("common.music", "" + value);
	}
	public void setSoundVolume(float value) {
		soundVolume = value;
		_setProperty("common.sound", "" + value);
	}
	
	public void setProperty(String key, String value) {
		properties.setProperty(key, value);
		checkForSimpleSettings(key);
	}
}
