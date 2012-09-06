package de.radicalfish.external;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/*
 * TODO: make tests
 * <pre>
 * // create new mesh with desired maximum number of points
 * PathMesh mesh = new PathMesh( 500 );
 *
 * // StrokePoints are defined by an x,y position and a width
 * StrokePoint point = new StrokePoint( x, y, width );
 *
 * // add points when you want to
 * mesh.addPoint( point );
 *
 * // when you render, it will re-evaluate the point list
 * // if new points were added since previous render call
 * mesh.render( shader, GL_TRIANGLES );
 * </pre>
 */

/**
 * A simple attempt to have a mesh that represents a path with a certain thickness and color. The shader used must have
 * a uniform called u_color like so:
 * 
 * <pre>
 * VERTEX SHADER
 * =========================================================
 * attribute vec4 a_position;
 * attribute vec2 a_texCoord0;
 * 
 * varying vec2 v_texCoord;
 * varying vec4 v_color;
 * 
 * void main() {
 * 	v_texCoord = a_texCoord0;
 * 	v_color = u_color;
 * 	gl_Position = u_modelViewProject * a_position;
 * }
 * 
 * FRAGMENT SHADER
 * =========================================================
 * #ifdef GL_ES
 * 	precision lowp float;
 * #endif
 * 
 * uniform sampler2D u_diffuseMap;
 * 
 * varying vec2 v_texCoord;
 * varying vec4 v_color;
 * 
 * void main() {
 * 	gl_FragColor = v_color * texture2D( u_diffuseMap, v_texCoord );
 * }
 * </pre>
 * 
 * This shows a very simple shader you could use. There is currently now test but if someone makes one I will add to the
 * list of test.
 * 
 * @author gentlemandroid, Stefan Lange (refactoring)
 * @version 0.5.0
 * @since 05.09.2012
 */
public class PathMesh extends Mesh {
	
	private static final int SEG_VERT_SIZE = 16;
	private static final int SEG_IND_SIZE = 6;
	
	/** The Color used for the stroke. */
	public final Color strokeColor = new Color(1, 1, 1, 1);
	
	private Vector3 forward = new Vector3(0, 0, 1);
	private Vector3 p1 = new Vector3();
	private Vector3 p2 = new Vector3();
	private Vector3 p3 = new Vector3();
	private Vector3 n1 = new Vector3();
	private Vector3 n2 = new Vector3();
	
	private Array<Vector3> points;
	
	private int pointIndex = 0;
	private int vertIndex = 0;
	private int indexIndex = 0;
	private float uvOffset = 0;
	
	private float[] widths;
	
	private float[] verts;
	private short[] indices;
	
	private boolean eval = true;
	private boolean dirty = true;
	
	private boolean useLines = false;
	
	/**
	 * Creates a new {@link PathMesh} with a given <code>length</code>.
	 */
	public PathMesh(int length) {
		super(false, 4 * (2 + 2) * length, SEG_IND_SIZE * length, new VertexAttribute(Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE),
				new VertexAttribute(Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));
		
		verts = new float[getMaxVertices()];
		indices = new short[getMaxIndices()];
		indexIndex = 0;
		int offset = 0;
		int count = indices.length / 6;
		for (int i = 0; i < count; i++) {
			indices[indexIndex++] = (short) offset;
			indices[indexIndex++] = (short) (2 + offset);
			indices[indexIndex++] = (short) (1 + offset);
			indices[indexIndex++] = (short) offset;
			indices[indexIndex++] = (short) (3 + offset);
			indices[indexIndex++] = (short) (2 + offset);
			offset += 4;
		}
		
		points = new Array<Vector3>(length);
		for (int i = 0; i < length; i++) {
			points.add(new Vector3());
		}
		widths = new float[length];
	}
	
	// RENDER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void render(ShaderProgram shader, int primitiveType) {
		if (eval) {
			evalPoints();
			eval = false;
			dirty = true;
		}
		
		if (dirty) {
			setVertices(verts, 0, Math.min(vertIndex, getMaxVertices()));
			setIndices(indices, 0, Math.min(indexIndex, getMaxIndices()));
			dirty = false;
		}
		
