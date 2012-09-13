/*
 * Copyright (c) 2012, Stefan Lange
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
package de.radicalfish.assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import de.radicalfish.SpriteSheet;
import de.radicalfish.assets.FontSheetLoader.FontSheetParameter;
import de.radicalfish.assets.SpriteSheetLoader.SpriteSheetParameter;
import de.radicalfish.font.FontSheet;
import de.radicalfish.font.SpriteFont;

/**
 * Class for loading assets. This is basically the {@link AssetManager} with added {@link AssetLoader}.
 * <hr>
 * List of extra class loaders:
 * <li>{@link SpriteSheetLoader} : needs a {@link SpriteSheetParameter} when loading.</li>
 * <li>{@link FontSheetLoader} : needs a {@link FontSheetParameter} when loading.</li>
 * <hr>
 * For these loader the fileName can be anything. The important part are the parameters, where you specify the
 * dependencies.
 * <p>
 * Additionally there are methods to create unmanaged {@link Music} or {@link Sound} instances and switch logging off or
 * on.
 * 
 * @author Stefan Lange
 * @version 0.2.0
 * @since 08.09.2012
 */
public class Assets extends AssetManager {
	
	private final Array<Preferences> preferences = new Array<Preferences>();
	
	private boolean done;
	
	public Assets() {
		this(new InternalFileHandleResolver());
	}
	public Assets(FileHandleResolver resolver) {
		super(resolver);
		setLoader(SpriteSheet.class, new SpriteSheetLoader(resolver));
		setLoader(FontSheet.class, new FontSheetLoader(resolver));
		setLoader(SpriteFont.class, new SpriteFontLoader(resolver));
		setLoader(ShaderProgram.class, new ShaderLoader(resolver));
		done = false;
	}
	
	// OVERRIDE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public synchronized boolean update() {
		boolean done = false;
		if (super.update()) {
			done = true;
		}
		if (done) {
			this.done = done;
		}
		return done;
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * 
	 * @return a managed {@link Preferences} implementation. It can be used to store application settings across runs.
	 *         The returned instance
	 */
	public Preferences createPreferences(String name) {
		Preferences pref = Gdx.app.getPreferences(name);
		preferences.add(pref);
		return pref;
	}
	
	/**
	 * @return a new unmanaged {@link Sound} object.
	 */
	public Sound createSound(String path) {
		return createSound(Gdx.files.internal(path));
	}
	/**
	 * @return a new unmanaged {@link Sound} object.
	 */
	public Sound createSound(FileHandle handle) {
		return Gdx.audio.newSound(handle);
	}
	/**
	 * @return a new unmanaged {@link Music} object.
	 */
	public Music createMusic(String path) {
		return createMusic(Gdx.files.internal(path));
	}
	/**
	 * @return a new unmanaged {@link Music} object.
	 */
	public Music createMusic(FileHandle handle) {
		return Gdx.audio.newMusic(handle);
	}
	
	// SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * True if we want logging or not.
	 */
	public void setLogging(boolean logging) {
		if (!logging) {
			getLogger().setLevel(Logger.NONE);
		} else {
			getLogger().setLevel(Logger.DEBUG);
		}
	}
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return the number of preferences managed by this class.
	 */
	public int getNumberOfPreferences() {
		return preferences.size;
	}
	
	/**
	 * @return true if the loading is done. This will be set to true if the {@link AssetManager#update()} returns true
	 *         (if you unload content you must call to update the done flag).
	 */
	public boolean isDone() {
		return done;
	}
}
