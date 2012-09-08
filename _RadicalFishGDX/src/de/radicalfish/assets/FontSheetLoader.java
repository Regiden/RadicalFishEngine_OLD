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
package de.radicalfish.assets;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import de.radicalfish.SpriteSheet;
import de.radicalfish.assets.FontSheetLoader.FontSheetParameter;
import de.radicalfish.font.FontSheet;

/**
 * A Loader for {@link SpriteSheet}s. Passing a Parameter specifies the width and height of a tile. Meaning you MUST
 * pass this information.
 * <p>
 * You can name the SpriteSheet like you want, the dependency will be loaded if needed. it must be set in the parameters.
 */
public class FontSheetLoader extends SynchronousAssetLoader<FontSheet, FontSheetParameter> {
	
	public FontSheetLoader(FileHandleResolver resolver) {
		super(resolver);
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	@Override
	public FontSheet load(AssetManager assetManager, String fileName, FontSheetParameter parameter) {
		Texture tex = assetManager.get(parameter.texpath, Texture.class);
		return new FontSheet(tex, parameter.tilesWidths, parameter.tilesHeight);
	}
	public Array<AssetDescriptor> getDependencies(String fileName, FontSheetParameter parameter) {
		if (parameter == null)
			throw new IllegalArgumentException("Missing FontSheetParameter: " + fileName);
		
		Array<AssetDescriptor> deps = new Array<AssetDescriptor>();
		deps.add(new AssetDescriptor(parameter.texpath, Texture.class));
		return deps;
	}
	
	public static class FontSheetParameter extends AssetLoaderParameters<FontSheet> {
		/** The path to the texture which backs the {@link SpriteSheet}. */
		public String texpath;
		/** the width of a single tile. */
		public int[][] tilesWidths;
		/** The height of a single tile. */
		public int tilesHeight;
		
		public FontSheetParameter(String texpath, int[][] tilesWidths, int tilesHeight) {
			this.texpath = texpath;
			this.tilesWidths = tilesWidths;
			this.tilesHeight = tilesHeight;
		}
		
	}
	
}