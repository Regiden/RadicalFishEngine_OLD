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
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import de.radicalfish.assets.TextureRegionLoader.TextureRegionLoaderParameter;

/**
 * A loader for texture regions. This can be used to cache regions you often use.
 * <p>
 * A parameter must be given to tell the loader which texture it should use as base (the loader will first try to get
 * the base texture from the already loaded assets. If it can not find the resource it will load it)
 * <p>
 * You can use {@link TextureRegionLoaderParameter#TextureRegionLoaderParameter(String, Rectangle)} to shorten the code
 * since the {@link Rectangle} values will be used to define the region.
 */
public class TextureRegionLoader extends SynchronousAssetLoader<TextureRegion, TextureRegionLoaderParameter> {
	
	public TextureRegionLoader(FileHandleResolver resolver) {
		super(resolver);
	}
	
	public TextureRegion load(AssetManager assetManager, String fileName, TextureRegionLoaderParameter p) {
		Texture tex = assetManager.get(p.textureName, Texture.class);
		return new TextureRegion(tex, p.x, p.y, p.width, p.height);
	}
	public Array<AssetDescriptor> getDependencies(String fileName, TextureRegionLoaderParameter parameter) {
		if (parameter == null)
			throw new IllegalArgumentException("Missing FontSheetParameter: " + fileName);
		
		if(parameter.baseLoaded) {
			return null;
		}
		
		Array<AssetDescriptor> deps = new Array<AssetDescriptor>();
		deps.add(new AssetDescriptor(parameter.textureName, Texture.class));
		
		return deps;
	}
	
	public static class TextureRegionLoaderParameter extends AssetLoaderParameters<TextureRegion> {
		
		/** The name or path to the base texture. */
		public String textureName;
		
		/** The y start position of the region in texture bounds. */
		public int x;
		/** The x start position of the region in texture bounds. */
		public int y;
		/** The width of the region. */
		public int width;
		/** The height of the region. */
		public int height;
		
		/**
		 * Can be set to avoid the prints of "dependency already loaded. setting this will assume that the base texture
		 * is already loaded.
		 */
		public boolean baseLoaded = false;
		
		public TextureRegionLoaderParameter(String textureName, int x, int y, int width, int height) {
			this.textureName = textureName;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
		/**
		 * Float values form the <code>rect</code> will be casted to int.
		 */
		public TextureRegionLoaderParameter(String textureName, Rectangle rect) {
			this.textureName = textureName;
			x = (int) rect.x;
			y = (int) rect.y;
			width = (int) rect.width;
			height = (int) rect.height;
		}
		
	}
}
