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
package de.radicalfish.test.map;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import de.radicalfish.world.map.TileSet;

public class SimpleTileSet implements TileSet {
	
	private transient SpriteSheet sheet;
	
	private String name, location;
	
	public SimpleTileSet(String resourceName, SpriteSheet sheet) {
		name = resourceName;
		this.sheet = sheet;
		location = sheet.getResourceReference();
	}
	
	@Override
	public String getResourceName() {
		return name;
	}
	
	@Override
	public String getResourceLocation() {
		return location;
	}
	
	@Override
	public Image getTileAt(int index) {
		return getTileAt(index % sheet.getHorizontalCount(), index / sheet.getVerticalCount());
	}
	
	@Override
	public Image getTileAt(int x, int y) {
		return sheet.getSubImage(x, y);
	}
	
	@Override
	public void setResourceName(String name) {
		this.name = name;
	}
	
}
