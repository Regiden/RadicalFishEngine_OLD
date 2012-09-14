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
package de.radicalfish.font.commands;
import de.radicalfish.GameContainer;
import de.radicalfish.font.StyleInfo;

/**
 * This can be used to reset a change made to the {@link StyleInfo}. Currently the following reset possibilities are
 * available:
 * <p>
 * <li>RESET.ALL : resets both color (to white) and the geometry</li>
 * <li>RESET.COLOR : resets only the color (to white) on all corners</li>
 * <li>RESET.ALPHA : resets the alpha value of all color corners</li>
 * <li>RESET.GEOM : resets the geometry of the characters (position, rotation etc.)</li>
 * <hr>
 * The reset will be applied on at the given <code>charpoint</code>.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 13.09.2012
 */
public class ResetCommand extends StyleCommand {
	
	/** The type the reset command can have. */
	public enum RESET {
		ALL, COLOR, ALPHA, GEOM
	}
	
	protected final RESET type;
	
	/**
	 * Creates a new {@link ResetCommand}.
	 * 
	 * @param type
	 *            the type to the command what to reset.
	 * @param charpoint
	 *            the charpoint to start
	 */
	public ResetCommand(RESET type, int charpoint) {
		super(charpoint);
		this.type = type;
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void execute(GameContainer container, StyleInfo style) {
		switch (type) {
			case ALL:
				style.reset();
				break;
			case COLOR:
				style.resetColor();
				break;
			case GEOM:
				style.resetGeom();
				break;
			case ALPHA:
				style.colorTopLeft.a = 1.0f;
				style.colorTopRight.a = 1.0f;
				style.colorBottomLeft.a = 1.0f;
				style.colorBottomRight.a = 1.0f;
				break;
		}
	}
	
	// UNUSED
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void update(GameContainer container, float delta) {
		
	}
	public void finish(GameContainer container, StyleInfo style) {
		switch (type) {
			case ALL:
				style.reset();
				break;
			case COLOR:
				style.resetColor();
				break;
			case GEOM:
				style.resetGeom();
				break;
			case ALPHA:
				style.colorTopLeft.a = 1.0f;
				style.colorTopRight.a = 1.0f;
				style.colorBottomLeft.a = 1.0f;
				style.colorBottomRight.a = 1.0f;
				break;
		}
	}
	public void reset() {
		
	}
}
