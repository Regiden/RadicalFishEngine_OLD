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
package de.radicalfish;
import java.io.Serializable;

/**
 * A rectangle used for collision. This is a special implementation meant to be used for 2D games with a scale of two.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 07.02.2012
 */
public class Rectangle2D implements Serializable {
	
	private static final long serialVersionUID = 101L;
	
	private int x, y, width, height;
	
	// CONSTRUCTORS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Creates a new empty Rectangle.
	 */
	public Rectangle2D() {
		this(0, 0, 0, 0);
	}
	/**
	 * Creates a new Rectangle with a position, width and height.
	 */
	public Rectangle2D(int x, int y, int width, int height) {
		setBounds(x, y, width, height);
	}
	/**
	 * Creates a new Rectangle with a position, width and height. the position gets rounded.
	 */
	public Rectangle2D(float x, float y, int width, int height) {
		setBounds(x, y, width, height);
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @param rect
	 *            the rectangle to check
	 * @return true if <code>rect</code> intersects with this rectangle
	 */
	public boolean intersects(Rectangle2D rect) {
		if (x > rect.getRight() || getRight() < rect.getX())
			return false;
		if (y > rect.getBottom() || getBottom() < rect.getY())
			return false;
		return true;
	}
	/**
	 * @param x
	 *            the x position to test
	 * @param y
	 *            the y position to test
	 * @return true if the point is inside the rectangle
	 */
	public boolean contains(float x, float y) {
		return (x >= this.x) && (y >= this.y) && (x <= getRight()) && (y <= getBottom());
	}
	
	// SETTER AND GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Sets the bounds of the rectangle.
	 */
	public void setBounds(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	/**
	 * Sets the bounds of the rectangle. the position gets rounded.
	 */
	public void setBounds(float x, float y, int width, int height) {
		this.x = (int)x;
		this.y = (int)y;
		this.width = width;
		this.height = height;
	}
	/**
	 * Sets the position of the rectangle.
	 */
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	/**
	 * Sets the position of the rectangle. the position gets rounded.
	 */
	public void setPosition(float x, float y) {
		this.x = (int) x;
		this.y = (int) y;
	}
	/**
	 * Sets the size of the rectangle.
	 */
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	/**
	 * Sets the size of the rectangle. the size gets rounded.
	 */
	public void setSize(float width, float height) {
		this.width = (int) width;
		this.height = (int) height;
	}
	
	/**
	 * Sets the x position of the rectangle.
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * Sets the x position of the rectangle. the position gets rounded.
	 */
	public void setX(float x) {
		this.x = (int) x;
	}
	/**
	 * Sets the y position of the rectangle.
	 */
	public void setY(int y) {
		this.y = y;
	}
	/**
	 * Sets the y position of the rectangle. the position gets rounded.
	 */
	public void setY(float y) {
		this.y = (int)y;
	}
	/**
	 * Sets the width of the rectangle.
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	/**
	 * Sets the height of the rectangle.
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	
	/**
	 * @return the x position.
	 */
	public int getX() {
		return x;
	}
	/**
	 * @return the y position.
	 */
	public int getY() {
		return y;
	}
	/**
	 * @return the width.
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * @return the height.
	 */
	public int getHeight() {
		return height;
	}
	/**
	 * @return the x position plus the width.
	 */
	public int getRight() {
		return x + width;
	}
	/**
	 * @return the y position plus the height.
	 */
	public int getBottom() {
		return y + height;
	}
	
	/**
	 * @return true if the width and height of the Rectangle is equal or smaller then 0.
	 */
	public boolean isEmpty() {
		return width <= 0 && height <= 0;
	}
	public String toString() {
		return "(rect:" +x + ", " +  y +", " + width + ", " + height + ")";
	}
}
