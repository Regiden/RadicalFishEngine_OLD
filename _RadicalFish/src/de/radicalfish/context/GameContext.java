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
package de.radicalfish.context;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;
import de.radicalfish.debug.DebugHook;
import de.radicalfish.effects.PostProcesser;
import de.radicalfish.effects.ToneModel;
import de.radicalfish.text.FontRenderer;

/**
 * Context of a game, e.g. settings method for getting game container and stuff.
 * 
 * @author Stefan Lange
 * @version 0.5.0
 * @since 11.03.2012
 */
public interface GameContext {
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return the base scale of the game.
	 */
	public float getGameScale();
	/**
	 * @return the width of the game. NOT the width of the container!
	 */
	public int getGameWidth();
	/**
	 * @return the height of the game. NOT the height of the container!
	 */
	public int getGameHeight();
	/**
	 * @return the width of the container.
	 */
	public int getContainerWidth();
	/**
	 * @return the height of the container.
	 */
	public int getContainerHeight();
	/**
	 * @return the game from which you can enter other states.
	 */
	public StateBasedGame getGame();
	/**
	 * @return the container the game runs in.
	 */
	public GameContainer getContainer();
	/**
	 * @return the input handler.
	 */
	public Input getInput();
	/**
	 * @return the game's settings.
	 */
	public Settings getSettings();
	/**
	 * @return a implementation of the GameSpeed interface.
	 */
	public GameDelta getGameDelta();
	/**
	 * @return the variables shared across the game.
	 */
	public GameVariables getGameVariables();
	/**
	 * @return a tone model to use for the game if any.
	 */
	public ToneModel getGameTone();
	/**
	 * @return the post processer used fot this can if any.
	 */
	public PostProcesser getPostProcesser();
	/**
	 * @return the font renderer to use.
	 */
	public FontRenderer getFontRenderer();
	/**
	 * @return the default font to use.
	 */
	public Font getDefaultFont();
	/**
	 * @return the resource manager for the game. can be null if not needed.
	 */
	public Resources getResources();
	/**
	 * @return the debug hook is any.
	 */
	public DebugHook getDebugHook();
}
