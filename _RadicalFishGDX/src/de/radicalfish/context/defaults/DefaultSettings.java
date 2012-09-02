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
import de.radicalfish.context.GraphicDetails;
import de.radicalfish.context.Settings;
import de.radicalfish.debug.Logger;
import de.radicalfish.util.RadicalFishException;

/**
 * Simple implementation of {@link Settings} and {@link GraphicDetails}. The {@link Preferences} interface from libgdx
 * will be used here.
 * <p>
 * Note that values are loaded as soon as <code>loadSettings</code> is called. The C'Tor just loads the properties
 * without setting any values. Use the C'Tor without a parameter to create an empty settings implementation. all values
 * will be set to defaults (false for boolean and 1.0 for floats).
 * <p>
 * Common values will be auto set. For this all common properties must use the "pre" value of <code>common</code>
 * .property. The same goes for graphics with the <code>graphics</code>.property.
 * <p>
 * FBO support and Shader support works if GL20 is available.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 15.03.2012
 */
public class DefaultSettings implements Settings, GraphicDetails {
	
	private static boolean shaderSupported, fboSupported;
	
	private Preferences prefs;
	private String localStore, externalStore;
	
	private OperatingSystem system;
	
	private float musicVolume, soundVolume;
	private boolean debug, logging, fullscreen, sound3D, vsync, smoothDelta;
	private boolean postprocessing, animations, effects, shaders;
	
	/**
	 * Creates an empty settings object. the name of the {@link Preferences} used here will be defaults-rfe.xml. Logging
	 * will be set to true.
	 * 
	 * @throws RadicalFishException
	 */
	public DefaultSettings() throws RadicalFishException {
		loadSettings("defaults-rfe.xml");
		setLogging(true);
	}
	/**
	 * Loads the settings file by the given <code>name</code>.
	 */
	public DefaultSettings(String name) throws RadicalFishException {
		loadSettings(name);
		Logger.setLogging(logging);
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void loadSettings(String name) throws RadicalFishException {
		loadPrefs(name);
		
	}
	public void saveSettings() throws RadicalFishException {
		try {
			prefs.flush();
		} catch (Exception e) {
			Logger.error(e.getMessage(), e.getCause());
			throw new RadicalFishException(e.getMessage());
		}
	}
	public void printSettings() {
		Logger.none("Operating System:  " + system);
		Logger.none("Local Storage:     " + localStore);
		Logger.none("External Strorage: " + externalStore);
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
		Logger.none("Others:");
		printAllUncommonProperties();
	}
	
	public boolean contains(String key) {
		return prefs.contains(key);
	}
	
	public Map<String, ?> getAll() {
		return prefs.get();
	}
	
	// PRIVATE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void loadPrefs(String name) throws RadicalFishException {
		loadOS();
		prefs = Gdx.app.getPreferences(name);
		if (name.equals("defaults-rfe.xml")) {
			loadDefaults();
		}
		
		loadCommonSettings();
		loadGraphicDetails();
		loadPaths();
		checkGraphicDetails();
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
	private void loadDefaults() {
		prefs.putBoolean("common.fullscreen", false);
		prefs.putBoolean("common.debug", false);
		prefs.putBoolean("common.logging", true);
		prefs.putBoolean("common.vsync", true);
		prefs.putBoolean("common.sound3D", false);
		prefs.putBoolean("common.smoothdelta", false);
		
		prefs.putFloat("common.music", 1.0f);
		prefs.putFloat("common.sound", 1.0f);
		
		prefs.putBoolean("graphics.postprocessing", false);
		prefs.putBoolean("graphics.animations", false);
		prefs.putBoolean("graphics.shaders", false);
		prefs.putBoolean("graphics.effects", false);
		
	}
	private void loadGraphicDetails() {
		postprocessing = prefs.getBoolean("graphics.postprocessing", false);
		animations = prefs.getBoolean("graphics.animations", false);
		shaders = prefs.getBoolean("graphics.shaders", false);
		effects = prefs.getBoolean("graphics.effects", false);
	}
	private void loadCommonSettings() {
		fullscreen = prefs.getBoolean("common.fullscreen", false);
		debug = prefs.getBoolean("common.debug", false);
		logging = prefs.getBoolean("common.logging", false);
		sound3D = prefs.getBoolean("common.sound3D", false);
		vsync = prefs.getBoolean("common.vsync", false);
		smoothDelta = prefs.getBoolean("common.smoothdelta", false);
		
		musicVolume = prefs.getFloat("common.music", 1.0f);
		soundVolume = prefs.getFloat("common.sound", 1.0f);
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
		Iterator<String> keys = prefs.get().keySet().iterator();
		String temp;
		int longestKey = 0;
		
		List<String> list = new ArrayList<String>();
		while (keys.hasNext()) {
			temp = keys.next();
			if (temp.contains(".") && !temp.startsWith("common") && !temp.startsWith("graphics")) {
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
			prefs.putBoolean("graphics.shaders", value);
		} else {
			shaders = false;
			prefs.putBoolean("graphics.shaders", false);
		}
	};
	public void setUsePostProcessing(boolean value) {
		if (isFBOSupported()) {
			postprocessing = value;
			prefs.putBoolean("graphics.postprocessing", value);
		} else {
			postprocessing = false;
			prefs.putBoolean("graphics.postprocessing", false);
		}
	}
	public void setUseAnimations(boolean value) {
		animations = value;
		prefs.putBoolean("graphics.animations", value);
	}
	public void setUseEffects(boolean value) {
		effects = value;
		prefs.putBoolean("graphics.effects", value);
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
	
	// SETTER SETTINGS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void setProperty(String key, String value) {
		prefs.putString(key, value);
		checkForSimpleSettings(key);
	}
	public void setProperty(String key, boolean value) {
		prefs.putBoolean(key, value);
		checkForSimpleSettings(key);
	}
	public void setProperty(String key, float value) {
		prefs.putFloat(key, value);
		checkForSimpleSettings(key);
	}
	public void setProperty(String key, int value) {
		prefs.putInteger(key, value);
		checkForSimpleSettings(key);
	}
	
	public void setFullscreen(boolean value) {
		fullscreen = value;
		prefs.putBoolean("common.fullscreen", value);
	}
	public void setDebugging(boolean value) {
		debug = value;
		prefs.putBoolean("common.debug", value);
	}
	public void setLogging(boolean value) {
		logging = value;
		prefs.putBoolean("common.logging", value);
		Logger.setLogging(logging);
	}
	public void setVSync(boolean value) {
		vsync = value;
		prefs.putBoolean("common.vsync", value);
	}
	public void setSmoothDelta(boolean value) {
		smoothDelta = value;
		prefs.putBoolean("common.smoothdelta", value);
	}
	
	public void setSound3D(boolean value) {
		sound3D = value;
		prefs.putBoolean("common.sound3D", value);
	}
	public void setMusicVolume(float value) {
		musicVolume = value;
		prefs.putFloat("common.music", value);
	}
	public void setSoundVolume(float value) {
		soundVolume = value;
		prefs.putFloat("common.sound", value);
	}
	
}
