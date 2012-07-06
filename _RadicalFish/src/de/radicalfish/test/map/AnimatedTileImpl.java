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

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;
import de.radicalfish.context.GameContext;
import de.radicalfish.context.GameDelta;
import de.radicalfish.world.World;
import de.radicalfish.world.map.AnimatedTile;

public class AnimatedTileImpl implements AnimatedTile {

	private int[] times, index;
	private boolean pingPong;
	private int curIndex, curTime, direction;
	
	public AnimatedTileImpl(int[] times, int[] index, boolean pingPong) {
		this.times = times;
		this.index = index;
		this.pingPong = pingPong;
		
		curTime = times[0];
		curIndex = 0;
	}
	
	public void update(GameContext context, World world, GameDelta delta) throws SlickException {
		curTime -= delta.getDelta();
		
		while (curTime < 0) {
			curIndex = (curIndex + direction) % index.length;
			if (pingPong) {
				if (curIndex <= 0) {
					curIndex = 0;
					direction = 1;
				} else if (curIndex >= index.length - 1) {
					curIndex = index.length - 1;
					direction = -1;
				}
			}
			curTime += times[curIndex];
		}
	}
	public void render(GameContext context, World world, Graphics g) throws SlickException {
		
	}

	public int getTileID() {
		return index[curIndex];
	}

	public void setTileID(int id) {
		Log.error("cannot set on animated tiles");
	}

	
	@Override
	public int[] getFrameTimes() {
		return times;
	}

	@Override
	public int[] getIndexes() {
		return index;
	}

	@Override
	public int getFrameTime(int index) {
		return times[index];
	}

	@Override
	public int getIndex(int index) {
		return this.index[index];
	}

	@Override
	public void setFrameTimes(int[] times) {
		this.times = times;
	}

	@Override
	public void setIndexes(int[] indexes) {
		index = indexes;
	}

	@Override
	public void setFrameTime(int index, int time) {
		times[index] = time;
	}

	@Override
	public void setIndex(int index, int tileIndex) {
		this.index[index] = tileIndex;
	}
	
}
