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
package de.radicalfish.state;
import java.util.Iterator;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.IntMap;
import de.radicalfish.Game;
import de.radicalfish.GameContainer;
import de.radicalfish.GameInput;
import de.radicalfish.context.GameContext;
import de.radicalfish.context.GameDelta;
import de.radicalfish.context.GameVariables;
import de.radicalfish.context.Resources;
import de.radicalfish.context.Settings;
import de.radicalfish.context.defaults.DefaultGameDelta;
import de.radicalfish.context.defaults.DefaultGameVariables;
import de.radicalfish.context.defaults.DefaultSettings;
import de.radicalfish.graphics.Graphics;
import de.radicalfish.state.transitions.EmptyTransition;
import de.radicalfish.state.transitions.Transition;
import de.radicalfish.text.FontRenderer;
import de.radicalfish.util.RadicalFishException;
import de.radicalfish.util.Utils;
import de.radicalfish.world.World;

/**
 * A game which handles a set of {@link GameState}s. The class always updates and renders one state but exposes access
 * to all states via setter/getter.
 * 
 * @author Stefan Lange
 * @version 0.5.0
 * @since 17.08.2012
 */
public abstract class StateBasedGame implements Game, InputProcessor {
	
	private IntMap<GameState> states = new IntMap<GameState>();
	
	private GameState currentState, nextState, previousState;
	private Transition enterTransition, leaveTransition;
	private GameContext context;
	private World world;
	
	private boolean pauseUpdate, pauseRender;
	private boolean usingDefaultContext = false;
	
