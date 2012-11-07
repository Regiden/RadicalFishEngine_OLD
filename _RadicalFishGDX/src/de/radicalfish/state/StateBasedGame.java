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
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.IntMap;
import de.radicalfish.GameContainer;
import de.radicalfish.context.GameWithContext;
import de.radicalfish.context.GameContext;
import de.radicalfish.context.GameDelta;
import de.radicalfish.context.defaults.DefaultGameContext;
import de.radicalfish.graphics.Graphics;
import de.radicalfish.state.transitions.EmptyTransition;
import de.radicalfish.state.transitions.Transition;
import de.radicalfish.util.RadicalFishException;
import de.radicalfish.util.Utils;
import de.radicalfish.world.GameWorld;

/**
 * A game which handles a set of {@link GameState}s. The class always updates and renders one state but exposes access
 * to all states via setter/getter.
 * <p>
 * You use {@link Transition}s to switch from one state to another with a nice animation. The delta value of the update
 * methods in the {@link Transition} class will be the modified delta form the {@link GameDelta} object.
 * <p>
 * For the {@link StateBasedGame} a {@link GameContext} will be needed. If you don't want to create one then just return
 * null for the method {@link StateBasedGame#initGameContext(GameContainer)}.
 * <p>
 * Also a {@link GameWorld} object will be used. You don't need one and leave the reference null if you like. You can
 * use the protected method {@link StateBasedGame#initWorld(GameContainer)} to create an return your own
 * {@link GameWorld} instance.
 * <p>
 * Use the {@link StateBasedGame#preUpdate(GameContext, GameWorld, GameDelta)},
 * {@link StateBasedGame#preRender(GameContext, GameWorld, Graphics)},
 * {@link StateBasedGame#postUpdate(GameContext, GameWorld, GameDelta)} or
 * {@link StateBasedGame#postRender(GameContext, GameWorld, Graphics)} method to hook in before or after the updates on
 * the current {@link GameState}.
 * <p>
 * {@link StateBasedGame} is a {@link GameWithContext} which means you a getter for the {@link GameContext} and the
 * {@link GameWorld}.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 17.08.2012
 */
public abstract class StateBasedGame implements GameWithContext, InputProcessor {
	
	/** A list of all the states. */
	protected IntMap<GameState> states = new IntMap<GameState>();
	
	/** The current {@link GameState} we update/render. */
	protected GameState currentState;
	/** The {@link GameContext} we use. */
	protected GameContext context;
	/** The {@link GameWorld} we use. */
	protected GameWorld world;
	
	private GameContainer container;
	private GameState nextState, previousState;
	private Transition enterTransition, leaveTransition;
	
	/** True if we want to pause updating the current game state. */
	public boolean pauseUpdate = false;
	/** True if we want to pause rendering the current game state. */
	public boolean pauseRender = false;
	
