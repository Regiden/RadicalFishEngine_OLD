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
package de.radicalfish.font;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import de.radicalfish.SpriteSheet;
import de.radicalfish.util.RadicalFishException;
import de.radicalfish.util.Utils;

/**
 * A special kind of sheet which takes an 2D array for the widths of each tile and one height value for a tile (fonts a
 * usually aligned at a base line). It will cache all sub images automatically. This can be used if you have a very
 * small bitmap font which you made yourself.
 * <p>
 * By default the {@link TextureRegion} will be loaded with y down. You can change this via
 * {@link FontSheet#flip(boolean, boolean)}.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 04.09.2012
 */
public class FontSheet implements Disposable {
	
	/** The base texture all the sub regions share */
	public final Texture base;
	private TextureRegion[][] regions;
	
	/** The widths for each tile across the sheet. */
	public final int[][] tw;
	/** The height of a tile in the sheet. */
	public final int th;
	/** Number of tiles across the sheet. */
	public final int tilesAcross;
	/** Number of tiles down the sheet. */
	public final int tilesDown;
	
	/** The max width of a tile. */
	public final int maxTileWidth;
	
	// C'TOR
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Creates a new {@link FontSheet} from an internal {@link FileHandle}.
	 * 
	 * @param internalPath
	 *            the internal path
	 * @param tw
	 *            the widths for each tile the first index (eg. tw[i]) is the vertical index and the second index (eg.
	 *            tw[i][tilewidth]). So all widths are in a horizontal line.
	 * @param th
	 *            the height of a tile
	 */
	public FontSheet(String internalPath, int[][] tw, int th) {
		this(Gdx.files.internal(internalPath), tw, th);
	}
	/**
	 * Creates a new {@link FontSheet} from a {@link FileHandle}.
	 * 
	 * @param handle
	 *            the handle to load from.
	 * @param tw
	 *            the widths for each tile
	 * @param th
	 *            the height of a tile
	 */
	public FontSheet(FileHandle handle, int[][] tw, int th) {
		this(new Texture(handle), tw, th);
	}
	/**
	 * Creates a new {@link FontSheet} from a texture.
	 * 
	 * @param base
	 *            the base texture to use
	 * @param tw
	 *            the widths for each tile
	 * @param th
	 *            the height of a tile
	 */
	public FontSheet(Texture base, int[][] tw, int th) {
		Utils.notNull("texture", base);
		Utils.notNull("tw", tw);
		
		this.tw = tw;
		this.th = th;
		this.base = base;
		tilesAcross = tw[0].length;
		tilesDown = tw.length;
		
		init();
		
		int localMax = 0;
		for (int x = 0; x < tilesAcross; x++) {
			for (int y = 0; y < tilesDown; y++) {
				if (localMax < tw[y][x])
					localMax = tw[y][x];
			}
		}
		maxTileWidth = localMax;
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Flips each {@link TextureRegion} on the x-axis or y-axis.
	 * 
	 * @param xflip
	 *            true if the x-axis should be flipped
	 * @param yflip
	 *            true if the y-axis should be flipped
	 */
	public void flip(boolean xflip, boolean yflip) {
		for (int y = 0; y < tilesDown; y++) {
			for (int x = 0; x < tilesAcross; x++) {
				regions[x][y].flip(xflip, yflip);
			}
		}
	}
	
	/**
	 * @param index
	 *            the index of the tile
	 * @return a cached {@link TextureRegion} kept in this sheet.
	 */
	public TextureRegion getSubImage(int index) {
		return getSubImage(index & tilesAcross, index / tilesDown);
	}
	/**
	 * @param x
	 *            the x index of the {@link TextureRegion}
	 * @param y
	 *            the y index of the {@link TextureRegion}
	 * @return a cached {@link TextureRegion} kept in this sheet.
	 */
	public TextureRegion getSubImage(int x, int y) {
		return regions[x][y];
	}
	
	public void dispose() {
		base.dispose();
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void init() {
		if (tilesAcross < 1 || tilesDown < 1) {
			throw new RadicalFishException("Number of tiles is smaller then 1 (Wrong tiles width/height?)");
		}
		int wcount = 0, hcount = 0;
		regions = new TextureRegion[tilesAcross][tilesDown];
		for (int y = 0; y < tilesDown; y++) {
			for (int x = 0; x < tilesAcross; x++) {
				wcount = hcount = 0;
				for (int i = 0; i < x; i++) {
					wcount += tw[y][i];
				}
				hcount = y * th;
				TextureRegion reg = new TextureRegion(base, wcount, hcount, tw[y][x], th);
				reg.flip(false, true);
				regions[x][y] = reg;
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
	 * @return the maximum width a tile has in this {@link FontSheet}.
	 */
	public int getMaxTileWidth() {
		return maxTileWidth;
	}
	/**
	 * @return the width of a single tile at the given <code>x</code>, <code>y</code>.
	 */
	public int getTileWidthAt(int x, int y) {
		return regions[x][y].getRegionWidth();
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
}
