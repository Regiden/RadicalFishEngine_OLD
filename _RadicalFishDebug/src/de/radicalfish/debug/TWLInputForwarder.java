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
import org.lwjgl.opengl.Display;
import com.badlogic.gdx.InputProcessor;
import de.matthiasmann.twl.GUI;
import de.radicalfish.GameInput;

/**
 * Forwards input events from Slick to TWL.
 * 
 * @author Matthias Mann
 */
public class TWLInputForwarder implements InputProcessor {

    private final GameInput input;
    private final GUI gui;

    public TWLInputForwarder(GUI gui, GameInput input) {
        if (gui == null) {
            throw new NullPointerException("gui");
        }
        if (input == null) {
            throw new NullPointerException("input");
        }

        this.gui = gui;
        this.input = input;
    }

	@Override
	public boolean keyDown(int keycode) {
		gui.handleKey(keycode, Keyboard.getEventCharacter(), true);
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return true;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		return true;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		return true;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		gui.handleMouseWheel(amount);
		return true;
	}

   
}
