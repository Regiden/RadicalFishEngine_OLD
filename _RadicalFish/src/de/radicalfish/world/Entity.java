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
package de.radicalfish.world;
import java.io.Serializable;
import java.util.Locale;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import de.radicalfish.Grid;
import de.radicalfish.Rectangle2D;
import de.radicalfish.animation.Animator;
import de.radicalfish.context.GameContext;
import de.radicalfish.context.GameDelta;

/**
 * An abstract basic Entity holding some default fields. Most field are public to ensure fast usage in sub-classes an
 * calculations.
 * <p>
 * it uses an offset position to set a real bounding box around the part of the entity that should be check.
 * <p>
 * For collision the method <code>getBounds</code> should be used. You can use the method
 * <code>calculateCollsionBox</code> to automatically make a the rectangle based on the postion, offset and the
 * width/height. Updates on the Animator and the flash effect will be made with the modified delta value.
 * 
 * 
 * @author Stefan Lange
 * @version 0.6.0
 * @since 03.06.2012
 */
public abstract class Entity implements Serializable {
	
	private static final long serialVersionUID = 100L;
	
	private static Image MISSINGSPRITE;
	private static final Rectangle2D BOUNDS = new Rectangle2D();
	
	private Color flashColor = new Color(1f, 1f, 1f, 1f);
	
	protected Vector2f position = new Vector2f();
	protected Vector2f velocity = new Vector2f();
	protected Vector2f acceleration = new Vector2f(1, 1);
	protected Vector2f old = new Vector2f();
	protected Vector2f screen = new Vector2f();
	protected Vector2f offset = new Vector2f(); // starting from the top-left of the collision box
	protected Vector2f offscreen = new Vector2f(200, 200);
	protected Vector2f direction = new Vector2f(0, 0);
	
	protected Rectangle2D collisionbox = new Rectangle2D();
	protected Grid grid = new Grid();
	
	protected Animator animator = new Animator();
	
	protected String name = "base-entity";
	protected int ID = -1;
	
	private float ySortOffset = 0;
	
	private float flashValue = 0;
	private int flashTimer = 0, flashStartTime = 0;
	
	protected boolean active = true;
	protected boolean visible = true;
	protected boolean flash = false;
	protected boolean alive = true;
	
	/**
	 * Creates a new basic Entity.
	 */
	public Entity() {
		if (this.getClass() != null) {
			name = this.getClass().getSimpleName().toLowerCase(Locale.ENGLISH);
		}
	}
	
	// STATIC METHODS
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	/**
	 * @return a default image with the size of 32x32. Useful for testing.
	 * @throws SlickException
	 */
	public static Image getMissingImage() throws SlickException {
		if (MISSINGSPRITE == null) {
			MISSINGSPRITE = new Image("de/radicalfish/assets/missingsprite.png", false, Image.FILTER_NEAREST);
		}
		return MISSINGSPRITE;
	}
	/**
	 * Checks if the given <code>image</code> is null and returns the missing image constant if so.
	 * 
	 * @param image
	 *            the Image to test
	 * @return the missing sprite if <code>image</code> is null, otherwise <code>image</code>.
	 * @throws SlickException
	 */
	public static Image checkMissing(Image image) throws SlickException {
		if (image == null) {
			return getMissingImage();
		} else {
			return image;
		}
	}
	
