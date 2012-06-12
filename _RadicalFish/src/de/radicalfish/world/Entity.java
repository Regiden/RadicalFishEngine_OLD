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
import org.newdawn.slick.geom.Vector2f;
import de.radicalfish.Grid;
import de.radicalfish.Rectangle2D;
import de.radicalfish.context.GameContext;

/**
 * An abstract basic Entity holding some default fields. Most field are public to ensure fast usage in sub-classes an
 * calculations.
 * <p>
 * it uses an offset position to set a real bounding box around the part of the entity that should be check.
 * <p>
 * For collision the method <code>getBounds</code> should be uses. best case it returns the position plus the offset and
 * the correct width and height to make collision as tight as possible.
 * 
 * 
 * @author Stefan Lange
 * @version 0.6.0
 * @since 03.06.2012
 */
public abstract class Entity implements Serializable {
	
	private static final long serialVersionUID = 100L;
	
	// used for collision
	private static final Rectangle2D BOUNDS = new Rectangle2D();
	
	private Color flashColor = new Color(1f, 1f, 1f, 1f);
	
	protected Vector2f position = new Vector2f();
	protected Vector2f velocity = new Vector2f();
	protected Vector2f acceleration = new Vector2f(1, 1);
	protected Vector2f old = new Vector2f();
	protected Vector2f screen = new Vector2f();
	protected Vector2f offset = new Vector2f(); // starting from the top-left of the collision box
	protected Vector2f offscreen = new Vector2f(200, 200);
	
	private Grid grid = new Grid();
	
	protected Animator animator = new Animator();
	
	protected String name = "base-entity";
	
	private float ySortOffset = 0;
	
	private float flashValue = 0;
	private int flashTimer = 0, flashStartTime = 0;
	
	protected boolean active = true;
	protected boolean visible = true;
	protected boolean flash = false;
	
	/**
	 * Creates a new basic Entity.
	 */
	public Entity() {
		if (this.getClass() != null) {
			name = this.getClass().getSimpleName().toLowerCase(Locale.ENGLISH);
		}
	}
	
	// GAME METHODS
	// ��������������������������������������������������������������������������������������������
	/**
	 * Can be called by an entity system to load files and such. Also this should be used to load the animations into the animator via
	 * <code>animator.loadAnimations()</code>.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param world
	 *            the world in which the entity lives
	 */
	protected void init(GameContext context, World world) {}
	/**
	 * Updates the entity. this should be called instead of <code>doUpdate</code>. <code>doUpdate</code> gets called by
	 * this method after automatically checking if the entity needs an update. If this is not wanted this method can be
	 * ignored an the <code>doUpdate</code> method can be used directly.
	 * This also updates the {@link Animator} used for this entity.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param world
	 *            the world in which the entity lives
	 * @param delta
	 *            the time since the last update.
	 */
	public final void update(GameContext context, World world, float delta) {
		if (isActive()) {
			old.set(position);
			
			animator.update(delta);
			updateFlashTimer(delta);
			doUpdate(context, world, delta);
			
			calculateScreenPosition(world.getCamera().getCurrent());
			calculateGridPosition(world.getTileSize());
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
	 *            the time since the last update.
	 */
	public abstract void doUpdate(GameContext context, World world, float delta);
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
	public final void render(GameContext context, World world, Graphics g) {
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
	public abstract void doRender(GameContext context, World world, Graphics g);
	/**
	 * Can be called by an entity system if an entity is removed from the system and should release all it resources.
	 * 
	 * @param context
	 *            the context the game runs in
	 * @param world
	 *            the world in which the entity lives
	 */
	protected void destroy(GameContext context, World world) {}
	
	// COLLISION
	// ��������������������������������������������������������������������������������������������
	/**
	 * Can be called from an entity system if an entity collides with another.
	 * 
	 * @param entity
	 *            the entity this entity collided with if any.
	 */
	protected void onCollision(Entity entity) {}
	/**
	 * Can be called from an entity system if an entity collides with something on the map, like a solid block
	 * 
	 * @param tileID
	 *            the id of the collision value. e.g. 0 means air while 1 means solid block and 2 water and so on
	 * 
	 */
	protected void onMapCollision(int tileID, int tileX, int tileY) {}
	
	/**
	 * This should return the rectangle used to collision.
	 * 
	 * @param rect
	 *            the {@link Rectangle2D} to put the bounds into
	 * @return the <code>rect</code> it was not null or a new Rectangle if it was null.
	 */
	public abstract int getBounds(Rectangle2D rect);
	/**
	 * @return true if the point container/touches with this entity. Note this method uses the <code>getBounds()</code>
	 *         method by default.
	 */
	public boolean touches(float x, float y) {
		getBounds(BOUNDS);
		return BOUNDS.contains(x, y) && !BOUNDS.isEmpty();
	}
	
	// PROTECTED
	// ��������������������������������������������������������������������������������������������
	/**
	 * Renders the current sprite as flash effect. Note that isFlash must return true in order for this method to work.
	 * 
	 * @param image
	 *            the sprite to render
	 */
	protected void renderFlash(Image image, float x, float y) {
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
	protected void calculateScreenPosition(Vector2f cameraPosition) {
		screen.x = (int) (Math.max(position.x - cameraPosition.x, 0));
		screen.y = (int) (Math.max(position.y - cameraPosition.y, 0));
	}
	/**
	 * Calculates the grid position of this entity.
	 * 
	 * @param tileSize
	 *            the size of one tile.
	 */
	protected void calculateGridPosition(int tileSize) {
		grid.set(position.x / tileSize, position.y / tileSize);
	}
	/**
	 * Checks if the entity is off screen based on the off screen test ranges.
	 * 
	 * @return true if the entity is off screen.
	 */
	protected boolean isOffscreen(GameContext context) {
		return screen.x <= -offscreen.x || screen.x >= context.getGameWidth() + offscreen.x || screen.y <= -offscreen.y
				|| screen.y >= context.getGameHeight() + offscreen.y;
		
	}
	
	// OTHER METHODS
	// ��������������������������������������������������������������������������������������������
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
	// ��������������������������������������������������������������������������������������������
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
	// ��������������������������������������������������������������������������������������������
	/**
	 * @return true if the entity is solid meaning it can collide with other solid objects
	 */
	public abstract boolean canCollide();
	/**
	 * @return the ordinal layer the entity is on.
	 */
	public abstract int getLayer();
	
	// GETTER POSITIONS
	// ��������������������������������������������������������������������������������������������
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
	public Vector2f getOffset() {
		return offset;
	}
	public Vector2f getScreenPosition() {
		return screen;
	}
	/**
	 * @return the position before the last update call. Meaning this position should always be one frame behind the
	 *         current one.
	 */
	public Vector2f getOldPosition() {
		return old;
	}
	public Vector2f getOffscreenRanges() {
		return offscreen;
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
	
	public Animator getAnimator() {
		return animator;
	}
	public float getYSortOffset() {
		return ySortOffset;
	}
	
	
	// GETTER OTHERS
	// ��������������������������������������������������������������������������������������������
	/**
	 * @return default is the class name with lower-case characters.
	 */
	public String getName() {
		return name;
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
	
	// SETTER POSITIONS
	// ��������������������������������������������������������������������������������������������
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
	// ��������������������������������������������������������������������������������������������
	public void setName(String name) {
		this.name = name;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
}