	private boolean usingDefaultContext = false;
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Adds a state to the state list. if the current state is null the added state will be set as the current state.
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
	 * 
	 * @
	 */
	public void enterState(int ID) {
		enterState(ID, null, null);
	}
	/**
	 * Enters the state with the specific <code>ID</code>. if the states does not exits and exception will be thrown.
	 * 
	 * @param leave
	 *            the "Out"-Transition to use (can be null);
	 * @param enter
	 *            the "In"-Transition to use (can be null)
	 * 
	 *            @
	 */
	public void enterState(int ID, Transition leave, Transition enter) {
		if (!states.containsKey(ID)) {
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
		leaveTransition.init(container, currentState, nextState);
		currentState.leaving(context, world, nextState);
	}
	
	// ABSTRACT METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Gets called to initialize the {@link GameContext}. If null is returned a default implementation will be created.
	 * <p>
	 * The default context will return null on the current getters:
	 * <li> {@link GameContext#getAssets()}</li>
	 * <li> {@link GameContext#getFontRenderer()}</li>
	 * <hr>
	 * 
	 * @return an instance of the {@link GameContext} Interface.
	 */
	protected abstract GameContext initGameContext(GameContainer container);
	/**
	 * Gets called to initialize the {@link GameWorld}. You may return null. The class will not create a default
	 * implementation (would be hard to guess how you want your world, also not all games need a world). Hence it's a
	 * protected method you can override on need.
	 * 
	 * @return an instance of the {@link GameWorld} Interface (if wanted).
	 */
	protected GameWorld initWorld(GameContainer container) {
		return null;
	};
	/**
	 * Gets called after {@link StateBasedGame#initGameContext(GameContainer)} and
	 * {@link StateBasedGame#initWorld(GameContainer)} to add all states.
	 * <p>
	 * After this call all states get a call to the {@link GameState#init(GameContext, GameWorld)} method.
	 * <p>
	 * Use this code to initiate your game to. After this call the game will start running.
	 */
	protected abstract void initStates(GameContext context);
	
	/**
	 * Gets called before the update call to the current state.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param world
	 *            the world the game plays in
	 * @param delta
	 *            the {@link GameDelta} object containing the delta values. @
	 */
	protected void preUpdate(GameContext context, GameWorld world, GameDelta delta) {}
	/**
	 * Gets called after the update call to the current state.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param world
	 *            the world the game plays in
	 * @param delta
	 *            the {@link GameDelta} object containing the delta values. @
	 */
	protected void postUpdate(GameContext context, GameWorld world, GameDelta delta) {}
	
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
	protected void preRender(GameContext context, GameWorld world, Graphics g) {}
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
	protected void postRender(GameContext context, GameWorld world, Graphics g) {}
	
	// GAME METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public final void init(GameContainer container) {
		this.container = container;
		context = initGameContext(container);
		world = initWorld(container);
		if (context == null) {
			context = new DefaultGameContext(container, this);
			usingDefaultContext = true;
		}
		
		initStates(context);
		for (GameState s : states.values()) {
			s.init(context, world);
		}
		
		if (currentState != null) {
			currentState.entered(context, world, null);
		}
	}
	public final void update(GameContainer container, float delta) {
		if (usingDefaultContext) {
			context.getGameDelta().update(context, world, delta);
		}
		preUpdate(context, world, context.getGameDelta());
		
		if (leaveTransition != null) {
			leaveTransition.update(container, context.getGameDelta().getDelta());
			if (leaveTransition.isFinished()) {
				currentState.left(context, world, nextState);
				previousState = currentState;
				currentState = nextState;
				nextState = null;
				leaveTransition = null;
				currentState.entering(context, world, previousState);
				if (enterTransition != null) {
					enterTransition.init(container, previousState, currentState);
				}
			} else {
				postUpdate(context, world, context.getGameDelta());
				return;
			}
		}
		
		if (enterTransition != null) {
			enterTransition.update(container, context.getGameDelta().getDelta());
			if (enterTransition.isFinished()) {
				currentState.entered(context, world, previousState);
				previousState = null;
				enterTransition = null;
			} else {
				postUpdate(context, world, context.getGameDelta());
				return;
			}
		}
		
		if (!pauseUpdate) {
			if (currentState != null) {
				currentState.update(context, world, context.getGameDelta());
			}
		}
		
		postUpdate(context, world, context.getGameDelta());
	}
	public final void render(GameContainer container, Graphics g) {
		preRender(context, world, g);
		
		if (leaveTransition != null) {
			leaveTransition.preRender(container, g);
		} else if (enterTransition != null) {
			enterTransition.preRender(container, g);
		}
		
		if (!pauseRender) {
			if (currentState != null) {
				currentState.render(context, world, g);
			}
		}
		
		if (leaveTransition != null) {
			leaveTransition.postRender(container, g);
		} else if (enterTransition != null) {
			enterTransition.postRender(container, g);
		}
		
		postRender(context, world, g);
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
	public boolean mouseMoved(int x, int y) {
		if (canForwardInput()) {
			return currentState.mouseMoved(x, y);
		}
		return false;
	}
	public boolean scrolled(int amount) {
		if (canForwardInput()) {
			return currentState.scrolled(amount);
		}
		return false;
	}
	
	// OVERRIDE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void pause(GameContainer container) {}
	public void resume(GameContainer container) {}
	
	/**
	 * If you override this, call super.dispose. This will dispose the {@link GameContext} and the {@link GameWorld} for
	 * you.
	 */
	public void dispose() {
		context.dispose();
		if (world != null) {
			world.dispose();
		}
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private boolean canForwardInput() {
		return (leaveTransition == null) && (enterTransition == null) && !isUpdatePaused() && (currentState != null);
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
	 * @return the context in which the game runs.
	 */
	public GameContext getGameContext() {
		return context;
	}
	/**
	 * @return the world the game plays in. can be null if you did not provide one!
	 */
	public GameWorld getWorld() {
		return world;
	}
	
	/**
	 * @return the current state.
	 */
	public GameState getCurrentState() {
		return currentState;
	}
	/**
	 * @return the state mapped by <code>ID</code>. null is returned if there is state mapped to the ID.
	 */
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
	
}