		shader.setUniformf("u_color", strokeColor);
		super.render(shader, primitiveType);
	}
	/**
	 * see {@link Mesh#render(ShaderProgram, int)}.
	 * 
	 * @param shader
	 */
	public void render(ShaderProgram shader) {
		if (pointIndex > 2)
			render(shader, useLines ? GL20.GL_LINES : GL20.GL_TRIANGLES);
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Adds a point to the list
	 */
	public void addPoint(float x, float y, float width) {
		if (isFull())
			return;
		
		points.get(pointIndex).set(x, y, 0);
		// use of negative width means dot and not line
		widths[pointIndex] = width;
		
		pointIndex++;
		
		eval = true;
	}
	
	/**
	 * Increments the indices by the given <code>factor</code>.
	 */
	public void incIndices(int factor) {
		// pointIndex += factor;
		// if ( pointIndex < 0 ) pointIndex = 0;
		vertIndex += factor * SEG_VERT_SIZE;
		if (vertIndex < 0)
			vertIndex = 0;
		indexIndex += factor * SEG_IND_SIZE;
		if (indexIndex < 0)
			indexIndex = 0;
		dirty = true;
	}
	/**
	 * Increments the current index for the points.
	 */
	public void incPointIndex(int offset) {
		pointIndex += offset;
		if (pointIndex < 0)
			pointIndex = 0;
		eval = true;
	}
	/**
	 * Resets the indexes.
	 */
	public void reset() {
		pointIndex = vertIndex = indexIndex = 0;
		uvOffset = 0;
		dirty = true;
	}
	/**
	 * Evaluates all points (if new points are added this can be called. the {@link PathMesh#render(ShaderProgram, int)}
	 * will call this too!)
	 */
	public void evalPoints() {
		if (pointIndex < 2) {
			return;
		}
		
		int count = pointIndex;
		reset();
		// bounds.set( 0, 0, 0, 0 );
		
		float uvTop = 0;
		float uvBottom = 0.5f;
		for (int i = 0; i < count; i++) {
			
			p1.set(points.get(Math.max(0, pointIndex)));
			p2.set(points.get(Math.max(0, Math.min(count - 1, pointIndex + 1))));
			p3.set(points.get(Math.max(0, Math.min(count - 2, pointIndex + 2))));
			
			if (i == 0) {// <= 2 ) {
				n1.set(p2).sub(p1).crs(forward).nor();
				n2.set(p3).sub(p2).crs(forward).nor().add(n1).nor();
			} else if (i < count - 2) {
				n1.set(n2);
				n2.set(p3).sub(p2).crs(forward).nor().add(n1).nor();
			}
			
			float strokeWidth = widths[pointIndex];
			float lastWidth = widths[Math.min(count - 1, pointIndex + 1)];
			
			float dist = p2.cpy().sub(p1).len();
			float uvdist = 0.04f * dist;
			
			// cap ends
			if (i == 0) {
				uvOffset = 0;
				uvdist = 0.5f;
				uvTop = 0.5f;
				uvBottom = 1;
				Vector3 p = p2.tmp().sub(p1).nor().mul(0.5f * lastWidth);
				p1.sub(p);
			} else if (i == 1) {
				uvOffset = 0;
				uvdist = 0.5f;
				uvTop = 0;
				uvBottom = 0.5f;
			} else if (i == count - 3) {
				uvOffset = 0;
				uvdist = 0.5f;
			} else if (i == count - 2) {
				uvOffset = 0.5f;
				uvdist = 0.5f;
				uvTop = 0.5f;
				uvBottom = 1;
				Vector3 p = p2.tmp().sub(p1).nor().mul(0.5f * lastWidth);
				p2.add(p);
			}
			
			verts[vertIndex++] = p1.x - n1.x * strokeWidth;
			verts[vertIndex++] = p1.y - n1.y * strokeWidth;
			verts[vertIndex++] = uvOffset;
			verts[vertIndex++] = uvTop;
			
			verts[vertIndex++] = p2.x - n2.x * lastWidth;
			verts[vertIndex++] = p2.y - n2.y * lastWidth;
			verts[vertIndex++] = uvOffset + uvdist;
			verts[vertIndex++] = uvTop;
			
			verts[vertIndex++] = p2.x + n2.x * lastWidth;
			verts[vertIndex++] = p2.y + n2.y * lastWidth;
			verts[vertIndex++] = uvOffset + uvdist;
			verts[vertIndex++] = uvBottom;
			
			verts[vertIndex++] = p1.x + n1.x * strokeWidth;
			verts[vertIndex++] = p1.y + n1.y * strokeWidth;
			verts[vertIndex++] = uvOffset;
			verts[vertIndex++] = uvBottom;
			
			uvOffset += uvdist;
			
			indexIndex += 6;
			pointIndex++;
		}
	}
	
	// SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * Sets the color to use for the lines.
	 */
	public void setStrokeColor(Color color) {
		strokeColor.set(color);
	}
	/**
	 * Sets the color to use for the lines.
	 */
	public void setStrokeColor(float r, float g, float b, float a) {
		strokeColor.set(r, g, b, a);
	}
	/**
	 * True if the standard render method which only takes the shader as arguments should use line instead of triangle
	 * fans.
	 */
	public void setUseLines(boolean useLines) {
		this.useLines = useLines;
	}
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @return The color used for the lines
	 */
	public Color getStrokeColor() {
		return strokeColor;
	}
	/**
	 * @return True if the current index exceeds the number of points.
	 */
	public boolean isFull() {
		return pointIndex >= points.size;
	}
	/**
	 * @return True if we use lines for the standard render method which only takes the shader as arguments.
	 */
	public boolean isUseLines() {
		return useLines;
	}
}