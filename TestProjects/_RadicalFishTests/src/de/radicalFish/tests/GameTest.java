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
package de.radicalfish.tests;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.radicalfish.Game;
import de.radicalfish.GameContainer;
import de.radicalfish.GameInput;
import de.radicalfish.SpriteSheet;
import de.radicalfish.assets.Assets;
import de.radicalfish.assets.SpriteSheetLoader.SpriteSheetParameter;
import de.radicalfish.font.FontSheet;
import de.radicalfish.font.SimpleStyleParser;
import de.radicalfish.font.SpriteFont;
import de.radicalfish.font.StyleInfo;
import de.radicalfish.font.StyledLine;
import de.radicalfish.graphics.Graphics;
import de.radicalfish.tests.utils.RadicalFishTest;
import de.radicalfish.util.RadicalFishException;

/**
 * My testing ground.
 */
public class GameTest implements Game, RadicalFishTest {
	
	private TextureRegion sprite;
	private Texture part;
	
	private StyleInfo info;
	private FontSheet fontsheet;
	private SpriteFont font;
	
	private StyledLine line;
	
	private Assets assets;
	
	private String text = "[col:1,0,0,1]Te[scol:1,1,0,1]st";
	
	private final int[][] widths = new int[][] { { 3, 3, 5, 7, 5, 7, 7, 3, 4, 4, 5, 5, 4, 5, 3, 5 },
			{ 5, 3, 5, 5, 5, 5, 5, 5, 5, 5, 3, 4, 5, 5, 5, 5 }, { 7, 5, 5, 5, 5, 5, 5, 5, 5, 3, 5, 5, 5, 7, 6, 5 },
			{ 5, 5, 5, 5, 5, 5, 5, 7, 5, 5, 5, 4, 5, 4, 5, 5 } };
	
	// GAME METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void init(GameContainer container) throws RadicalFishException {
		assets = new Assets();
		Texture.setAssetManager(assets);
		part = new Texture(Gdx.files.internal("data/block.png"));
		sprite = new TextureRegion(part, 16, 16);
		sprite.flip(false, true);
		
		info = new StyleInfo();
		info.size.set(16, 16);
		info.size.mul(2);
		
		fontsheet = new FontSheet("data/font.png", widths, 11);
		font = new SpriteFont(fontsheet, -1, ' ');
		line = new StyledLine();
		
		SimpleStyleParser p = SimpleStyleParser.INSTANCE;
		text = p.parseLine(text, line);
		
		assets.setLogging(true);
		assets.load("data/block.png", Texture.class);
		assets.load("sp1", SpriteSheet.class, new SpriteSheetParameter("data/block.png", 16, 16));
		assets.load("sp2", SpriteSheet.class, new SpriteSheetParameter("data/block.png", 16, 16));
	}
	public void update(GameContainer container, float delta) throws RadicalFishException {
		assets.update();
		handleInput(container.getInput(), delta);
		
		line.update(container, delta);
	}
	public void render(GameContainer container, Graphics g) throws RadicalFishException {
		g.setClearColor(0.7f, 0.1f, 0.3f);
		
		SpriteBatch batch = g.getSpriteBatch();
		
		batch.begin();
		{
			batch.draw(sprite.getTexture(), info.createVertices(sprite, 200, 200), 0, 20);
			
			g.fillRect(100, 50, assets.getProgress() * 200, 20);
			g.drawRect(100, 50, 200, 20);
			
			
			font.draw(batch, text.toUpperCase(), 100, 100, container, line);
			
			
		}
		batch.end();
		
	}
	
	private void handleInput(GameInput input, float delta) {
		
	}
	
	// OTHER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void pause(GameContainer container) {}
	public void resume(GameContainer container) {}
	public void dispose() {}
	
	// TEST METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void initContainer(GameContainer container) {
		container.setSmoothDelta(true);
	}
	
	public String getTitle() {
		return "Simple Game Test";
	}
	public int getWidth() {
		return 800;
	}
	public int getHeight() {
		return 600;
	}
	public boolean needsGL20() {
		return true;
	}
	
}
