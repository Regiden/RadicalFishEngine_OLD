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
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.radicalfish.GameContainer;
import de.radicalfish.GameInput;
import de.radicalfish.assets.Assets;
import de.radicalfish.assets.FontSheetLoader.FontSheetParameter;
import de.radicalfish.assets.SpriteFontLoader.SpriteFontParameter;
import de.radicalfish.context.GameWithContext;
import de.radicalfish.context.GameContext;
import de.radicalfish.context.defaults.DefaultGameContext;
import de.radicalfish.font.SpriteFont;
import de.radicalfish.font.StyleInfo;
import de.radicalfish.font.StyleParser;
import de.radicalfish.font.StyledText;
import de.radicalfish.graphics.Graphics;
import de.radicalfish.tests.utils.RadicalFishTest;
import de.radicalfish.util.RadicalFishException;
import de.radicalfish.world.GameWorld;

/**
 * My testing ground.
 */
public class GameTest implements GameWithContext, InputProcessor, RadicalFishTest {
	
	private TextureRegion sprite;
	private Texture part;
	
	private StyleInfo info;
	private SpriteFont font;
	
	private StyledText texCom;
	
	private GameContext context;
	
	private String text = "[rp:(sc:1,0,0,1),4][gp:(fd:1.0,out), 0.2, 4][gp:(sm:0,10,1), 0.2, 4]FADE[x:all] [co:1,0,0,1]Te[sc:1,1,0,1]st [x:color]Bla[co:1,0,1,1]lalalala";
	
	private final int[][] widths = new int[][] { { 3, 3, 5, 7, 5, 7, 7, 3, 4, 4, 5, 5, 4, 5, 3, 5 },
			{ 5, 3, 5, 5, 5, 5, 5, 5, 5, 5, 3, 4, 5, 5, 5, 5 }, { 7, 5, 5, 5, 5, 5, 5, 5, 5, 3, 5, 5, 5, 7, 6, 5 },
			{ 5, 5, 5, 5, 5, 5, 5, 7, 5, 5, 5, 4, 5, 4, 5, 5 } };
	
	// GAME METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void init(GameContainer container) throws RadicalFishException {
		context = new DefaultGameContext(container, null);
		
		Texture.setAssetManager(context.getAssets());
		Assets assets = context.getAssets();
		assets.setLogging(true);
		assets.load("data/block.png", Texture.class);
//		assets.load("sp1", SpriteSheet.class, new SpriteSheetParameter("data/block.png", 16, 16));
//		assets.load("sp2", SpriteSheet.class, new SpriteSheetParameter("data/block.png", 16, 16));
		
		FontSheetParameter fsp = new FontSheetParameter("data/font.png", widths, 11);
		assets.load("spfont", SpriteFont.class, new SpriteFontParameter(fsp, -1, 0, ' '));
		
		assets.finishLoading();
		
		part = assets.get("data/block.png");
		sprite = new TextureRegion(part, 16, 16);
		sprite.flip(false, true);
		
		info = new StyleInfo();
		info.size.set(16, 16);
		info.size.mul(2);
		
		font = assets.get("spfont", SpriteFont.class);
		texCom = new StyledText();
		
		StyleParser p = StyleParser.INSTANCE;
		text = p.parseMultiLine(text, texCom);
		
	}
	public void update(GameContainer container, float delta) throws RadicalFishException {
		context.getAssets().update();
		handleInput(container.getInput(), delta);
		
		texCom.update(container, delta);
	}
	public void render(GameContainer container, Graphics g) throws RadicalFishException {
		g.setClearColor(0.7f, 0.1f, 0.3f);
		
		SpriteBatch batch = g.getSpriteBatch();
		
		batch.begin();
		{
			batch.draw(sprite.getTexture(), info.createVertices(sprite, 200, 200), 0, 20);
			
			g.fillRect(100, 50, context.getAssets().getProgress() * 200, 20);
			g.drawRect(100, 50, 200, 20);
			
			font.draw(batch, text.toUpperCase(), 100, 100, container, texCom);
			
		}
		batch.end();
		
	}
	
	private void handleInput(GameInput input, float delta) {
		if (input.isKeyPressed(Keys.R)) {
			texCom.reset();
		}
	}
	
	private void unload() {
		context.getAssets().unload("data/font.png");
	}
	private void reload() {
		context.getAssets().load("data/font.png", Texture.class);
	}
	
	
	// OTHER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public boolean keyDown(int keycode) {
		if(keycode == Keys.U) {
			unload();
		}
		if(keycode == Keys.I) {
			reload();
		}
		return false;
	}
	public boolean keyTyped(char character) {
		return false;
	}
	public boolean keyUp(int keycode) {
		return false;
	}
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}
	public boolean scrolled(int amount) {
		return false;
	}
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}
	
	public void pause(GameContainer container) {}
	public void resume(GameContainer container) {}
	public void dispose() {}
	
	// TEST METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void initContainer(GameContainer container) {
		container.setSmoothDeltas(true);
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
	
	public GameContext getGameContext() {
		return context;
	}
	public GameWorld getWorld() {
		return null;
	}
	
}
