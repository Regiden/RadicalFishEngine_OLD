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
package de.radicalfish.state.transitions;
import de.radicalfish.GameContainer;
import de.radicalfish.graphics.Graphics;
import de.radicalfish.state.GameState;

/**
 * A {@link Transition} is used to blend from one {@link GameState} to another.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 17.08.2012
 */
public interface Transition {
	
	/**
	 * Initiates this transition.
	 * 
	 * @param container
	 *            the container the game runs in
	 * @param from
	 *            the state we coming from
	 * @param to
	 *            the state we moving to
	 */
	public void init(GameContainer container, GameState from, GameState to) ;
	/**
	 * Updates the transition.
	 * 
	 * @param container
	 *            the container the game runs in
	 * @param delta
	 *            the time since the last frame in seconds
	 */
	public void update(GameContainer container, float delta) ;
	/**
	 * Renders the transition before the existing state.
	 * 
	 * @param container
	 *            the container the game runs in
	 * @param g
	 *            the wrapper for graphics
	 */
	public void preRender(GameContainer container, Graphics g) ;
	/**
	 * Renders the transition after the existing state.
	 * 
	 * @param container
	 *            the container the game runs in
	 * @param g
	 *            the wrapper for graphics
	 */
	public void postRender(GameContainer container, Graphics g) ;
	
	/**
	 * @return true if the transition is completed.
	 */
	public boolean isFinished();
	
}
