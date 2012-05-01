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
package de.radicalfish.twl;

import de.matthiasmann.twl.HAlignment;
import de.matthiasmann.twl.renderer.AnimationState;
import de.matthiasmann.twl.renderer.Font;
import de.matthiasmann.twl.renderer.FontCache;
import de.radicalfish.text.SpriteFont;

public class TWLSpriteFont implements Font {

	private SpriteFont font;
	
	public TWLSpriteFont(SpriteFont font) {
		this.font = font;
		
	}
	
	
	public void destroy() {
		
	}

	public int getBaseLine() {
		return 0;
	}

	public int getLineHeight() {
		return 0;
	}

	public int getSpaceWidth() {
		return 0;
	}

	@Override
	public int getEM() {
		return font.getWidth("M");
	}

	public int getEX() {
		return font.getWidth("X");
	}

	public int computeMultiLineTextWidth(CharSequence str) {
		return 0;
	}

	public int computeTextWidth(CharSequence str) {
		return 0;
	}

	public int computeTextWidth(CharSequence str, int start, int end) {
		return 0;
	}

	public int computeVisibleGlpyhs(CharSequence str, int start, int end, int width) {
		return 0;
	}

	public int drawMultiLineText(AnimationState as, int x, int y, CharSequence str, int width, HAlignment align) {
		return 0;
	}

	public int drawText(AnimationState as, int x, int y, CharSequence str) {
		return 0;
	}

	public int drawText(AnimationState as, int x, int y, CharSequence str, int start, int end) {
		return 0;
	}

	public FontCache cacheMultiLineText(FontCache prevCache, CharSequence str, int width, HAlignment align) {
		return null;
	}

	public FontCache cacheText(FontCache prevCache, CharSequence str) {
		return null;
	}

	public FontCache cacheText(FontCache prevCache, CharSequence str, int start, int end) {
		return null;
	}

	public boolean isProportional() {
		return false;
	}
	
	
}
