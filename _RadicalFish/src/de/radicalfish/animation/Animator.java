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
package de.radicalfish.animation;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;
import org.xmlpull.v1.XmlPullParser;
import de.matthiasmann.twl.utils.TextUtil;
import de.matthiasmann.twl.utils.XMLParser;
import de.radicalfish.context.GameContext;
import de.radicalfish.context.Resources;
import de.radicalfish.debug.Logger;
import de.radicalfish.util.Utils;

/**
 * Handles animations for entities.
 * 
 * @author Stefan Lange
 * @version 0.5.0
 * @since 11.06.2012
 */
public class Animator implements Serializable {
	
	private static final long serialVersionUID = 8460918241921046816L;

	private static long time;
	
	private HashMap<String, Animation> animations;
	private Animation current;
	
	public Animator() {
		animations = new HashMap<String, Animation>();
		current = null;
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Updates the current animation if any. Checks the stack if any animation should be played after the current has
	 * been stopped.
	 * 
	 * @param delta
	 *            the time since the last update call.
	 */
	public void update(float delta) {
		if (current != null) {
			current.update(delta);
		}
	}
	/**
	 * Loads a set of animations from an XML file. the format of the XML must be as followed:
	 * 
	 * <pre>
	 * {@code <animations>}
	 * 	<b>// here you define the several animations you need with different parameters.
	 * 	// The sheet name can be one of the resource class. if no sheet was found then it will be loaded
	 * 	// but not added to the resource class. in this case the parameter "tw" and "th" must be set which sets 
	 * 	// the tiles width and height of the sheet. those parameters must be int.
	 * 	// Times should be an array which contains the time for each frame in milliseconds, separated by commas with no space.
	 * 	// For frames goes the same. Each value in the array (must be int) serves as index for the sheet.
	 * 	// 0 means the upper left, the first tile. 1 the right next to 0 and so on</b>
	 * 
	 * 	{@code <animation name="name" sheet="sheet name" times="100,100 frames="0,1" loops="true" pingpong="false" loopat=""/>
	 * 	<animation name="name" sheet="long path" tw="16" th="16" times="100,100,200,100 frames="0,1,3,4"/>
	 * ...
	 * </animations>
	 * }
	 * </pre>
	 * 
	 * Some parameters can be missing. Those are: loops, pingpong and loopat.
	 * <p>
	 * 
	 * @param context
	 *            the context the game runs in.
	 * @param pathXML
	 *            the path to the XML, can be in the jar or outside of it
	 * @param required
	 *            the list of required names used for animations. if some are missing an exception will be raised. can
	 *            be null to ignore it
	 * @throws SlickException
	 */
	public void loadAnimations(GameContext context, String pathXML, List<String> required) throws SlickException {
		try {
			if (context.getSettings().getProperty("debug.xmlanimations", false)) {
				time = System.nanoTime();
			}
			XMLParser xpp = new XMLParser(ResourceLoader.getResource(pathXML));
			
			xpp.ignoreOtherAttributes();
			xpp.nextTag();
			xpp.require(XmlPullParser.START_TAG, null, "animations");
			
			xpp.ignoreOtherAttributes();
			xpp.nextTag();
			while (!xpp.isEndTag()) {
				processElement(context.getResources(), xpp);
				xpp.ignoreOtherAttributes();
				xpp.nextTag();
			}
			
			// check if all animations that are required are there.
			if(required != null) {
				for(String name : required) {
					if(!animations.containsKey(name)) {
						throw new SlickException("required name not found int the animations list: " + name);
					}
				}
			}
			
			if (context.getSettings().getProperty("debug.xml_animations", false)) {
				Log.info("Animations were loaded from xml: " + pathXML);
				Logger.none("\t=> Time:   " + Utils.formatTime((System.nanoTime() - time)));
				Logger.none("\t=> Loaded: " + animations.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SlickException(e.getMessage(), e.getCause());
		}
	}
	
	/**
	 * Adds a new Animation to the Animator.
	 * 
	 * @param key
	 *            the name for the animation.
	 * @throws SlickException
	 */
	public void addAnimation(String key, Animation animation) throws SlickException {
		if (animations.containsKey(key)) {
			throw new SlickException("key already exits: " + key);
		}
		if (animation == null) {
			throw new NullPointerException("animation is null");
		}
		
		animations.put(key, animation);
		if (current == null) {
			current = animation;
		}
	}
	public void removeAnimation(String key) throws SlickException {
		if (!animations.containsKey(key)) {
			throw new SlickException("no such animation found: " + key);
		}
		
	}
	
	/**
	 * Plays a specific animation mapped by the animator. It will make the animation mapped by <code>key</code> the
	 * current animation. This calls start on the animation which means it restarts the animation.
	 * 
	 * @throws SlickException
	 */
	public void playAnimation(String key) throws SlickException {
		playAnimation(key, true);
	}
	/**
	 * Plays a specific animation mapped by the animator. It will make the animation mapped by <code>key</code> the
	 * current animation. It will also stop the current animation if any.
	 * 
	 * @param restart
	 *            false if you want the new animation to be resumed instead of restarted
	 * @throws SlickException
	 */
	public void playAnimation(String key, boolean restart) throws SlickException {
		if (!animations.containsKey(key)) {
			throw new SlickException("no such animation found: " + key);
		}
		
		if (current != null) {
			current.stop();
		}
		current = animations.get(key);
		if (restart) {
			current.start();
		} else {
			current.resume();
		}
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void processElement(Resources resources, XMLParser xpp) throws Exception {
		xpp.require(XmlPullParser.START_TAG, null, "animation");
		
		String name = xpp.getAttributeNotNull("name");
		
		// try to load sprite sheet
		String temp = xpp.getAttributeValue(null, "sheet");
		SpriteSheet sheet = resources.getSpriteSheet(temp);
		if (sheet == null) {
			// load it from scratch
			int tw = xpp.parseIntFromAttribute("tw");
			int th = xpp.parseIntFromAttribute("th");
			sheet = new SpriteSheet(temp, tw, th);
		}
		
		int[] times = TextUtil.parseIntArray(xpp.getAttributeNotNull("times"));
		int[] frames = TextUtil.parseIntArray(xpp.getAttributeNotNull("frames"));
		
		Utils.notNull("times array", times);
		Utils.notNull("frames array", frames);
		if (times.length != frames.length) {
			throw new SlickException("Mismatch in times and frames length! times: " + times.length + " | frames: " + frames.length);
		}
		
		// ignorable params
		boolean loops = xpp.parseBoolFromAttribute("loops", false);
		boolean pingPong = xpp.parseBoolFromAttribute("pingpong", false);
		int loopAt = xpp.parseIntFromAttribute("loopat", -1);
		
		Animation animation = new Animation();
		animation.setLoops(loops);
		animation.setPingPong(pingPong);
		if (loopAt >= 0) {
			animation.setLoopsAt(true, loopAt);
		}
		for (int i = 0, x = 0, y = 0; i < frames.length; i++) {
			// tileX = id % currentSheet.tilesAcross;
			// tileY = id / currentSheet.tilesDown;
			if (frames[i] < 0) {
				throw new SlickException("sheet index can not be below 0: " + frames[i]);
			}
			if (times[i] < 0) {
				throw new SlickException("time can not be below 0: " + times[i]);
			}
			
			x = frames[i] % sheet.getHorizontalCount();
			y = frames[i] / sheet.getVerticalCount();
			
			animation.addFrame(sheet.getSubImage(x, y), times[i]);
		}
		addAnimation(name, animation);
		
		// jump the end tag of animation!
		xpp.nextTag();
		xpp.require(XmlPullParser.END_TAG, null, "animation");
	}
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return a animation handle by the Animator. or null if no animations was mapped for the <code>key</code>.
	 */
	public Animation getAnimation(String key) {
		return animations.get(key);
	}
	/**
	 * @return the current used animation. can be null!
	 */
	public Animation getCurrentAnimation() {
		return current;
	}
	/**
	 * @return the current image (frame) in the current animation if any.
	 */
	public Image getCurrentImage() {
		if(current != null) {
			return current.getCurrentSprite();
		}
		return null;
	}
	
}
