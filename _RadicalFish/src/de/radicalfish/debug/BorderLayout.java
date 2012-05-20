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
package de.radicalfish.debug;
import de.matthiasmann.twl.Widget;

/**
 * Look at {@link java.awt.BorderLayout} for information about the BorderLayout.
 * 
 * @author Stefan Lange
 */
public class BorderLayout extends Widget {
	
	private Widget north, south, east, west, center;
	
	private int hgap, vgap;
	
	/** The location of a component. */
	public enum Location {
		EAST, WEST, NORTH, SOUTH, CENTER
	}
	
	public BorderLayout() {
		this(0, 0);
	}
	public BorderLayout(int hgap, int vgap) {
		this.hgap = hgap;
		this.vgap = vgap;
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Adds the specific <code>widget</code> to a <code>location</code> in the BorderLayout.
	 * 
	 * @param widget
	 *            the widget to add, can be null
	 * @param location
	 *            the location to set the widget to
	 */
	public void add(Widget widget, Location location) {
		if (location == null) {
			throw new NullPointerException("location is null");
		}
		switch (location) {
			case NORTH:
				north = widget;
				break;
			case SOUTH:
				south = widget;
				break;
			case EAST:
				east = widget;
				break;
			case WEST:
				west = widget;
				break;
			case CENTER:
				center = widget;
				break;
		}
		super.add(widget);
	}
	
	// OVERRIDE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	@Override
	/**
	 * Adds the widget to the center location of the layout.
	 */
	public void add(Widget child) {
		add(child, Location.CENTER);
	};
	@Override
	protected void layout() {
		super.layout();
		
		int top = getParent().getInnerY();
		int bottom = getParent().getInnerBottom();
		int left = getParent().getInnerX();
		int right = getParent().getInnerRight();
		
		Widget w = null;
		
		if ((w = north) != null) {
			w.setPosition(left, top);
			w.setSize(Math.max(right - left, 0), w.getPreferredHeight());
			top += w.getPreferredHeight() + vgap;
		}
		if ((w = south) != null) {
			w.setPosition(left, bottom - w.getPreferredHeight());
			w.setSize(right - left, w.getPreferredHeight());
			bottom -= w.getPreferredHeight() + vgap;
		}
		if ((w = east) != null) {
			w.setPosition(right - w.getPreferredWidth(), top);
			w.setSize(w.getPreferredWidth(), Math.max(bottom - top, 0));
			right -= w.getPreferredWidth() + hgap;
		}
		if ((w = west) != null) {
			w.setPosition(left, top);
			w.setSize(w.getPreferredWidth(), Math.max(bottom - top, 0));
			left += w.getPreferredWidth() + hgap;
		}
		if ((w = center) != null) {
			w.setPosition(left, top);
			w.setSize(Math.max(right - left, 0), Math.max(bottom - top, 0));
		}
		
		setMinSize();
		
	}
	
	// PRIVATE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void setMinSize() {
		int width = 0;
		int height = 0;
		
		Widget w = null;
		
		if ((w = east) != null) {
			width += w.getPreferredWidth() + hgap;
			height = Math.max(w.getPreferredHeight(), height);
		}
		if ((w = west) != null) {
			width += w.getPreferredWidth() + hgap;
			height = Math.max(w.getPreferredHeight(), height);
		}
		if ((w = center) != null) {
			width += w.getPreferredWidth();
			height = Math.max(w.getPreferredHeight(), height);
		}
		if ((w = north) != null) {
			width = Math.max(w.getPreferredWidth(), width);
			height += w.getPreferredHeight() + vgap;
		}
		if ((w = south) != null) {
			width = Math.max(w.getPreferredWidth(), width);
			height += w.getPreferredHeight() + vgap;
		}
		
		width += getParent().getInnerX() + (getParent().getWidth() - getParent().getInnerRight());
		height += getParent().getInnerY() + (getParent().getHeight() - getParent().getInnerBottom());
		
		getParent().setSize(Math.max(width, getParent().getWidth()), Math.max(height, getParent().getHeight()));
		
	}
	
}