	// GAME METHODS
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	/**
	 * Can be called by an entity system to load images and such. Also this should be used to load the animations into
	 * the animator via <code>animator.loadAnimations()</code>.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param world
	 *            the world in which the entity lives
	 */
	public abstract void init(GameContext context, World world) throws SlickException;
	/**
	 * Updates the entity. this should be called instead of <code>doUpdate</code>. <code>doUpdate</code> gets called by
	 * this method after automatically checking if the entity needs an update. If this is not wanted this method can be
	 * ignored an the <code>doUpdate</code> method can be used directly. This also updates the {@link Animator} used for
	 * this entity and the direction in which the entity moved. After an update a collision manager should check
	 * collision on this entity.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param world
	 *            the world in which the entity lives
	 * @param delta
	 *            the {@link GameDelta} object containing the delta values.
	 */
	public final void update(GameContext context, World world, GameDelta delta) throws SlickException {
		if (isActive()) {
			old.set(position);
			
			animator.update(delta.getDelta());
			updateFlashTimer(delta.getDelta());
			doUpdate(context, world, delta);
			
			calculateScreenPosition(world.getCamera().getCurrent());
			calculateGridPosition(world.getTileSize());
			calculateDirection();
		}
	}
	/**
	 * Updates the implementation of this entity.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param world
	 *            the world in which the entity lives
	 * @param delta
	 *            tthe {@link GameDelta} object containing the delta values.
	 */
	public abstract void doUpdate(GameContext context, World world, GameDelta delta) throws SlickException;
	/**
	 * Renders the entity. this should be called instead of <code>doRender</code>. <code>doRender</code> gets called by
	 * this method after automatically checking if the entity needs to render. If this is not wanted this method can be
	 * ignored an the <code>doRender</code> method can be used directly.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param world
	 *            the world in which the entity lives
	 * @param g
	 *            the graphics context to draw to
	 */
	public final void render(GameContext context, World world, Graphics g) throws SlickException {
		if (isVisible()) {
			// check if we out of screen, if so no need to render
			if (!isOffscreen(context)) {
				doRender(context, world, g);
			}
		}
	}
	/**
	 * Renders this entity.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param world
	 *            the world in which the entity lives
	 * @param g
	 *            the graphics context to draw to
	 */
	public abstract void doRender(GameContext context, World world, Graphics g) throws SlickException;
	/**
	 * Can be called by an entity system if an entity is removed from the system and should release all it resources.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param world
	 *            the world in which the entity lives
	 */
	protected void destroy(GameContext context, World world) throws SlickException {}
	
	// COLLISION
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	/**
	 * @return the collision box used for collision.
	 */
	public Rectangle2D getCollisionBox() {
		return collisionbox;
	}
	/**
	 * Can be called from an entity system if an entity collides with another.
	 * 
	 * @param entity
	 *            the entity this entity collided with if any.
	 */
	public void onCollision(Entity entity) {}
	/**
	 * Can be called from an entity system if an entity collides with something on the map, like a solid block
	 * 
	 * @param tileID
	 *            the id of the collision value. e.g. 0 means air while 1 means solid block and 2 water and so on
	 * 
	 */
	public void onMapCollision(int tileID, int tileX, int tileY) {}
	
	/**
	 * @return true if the point container/touches with this entity. Note this method uses the <code>getBounds()</code>
	 *         method by default.
	 */
	public boolean touches(float x, float y) {
		calculateCollisionBox(BOUNDS);
		return BOUNDS.contains(x, y) && !BOUNDS.isEmpty();
	}
	