	public StateBasedGame() {
		// TODO Auto-generated constructor stub
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Adds a state to the state list. if the current state is null the added state will be set as teh current state.
	 */
	public void addState(GameState state) {
		Utils.notNull("state", state);
		
		states.put(state.getID(), state);
		if (currentState == null) {
			currentState = state;
		}
	}
	/**
	 * Enters the state with the specific <code>ID</code>. if the states does not exits and exception will be thrown.
	 * @throws RadicalFishException 
	 */
	public void enterState(int ID) throws RadicalFishException {
		enterState(ID, null, null);
	}
	/**
	 * Enters the state with the specific <code>ID</code>. if the states does not exits and exception will be thrown. 
	 * 
	 * @param leave the "Out"-Transition to use (can be null);
	 * @param enter the "In"-Transition to use (can be null)
	 * 
	 * @throws RadicalFishException 
	 */
	public void enterState(int ID, Transition leave, Transition enter ) throws RadicalFishException {
		if(!states.containsKey(ID)) {
			throw new RadicalFishException("No GameState is registered with the ID: " + ID);
		}
		
		if (leave == null) {
			leave = new EmptyTransition();
		}
		if (enter == null) {
			enter = new EmptyTransition();
		}
		leaveTransition = leave;
		enterTransition = enter;
		
		nextState = getState(ID);
		leaveTransition.init(context, currentState, nextState);
		currentState.leaving(context, world, nextState);
	}
	
	// ABSTRACT METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Gets called to initialize the {@link GameContext}. if null is returned. A default implementation will be created.
	 * <p>
	 * The default context will return null on the current getters:
	 * <li> {@link GameContext#getResources()}</li>
	 * <li> {@link GameContext#getPostProcesser()}</li>
	 * <li> {@link GameContext#getFontRenderer()}</li>
	 * <hr>
	 * 
	 * @return an instance of the {@link GameContext} Interface.
	 */
	public abstract GameContext initGameContext(GameContainer container) throws RadicalFishException;
	/**
	 * Gets called to initialize the {@link World}. You may return null. The class will not create a default
	 * implementation (would be hard to guess how you want a world, also not all games need a world).
	 * 
	 * @return an instance of the {@link World} Interface (if wanted).
	 */
	public abstract World initWorld(GameContainer container) throws RadicalFishException;
	/**
	 * Gets called after {@link StateBasedGame#initGameContext(GameContainer)} and
	 * {@link StateBasedGame#initWorld(GameContainer)} to add all states.
	 * <p>
	 * After this call all states get a call to the {@link GameState#init(GameContext, World)} method.
	 */
	public abstract void initStates(GameContext context) throws RadicalFishException;
	
	/**
	 * Gets called before the update call to the current state.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param world
	 *            the world the game plays in
	 * @param delta
	 *            the {@link GameDelta} object containing the delta values.
	 * @throws RadicalFishException
	 */
	public abstract void preUpdate(GameContext context, World world, GameDelta delta) throws RadicalFishException;
	/**
	 * Gets called after the update call to the current state.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param world
	 *            the world the game plays in
	 * @param delta
	 *            the {@link GameDelta} object containing the delta values.
	 * @throws RadicalFishException
	 */
	public abstract void postUpdate(GameContext context, World world, GameDelta delta) throws RadicalFishException;
	
	/**
	 * Gets called before the render call to the current state.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param world
	 *            the world the game plays in
	 * @param g
	 *            the wrapper for graphics
	 */
	public abstract void preRender(GameContext context, World world, Graphics g) throws RadicalFishException;
	/**
	 * Gets called after the render call to the current state.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param world
	 *            the world the game plays in
	 * @param g
	 *            the wrapper for graphics
	 */
	public abstract void postRender(GameContext context, World world, Graphics g) throws RadicalFishException;
	
	// GAME METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public final void init(GameContainer container) throws RadicalFishException {
		context = initGameContext(container);
		world = initWorld(container);
		if (context == null) {
			context = new DefaultGameContext(container, this);
			usingDefaultContext = true;
		}
		
		initStates(context);
		
		Iterator<GameState> ite = states.values();
		while (ite.hasNext()) {
			ite.next().init(context, world);
		}
		
		if (currentState != null) {
			currentState.entered(context, world, null);
		}
	}
	public final void update(GameContainer container, float delta) throws RadicalFishException {
		if(usingDefaultContext) {
			context.getGameDelta().update(context, world, delta);
		}
		preUpdate(context, world, context.getGameDelta());
		
		if (leaveTransition != null) {
			leaveTransition.update(context, context.getGameDelta());
			if (leaveTransition.isFinished()) {
				currentState.left(context, world, nextState);
				previousState = currentState;
				currentState = nextState;
				nextState = null;
				leaveTransition = null;
				currentState.entering(context, world, previousState);
				if (enterTransition != null) {
					enterTransition.init(context, previousState, currentState);
				}
			} else {
				postUpdate(context, world, context.getGameDelta());
				return;
			}
		}
		
		if (enterTransition != null) {
			enterTransition.update(context, context.getGameDelta());
			if (enterTransition.isFinished()) {
				currentState.entered(context, world, previousState);
				previousState = null;
				enterTransition = null;
			} else {
				postUpdate(context, world, context.getGameDelta());
				return;
			}
		}
		
		if(!pauseUpdate) {
			currentState.update(context, world, context.getGameDelta());
		}
		
		postUpdate(context, world, context.getGameDelta());
	}
	
	public final void render(GameContainer container, Graphics g) throws RadicalFishException {
		
	}
	
	// INPUT METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public boolean keyDown(int keycode) {
		if (canForwardInput()) {
			return currentState.keyDown(keycode);
		}
		return false;
	}
	public boolean keyUp(int keycode) {
		if (canForwardInput()) {
			return currentState.keyUp(keycode);
		}
		return false;
	}
	public boolean keyTyped(char character) {
		if (canForwardInput()) {
			return currentState.keyTyped(character);
		}
		return false;
	}
	public boolean touchDown(int x, int y, int pointer, int button) {
		if (canForwardInput()) {
			return currentState.touchDown(x, y, pointer, button);
		}
		return false;
	}
	public boolean touchUp(int x, int y, int pointer, int button) {
		if (canForwardInput()) {
			return currentState.touchUp(x, y, pointer, button);
		}
		return false;
	}
	public boolean touchDragged(int x, int y, int pointer) {
		if (canForwardInput()) {
			return currentState.touchDragged(x, y, pointer);
		}
		return false;
	}
	public boolean touchMoved(int x, int y) {
		if (canForwardInput()) {
			return currentState.touchMoved(x, y);
		}
		return false;
	}
	public boolean scrolled(int amount) {
		if (canForwardInput()) {
			return currentState.scrolled(amount);
		}
		return false;
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private boolean canForwardInput() {
		return (leaveTransition != null) && (enterTransition != null) && !isUpdatePaused() && (currentState != null);
	}
	
	// SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * True if the update calls to the current state should be paused. (the pre and post update methods still get a call
	 * each frame).
	 */
	public void setPauseUpdate(boolean pauseUpdate) {
		this.pauseUpdate = pauseUpdate;
	}
	/**
	 * True if the render calls to the current state should be paused. (the pre and post render methods still get a call
	 * each frame).
	 */
	public void setPauseRender(boolean pauseRender) {
		this.pauseRender = pauseRender;
	}
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return the current state.
	 */
	public GameState getCurrentState() {
		return currentState;
	}
	public GameState getState(int ID) {
		return states.get(ID);
	}
	
	/**
	 * @return true if update is paused. (the pre and post update methods still get a call each frame).
	 */
	public boolean isUpdatePaused() {
		return pauseUpdate;
	}
	/**
	 * @return true if render is paused. (the pre and post render methods still get a call each frame).
	 */
	public boolean isRenderPauser() {
		return pauseRender;
	}
	
	// INTERN CLASSES
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private static class DefaultGameContext implements GameContext {
		private GameContainer container;
		private StateBasedGame game;
		private GameDelta delta;
		private Settings settings;
		private GameVariables varis;
		
		public DefaultGameContext(GameContainer container, StateBasedGame game) {
			this.container = container;
			this.game = game;
			
			delta = new DefaultGameDelta();
			settings = new DefaultSettings();
			varis = new DefaultGameVariables();
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
			return container.getWidth();
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
		public Settings getSettings() {
			return settings;
		}
		public GameDelta getGameDelta() {
			return delta;
		}
		public GameVariables getGameVariables() {
			return varis;
		}
		public FontRenderer getFontRenderer() {
			return null;
		}
		public BitmapFont getDefaultFont() {
			return container.getFont();
		}
		public Resources getResources() {
			return null;
		}
		
	}
}
