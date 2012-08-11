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
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Registry;
import org.newdawn.slick.SlickException;
import de.radicalfish.debug.Logger;

/**
 * A simple class for display hardware information from LWJGL.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 06.06.2012
 */
public class LWJGLDebugUtil {
	
	/**
	 * Prints the complete LWJGL information available by this class with the {@link Logger} class.
	 * 
	 * @throws SlickException
	 */
	public static void printInformationToLog() throws SlickException {
		try {
			Logger.none("Graphics Card: ");
			Logger.none("\tAdapter:        " + Display.getAdapter());
			Logger.none("\tDriver Version: " + Display.getVersion());
			Logger.none("\tDisplay Modes:  " + transformArrayNicely(Display.getAvailableDisplayModes(), 4, 3));
			
			Logger.none("\tOpenGL Version: " + GL11.glGetString(GL11.GL_VERSION));
			Logger.none("\tSupported...:   " + getSupportedOpenGLVersions());
			
			Logger.none("GLU: ");
			Logger.none("\tGLU Version:  " + Registry.gluGetString(GLU.GLU_VERSION));
			Logger.none("\tExtentions: " + Registry.gluGetString(GLU.GLU_EXTENSIONS));
			
		} catch (Exception e) {
			throw new SlickException(e.getMessage(), e.getCause());
		}
		
	}

	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private static String getSupportedOpenGLVersions() {
		StringBuilder buffer = new StringBuilder();
		ContextCapabilities caps = GLContext.getCapabilities();
		
		if (caps.OpenGL11)
			buffer.append("1.1");
		if (caps.OpenGL12)
			buffer.append(", 1.2");
		if (caps.OpenGL13)
			buffer.append(", 1.3");
		if (caps.OpenGL14)
			buffer.append(", 1.4");
		if (caps.OpenGL15)
			buffer.append(", 1.5");
		if (caps.OpenGL20)
			buffer.append(", 2.0");
		if (caps.OpenGL21)
			buffer.append(", 2.1");
		if (caps.OpenGL30)
			buffer.append(", 3.0");
		if (caps.OpenGL31)
			buffer.append(", 3.1");
		if (caps.OpenGL32)
			buffer.append(", 3.2");
		if (caps.OpenGL33)
			buffer.append(", 3.3");
		if (caps.OpenGL40)
			buffer.append(", 4.0");
		if (caps.OpenGL41)
			buffer.append(", 4.1");
		
		return buffer.toString();
	}
	private static String transformArrayNicely(Object[] array, int maxColumns, int tabs) {
		StringBuilder content = new StringBuilder();
		
		String lineFeed = System.getProperty("line.separator");
		int counter = 0;
		for (int i = 0; i < array.length; i++) {
			content.append('[').append(array[i].toString()).append("]");
			if (i != array.length - 1) {
				content.append(", ");
			}
			if (counter >= maxColumns) {
				content.append(lineFeed);
				for (int j = 0; j < tabs; j++) {
					content.append("\t");
				}
				counter = 0;
			} else {
				counter++;
			}
			
		}
		return content.toString();
	}
	
}