	// OVERRIDE
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	/**
	 * Renders the current sprite as flash effect. Note that isFlash must return true in order for this method to work.
	 * 
	 * @param image
	 *            the sprite to render
	 */
	public void renderFlash(Image image, float x, float y) {
		if (isFlash()) {
			flashColor.a = flashValue;
			image.drawFlash(x, x, image.getWidth(), image.getHeight(), flashColor);
		}
	}
	/**
	 * Calculates the screen position based on the camera position
	 * 
	 * @param cameraPosition
	 *            the current position of the camera in the world.
	 */
	public void calculateScreenPosition(Vector2f cameraPosition) {
		screen.x = (int) (position.x - cameraPosition.x);
		screen.y = (int) (position.y - cameraPosition.y);
	}
	/**
	 * Calculates the grid position of this entity.
	 * 
	 * @param tileSize
	 *            the size of one tile.
	 */
	public void calculateGridPosition(int tileSize) {
		grid.set(position.x / tileSize, position.y / tileSize);
	}
	/**
	 * Calculates the collision box by using the <code>getCollsionWidth()</code> and <code>getCollisionHeight</code>
	 * methods. it also automatically adds the offset to the position. the result will be put into the collision box by
	 * calling <code>setCollisionbox()</code>.
	 * <p>
	 * Calculation:
	 * 
	 * <pre>
	 *  rect = (position.x + offset.x, position.y + offset.y, getCollisionWidth, getCollisionHeight)
	 * </pre>
	 * 
	 * @param target
	 *            a target if needed to save memory
	 * @return <code>target</code> with the collsion values inside or a new Rectanlge2D if <code>target</code> is null.
	 */
	public Rectangle2D calculateCollisionBox(Rectangle2D target) {
		if (target == null) {
			target = new Rectangle2D(0, 0, 0, 0);
		}
		target.setBounds(position.x + offset.x, position.y + offset.y, getCollisionWidth(), getCollisionHeight());
		return target;
	}
	/**
	 * Calculates the direction in which the entity moved after an call to <code>update</code>. The direction is a
	 * normalized vector with the following meaning:
	 * <p>
	 * <li>x = -1 / 1: left / right</li>
	 * <li>y = -1 / 1: up / down</li>
	 * <p>
	 * 
	 * if x or y in the direction vector is 0, no movement happend.
	 * 
	 */
	public void calculateDirection() {
		direction.x = position.x - old.x;
		direction.y = position.y - old.y;
		direction.normalise();
	}
	/**
	 * Checks if the entity is off screen based on the off screen test ranges.
	 * 
	 * @return true if the entity is off screen.
	 */
	public boolean isOffscreen(GameContext context) {
		return screen.x <= -offscreen.x || screen.x >= context.getGameWidth() + offscreen.x || screen.y <= -offscreen.y
				|| screen.y >= context.getGameHeight() + offscreen.y;
		
	}
	
	// OTHER METHODS
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	/**
	 * Start the computation of the flash effect. use the method <code>drawFlash()</code> to draw the flash in your
	 * render implementation. This way you have full control over the flash and how it's renderer.
	 * 
	 * @param time
	 *            the time to flash. must be greater 0
	 * @param color
	 *            the color in which the entity should be flashed
	 */
	public void flashEntity(int time, Color color) {
		if (color == null)
			throw new NullPointerException("color is null");
		flashStartTime = time;
		flashTimer = time;
		flashValue = 1f;
		flashColor = color;
		flash = true;
	}
	
	// INTERN
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	private void updateFlashTimer(float delta) {
		if (flashTimer > 0) {
			flashValue = flashTimer / (float) flashStartTime;
		} else {
			flashTimer -= delta;
			if (flashTimer <= 0) {
				flashValue = 0;
				flash = false;
			}
		}
	}
	
	// ABSTRACT GETTER
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	/**
	 * @return true if the entity is solid meaning it can collide with other solid objects
	 */
	public abstract boolean canCollide();
	/**
	 * @return the ordinal layer the entity is on.
	 */
	public abstract int getLayer();
	
	/**
	 * @return the width of the collision box.
	 */
	public abstract int getCollisionWidth();
	/**
	 * @return the height of the collision box.
	 */
	public abstract int getCollisionHeight();
	
	// GETTER POSITIONS
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	public Grid getGridPosition() {
		return grid;
	}
	public Vector2f getPosition() {
		return position;
	}
	public Vector2f getVelocity() {
		return velocity;
	}
	public Vector2f getAcceleration() {
		return acceleration;
	}
	/**
	 * @return the offset used for collision.
	 */
	public Vector2f getOffset() {
		return offset;
	}
	public Vector2f getScreenPosition() {
		return screen;
	}
	/**
	 * @return the position set before <code>doUpdate</code> is called. Used for collision to detect the direction of
	 *         collision.
	 */
	public Vector2f getOldPosition() {
		return old;
	}
	public Vector2f getOffscreenRanges() {
		return offscreen;
	}
	/**
	 * The direction is a normalized vector with the following meaning:
	 * <p>
	 * <li>x = -1 / 1: left / right</li>
	 * <li>y = -1 / 1: up / down</li>
	 * <p>
	 * 
	 * if x or y in the direction vector is 0, no movement happened.
	 */
	public Vector2f getDirection() {
		return direction;
	}
	
