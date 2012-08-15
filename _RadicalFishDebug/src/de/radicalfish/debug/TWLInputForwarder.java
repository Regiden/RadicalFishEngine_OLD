/*
 * Copyright (c) 2008-2010, Matthias Mann
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
 *     * Neither the name of Matthias Mann nor the names of its contributors may
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
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.GUI;
import de.radicalfish.util.Utils;

/**
 * Forwards input events from Slick to TWL.
 * 
 * @author Matthias Mann
 */
public class TWLInputForwarder implements InputProcessor {
	
	private final GUI gui;
	
	private boolean mouseDown;
	private boolean ignoreMouse;
	private boolean consumedLastPress;
	
	public TWLInputForwarder(GUI gui) {
		if (gui == null) {
			throw new NullPointerException("gui");
		}
		this.gui = gui;
	}
	
	// INTERFACE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public boolean keyDown(int keycode) {
		return gui.handleKey(Utils.mapGDXKeyToLWJGL(keycode), '\000', true);
	}
	public boolean keyUp(int keycode) {
		return gui.handleKey(Utils.mapGDXKeyToLWJGL(keycode), '\000', false);
	}
	public boolean keyTyped(char character) {
		return gui.handleKey(Event.KEY_NONE, character, true);
	}
	public boolean touchDown(int x, int y, int pointer, int button) {
		if (!mouseDown) {
			consumedLastPress = false;
		}
		mouseDown = true;
		if (ignoreMouse) {
			return false;
		}
		boolean handled = gui.handleMouse(x, y, button, true);
		if (handled) {
			consumedLastPress = true;
		}
		return handled;
	}
	public boolean touchUp(int x, int y, int pointer, int button) {
		mouseDown = false;
		if (ignoreMouse) {
			ignoreMouse = false;
			return false;
		}
		boolean handled = gui.handleMouse(x, y, button, false);
		if (Gdx.app.getType() == Application.ApplicationType.Android) {
			gui.handleMouse(-9999, -9999, -1, false);
		}
		return handled;
	}
	public boolean touchDragged(int x, int y, int pointer) {
		if ((mouseDown) && (!consumedLastPress)) {
			ignoreMouse = true;
			gui.clearMouseState();
			return false;
		}
		return gui.handleMouse(x, y, -1, true);
	}
	public boolean touchMoved(int x, int y) {
		return gui.handleMouse(x, y, -1, true);
	}
	public boolean scrolled(int amount) {
		gui.handleMouseWheel(amount);
		return true;
	}
	
}
