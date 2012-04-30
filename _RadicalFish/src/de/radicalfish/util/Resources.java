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
package de.radicalfish.util;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.util.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This class is responsible for the image loading in any way. Best use this to load all the data in one step and use
 * the get methods to obtain a reference to an image or a sound file. Music files are not included as well as animations
 * (Music = only one channel, pulling a new file means deleting the old | Animations: have a SpriteSheet as backend
 * which can be loaded trough this loader)
 * 
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 16.11.2011
 */
public final class Resources {
	
	/**
	 * The singleton instance of this class.
	 */
	public static final Resources INSTANCE = new Resources();
	
	private HashMap<String, Image> images;
	private HashMap<String, SpriteSheet> sheets;
	private HashMap<String, Sound> sounds;
	
	private boolean initiated = false;
	
	// PRIVATE C'TOR AND INIT
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private Resources() {
	}
	private void init() {
		if (initiated)
			return;
		images = new HashMap<String, Image>();
		sheets = new HashMap<String, SpriteSheet>();
		sounds = new HashMap<String, Sound>();
		
		initiated = true;
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Loads all files from a xml file with a certain format.
	 * 
	 * @param filePath
	 *            the path to the xml file
	 * @param deffered
	 *            true if the resources should be loaded deferred
	 */
	public void LoadAll(String filePath, boolean deffered) {
		init();
		if (deffered)
			LoadingList.setDeferredLoading(true);
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setEntityResolver(new EntityResolver() {
				public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
					return new InputSource(new ByteArrayInputStream(new byte[0]));
				}
			});
			
			Document doc = builder.parse(ResourceLoader.getResourceAsStream(filePath));
			Element docElement = doc.getDocumentElement();
			Element images = (Element) docElement.getElementsByTagName("images").item(0);
			Element sheets = (Element) docElement.getElementsByTagName("spritesheets").item(0);
			Element sounds = (Element) docElement.getElementsByTagName("sounds").item(0);
			
			// images
			if (images != null) {
				NodeList list = images.getElementsByTagName("image");
				for (int i = 0; i < list.getLength(); i++) {
					Element e = (Element) list.item(i);
					loadImage(e.getAttribute("name"), e.getAttribute("path"));
				}
			}
			if (sheets != null) {
				NodeList list = sheets.getElementsByTagName("image");
				for (int i = 0; i < list.getLength(); i++) {
					Element e = (Element) list.item(i);
					loadSpriteSheet(e.getAttribute("name"), e.getAttribute("path"), Integer.parseInt(e.getAttribute("tw")),
							Integer.parseInt(e.getAttribute("th")));
				}
			}
			if (sounds != null) {
				NodeList list = sounds.getElementsByTagName("sound");
				for (int i = 0; i < list.getLength(); i++) {
					Element e = (Element) list.item(i);
					loadSound(e.getAttribute("name"), e.getAttribute("path"));
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Loads an {@link Image} and puts it into a {@link HashMap}. If the key already exits the image with the assigned
	 * key will be returned.
	 * 
	 * @param key
	 *            the key to set for this image
	 * @param path
	 *            the path to the image
	 * @return the image we just loaded
	 * @throws SlickException
	 */
	public Image loadImage(String key, String path) throws SlickException {
		init();
		if (images.containsKey(key))
			return images.get(key);
		Image image = new Image(path, false, Image.FILTER_NEAREST);
		images.put(key, image);
		return image;
	}
	/**
	 * Loads a sub-class of image into the {@link HashMap}. If the key already exits the image with the assigned key
	 * will be returned as
	 * 
	 * @param key
	 *            the key to set for this image
	 * @param image
	 *            the sub-class image to add
	 * @return the image we just loaded or the image assigned to the key (casted to the sub-class)
	 */
	@SuppressWarnings("unchecked")
	public <T extends Image> T loadImageAs(String key, Image image) {
		init();
		if (images.containsKey(key))
			return (T) images.get(key); // need SuppressWarnings (it is save due to the extends but java does not see
										// this at runtime ;)
		images.put(key, image);
		return (T) image; // same as above :)
	}
	/**
	 * @param key
	 *            the key of the image
	 * @return a reference of the image specified by <code>key</code> or null if the key does not exists in the map
	 */
	public Image getImage(String key) {
		init();
		return images.get(key);
	}
	/**
	 * @param key
	 *            the key of the image
	 * @return a reference of the image (casted to left handed type) specified by <code>key</code> or null if the key
	 *         does not exists in the map
	 */
	@SuppressWarnings("unchecked")
	public <T extends Image> T getImageAs(String key) {
		init();
		return (T) images.get(key);
	}
	/**
	 * Deletes an image from the map an frees all native resources.
	 * 
	 * @param key
	 *            he key of the image
	 * @throws SlickException
	 */
	public void destoryImage(String key) throws SlickException {
		destroyImage(key, true);
	}
	/**
	 * Deletes an image from the map an frees all native resources if <code>freeResource</code> is true (Useful on
	 * mobile devices). Does nothing if no image was assigned to <code>key</code>
	 * 
	 * @param key
	 *            the key of the image
	 * @param freeResources
	 *            true if we want to free all native resources too, false otherwise
	 * @throws SlickException
	 */
	public void destroyImage(String key, boolean freeResources) throws SlickException {
		init();
		if (!images.containsKey(key))
			return;
		
		if (freeResources)
			images.remove(key).destroy();
		else
			images.remove(key);
	}
	
	/**
	 * Loads a {@link SpriteSheet} and puts it into a {@link HashMap}. If the key already exits the spritesheet with the
	 * assigned key will be returned.
	 * 
	 * @param key
	 *            the key to set for this spritesheet
	 * @param path
	 *            the path to the image
	 * @param tileWidth
	 *            the width of each tile in the sheet
	 * @param tileHeight
	 *            the height of each tile int the sheet
	 * @return the spritesheet we just loaded
	 * @throws SlickException
	 */
	public SpriteSheet loadSpriteSheet(String key, String path, int tileWidth, int tileHeight) throws SlickException {
		init();
		if (sheets.containsKey(key))
			return sheets.get(key);
		SpriteSheet sheet = new SpriteSheet(path, tileWidth, tileHeight);
		sheets.put(key, sheet);
		return sheet;
	}
	/**
	 * @param key
	 *            the key of the spritesheet
	 * @return a reference of the spritesheet specified by <code>key</code> or null if the key does not exists in the
	 *         map
	 */
	public SpriteSheet getSpriteSheet(String key) {
		init();
		return sheets.get(key);
	}
	/**
	 * Deletes a spritesheet from the map an frees all native resources.
	 * 
	 * @param key
	 *            he key of the image
	 * @throws SlickException
	 */
	public void destorySpriteSheet(String key) throws SlickException {
		destroyImage(key, true);
	}
	/**
	 * Deletes a spritesheet from the map an frees all native resources if <code>freeResource</code> is true (Useful on
	 * mobile devices). Does nothing if no spritesheet was assigned to <code>key</code>
	 * 
	 * @param key
	 *            the key of the image
	 * @param freeResources
	 *            true if we want to free all native resources too, false otherwise
	 * @throws SlickException
	 */
	public void destroySpriteSheet(String key, boolean freeResources) throws SlickException {
		init();
		if (!sheets.containsKey(key))
			return;
		
		if (freeResources)
			sheets.remove(key).destroy();
		else
			sheets.remove(key);
	}
	
	/**
	 * Loads a {@link Sound} and puts it into a {@link HashMap}. If the key already exits the sound with the assigned
	 * key will be returned.
	 * 
	 * @param key
	 *            the key to set for this sound
	 * @param path
	 *            the path to the sound
	 * @return the sound we just loaded
	 * @throws SlickException
	 *             if the map already contains an sound with <code>key</code> or sound loading failed
	 */
	public Sound loadSound(String key, String path) throws SlickException {
		init();
		if (sounds.containsKey(key))
			return sounds.get(key);
		Sound sound = new Sound(path);
		sounds.put(key, sound);
		return sound;
	}
	/**
	 * @param key
	 *            the key of the sound
	 * @return a reference of the sound specified by <code>key</code> or null if the key does not exists in the map
	 */
	public Sound getSound(String key) {
		init();
		return sounds.get(key);
	}
	/**
	 * Deletes a sound from the map.
	 * 
	 * @param key
	 *            he key of the sound
	 */
	public void destorySound(String key) {
		init();
		if (!sounds.containsKey(key))
			return;
		sounds.remove(key);
	}
}