	public float getPositionX() {
		return position.x;
	}
	public float getPositionY() {
		return position.y;
	}
	public float getVelocityX() {
		return velocity.x;
	}
	public float getVelocityY() {
		return velocity.y;
	}
	public float getAccelerationX() {
		return acceleration.x;
	}
	public float getAccelerationY() {
		return acceleration.y;
	}
	public float getOffsetX() {
		return offset.x;
	}
	public float getOffsetY() {
		return offset.y;
	}
	public float getScreenPositionX() {
		return screen.x;
	}
	public float getScreenPositionY() {
		return screen.y;
	}
	public float getOldPositionX() {
		return old.x;
	}
	public float getOldPositionY() {
		return old.y;
	}
	public float getDirectionX() {
		return direction.x;
	}
	public float getDirectionY() {
		return direction.y;
	}
	
	public float getYSortOffset() {
		return ySortOffset;
	}
	
	// GETTER OTHERS
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	public Animator getAnimator() {
		return animator;
	}
	
	/**
	 * @return default is the class name with lower-case characters.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the id of the entity if any, default is -1.
	 */
	public int getID() {
		return ID;
	}
	
	public boolean isActive() {
		return active;
	}
	public boolean isVisible() {
		return visible;
	}
	public boolean isFlash() {
		return flash;
	}
	/**
	 * @return false indicates that the EntitySystem should remove this entity from the list!
	 */
	public boolean isAlive() {
		return alive;
	}
	
	// SETTER POSITIONS
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	public void setGridPosition(int x, int y) {
		grid.set(x, y);
	}
	public void setPosition(float x, float y) {
		position.set(x, y);
	}
	public void setVelocity(float x, float y) {
		velocity.set(x, y);
	}
	public void setAcceleration(float x, float y) {
		acceleration.set(x, y);
	}
	public void setOffset(float x, float y) {
		offset.set(x, y);
	}
	public void setScreenPosition(float x, float y) {
		screen.set(x, y);
	}
	public void setOldPosition(float x, float y) {
		old.set(x, y);
	}
	/**
	 * Sets the min/max values to check if an entity is off screen and needs no render call.
	 * <p>
	 * Default values are 200, 200.
	 * 
	 * @param leftRight
	 *            the left and right values to test for. in pixels
	 * @param topBottom
	 *            the top and bottom values to test for. in pixels.
	 */
	public void setOffScreenRanges(float leftRight, float topBottom) {
		offscreen.set(leftRight, topBottom);
	}
	/**
	 * Sets the direction which the entity faces. This will be automatically set by the <code>update</code> method. You
	 * can use it if you use your own update loop.
	 */
	public void setDirection(float x, float y) {
		direction.set(x, y);
	}
	public void setPositionX(float x) {
		position.x = x;
	}
	public void setPositionY(float y) {
		position.y = y;
	}
	public void setVelocityX(float x) {
		velocity.x = x;
	}
	public void setVelocityY(float y) {
		velocity.y = y;
	}
	public void setAccelerationX(float x) {
		acceleration.x = x;
	}
	public void setAccelerationY(float y) {
		acceleration.y = y;
	}
	public void setOffsetX(float x) {
		offset.x = x;
	}
	public void setOffsetY(float y) {
		offset.y = y;
	}
	public void setScreenX(float x) {
		screen.x = x;
	}
	public void setSreenY(float y) {
		screen.y = y;
	}
	public void setOldPositionX(float x) {
		old.x = x;
	}
	public void setOldPositionY(float y) {
		old.y = y;
	}
	
	/**
	 * @param offset
	 *            an offset to take into account when sorting sprites
	 */
	public void setYSortOffset(float offset) {
		ySortOffset = offset;
	}
	
	// SETTER OTHERS
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	public void setName(String name) {
		this.name = name;
	}
	public void setID(int id) {
		ID = id;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
}
