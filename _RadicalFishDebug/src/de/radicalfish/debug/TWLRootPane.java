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
import de.matthiasmann.twl.ActionMap;
import de.matthiasmann.twl.DesktopArea;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.Widget;

/**
 * RootPane for a TWL game state. It forwards input events which where not handled by the UI to the game state.
 * 
 * @author Matthias Mann
 * @author Stefan Lange (added some features)
 */
public class TWLRootPane extends DesktopArea {
	
	private TWLGameState state;
	
	private Widget currentChildInFocus;
	
	private int oldMouseX;
	private int oldMouseY;
	
	public TWLRootPane(TWLGameState state) {
		this.state = state;
		
		ActionMap actionMap = getActionMap();
		if (actionMap == null) {
			actionMap = new ActionMap();
			setActionMap(actionMap);
		}
		
		if(state != null) {
			actionMap.addMapping(state);
		}
		
		
		setCanAcceptKeyboardFocus(true);
		
		
	}
	
	// OVERRIDES
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	@Override
	protected void keyboardFocusLost() {
		if(state != null) {
			state.keyboardFocusLost();
		}
	}
	@Override
	protected boolean requestKeyboardFocus(Widget child) {
		if (child != null) {
			if(state != null) {
				state.keyboardFocusLost();
			}
		}
		return super.requestKeyboardFocus(child);
	}
	@Override
	protected void keyboardFocusChildChanged(Widget child) {
		currentChildInFocus = child;
		super.keyboardFocusChildChanged(child);
	}
	
	@Override
	protected boolean handleEvent(Event evt) {
		if (super.handleEvent(evt)) {
			return true;
		}
		if(state == null) {
			return true;
		}
		
		switch (evt.getType()) {
			case KEY_PRESSED:
				state.keyPressed(evt.getKeyCode(), evt.getKeyChar());
				break;
			case KEY_RELEASED:
				state.keyReleased(evt.getKeyCode(), evt.getKeyChar());
				break;
			case MOUSE_BTNDOWN:
				state.mousePressed(evt.getMouseButton(), evt.getMouseX(), evt.getMouseY());
				break;
			case MOUSE_BTNUP:
				state.mouseReleased(evt.getMouseButton(), evt.getMouseX(), evt.getMouseY());
				break;
			case MOUSE_CLICKED:
				state.mouseClicked(evt.getMouseButton(), evt.getMouseX(), evt.getMouseY(), evt.getMouseClickCount());
				break;
			case MOUSE_ENTERED:
			case MOUSE_MOVED:
				state.mouseMoved(oldMouseX, oldMouseY, evt.getMouseX(), evt.getMouseY());
				break;
			case MOUSE_DRAGGED:
				state.mouseDragged(oldMouseX, oldMouseY, evt.getMouseX(), evt.getMouseY());
				break;
			case MOUSE_WHEEL:
				state.mouseWheelMoved(evt.getMouseWheelDelta());
				break;
		}
		
		if (evt.isMouseEvent()) {
			oldMouseX = evt.getMouseX();
			oldMouseY = evt.getMouseY();
		}
		
		return true;
	}
	@Override
	protected void layout() {
		super.layout();
		if(state != null) {
			state.layoutRootPane();
		}
	}
	
	// GETTER AND SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return the current focus qwner, or null if no child has the focus.
	 */
	public Widget getCurrentFocusOwner() {
		return currentChildInFocus;
	}
	
	
}
