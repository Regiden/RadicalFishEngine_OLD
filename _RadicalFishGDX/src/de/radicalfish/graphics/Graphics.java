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
package de.radicalfish.graphics;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import de.radicalfish.GameContainer;
import de.radicalfish.GameContainer.VIEWTYPE;
import de.radicalfish.debug.Logger;
import de.radicalfish.util.RadicalFishException;

/**
 * A wrapper for translating the context, drawing sprites with the {@link SpriteBatch} and drawing primitives.
 * 
 * @author Stefan Lange
 * @version 0.8.0
 * @since 08.08.2012
 */
public class Graphics implements Disposable {
	
	// this pool should avoid allocating memory for stacking.
	private Pool<GraphicsTransform> pool = new Pool<GraphicsTransform>() {
		protected GraphicsTransform newObject() {
			return new GraphicsTransform();
		}
	};
	
	private SpriteBatch spriteBatch;
	private Color clearColor, shapeColor;
	private Texture texture;
	private Vector3 origin;
	
	private GraphicsContext gContext;
	private BlendMode blendMode;
	
	private Array<GraphicsTransform> transformStack = new Array<GraphicsTransform>();
	
	private float[] lineVerts = new float[4 * 5];
	private float linewidth = 1f;
	private float color = 0f;
	
	private int width, height;
	
	private boolean useGL20;
	private boolean wasAlphaMap = false, wasAlphaBlend = false;
	private boolean wasSubtract = false;
	
	public Graphics(int width, int height, boolean useGL20, int batchSize) {
		this.width = width;
		this.height = height;
		this.useGL20 = useGL20;
		
		gContext = new GraphicsContext(width, height);
		gContext.position.set(0, 0, 0);
		
		if (useGL20) {
			spriteBatch = new SpriteBatch(batchSize, SpriteBatch.createDefaultShader());
		} else {
			spriteBatch = new SpriteBatch(batchSize);
		}
		blendMode = BlendMode.NORMAL;
		
		// create a simple white pixel
		Pixmap white = new Pixmap(1, 1, Format.RGBA8888);
		white.setColor(1, 1, 1, 1);
		white.drawPixel(0, 0);
		texture = new Texture(white);
		white.dispose(); // we don't need data data so flush it down the memory toilet
		
		origin = new Vector3(gContext.position);
		clearColor = new Color(0, 0, 0, 1);
		shapeColor = new Color(1, 1, 1, 1);
		color = shapeColor.toFloatBits();
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Clears the color with the clearColor set
	 */
	public void clearScreen() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	}
	
	/**
	 * Resets the translation and only the translation!
	 */
	public void resetTranslate() {
		gContext.position.set(origin);
	}
	/**
	 * Translate the context to <code>x</code>, <code>y</code>.
	 */
	public void translate(float x, float y) {
		gContext.translate(-x, -y);
	}
	
	/**
	 * Resets the scale and only the scale!
	 */
	public void resetScale() {
		gContext.setScale(1f);
	}
	/**
	 * scales the context.
	 */
	public void scale(float scale) {
		gContext.scale(scale);
	}
	/**
	 * scales the context.
	 */
	public void scale(float x, float y) {
		gContext.scale(x, y);
	}
	
	/**
	 * Pushes the current transforms. This pushes the position, scale and origin.
	 */
	public void pushTransform() {
		GraphicsTransform newStack = pool.obtain();
		newStack.stackPos(gContext.position.x, gContext.position.y, gContext.position.z);
		newStack.stackOriginPos(origin.x, origin.y, origin.z);
		newStack.stackScale(getScaleX(), getScaleY());
		transformStack.add(newStack);
		
	}
	/**
	 * Pops the last transform which was pushed.
	 * 
	 * @param apply
	 *            true if the popped transform should be applied directly
	 * 
	 * @throws RadicalFishException
	 */
	public void popTransform(boolean apply) throws RadicalFishException {
		if (transformStack.size == 0) {
			throw new RadicalFishException("Pop failed! No Transform is in the stack (forgot a pushTransform()?)");
		}
		GraphicsTransform popTransform = transformStack.pop();
		origin.set(popTransform.oPosX, popTransform.oPosY, popTransform.oPosZ);
		setScale(popTransform.scaleX, popTransform.scaleY);
		gContext.position.set(popTransform.posX, popTransform.posY, popTransform.posZ);
		pool.free(popTransform);
		if(apply) {
			apply();
		}
	}
	
	/**
	 * Resets the translation and the scale.
	 * 
	 * @param apply
	 *            true if we want to apply the changes directly (on the batch too).
	 */
	public void resetTransform(boolean apply) {
		resetTranslate();
		resetScale();
		
		if (apply) {
			apply();
		}
		
	}
	/**
	 * Applies changes to both the context and the batch.
	 */
	public void apply() {
		applyContext();
		applyBatch();
	}
	/**
	 * Applies the Translations on the context.
	 */
	public void applyContext() {
		gContext.update();
	}
	/**
	 * Applies the context changes to the batch. This should be called as less as possible to avoid flushing the batch
	 * while batching.
	 */
	public void applyBatch() {
		spriteBatch.setProjectionMatrix(gContext.combined);
	}
	
