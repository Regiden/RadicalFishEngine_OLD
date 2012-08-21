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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import de.radicalfish.tests.GameTest;
import de.radicalfish.tests.ParticleTest;

/**
 * Methods to get all the tests.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 21.08.2012
 */
public class RadicalFishTests {
	public static final Class<?>[] classes = new Class[] { GameTest.class, ParticleTest.class };
	
	public static String[] getNames() {
		List<String> names = new ArrayList<String>();
		for (Class<?> clazz : classes)
			names.add(clazz.getSimpleName());
		Collections.sort(names);
		return names.toArray(new String[names.size()]);
	}
	
	public static RadicalFishTest newTest(String testName) {
		try {
			Class<?> clazz = Class.forName("de.radicalfish.tests." + testName);
			return (RadicalFishTest) clazz.newInstance();
		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		}
	}
	
}
