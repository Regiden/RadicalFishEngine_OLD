package de.radicalfish.extern;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_FRAMEBUFFER_COMPLETE_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_FRAMEBUFFER_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glBindFramebufferEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glCheckFramebufferStatusEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glDeleteFramebuffersEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glFramebufferTexture2DEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glGenFramebuffersEXT;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRANSFORM_BIT;
import static org.lwjgl.opengl.GL11.GL_VIEWPORT_BIT;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopAttrib;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushAttrib;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glViewport;
import org.lwjgl.opengl.GLContext;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.renderer.Renderer;

/**
 * @author davedes
 */
public class FBO {
	
	public static boolean isSupported() {
		return GLContext.getCapabilities().GL_EXT_framebuffer_object;
	}
	
	/** Our render texture. */
	private Texture texture;
	/** The ID of the FBO in use */
	private int id;
	private int width, height;
	private int pushAttrib = GL_VIEWPORT_BIT | GL_TRANSFORM_BIT | GL_COLOR_BUFFER_BIT;
	
	private boolean inUse = false;
	
	public static final int NO_BITS = 0;
	
	public FBO(Texture texture) throws SlickException {
		if (!isSupported()) {
			throw new SlickException("Your OpenGL card does not support FBO and hence can't handle " +
					"the dynamic images required for this application.");
		}
		this.texture = texture;
		this.width = texture.getImageWidth();
		this.height = texture.getImageHeight();
		try {
			texture.bind();
			id = glGenFramebuffersEXT();
			glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, id);
			glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT,
								 	  GL_TEXTURE_2D, texture.getTextureID(), 0);
			completeCheck();
			glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
		} catch (SlickException ex) {
			throw ex;
		} catch (Exception e) {
			throw new SlickException("Failed to create new texture for FBO");
		}
	}
	
	public FBO(int width, int height) throws SlickException {
		this(TextureUtils.createEmptyTexture(width, height));
	}
	
	public void setPushAttrib(int attrib) {
		this.pushAttrib = attrib;
	}
	
	public int getPushAttrib() {
		return pushAttrib;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getID() {
		return id;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	/**
	 * Check the FBO for completeness as shown in the LWJGL tutorial
	 * 
	 * @throws SlickException Indicates an incomplete FBO
	 */
	private void completeCheck() throws SlickException {
		int framebuffer = glCheckFramebufferStatusEXT(GL_FRAMEBUFFER_EXT); 
		switch ( framebuffer ) {
			case GL_FRAMEBUFFER_COMPLETE_EXT:
				break;
			case GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT:
				throw new SlickException( "FrameBuffer: " + id
						+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT exception" );
			case GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT:
				throw new SlickException( "FrameBuffer: " + id
						+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT exception" );
			case GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT:
				throw new SlickException( "FrameBuffer: " + id
						+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT exception" );
			case GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT:
				throw new SlickException( "FrameBuffer: " + id
						+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT exception" );
			case GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT:
				throw new SlickException( "FrameBuffer: " + id
						+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT exception" );
			case GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT:
				throw new SlickException( "FrameBuffer: " + id
						+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT exception" );
			default:
				throw new SlickException( "Unexpected reply from glCheckFramebufferStatusEXT: " + framebuffer);
		}
	}
	
	/**
	 * Bind to the FBO created
	 */
	public void bind() {
		if (id == 0)
			throw new IllegalStateException("can't use FBO as it has been destroyed..");
		if (pushAttrib != NO_BITS)
			glPushAttrib(pushAttrib);
		
		glViewport(0, 0, width, height);
	    glMatrixMode(GL_PROJECTION);
		glPushMatrix();
	    glLoadIdentity();
	    glOrtho(0, width, 0, height, 1, -1);
	    glMatrixMode(GL_MODELVIEW);
		glPushMatrix();
	    glLoadIdentity();
		
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, id);
		
		inUse = true;
	}
	
	/**
	 * Unbind from the FBO created
	 */
	public void unbind() {
		if (id==0)
			return;
		Renderer.get().flush();
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
		
		glMatrixMode(GL_PROJECTION);
		glPopMatrix();
		glMatrixMode(GL_MODELVIEW);
		glPopMatrix();
		if (pushAttrib != NO_BITS) 
			glPopAttrib();
		
		inUse = false;
	}
	
	/**
	 * @see org.newdawn.slick.Graphics#destroy()
	 */
	public void release() {
		glDeleteFramebuffersEXT(id);
		id = 0;
	}

	/**
	 * @return true if the FBO is currently bing used.
	 */
	public boolean isInUse() {
		return inUse;
	}
}