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
package de.radicalfish.util;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.TextureImpl;
import de.radicalfish.Rectangle2D;

/**
 * Graphics utilities for rendering.
 * 
 * @author Stefan Lange
 * @author Matthias Mann (Line Rendering)
 * @version 0.0.0
 * @since 15.06.2012
 */
public class GraphicUtils {
	
	private static float[] rectVerticies = new float[10];
	
	// LINE RENDERING BY Matthias M
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Draws a rectangle to the screen.
	 */
	public static void drawRect(Rectangle2D rect, Color color, float lineWidth) {
		drawRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), color, lineWidth);
	}
	/**
	 * Draws a rectangle to the screen.
	 */
	public static void drawRect(float x, float y, float width, float height, Color color, float lineWidth) {
		rectVerticies[0] = x;
		rectVerticies[1] = y;
		rectVerticies[2] = x + width;
		rectVerticies[3] = y;
		rectVerticies[4] = x + width;
		rectVerticies[5] = y + height;
		rectVerticies[6] = x;
		rectVerticies[7] = y + height;
		rectVerticies[8] = x;
		rectVerticies[9] = y + height;
		
		drawLine(rectVerticies, 4, lineWidth, color, true);
	}
	/**
	 * Draws lines to the screen.
	 */
	public static void drawLine(float[] pts, int numPts, float width, Color color, boolean drawAsLoop) {
		if (numPts * 2 > pts.length) {
			throw new ArrayIndexOutOfBoundsException(numPts * 2);
		}
		if (numPts >= 2) {
			color.bind();
			TextureImpl.bindNone();
			drawLinesAsQuads(numPts, pts, width, drawAsLoop);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
	}
	private static void drawLinesAsQuads(int numPts, float[] pts, float width, boolean drawAsLoop) {
		width *= 0.5f;
		GL11.glBegin(GL11.GL_QUADS);
		for (int i = 1; i < numPts; i++) {
			drawLineAsQuad(pts[i * 2 - 2], pts[i * 2 - 1], pts[i * 2 + 0], pts[i * 2 + 1], width);
		}
		if (drawAsLoop) {
			int idx = numPts * 2;
			drawLineAsQuad(pts[idx], pts[idx + 1], pts[0], pts[1], width);
		}
		GL11.glEnd();
	}
	private static void drawLineAsQuad(float x0, float y0, float x1, float y1, float w) {
		float dx = x1 - x0;
		float dy = y1 - y0;
		float l = (float) Math.sqrt(dx * dx + dy * dy) / w;
		dx /= l;
		dy /= l;
		GL11.glVertex2f(x0 - dx + dy, y0 - dy - dx);
		GL11.glVertex2f(x0 - dx - dy, y0 - dy + dx);
		GL11.glVertex2f(x1 + dx - dy, y1 + dy + dx);
		GL11.glVertex2f(x1 + dx + dy, y1 + dy - dx);
	}
}
