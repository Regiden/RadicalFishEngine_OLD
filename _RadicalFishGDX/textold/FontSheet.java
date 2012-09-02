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
package de.radicalfish.text;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * A Sheet for the {@link SpriteFont} class. it takes different width per tile and one height value (text is usually
 * aligned at a fixed height).
 * 
 * @author Stefan Lange
 * @version 1.5.0
 * @since 18.10.2011
 */
public class FontSheet extends Image {
	
	// the array which contains the tiles width and height for the sheet.
	private int[][] tw;
	// the height of a tile;
	private int th;
	// contains every tile as subimage.
	private Image[][] subImages;
	
	private int maxTileWidth;
	
	/**
	 * Creates a new {@link FontSheet}. This is a
	 * 
	 * @param ref
	 *            path to the image file
	 * @param tilesWidth
	 *            the widths for each tile (tilesWidth[x] should give the row and tilesWidth[x][y] the column)
	 * @param tilesHeight
	 *            the heights for each tile (tilesHeight[x] should give row line and tilesHeight[x][y] the column)
	 * @throws SlickException
	 */
	public FontSheet(String ref, int[][] tilesWidth, int tilesHeight) throws SlickException {
		super(ref, false, FILTER_NEAREST);
		
		if (tilesWidth == null)
			throw new SlickException("tilesWidth array is null!");
		if (tilesHeight < 1)
			throw new SlickException("tilesHeight can't be zero or less");
		
		tw = tilesWidth;
		th = tilesHeight;
		
		calculateMaxSize();
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Render a sprite when this sprite sheet is in use.
	 * 
	 * @see #startUse()
	 * @see #endUse()
	 * 
	 * @param x
	 *            The x position to render the sprite at
	 * @param y
	 *            The y position to render the sprite at
	 * @param sx
	 *            The x location of the cell to render
	 * @param sy
	 *            The y location of the cell to render
	 */
	public void renderInUse(float x, float y, int sx, int sy) {
		subImages[sx][sy].drawEmbedded(x, y, tw[sy][sx], th);
	}
	/**
	 * Get a sprite at a particular cell on the sprite sheet.
	 * 
	 * @param x
	 *            The x position of the cell on the sprite sheet
	 * @param y
	 *            The y position of the cell on the sprite sheet
	 * @return The single image from the sprite sheet
	 */
	public Image getSprite(int x, int y) {
		init();
		initImpl();
		
		if ((x < 0) || (x >= subImages.length)) {
			throw new RuntimeException("SubImage out of sheet bounds: " + x + "," + y);
		}
		if ((y < 0) || (y >= subImages[0].length)) {
			throw new RuntimeException("SubImage out of sheet bounds: " + x + "," + y);
		}
		
		// count to he tile
		int wcount = 0, hcount = 0;
		for (int i = 0; i < x; i++)
			wcount += tw[y][i];
		hcount = y * th;
		
		return getSubImage(wcount, hcount, tw[y][x], th);
	}
	/**
	 * Returns a cached sub image from this sheet. This can be used to enhance cut down memory usage.
	 * 
	 * @param x
	 *            the x position of the cell on the sprite sheet
	 * @param y
	 *            the y position of the cell on the sprite sheet
	 * @return
	 */
	public Image getCachedSubImage(int x, int y) {
		init();
		initImpl();
		if ((x < 0) || (x >= subImages.length)) {
			throw new RuntimeException("SubImage out of sheet bounds: " + x + "," + y);
		}
		if ((y < 0) || (y >= subImages[0].length)) {
			throw new RuntimeException("SubImage out of sheet bounds: " + x + "," + y);
		}
		return subImages[x][y];
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void calculateMaxSize() {
		maxTileWidth = 0;
		for (int x = 0; x < getHorizontalCount(); x++) {
			for (int y = 0; y < getVerticalCount(); y++) {
				if (maxTileWidth < tw[y][x])
					maxTileWidth = tw[y][x];
			}
		}
	}
	
	// SUPER METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	protected void initImpl() {
		if (subImages != null) {
			return;
		}
		
		int tilesAcross = tw[0].length;
		int tilesDown = tw.length;
		subImages = new Image[tilesAcross][tilesDown];
		
		subImages = new Image[tilesAcross][tilesDown];
		for (int x = 0; x < tilesAcross; x++) {
			for (int y = 0; y < tilesDown; y++) {
				subImages[x][y] = getSprite(x, y);
			}
		}
	}
	
	// GETTER AND SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return the number of tiles across the sheet
	 */
	public int getHorizontalCount() {
		init();
		initImpl();
		return subImages.length;
	}
	/**
	 * @return the number of tiles down the sheet
	 */
	public int getVerticalCount() {
		init();
		initImpl();
		return subImages[0].length;
	}
	/**
	 * @return the value of all tiles with the greatest width
	 */
	public int getMaxTileWidth() {
		return maxTileWidth;
	}
	/**
	 * @return the value of all tile with the greatest height
	 */
	public int getMaxTileHeight() {
		return th;
	}
	/**
	 * @return the width of the tile at x, y
	 */
	public int getTileWidthAt(int x, int y) {
		return subImages[x][y].getWidth();
	}
	/**
	 * @return the height of the tile at x, y
	 */
	public int getTileHeightAt(int x, int y) {
		return th;
	}
}
