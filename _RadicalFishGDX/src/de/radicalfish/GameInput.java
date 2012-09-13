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
package de.radicalfish;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Orientation;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.ObjectIntMap;

/**
 * Wraps the {@link Input} implementation to support Slick2D like keyPressed and keyDown methods. It effectively hides
 * away the {@link Input} class from libGDX so you always only ADD listener, never set one. Most of the docs a straight
 * copies from {@link Input} from libGDX. all credits goes to the creator of libGDX here :)
 * 
 * @author Stefan Lange
 * @version 0.1.1
 * @since 10.08.2012
 */
public class GameInput implements InputProcessor {
	
	private final static String[][] keynames = new String[256][2];
	private static int counter = 0;
	static {
		Field[] fields = Keys.class.getFields();
		try {
			for (Field field : fields) {
				if (Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers())
						&& Modifier.isFinal(field.getModifiers()) && field.getType().equals(int.class)) {
					
					int key = field.getInt(null);
					
					if (key >= 0) {
						
						for (int i = 0; i < 3; i++) {
							if (keynames[key][i] == null) {
								keynames[key][i] = field.getName();
								break;
							}
						}
					}
					counter++;
				}
			}
			
		} catch (Exception e) {}
	}
	private static final int keyCount = counter;
	
	private ObjectIntMap<String> mappings = new ObjectIntMap<String>();
	
	private Input input;
	private InputMultiplexer listener;
	
	private boolean[] pressed;
	private boolean[] buttons;
	
	public GameInput() {
		input = Gdx.input;
		listener = new InputMultiplexer();
		input.setInputProcessor(listener);
		
		pressed = new boolean[512];
		buttons = new boolean[3];
		listener.addProcessor(this);
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Updates the input. this clears the pressed states for this frame.
	 */
	public void update() {
		Arrays.fill(pressed, false);
		Arrays.fill(buttons, false);
	}
	
	/**
	 * Adds an {@link InputProcessor} to the {@link GameInput}.
	 */
	public void addInputProcessor(InputProcessor processor) {
		listener.addProcessor(processor);
	}
	/**
	 * Removes an {@link InputProcessor} for the {@link GameInput}.
	 */
	public void removeInputProcessor(InputProcessor processor) {
		listener.removeProcessor(processor);
	}
	
	/**
	 * @return true if the key in <code>keycode</code> was pressed.
	 */
	public boolean isKeyPressed(int keycode) {
		if (pressed[keycode]) {
			pressed[keycode] = false;
			return true;
		}
		
		return false;
	}
	/**
	 * @return true if the key in <code>keycode</code> is down.
	 */
	public boolean isKeyDown(int keycode) {
		return input.isKeyPressed(keycode);
	}
	/**
	 * @return true if the button in <code>button</code> is pressed.
	 */
	public boolean isButtonPressed(int button) {
		if (buttons[button]) {
			buttons[button] = false;
			return true;
		}
		return false;
	}
	/**
	 * @return true if the button in <code>button</code> is down.
	 */
	public boolean isButtonDown(int button) {
		return input.isButtonPressed(button);
	}
	
	/**
	 * Binds a key to a name. If the mapping already exists it will be overwritten. This works for keys, not for
	 * buttons!
	 * 
	 * @param name
	 *            the name to bind
	 * @param key
	 *            the key to bind with the <code>name</code>
	 */
	public void bindKey(String name, int key) {
		mappings.put(name, key);
	}
	/**
	 * Checks if the key mapped wit the <code>name</code>.
	 * 
	 * @return rue if the key in mapped by <code>name</code> was pressed.
	 */
	public boolean isKeyPressed(String name) {
		return isKeyPressed(mappings.get(name, 0));
	}
	/**
	 * Checks if the key mapped wit the <code>name</code>.
	 * 
	 * @return rue if the key in mapped by <code>name</code> was pressed.
	 */
	public boolean isKeyDown(String name) {
		return isKeyDown(mappings.get(name, 0));
	}
	/**
	 * Clears all bindings.
	 */
	public void clearBindings() {
		mappings.clear();
	}
	
	// INPUT CLASS METHOD
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return whether a new touch down event just occured.
	 */
	public boolean justTouched() {
		return input.justTouched();
	}
	/**
	 * System dependent method to input a string of text. A dialog box will be created with the given title and the
	 * given text as a message for the user. Once the dialog has been closed the provided {@link TextInputListener} will
	 * be called on the rendering thread.
	 * 
	 * @param listener
	 *            The TextInputListener.
	 * @param title
	 *            The title of the text input dialog.
	 * @param text
	 *            The message presented to the user.
	 */
	public void getTextInput(TextInputListener listener, String title, String text) {
		input.getTextInput(listener, title, text);
	}
	/**
	 * System dependent method to input a string of text. A dialog box will be created with the given title and the
	 * given text as a hint message for the user. Once the dialog has been closed the provided {@link TextInputListener}
	 * will be called on the rendering thread.
	 * 
	 * @param listener
	 *            The TextInputListener.
	 * @param title
	 *            The title of the text input dialog.
	 * @param placeholder
	 *            The placeholder text presented to the user.
	 */
	public void getPlaceholderTextInput(TextInputListener listener, String title, String placeholder) {
		input.getPlaceholderTextInput(listener, title, placeholder);
	}
	/**
	 * Sets the on-screen keyboard visible if available.
	 * 
	 * @param visible
	 *            visible or not
	 */
	public void setOnscreenKeyboardVisible(boolean visible) {
		input.setOnscreenKeyboardVisible(visible);
	}
	
	/**
	 * Vibrates for the given amount of time. Note that you'll need the permission
	 * <code> uses-permission android:name="android.permission.VIBRATE"</code> in your manifest file in order for this
	 * to work.
	 * 
	 * @param milliseconds
	 *            the number of milliseconds to vibrate.
	 */
	public void vibrate(int milliseconds) {
		input.vibrate(milliseconds);
	}
	/**
	 * Vibrate with a given pattern. Pass in an array of ints that are the times at which to turn on or off the
	 * vibrator. The first one is how long to wait before turning it on, and then after that it alternates. If you want
	 * to repeat, pass the index into the pattern at which to start the repeat.
	 * 
	 * @param pattern
	 *            an array of longs of times to turn the vibrator on or off.
	 * @param repeat
	 *            the index into pattern at which to repeat, or -1 if you don't want to repeat.
	 */
	public void vibrate(long[] pattern, int repeat) {
		input.vibrate(pattern, repeat);
	}
	/**
	 * Stops the vibrator
	 */
	public void cancelVibrate() {
		input.cancelVibrate();
	}
	
	// INTERFACE
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public boolean keyDown(int keycode) {
		pressed[keycode] = true;
		return false;
	}
	public boolean keyUp(int keycode) {
		return false;
	}
	public boolean keyTyped(char character) {
		return false;
	}
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		buttons[button] = true;
		return false;
	}
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}
	public boolean scrolled(int amount) {
		return false;
	}
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return whether the screen is currently touched.
	 */
	public boolean isTouched() {
		return input.isTouched();
	}
	/**
	 * Whether the screen is currently touched by the pointer with the given index. Pointers are indexed from 0 to n.
	 * The pointer id identifies the order in which the fingers went down on the screen, e.g. 0 is the first finger, 1
	 * is the second and so on. When two fingers are touched down and the first one is lifted the second one keeps its
	 * index. If another finger is placed on the touch screen the first free index will be used.
	 * 
	 * @param pointer
	 *            the pointer
	 * @return whether the screen is touched by the pointer
	 */
	public boolean isTouched(int pointer) {
		return input.isTouched(pointer);
	}
	/**
	 * @return whether the mouse cursor is catched.
	 */
	public boolean isCursorCatched() {
		return input.isCursorCatched();
	}
	/**
	 * Queries whether a {@link Peripheral} is currently available. In case of Android and the
	 * {@link Peripheral#HardwareKeyboard} this returns the whether the keyboard is currently slid out or not.
	 * 
	 * @param peripheral
	 *            the {@link Peripheral}
	 * @return whether the peripheral is available or not.
	 */
	public boolean isPeripheralAvailable(Peripheral peripheral) {
		return input.isPeripheralAvailable(peripheral);
	}
	
	/**
	 * @return The value of the accelerometer on its x-axis. ranges between [-10,10].
	 */
	public float getAccelerometerX() {
		return input.getAccelerometerX();
	}
	/**
	 * @return The value of the accelerometer on its y-axis. ranges between [-10,10].
	 */
	public float getAceelerometerY() {
		return input.getAccelerometerY();
	}
	/**
	 * @return The value of the accelerometer on its y-axis. ranges between [-10,10].
	 */
	public float getAceelerometerZ() {
		return input.getAccelerometerZ();
	}
	
	/**
	 * @return the last touch x coordinate in screen coordinates. The screen origin is the top left corner.
	 */
	public int getX() {
		return input.getX();
	}
	/**
	 * Returns the x coordinate in screen coordinates of the given pointer. Pointers are indexed from 0 to n. The
	 * pointer id identifies the order in which the fingers went down on the screen, e.g. 0 is the first finger, 1 is
	 * the second and so on. When two fingers are touched down and the first one is lifted the second one keeps its
	 * index. If another finger is placed on the touch screen the first free index will be used.
	 * 
	 * @return the x coordinate
	 */
	public int getX(int pointer) {
		return input.getX(pointer);
	}
	/**
	 * @return the different between the current pointer location and the last pointer location on the x-axis.
	 */
	public int getDeltaX() {
		return input.getDeltaX();
	}
	/**
	 * @return the different between the current pointer location and the last pointer location on the x-axis.
	 */
	public int getDeltaX(int pointer) {
		return input.getDeltaX(pointer);
	}
	
	/**
	 * @return the last touch y coordinate in screen coordinates. The screen origin is the top left corner.
	 */
	public int getY() {
		return input.getY();
	}
	/**
	 * Returns the y coordinate in screen coordinates of the given pointer. Pointers are indexed from 0 to n. The
	 * pointer id identifies the order in which the fingers went down on the screen, e.g. 0 is the first finger, 1 is
	 * the second and so on. When two fingers are touched down and the first one is lifted the second one keeps its
	 * index. If another finger is placed on the touch screen the first free index will be used.
	 * 
	 * @param pointer
	 *            the pointer id.
	 * @return the y coordinate
	 */
	public int getY(int pointer) {
		return input.getY(pointer);
	}
	/**
	 * @return the different between the current pointer location and the last pointer location on the y-axis.
	 */
	public int getDeltaY() {
		return input.getDeltaY();
	}
	/**
	 * @return the different between the current pointer location and the last pointer location on the y-axis.
	 */
	public int getDeltaY(int pointer) {
		return input.getDeltaY(pointer);
	}
	
	/**
	 * The azimuth is the angle of the device's orientation around the z-axis. The positive z-axis points towards the
	 * earths center.
	 * 
	 * @see <a
	 *      href="http://developer.android.com/reference/android/hardware/SensorManager.html#getRotationMatrix(float[], float[], float[], float[])">http://developer.android.com/reference/android/hardware/SensorManager.html#getRotationMatrix(float[], float[], float[], float[])</a>
	 * @return the azimuth in degrees
	 */
	public float getAzimuth() {
		return input.getAzimuth();
	}
	/**
	 * The pitch is the angle of the device's orientation around the x-axis. The positive x-axis roughly points to the
	 * west and is orthogonal to the z- and y-axis.
	 * 
	 * @see <a
	 *      href="http://developer.android.com/reference/android/hardware/SensorManager.html#getRotationMatrix(float[], float[], float[], float[])">http://developer.android.com/reference/android/hardware/SensorManager.html#getRotationMatrix(float[], float[], float[], float[])</a>
	 * @return the pitch in degrees
	 */
	public float getPitch() {
		return input.getPitch();
	}
	/**
	 * The roll is the angle of the device's orientation around the y-axis. The positive y-axis points to the magnetic
	 * north pole of the earth.
	 * 
	 * @see <a
	 *      href="http://developer.android.com/reference/android/hardware/SensorManager.html#getRotationMatrix(float[], float[], float[], float[])">http://developer.android.com/reference/android/hardware/SensorManager.html#getRotationMatrix(float[], float[], float[], float[])</a>
	 * @return the roll in degrees
	 */
	public float getRoll() {
		return input.getRoll();
	}
	
	/**
	 * Returns the rotation matrix describing the devices rotation as per <a href=
	 * "http://developer.android.com/reference/android/hardware/SensorManager.html#getRotationMatrix(float[], float[], float[], float[])"
	 * >SensorManager#getRotationMatrix(float[], float[], float[], float[])</a>. Does not manipulate the matrix if the
	 * platform does not have an accelerometer.
	 * 
	 * @param matrix
	 */
	public void getRotationMatrix(float[] matrix) {
		input.getRotationMatrix(matrix);
	}
	/**
	 * @return the rotation of the device with respect to its native orientation.
	 */
	public int getRotation() {
		return input.getRotation();
	}
	
	/**
	 * @return the time of the event currently reported to the {@link InputProcessor}.
	 */
	public long getCurrentEventTime() {
		return input.getCurrentEventTime();
	}
	
	/**
	 * @return the native orientation of the device.
	 */
	public Orientation getNativeOrientation() {
		return input.getNativeOrientation();
	}
	
	/**
	 * @return the {@link InputProcessor} at <code>index</code>.
	 */
	public InputProcessor getInputProcessor(int index) {
		return listener.getProcessors().get(index);
	}
	
	// SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Sets whether the BACK button on Android should be caught. This will prevent the app from being paused. Will have
	 * no effect on the desktop.
	 * 
	 * @param catchBack
	 *            whether to catch the back button
	 */
	public void setCatchBackKey(boolean catchBack) {
		input.setCatchBackKey(catchBack);
	}
	/**
	 * Sets whether the MENU button on Android should be caught. This will prevent the onscreen keyboard to show up.
	 * Will have no effect on the desktop.
	 * 
	 * @param catchMenu
	 *            whether to catch the back button
	 */
	public void setCatchMenuKey(boolean catchMenu) {
		input.setCatchMenuKey(catchMenu);
	}
	
	/**
	 * Only viable on the desktop. Will confine the mouse cursor location to the window and hide the mouse cursor.
	 * 
	 * @param catched
	 *            whether to catch or not to catch the mouse cursor
	 */
	public void setCursorCatched(boolean catched) {
		input.setCursorCatched(catched);
	}
	/**
	 * Only viable on the desktop. Will set the mouse cursor location to the given window coordinates (origin top-left
	 * corner).
	 * 
	 * @param x
	 *            the x-position
	 * @param y
	 *            the y-position
	 */
	public void setCursorPosition(int x, int y) {
		input.setCursorPosition(x, y);
	}
	
	// STATIC METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private static final String[] ANYKEY = new String[] { "ANYKEY" };
	
	/**
	 * @return the names of the keycode. Some keys have 2 names, the length of the array is always 2. (returns null if
	 *         the keycode is unnamed)
	 */
	public static String[] getKeyName(int keycode) {
		if (keycode == -1) {
			return ANYKEY;
		}
		return keynames[keycode];
	}
	/**
	 * @return the number of keys supported.
	 */
	public static int getKeys() {
		return keyCount;
	}
	
}