	/**
	 * Clears the alpha map across the whole game size. This sets alpha to 0 everywhere, meaning in
	 * {@link BlendMode#ALPHA_BLEND} nothing will be drawn.
	 */
	public void clearAlphaMap() {
		BlendMode originalMode = blendMode;
		setBlendMode(BlendMode.ALPHA_MAP);
		Color c = getColor().tmp();
		setColor(0, 0, 0, 0);
		fillRect(0, 0, width, height);
		setColor(c);
		setBlendMode(originalMode);
	}
	
	/**
	 * This method is called by the {@link GameContainer} if the {@link VIEWTYPE} is FIX to avoid a stretched view. You
	 * can call it by your self if you want but it should be avoided.
	 */
	public void resize(int width, int height) {
		gContext.setToOrtho(width, height);
	}
	
	public void dispose() {
		texture.dispose();
		spriteBatch.dispose();
	}
	
	// METHODS PRIMITIVES
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Fills a rectangular area with the given parameters. This function uses a texture loaded from the engine. This can
	 * be used since it's slightly faster then {@link ShapeRenderer}.
	 * <p>
	 * You must <code>apply</code> all transformation for this too! also the {@link SpriteBatch} must be started!
	 */
	public void fillRect(float x, float y, float width, float height) {
		Color temp = spriteBatch.getColor();
		
		spriteBatch.setColor(shapeColor);
		spriteBatch.draw(texture, x, y, width, height);
		spriteBatch.setColor(temp);
	}
	/**
	 * draws a rectangular area with the given parameters. This function uses a texture loaded from the engine. This can
	 * be used since it's slightly faster then {@link ShapeRenderer}.
	 * <p>
	 * You must <code>apply</code> all transformation for this too! also the {@link SpriteBatch} must be started!
	 */
	public void drawRect(float x, float y, float width, float height) {
		spriteBatch.draw(texture, x, y, width, linewidth);
		spriteBatch.draw(texture, x + width, y, linewidth, height);
		spriteBatch.draw(texture, x + width, y + height, -width, linewidth);
		spriteBatch.draw(texture, x, y + height, linewidth, -height);
	}
	/**
	 * Draws a line from <code>x, y</code> to <code>x2, y2</code>. This function uses a texture loaded from the engine.
	 * This can be used since it's slightly faster then {@link ShapeRenderer}.
	 * <p>
	 * You must <code>apply</code> all transformation for this too! also the {@link SpriteBatch} must be started!
	 */
	public void drawLine(float x, float y, float x2, float y2) {
		// based on an idea from Matthias Mann (TWL)
		float dx = x2 - x;
		float dy = y2 - y;
		float l = (float) Math.sqrt(dx * dx + dy * dy) / (linewidth / 2f);
		dx /= l;
		dy /= l;
		
		vertexLine(0, x - dx + dy, y - dy - dx, color, 0, 0);
		vertexLine(5, (x - dx - dy), y - dy + dx, color, 1, 0);
		vertexLine(10, x2 + dx - dy, y2 + dy + dx, color, 1, 1);
		vertexLine(15, x2 + dx + dy, y2 + dy - dx, color, 0, 1);
		
		spriteBatch.draw(texture, lineVerts, 0, lineVerts.length);
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void vertexLine(int index, float x, float y, float color, float u, float v) {
		lineVerts[index] = x;
		lineVerts[index + 1] = y;
		lineVerts[index + 2] = color;
		lineVerts[index + 3] = u;
		lineVerts[index + 4] = v;
	}
	
	private void solveAlphaBlend(BlendMode mode) {
		if (blendMode == BlendMode.ALPHA_MAP) {
			if (!wasAlphaBlend) {
				spriteBatch.disableBlending();
				Gdx.gl.glColorMask(false, false, false, true);
				wasAlphaMap = true;
			}
		} else {
			if (wasAlphaMap) {
				spriteBatch.enableBlending();
				Gdx.gl.glColorMask(true, true, true, true);
				wasAlphaMap = false;
			}
		}
		if (blendMode == BlendMode.ALPHA_BLEND) {
			if (!wasAlphaBlend) {
				Gdx.gl.glColorMask(false, false, false, true);
				wasAlphaBlend = true;
			}
		} else {
			if (wasAlphaBlend) {
				Gdx.gl.glColorMask(true, true, true, true);
				wasAlphaBlend = false;
			}
		}
	}
	private void solveSubtractMode(BlendMode mode) {
		if (mode == BlendMode.SUB) {
			if (!wasSubtract) {
				Gdx.gl20.glBlendEquation(GL20.GL_FUNC_SUBTRACT);
				wasSubtract = true;
			}
		} else {
			if (wasSubtract) {
				Gdx.gl20.glBlendEquation(GL20.GL_FUNC_ADD);
				wasSubtract = false;
			}
		}
	}
	
	// SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Sets a custom {@link SpriteBatch}. Can be used to set your own batch. The batch will get automatically disposed
	 * on exit.
	 */
	public void setSpriteBatch(SpriteBatch spriteBatch) {
		this.spriteBatch = spriteBatch;
	}
	
	/**
	 * Sets the origin to translate if we reset the transform.
	 */
	public void setOrigin(float x, float y) {
		origin.set(x, y, 0);
	}
	
	/**
	 * Sets the color used for primitives.
	 * 
	 * @param shapeColor
	 *            the color to use, will not referenced
	 */
	public void setColor(Color shapeColor) {
		setColor(shapeColor.r, shapeColor.g, shapeColor.b, shapeColor.a);
	}
	/**
	 * Sets the color used for primitives.
	 * 
	 * @param r
	 *            the red component
	 * @param g
	 *            the green component
	 * @param b
	 *            the blue component
	 * @param a
	 *            the alpha component
	 */
	public void setColor(float r, float g, float b, float a) {
		shapeColor.set(r, g, b, a);
		color = shapeColor.toFloatBits();
	}
	/**
	 * Sets the color used for clearing the screen. (The color object will not be copied)
	 * 
	 * @param color
	 *            the color we want as clear color, will not referenced
	 */
	public void setClearColor(Color color) {
		setClearColor(color.r, color.g, color.b);
	}
	/**
	 * Sets the color used for clearing the screen.
	 * 
	 * @param r
	 *            the red component
	 * @param g
	 *            the green component
	 * @param b
	 *            the blue component
	 */
	public void setClearColor(float r, float g, float b) {
		clearColor.set(r, g, b, 1);
		Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, 1);
	}
	
