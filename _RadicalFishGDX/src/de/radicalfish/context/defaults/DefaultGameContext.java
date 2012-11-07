package de.radicalfish.context.defaults;

import de.radicalfish.GameContainer;
import de.radicalfish.GameInput;
import de.radicalfish.assets.Assets;
import de.radicalfish.context.GameContext;
import de.radicalfish.context.GameDelta;
import de.radicalfish.context.GameVariables;
import de.radicalfish.context.Settings;
import de.radicalfish.font.Font;
import de.radicalfish.graphics.Graphics;
import de.radicalfish.state.StateBasedGame;

/**
 * A default {@link GameContext} used by {@link StateBasedGame}.
 */
public class DefaultGameContext implements GameContext {
	private GameContainer container;
	private StateBasedGame game;
	private GameDelta delta;
	private Settings settings;
	private GameVariables varis;
	private Assets assets;
	
	public DefaultGameContext(GameContainer container, StateBasedGame game) {
		this.container = container;
		this.game = game;
		
		delta = new DefaultGameDelta();
		varis = new DefaultGameVariables();
		assets = new Assets();
		settings = new DefaultSettings(assets.createPreferences("defaults-rfe.xml"));
	}
	
	// INTERFACE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public float getGameScale() {
		return 1;
	}
	public int getGameWidth() {
		return container.getWidth();
	}
	public int getGameHeight() {
		return container.getHeight();
	}
	public int getContainerWidth() {
		return container.getWidth();
	}
	public int getContainerHeight() {
		return container.getHeight();
	}
	public StateBasedGame getGame() {
		return game;
	}
	public GameContainer getContainer() {
		return container;
	}
	public GameInput getInput() {
		return container.getInput();
	}
	public Graphics getGraphics() {
		return container.getGraphics();
	}
	public Settings getSettings() {
		return settings;
	}
	public GameDelta getGameDelta() {
		return delta;
	}
	public GameVariables getGameVariables() {
		return varis;
	}
	public Font getFont() {
		return container.getFont();
	}
	public Assets getAssets() {
		return assets;
	}
	
	public void dispose() {
		assets.dispose();
	}
	
}