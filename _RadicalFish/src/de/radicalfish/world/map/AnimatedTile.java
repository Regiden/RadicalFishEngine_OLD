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
package de.radicalfish.world.map;
import de.radicalfish.Animation;

/**
 * An animated tile that changes it's index like in an {@link Animation}. {@link MapIO} will check for this tile and
 * save the extra information for the tile.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 06.07.2012
 */
public interface AnimatedTile extends Tile {
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return the array containing the times for each frame.
	 */
	public int[] getFrameTimes();
	/**
	 * @return the array containing the index for each frame.
	 */
	public int[] getIndexes();
	
	/**
	 * @return the time of the frame at <code>index</code>.
	 */
	public int getFrameTime(int index);
	/**
	 * @return the index of the frame at <code>index</code>.
	 */
	public int getIndex(int index);
	
	/**
	 * @return true if the animation "ping-pong"s.
	 */
	public boolean isPingPong();
	
	// SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Sets the times for all frames.
	 */
	public void setFrameTimes(int[] times);
	/**
	 * Sets the indexes for all frames.
	 */
	public void setIndexes(int[] indexes);
	
	/**
	 * Sets the time of a frame at <code>index</code>.
	 */
	public void setFrameTime(int index, int time);
	/**
	 * Sets the index of a frame at <code>index</code>.
	 */
	public void setIndex(int index, int tileIndex);
	
	/**
	 * Sets if the AnimatedTile should "pingpong".
	 */
	public void setPingPong(boolean pingpong);
	
}
