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
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import de.radicalfish.assets.ShaderLoader.ShaderLoaderParameter;
import de.radicalfish.util.RadicalFishException;

/**
 * This loader loads a Shader (duh). The content of the file will be loaded asybc while the shader itself will be loaded
 * in sync on the GL-Thread.
 * <p>
 * Like in the other loaders provided by the engine the file name can be any name. The important part are the paths set
 * in the {@link ShaderLoaderParameter}. A parameter must be given otherwise the loader will throw an exception.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 12.09.2012
 */
public class ShaderLoader extends AsynchronousAssetLoader<ShaderProgram, ShaderLoaderParameter> {
	
	private String vertex, fragment;
	
	public ShaderLoader(FileHandleResolver resolver) {
		super(resolver);
	}
	
	public void loadAsync(AssetManager manager, String fileName, ShaderLoaderParameter parameter) {
		FileHandle v = resolve(parameter.vertexpath);
		vertex = v.readString();
		FileHandle f = resolve(parameter.fragmentpath);
		fragment = f.readString();
	}
	public ShaderProgram loadSync(AssetManager manager, String fileName, ShaderLoaderParameter parameter) {
		if (vertex != null && !vertex.isEmpty() && fragment != null && !fragment.isEmpty()) {
			boolean old = ShaderProgram.pedantic;
			ShaderProgram.pedantic = false;
			
			ShaderProgram prog = new ShaderProgram(vertex, fragment);
			
			if (!prog.isCompiled()) {
				throw new RadicalFishException("Compile Error in Shader: " + prog.getLog());
			}
			
			ShaderProgram.pedantic = old;
			
			return prog;
		} else {
			throw new RadicalFishException("Could not load shader because content of file is invalid");
		}
	}
	
	public Array<AssetDescriptor> getDependencies(String fileName, ShaderLoaderParameter parameter) {
		if (parameter == null)
			throw new IllegalArgumentException("Missing SpriteFontParameter: " + fileName);
		return null;
	}
	
	public static class ShaderLoaderParameter extends AssetLoaderParameters<ShaderProgram> {
		/** The path to the vertex file. */
		public String vertexpath;
		/** The path of the fragment file. */
		public String fragmentpath;
		
		public ShaderLoaderParameter(String vertexpath, String fragmentpath) {
			this.vertexpath = vertexpath;
			this.fragmentpath = fragmentpath;
		}
		
	}
}