	/**
	 * Sets the width of lines used by the primitives methods from this class.
	 */
	public void setLinesWidth(float width) {
		linewidth = width;
	}
	/**
	 * sets the scale of the context.
	 */
	public void setScale(float scale) {
		gContext.setScale(scale);
	}
	/**
	 * sets the scale of the context.
	 */
	public void setScale(float x, float y) {
		gContext.setScale(x, y);
	}
	
	/**
	 * Sets the {@link BlendMode} to use for drawing sprite via the {@link SpriteBatch}.
	 */
	public void setBlendMode(BlendMode mode) {
		if (blendMode == mode) {
			return;
		}
		if (blendMode == BlendMode.SUB && !useGL20) {
			Logger.warn("Can't set BlendMode.SUB because GL20 is not supported!");
			return;
		}
		
		blendMode = mode;
		solveAlphaBlend(mode);
		solveSubtractMode(mode);
		
		if (blendMode != BlendMode.ALPHA_MAP) {
			spriteBatch.setBlendFunction(mode.src(), mode.dst());
		}
		
	}
	/**
	 * Sets a custom blend mode to the {@link SpriteBatch}. Note that no checks are made here.
	 * 
	 * @param src
	 *            the source function
	 * @param dst
	 *            the destination function
	 */
	public void setBlendMode(int src, int dst) {
		spriteBatch.setBlendFunction(src, dst);
	}
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return the {@link SpriteBatch} used for batching.
	 */
	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}
	
	/**
	 * @return the origin of the {@link GraphicsContext}.
	 */
	public Vector3 getOrigin() {
		return origin;
	}
	
	/**
	 * @return the color used for clear the screen.
	 */
	public Color getClearColor() {
		return clearColor;
	}
	/**
	 * @return the color used for primitives.
	 */
	public Color getColor() {
		return shapeColor;
	}
	
	/**
	 * @return the width of the lines used by the primitives methods from this class.
	 */
	public float getLineWidth() {
		return linewidth;
	}
	/**
	 * @return the current x scale value.
	 */
	public float getScaleX() {
		return gContext.getScaleX();
	}
	/**
	 * @return the current y scale value.
	 */
	public float getScaleY() {
		return gContext.getScaleY();
	}
	
	// INTERN CLASSES
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private static class GraphicsTransform {
		
		float posX, posY, posZ;
		float scaleX, scaleY;
		float oPosX, oPosY, oPosZ;
		
		void stackPos(float posX, float posY, float posZ) {
			this.posX = posX;
			this.posY = posY;
			this.posZ = posZ;
		}
		void stackScale(float scaleX, float scaleY) {
			this.scaleX = scaleX;
			this.scaleY = scaleY;
		}
		void stackOriginPos(float oPosX, float oPosY, float oPosZ) {
			this.oPosX = oPosX;
			this.oPosY = oPosY;
			this.oPosZ = oPosZ;
		}
	}
}
