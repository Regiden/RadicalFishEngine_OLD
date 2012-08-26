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
import com.badlogic.gdx.utils.Disposable;
import de.radicalfish.context.GameContext;
import de.radicalfish.context.GameDelta;
import de.radicalfish.graphics.Graphics;
import de.radicalfish.util.RadicalFishException;
import de.radicalfish.world.World;

/**
 * Interface for a game state. It extends the {@link InputProcessor} interface. All input gets forwarded by the
 * {@link StateBasedGame} to current active state.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 17.08.2012
 */
public interface GameState extends InputProcessor, Disposable {
	
	/**
	 * Initiates this game state.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param world
	 *            the world the game plays in
	 */
	public void init(GameContext context, World world) throws RadicalFishException;
	/**
	 * Updates all logic of the state.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param world
	 *            the world the game plays in
	 * @param delta
	 *            the {@link GameDelta} object containing the delta values.
	 */
	public void update(GameContext context, World world, GameDelta delta) throws RadicalFishException;
	/**
	 * Renders all entities of the state.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param world
	 *            the world the game plays in
	 * @param g
	 *            the wrapper for graphics
	 */
	public void render(GameContext context, World world, Graphics g) throws RadicalFishException;
	
	/**
	 * Gets called after the "Out"-Transition is done. can be used to load files while the screen is faded or position
	 * entities.
	 * 
	 * @param context
	 *            the context the games runs in
	 * @param world
	 *            the world the game plays in
	 * @param form
	 *            the {@link GameState} from which we enter this state (can be null if this is the first state)
	 */
	public void entering(GameContext context, World world, GameState form) throws RadicalFishException;
	/**
	 * Gets called after the "In"-Transition is done.
	 * 
	 * @param context
	 *            the context the games runs in
	 * @param world
	 *            the world the game plays in
	 * @param form
	 *            the {@link GameState} from which we enter this state (can be null if this is the first state)
	 */
	public void entered(GameContext context, World world, GameState form) throws RadicalFishException;
	/**
	 * Gets called before the "Out"-Transition is starts.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param world
	 *            the world the game plays in
	 * @param to
	 *            the state we moving to.
	 */
	public void leaving(GameContext context, World world, GameState to) throws RadicalFishException;
	/**
	 * Gets called before the "In"-Transition is starts. Use it to unload content for this state.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param world
	 *            the world the game plays in
	 * @param to
	 *            the state we moving to.
	 */
	public void left(GameContext context, World world, GameState to) throws RadicalFishException;
	
	/**
	 * Gets called if the app pauses (e.g. on android putting the app in background).
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param world
	 *            the world the game plays in
	 * @throws RadicalFishException
	 */
	public void pause(GameContext context, World world) throws RadicalFishException;
	/**
	 * Gets called if the app resumes (e.g. on android returning to the app).
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param world
	 *            the world the game plays in
	 * @throws RadicalFishException
	 */
	public void resume(GameContext context, World world) throws RadicalFishException;
	
	/**
	 * @return the ID of this state.
	 */
	public int getID();
}
