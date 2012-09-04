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
package de.radicalfish;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import de.radicalfish.util.RadicalFishException;
import de.radicalfish.util.Utils;

/**
 * A sprite sheet splits a {@link Texture} into tiled pieces and gives cached or not-cached {@link TextureRegion}s you
 * can access via an index.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 03.09.2012
 */
public class SpriteSheet implements Disposable {
	
	/** The base texture all sub regions share. */
	public final Texture base;
	private TextureRegion[][] regions;
	
	/** The width of one tile. */
	public final int tw;
	/** The height of one tile. */
	public final int th;
	/** Number of tiles across the sheet. */
	public final int tilesAcross;
	/** Number of tiles down the sheet. */
	public final int tilesDown;
	
	/** True if the {@link SpriteSheet} uses cached {@link TextureRegion}s. */
	public final boolean cached;
	
	// C'TOR
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Creates a new {@link SpriteSheet} from an internal {@link FileHandle}.
	 * 
	 * @param internalPath
	 *            the internal path
	 * @param tw
	 *            the width of a single tile
	 * @param th
	 *            the height of a single tile
	 * @throws RadicalFishException
	 */
	public SpriteSheet(String internalPath, int tw, int th) throws RadicalFishException {
		this(internalPath, tw, th, true);
	}
	/**
	 * Creates a new {@link SpriteSheet} from an internal {@link FileHandle}.
	 * 
	 * @param internalPath
	 *            the internal path
	 * @param tw
	 *            the width of a single tile
	 * @param th
	 *            the height of a single tile
	 * @param cached
	 *            true if we want to cache all sub-tiles into {@link TextureRegion}s
	 * @throws RadicalFishException
	 */
	public SpriteSheet(String internalPath, int tw, int th, boolean cached) throws RadicalFishException {
		this(Gdx.files.internal(internalPath), tw, th, cached);
	}
	/**
	 * Creates a new {@link SpriteSheet} from a {@link FileHandle}.
	 * 
	 * @param handle
	 *            the handle to load the base {@link Texture} from
	 * @param tw
	 *            the width of a single tile
	 * @param th
	 *            the height of a single tile
	 * @throws RadicalFishException
	 */
	public SpriteSheet(FileHandle handle, int tw, int th) throws RadicalFishException {
		this(handle, tw, th, true);
	}
	/**
	 * Creates a new {@link SpriteSheet} from a {@link FileHandle}.
	 * 
	 * @param handle
	 *            the handle to load the base {@link Texture} from
	 * @param tw
	 *            the width of a single tile
	 * @param th
	 *            the height of a single tile
	 * @param cached
	 *            true if we want to cache all sub-tiles into {@link TextureRegion}s
	 * @throws RadicalFishException
	 */
	public SpriteSheet(FileHandle handle, int tw, int th, boolean cached) throws RadicalFishException {
		this(new Texture(handle), tw, th, cached);
	}
	/**
	 * Creates a new {@link SpriteSheet} from a {@link Texture}.
	 * 
	 * @param texture
	 *            the texture to use
	 * @param tw
	 *            the width of a single tile
	 * @param th
	 *            the height of a single tile
	 * @throws RadicalFishException
	 */
	public SpriteSheet(Texture texture, int tw, int th) throws RadicalFishException {
		this(texture, tw, th, true);
	}
	/**
	 * Creates a new {@link SpriteSheet} from a {@link Texture}.
	 * 
	 * @param texture
	 *            the texture to use
	 * @param tw
	 *            the width of a single tile
	 * @param th
	 *            the height of a single tile
	 * @param cached
	 *            true if we want to cache all sub-tiles into {@link TextureRegion}s
	 * @throws RadicalFishException
	 */
	public SpriteSheet(Texture texture, int tw, int th, boolean chached) throws RadicalFishException {
		Utils.notNull("texture", texture);
		this.tw = tw;
		this.th = th;
		this.cached = chached;
		base = texture;
		tilesAcross = base.getWidth() / tw;
		tilesDown = base.getHeight() / th;
		init();
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @param index
	 *            the index of the tile
	 * @return a cached (if cached was turned on) or un-cached {@link TextureRegion} kept in this sheet.
	 */
	public TextureRegion getSubImage(int index) {
		return getSubImage(index % tilesAcross, index / tilesAcross);
	}
	/**
	 * @param x
	 *            the x index of the {@link TextureRegion}
	 * @param y
	 *            the y index of the {@link TextureRegion}
	 * @return a cached (if cached was turned on) or un-cached {@link TextureRegion} kept in this sheet.
	 */
	public TextureRegion getSubImage(int x, int y) {
		if (cached) {
			return regions[x][y];
		} else {
			TextureRegion reg = new TextureRegion(base, x * tw, y * th, tw, th);
			reg.flip(false, true);
			return reg;
		}
	}
	
	public void dispose() {
		base.dispose();
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Loads the cached sub images if enabled.
	 * 
	 * @throws RadicalFishException
	 */
	protected void init() throws RadicalFishException {
		if (tilesAcross < 1 || tilesDown < 1) {
			throw new RadicalFishException("Number of tiles is smaller then 1 (Wrong tiles width/height?)");
		}
		
		if (cached) {
			regions = new TextureRegion[tilesAcross][tilesDown];
			for (int i = 0; i < regions.length; i++) {
				for (int j = 0; j < regions[0].length; j++) {
					TextureRegion reg = new TextureRegion(base);
					reg.setRegion(i * tw, j * th, tw, th);
					reg.flip(false, true);
					regions[i][j] = reg;
				}
			}
		}
	}
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return the {@link Texture} used as base.
	 */
	public Texture getBaseTexture() {
		return base;
	}
	
	/**
	 * @return the width of the {@link SpriteSheet}.
	 */
	public int getWidth() {
		return base.getWidth();
	}
	/**
	 * @return the height of the {@link SpriteSheet}.
	 */
	public int getHeight() {
		return base.getHeight();
	}
	/**
	 * @return the width of a single tile.
	 */
	public int getTileWidth() {
		return tw;
	}
	/**
	 * @return the height of a single tile.
	 */
	public int getTileHeight() {
		return th;
	}
	/**
	 * @return the number of tiles across the complete {@link SpriteSheet}.
	 */
	public int getTilesAcross() {
		return tilesAcross;
	}
	/**
	 * @return the number of tiles down the complete {@link SpriteSheet}.
	 */
	public int getTilesDown() {
		return tilesDown;
	}
	
	/**
	 * @return true if this {@link SpriteSheet} uses cached {@link TextureRegion}s.
	 */
	public boolean isCached() {
		return cached;
	}
}
