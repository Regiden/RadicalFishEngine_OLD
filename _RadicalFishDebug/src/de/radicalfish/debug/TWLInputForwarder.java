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
import org.lwjgl.input.Keyboard;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.GUI;

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
	
	// STATIC METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return the gdx code for the specific lwjgl code.
	 */
	public static int getGdxKeyCode (int lwjglKeyCode) {
		switch (lwjglKeyCode) {
		case Keyboard.KEY_0:
			return Input.Keys.NUM_0;
		case Keyboard.KEY_1:
			return Input.Keys.NUM_1;
		case Keyboard.KEY_2:
			return Input.Keys.NUM_2;
		case Keyboard.KEY_3:
			return Input.Keys.NUM_3;
		case Keyboard.KEY_4:
			return Input.Keys.NUM_4;
		case Keyboard.KEY_5:
			return Input.Keys.NUM_5;
		case Keyboard.KEY_6:
			return Input.Keys.NUM_6;
		case Keyboard.KEY_7:
			return Input.Keys.NUM_7;
		case Keyboard.KEY_8:
			return Input.Keys.NUM_8;
		case Keyboard.KEY_9:
			return Input.Keys.NUM_9;
		case Keyboard.KEY_A:
			return Input.Keys.A;
		case Keyboard.KEY_B:
			return Input.Keys.B;
		case Keyboard.KEY_C:
			return Input.Keys.C;
		case Keyboard.KEY_D:
			return Input.Keys.D;
		case Keyboard.KEY_E:
			return Input.Keys.E;
		case Keyboard.KEY_F:
			return Input.Keys.F;
		case Keyboard.KEY_G:
			return Input.Keys.G;
		case Keyboard.KEY_H:
			return Input.Keys.H;
		case Keyboard.KEY_I:
			return Input.Keys.I;
		case Keyboard.KEY_J:
			return Input.Keys.J;
		case Keyboard.KEY_K:
			return Input.Keys.K;
		case Keyboard.KEY_L:
			return Input.Keys.L;
		case Keyboard.KEY_M:
			return Input.Keys.M;
		case Keyboard.KEY_N:
			return Input.Keys.N;
		case Keyboard.KEY_O:
			return Input.Keys.O;
		case Keyboard.KEY_P:
			return Input.Keys.P;
		case Keyboard.KEY_Q:
			return Input.Keys.Q;
		case Keyboard.KEY_R:
			return Input.Keys.R;
		case Keyboard.KEY_S:
			return Input.Keys.S;
		case Keyboard.KEY_T:
			return Input.Keys.T;
		case Keyboard.KEY_U:
			return Input.Keys.U;
		case Keyboard.KEY_V:
			return Input.Keys.V;
		case Keyboard.KEY_W:
			return Input.Keys.W;
		case Keyboard.KEY_X:
			return Input.Keys.X;
		case Keyboard.KEY_Y:
			return Input.Keys.Y;
		case Keyboard.KEY_Z:
			return Input.Keys.Z;
		case Keyboard.KEY_LMENU:
			return Input.Keys.ALT_LEFT;
		case Keyboard.KEY_RMENU:
			return Input.Keys.ALT_RIGHT;
		case Keyboard.KEY_BACKSLASH:
			return Input.Keys.BACKSLASH;
		case Keyboard.KEY_COMMA:
			return Input.Keys.COMMA;
		case Keyboard.KEY_DELETE:
			return Input.Keys.FORWARD_DEL;
		case Keyboard.KEY_LEFT:
			return Input.Keys.DPAD_LEFT;
		case Keyboard.KEY_RIGHT:
			return Input.Keys.DPAD_RIGHT;
		case Keyboard.KEY_UP:
			return Input.Keys.DPAD_UP;
		case Keyboard.KEY_DOWN:
			return Input.Keys.DPAD_DOWN;
		case Keyboard.KEY_RETURN:
			return Input.Keys.ENTER;
		case Keyboard.KEY_HOME:
			return Input.Keys.HOME;
		case Keyboard.KEY_MINUS:
			return Input.Keys.MINUS;
		case Keyboard.KEY_PERIOD:
			return Input.Keys.PERIOD;
		case Keyboard.KEY_ADD:
			return Input.Keys.PLUS;
		case Keyboard.KEY_SEMICOLON:
			return Input.Keys.SEMICOLON;
		case Keyboard.KEY_LSHIFT:
			return Input.Keys.SHIFT_LEFT;
		case Keyboard.KEY_RSHIFT:
			return Input.Keys.SHIFT_RIGHT;
		case Keyboard.KEY_SLASH:
			return Input.Keys.SLASH;
		case Keyboard.KEY_SPACE:
			return Input.Keys.SPACE;
		case Keyboard.KEY_TAB:
			return Input.Keys.TAB;
		case Keyboard.KEY_LCONTROL:
			return Input.Keys.CONTROL_LEFT;
		case Keyboard.KEY_RCONTROL:
			return Input.Keys.CONTROL_RIGHT;
		case Keyboard.KEY_NEXT:
			return Input.Keys.PAGE_DOWN;
		case Keyboard.KEY_PRIOR:
			return Input.Keys.PAGE_UP;
		case Keyboard.KEY_ESCAPE:
			return Input.Keys.ESCAPE;
		case Keyboard.KEY_END:
			return Input.Keys.END;
		case Keyboard.KEY_INSERT:
			return Input.Keys.INSERT;
		case Keyboard.KEY_BACK:
			return Input.Keys.DEL;
		case Keyboard.KEY_SUBTRACT:
			return Input.Keys.MINUS;
		case Keyboard.KEY_APOSTROPHE:
			return Input.Keys.APOSTROPHE;
		case Keyboard.KEY_F1:
			return Input.Keys.F1;
		case Keyboard.KEY_F2:
			return Input.Keys.F2;
		case Keyboard.KEY_F3:
			return Input.Keys.F3;
		case Keyboard.KEY_F4:
			return Input.Keys.F4;
		case Keyboard.KEY_F5:
			return Input.Keys.F5;
		case Keyboard.KEY_F6:
			return Input.Keys.F6;
		case Keyboard.KEY_F7:
			return Input.Keys.F7;
		case Keyboard.KEY_F8:
			return Input.Keys.F8;
		case Keyboard.KEY_F9:
			return Input.Keys.F9;
		case Keyboard.KEY_F10:
			return Input.Keys.F10;
		case Keyboard.KEY_F11:
			return Input.Keys.F11;
		case Keyboard.KEY_F12:
			return Input.Keys.F12;
		case Keyboard.KEY_COLON:
			return Input.Keys.COLON;
		case Keyboard.KEY_NUMPAD0:
			return Input.Keys.NUM_0;
		case Keyboard.KEY_NUMPAD1:
			return Input.Keys.NUM_1;
		case Keyboard.KEY_NUMPAD2:
			return Input.Keys.NUM_2;
		case Keyboard.KEY_NUMPAD3:
			return Input.Keys.NUM_3;
		case Keyboard.KEY_NUMPAD4:
			return Input.Keys.NUM_4;
		case Keyboard.KEY_NUMPAD5:
			return Input.Keys.NUM_5;
		case Keyboard.KEY_NUMPAD6:
			return Input.Keys.NUM_6;
		case Keyboard.KEY_NUMPAD7:
			return Input.Keys.NUM_7;
		case Keyboard.KEY_NUMPAD8:
			return Input.Keys.NUM_8;
		case Keyboard.KEY_NUMPAD9:
			return Input.Keys.NUM_9;
		default:
			return Input.Keys.UNKNOWN;
		}
	}
	/**
	 * @return the lwjgl code for the specific gdx code.
	 */
	public static int getLwjglKeyCode (int gdxKeyCode) {
		switch (gdxKeyCode) {
		case Input.Keys.NUM_0:
			return Keyboard.KEY_0;
		case Input.Keys.NUM_1:
			return Keyboard.KEY_1;
		case Input.Keys.NUM_2:
			return Keyboard.KEY_2;
		case Input.Keys.NUM_3:
			return Keyboard.KEY_3;
		case Input.Keys.NUM_4:
			return Keyboard.KEY_4;
		case Input.Keys.NUM_5:
			return Keyboard.KEY_5;
		case Input.Keys.NUM_6:
			return Keyboard.KEY_6;
		case Input.Keys.NUM_7:
			return Keyboard.KEY_7;
		case Input.Keys.NUM_8:
			return Keyboard.KEY_8;
		case Input.Keys.NUM_9:
			return Keyboard.KEY_9;
		case Input.Keys.A:
			return Keyboard.KEY_A;
		case Input.Keys.B:
			return Keyboard.KEY_B;
		case Input.Keys.C:
			return Keyboard.KEY_C;
		case Input.Keys.D:
			return Keyboard.KEY_D;
		case Input.Keys.E:
			return Keyboard.KEY_E;
		case Input.Keys.F:
			return Keyboard.KEY_F;
		case Input.Keys.G:
			return Keyboard.KEY_G;
		case Input.Keys.H:
			return Keyboard.KEY_H;
		case Input.Keys.I:
			return Keyboard.KEY_I;
		case Input.Keys.J:
			return Keyboard.KEY_J;
		case Input.Keys.K:
			return Keyboard.KEY_K;
		case Input.Keys.L:
			return Keyboard.KEY_L;
		case Input.Keys.M:
			return Keyboard.KEY_M;
		case Input.Keys.N:
			return Keyboard.KEY_N;
		case Input.Keys.O:
			return Keyboard.KEY_O;
		case Input.Keys.P:
			return Keyboard.KEY_P;
		case Input.Keys.Q:
			return Keyboard.KEY_Q;
		case Input.Keys.R:
			return Keyboard.KEY_R;
		case Input.Keys.S:
			return Keyboard.KEY_S;
		case Input.Keys.T:
			return Keyboard.KEY_T;
		case Input.Keys.U:
			return Keyboard.KEY_U;
		case Input.Keys.V:
			return Keyboard.KEY_V;
		case Input.Keys.W:
			return Keyboard.KEY_W;
		case Input.Keys.X:
			return Keyboard.KEY_X;
		case Input.Keys.Y:
			return Keyboard.KEY_Y;
		case Input.Keys.Z:
			return Keyboard.KEY_Z;
		case Input.Keys.ALT_LEFT:
			return Keyboard.KEY_LMENU;
		case Input.Keys.ALT_RIGHT:
			return Keyboard.KEY_RMENU;
		case Input.Keys.BACKSLASH:
			return Keyboard.KEY_BACKSLASH;
		case Input.Keys.COMMA:
			return Keyboard.KEY_COMMA;
		case Input.Keys.FORWARD_DEL:
			return Keyboard.KEY_DELETE;
		case Input.Keys.DPAD_LEFT:
			return Keyboard.KEY_LEFT;
		case Input.Keys.DPAD_RIGHT:
			return Keyboard.KEY_RIGHT;
		case Input.Keys.DPAD_UP:
			return Keyboard.KEY_UP;
		case Input.Keys.DPAD_DOWN:
			return Keyboard.KEY_DOWN;
		case Input.Keys.ENTER:
			return Keyboard.KEY_RETURN;
		case Input.Keys.HOME:
			return Keyboard.KEY_HOME;
		case Input.Keys.MINUS:
			return Keyboard.KEY_MINUS;
		case Input.Keys.PERIOD:
			return Keyboard.KEY_PERIOD;
		case Input.Keys.PLUS:
			return Keyboard.KEY_ADD;
		case Input.Keys.SEMICOLON:
			return Keyboard.KEY_SEMICOLON;
		case Input.Keys.SHIFT_LEFT:
			return Keyboard.KEY_LSHIFT;
		case Input.Keys.SHIFT_RIGHT:
			return Keyboard.KEY_RSHIFT;
		case Input.Keys.SLASH:
			return Keyboard.KEY_SLASH;
		case Input.Keys.SPACE:
			return Keyboard.KEY_SPACE;
		case Input.Keys.TAB:
			return Keyboard.KEY_TAB;
		case Input.Keys.DEL:
			return Keyboard.KEY_BACK;
		case Input.Keys.CONTROL_LEFT:
			return Keyboard.KEY_LCONTROL;
		case Input.Keys.CONTROL_RIGHT:
			return Keyboard.KEY_RCONTROL;
		case Input.Keys.ESCAPE:
			return Keyboard.KEY_ESCAPE;
		case Input.Keys.F1:
			return Keyboard.KEY_F1;
		case Input.Keys.F2:
			return Keyboard.KEY_F2;
		case Input.Keys.F3:
			return Keyboard.KEY_F3;
		case Input.Keys.F4:
			return Keyboard.KEY_F4;
		case Input.Keys.F5:
			return Keyboard.KEY_F5;
		case Input.Keys.F6:
			return Keyboard.KEY_F6;
		case Input.Keys.F7:
			return Keyboard.KEY_F7;
		case Input.Keys.F8:
			return Keyboard.KEY_F8;
		case Input.Keys.F9:
			return Keyboard.KEY_F9;
		case Input.Keys.F10:
			return Keyboard.KEY_F10;
		case Input.Keys.F11:
			return Keyboard.KEY_F11;
		case Input.Keys.F12:
			return Keyboard.KEY_F12;
		case Input.Keys.COLON:
			return Keyboard.KEY_COLON;
		default:
			return Keyboard.KEY_NONE;
		}
	}

	// INTERFACE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public boolean keyDown(int keycode) {
		return gui.handleKey(getLwjglKeyCode(keycode), '\000', true);
	}
	public boolean keyUp(int keycode) {
		return gui.handleKey(getLwjglKeyCode(keycode), '\000', false);
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
