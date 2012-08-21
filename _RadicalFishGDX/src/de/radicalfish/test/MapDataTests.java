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
package de.radicalfish.test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import de.radicalfish.util.MathUtil;

/**
 * Test for writing a map and reading it.
 * @author Stefan Lange
 * @version 0.0.0
 * @since 14.06.2012
 */
public class MapDataTests {
	
	public static void main(String[] args) throws Exception {
		new MapDataTests();
	}
	
	private class Map {
		String name;
		int[][] data;
		
		public Map(int width, int height, String name) {
			this.name = name;
			data = new int[width][height];
		}
		
		// for loading
		public Map() {
			
		}
		
		public void fillRandom(int min, int max) {
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[0].length; j++) {
					data[i][j] = MathUtil.random(min, max);
				}
			}
		}
		public void printArray() {
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[0].length; j++) {
					System.out.print(data[i][j] + ", ");
				}
				System.out.println();
			}
		}
		
	}
	
	public MapDataTests() throws Exception {
		Map map = new Map(10, 5, "TEST MAP");
		map.fillRandom(0, 200);
		map.printArray();
		
		saveMap(map, "test.data");
		
		loadMap("test.data", new Map());
		
	}
	private void saveMap(Map map, String path) throws Exception {
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(path));
		
		dos.write(map.name.getBytes().length);
		dos.write(map.name.getBytes());
		
		dos.writeInt(map.data.length);
		dos.writeInt(map.data[0].length);
		for (int i = 0; i < map.data.length; i++) {
			for (int j = 0; j < map.data[0].length; j++) {
				dos.writeInt(map.data[i][j]);
			}
		}
		
	}
	private Map loadMap(String path, Map map) throws Exception {
		if(map == null) {
			map = new Map();
		}
		
		String name;
		int width, height;
		int data[][];
		
		DataInputStream dis = new DataInputStream(new FileInputStream(path));
		
		byte[] bytes = new byte[dis.read()];
		dis.read(bytes);
		name = new String(bytes);
		
		// data
		width = dis.readInt();
		height = dis.readInt();
		
		data = new int[width][height];
		
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				data[i][j] = dis.readInt();
			}
		}
		
		map.name = name;
		map.data = data;
		
		dis.close();
		
		return map;
	}
	
}
