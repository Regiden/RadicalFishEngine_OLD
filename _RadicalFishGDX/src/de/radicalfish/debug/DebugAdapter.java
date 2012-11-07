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
package de.radicalfish.debug;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;
import de.radicalfish.GameContainer;
import de.radicalfish.graphics.Graphics;

/**
 * And Adapter for the {@link DebugCallback} which let's you decide which method you want to override (you still have to
 * override init(), update() and render()).
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 09.09.2012
 */
public abstract class DebugAdapter implements DebugCallback, InputProcessor {
	
	/** A list of {@link PerformanceListener} added to the {@link DebugCallback}. */
	protected Array<PerformanceListener> listeners = new Array<PerformanceListener>();
	/** True if the callback is visible, false otherwise.*/
	protected boolean visible = false;
	
	public abstract void init(GameContainer container);
	public abstract void update(GameContainer container, float delta);
	public abstract void render(GameContainer container, Graphics g);
	
	public void pause(GameContainer container) {}
	public void resume(GameContainer container) {}
	public void dispose() {}
	
	public void addPerformanceListener(PerformanceListener listener) {
		listeners.add(listener);
	}
	public void removePerformanceListener(PerformanceListener listener) {
		listeners.removeValue(listener, true);
	}
	
	public Array<PerformanceListener> getPerformanceListener() {
		return listeners;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public boolean isVisible() {
		return visible;
	}
	
	public boolean keyDown(int keycode) {
		return false;
	}
	public boolean keyUp(int keycode) {
		return false;
	}
	public boolean keyTyped(char character) {
		return false;
	}
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}
	public boolean scrolled(int amount) {
		return false;
	}
	
}
