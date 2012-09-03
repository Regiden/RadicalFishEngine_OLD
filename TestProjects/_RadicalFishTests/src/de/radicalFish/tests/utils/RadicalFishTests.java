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
package de.radicalfish.tests.utils;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Methods to get all the tests. The class will go trough all packages starting from the de.radicalfish.test package. It
 * loads all classes which implement the {@link RadicalFishTest} interface or are a sub class of {@link SimpleTest}.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 21.08.2012
 */
public class RadicalFishTests {
	private static Class<?>[] classes;
	
	public static String[] getNames() throws ClassNotFoundException, IOException {
		if (classes == null) {
			classes = loadClasses("de.radicalfish.tests");
		}
		
		List<String> names = new ArrayList<String>();
		for (Class<?> clazz : classes)
			names.add(clazz.getSimpleName());
		Collections.sort(names);
		return names.toArray(new String[names.size()]);
	}
	public static RadicalFishTest newTest(String testName) {
		try {
			if (classes == null) {
				classes = loadClasses("de.radicalfish.tests");
			}
			for (Class<?> temp : classes) {
				if(temp.getSimpleName().equals(testName)) {
					return (RadicalFishTest) temp.newInstance();
				}
			}
			return null;
		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		}
	}
	
	private static Class<?>[] loadClasses(String packageName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		return classes.toArray(new Class[classes.size()]);
	}
	private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		Class<?> clazz = null;
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				clazz = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
				if (isValid(clazz, clazz.getInterfaces())) {
					classes.add(clazz);
				}
				
			}
		}
		return classes;
	}
	private static boolean isValid(Class<?> base, Class<?>[] classes) {
		if (base.equals(SimpleTest.class)) {
			return false;
		}
		if (base.getSuperclass() != null && base.getSuperclass().equals(SimpleTest.class)) {
			return true;
		}
		for (Class<?> c : base.getInterfaces()) {
			if (c.equals(RadicalFishTest.class)) {
				return true;
			}
		}
		return false;
	}
}
