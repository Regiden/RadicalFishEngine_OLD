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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Utility class to load files.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 28.08.2012
 */
public class ResourceLoader {
	
	private static final File root = new File(".");
	
	/**
	 * @return the resource as {@link URL} or null if the resource could not be found.
	 */
	public static URL getResourceAsURL(String path) {
		URL url = null;
		try {
			url = tryClassPath(path);
			if (url != null) {
				return url;
			}
			url = tryFileSystem(path);
			if (url != null) {
				return url;
			}
			url = getResource(path).file().toURI().toURL();
			
		} catch (Exception e) {}
		return url;
	}
	
	/**
	 * Tries to load the resource form the internal path. If that fails it will try the external path.
	 * 
	 * @return the {@link FileHandle} associated to <code>path</code> or null if the file could not be found.
	 */
	public static FileHandle getResource(String path) {
		FileHandle fh = Gdx.files.internal(path);
		if (fh == null) {
			fh = Gdx.files.external(path);
		}
		return fh;
	}
	/**
	 * Tries to load the resource form the internal path. If that fails it will try the external path.
	 * 
	 * @return an InputStream to read the file from or null if the file could not be found.
	 */
	public static InputStream getResourceAsStream(String path) {
		FileHandle fh = getResource(path);
		if (fh != null) {
			return getResource(path).read();
		} else {
			try {
				return getResourceAsURL(path).openStream();
			} catch (IOException e) {}
		}
		return null;
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private static URL tryClassPath(String ref) {
		String cpRef = ref.replace('\\', '/');
		return ResourceLoader.class.getClassLoader().getResource(cpRef);
	}
	public static URL tryFileSystem(String ref) {
		try {
			File file = new File(root, ref);
			if (!file.exists()) {
				file = new File(ref);
			}
			if (!file.exists()) {
				return null;
			}
			return file.toURI().toURL();
		} catch (IOException e) {
			return null;
		}
	}
	
}
