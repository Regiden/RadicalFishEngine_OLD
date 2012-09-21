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
import com.badlogic.gdx.utils.Array;
import de.radicalfish.assets.FontSheetLoader.FontSheetParameter;
import de.radicalfish.assets.SpriteFontLoader.SpriteFontParameter;
import de.radicalfish.font.FontSheet;
import de.radicalfish.font.SpriteFont;

/**
 * A loader for {@link SpriteFont}s. A parameter must be given with the name of the {@link FontSheet} mapped in the
 * {@link Assets}.
 * <p>
 * You can name the SpriteFont like you want, the dependency will be loaded if needed. it must be set in the parameters.
 * You can set a {@link FontSheetParameter} if the {@link FontSheet} was not loaded. With this the FontSheet will be
 * loaded as dependency. This FontSheet will be loaded with the name of the {@link SpriteFont} + "_fs".
 */
public class SpriteFontLoader extends SynchronousAssetLoader<SpriteFont, SpriteFontParameter> {
	
	private String fontSheetName;
	
	public SpriteFontLoader(FileHandleResolver resolver) {
		super(resolver);
	}
	
	public SpriteFont load(AssetManager assetManager, String fileName, SpriteFontParameter parameter) {
		FontSheet sheet = assetManager.get(fontSheetName, FontSheet.class);
		return new SpriteFont(sheet, parameter.characterSpace, parameter.lineSpace, parameter.startChar);
	}
	public Array<AssetDescriptor> getDependencies(String fileName, SpriteFontParameter parameter) {
		if (parameter == null)
			throw new IllegalArgumentException("Missing SpriteFontParameter: " + fileName);
		
		if (parameter.fontSheetParameter == null && parameter.fontsheetasset == null) {
			throw new IllegalArgumentException("Neither a fontsheetasset path nor a fontsheetparameter was given!");
		}
		
		Array<AssetDescriptor> deps = new Array<AssetDescriptor>();
		
		if (parameter.fontsheetasset != null) {
			fontSheetName = parameter.fontsheetasset;
			deps.add(new AssetDescriptor(fontSheetName, FontSheet.class));
		} else if (parameter.fontSheetParameter != null){
			fontSheetName = fileName + "_fs";
			deps.add(new AssetDescriptor(fontSheetName, FontSheet.class, parameter.fontSheetParameter));
		} 
		
		return deps;
	}
	
	public static class SpriteFontParameter extends AssetLoaderParameters<SpriteFont> {
		/** Can be set in case the font sheet is not in the {@link Assets} class and should be loaded with the {@link SpriteFont}. */
		public FontSheetParameter fontSheetParameter;
		
		/** The name of the fontsheet held in {@link Assets} class. */
		public String fontsheetasset;
		
		/** The space between two characters. */
		public int characterSpace;
		/** The space between two lines. */
		public int lineSpace;
		/** the starting character. */
		public char startChar;
		
		public SpriteFontParameter(FontSheetParameter fontsheet, int charakterSpace, int lineSpace, char startChar) {
			this.fontSheetParameter = fontsheet;
			this.characterSpace = charakterSpace;
			this.lineSpace = lineSpace;
			this.startChar = startChar;
		}
		public SpriteFontParameter(String fontsheetasset, int charakterSpace, int lineSpace, char startChar) {
			this.fontsheetasset = fontsheetasset;
			this.characterSpace = charakterSpace;
			this.lineSpace = lineSpace;
			this.startChar = startChar;
		}
		
	}
}
