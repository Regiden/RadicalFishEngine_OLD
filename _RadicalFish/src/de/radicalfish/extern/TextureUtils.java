package de.radicalfish.extern;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GLContext;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.ImageData;
import org.newdawn.slick.opengl.InternalTextureLoader;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;

/**
 * @author davedes
 */
public class TextureUtils {
	
	public static int toPowerOfTwo(int n) {
		return 1 << (32 - Integer.numberOfLeadingZeros(n - 1));
	}
	
	public static boolean isPowerOfTwo(int n) {
		return (n & -n) == n;
	}
	
	/**
	 * Returns true if non-power-of-two textures are supported in hardware via the GL_ARB_texture_non_power_of_two
	 * extension.
	 * 
	 * @return true if the extension is listed
	 */
	public static boolean isNPOTSupported() {
		// don't check GL20, nvidia/ATI usually don't advertise this extension
		// if it means requiring software fallback
		return GLContext.getCapabilities().GL_ARB_texture_non_power_of_two;
	}
	
	/**
	 * Slick uses GL30.glGenerateMipmap() or GL14.GL_GENERATE_MIPMAP to automatically build mipmaps (for advanced
	 * users). If neither of these versions are supported, this method returns false.
	 * 
	 * @return whether the version is >= 1.4
	 */
	public static boolean isGenerateMipmapSupported() {
		return GLContext.getCapabilities().OpenGL14;
	}
	
	public static TextureImpl createEmptyTexture(int width, int height) throws SlickException {
		return createEmptyTexture(width, height, ImageData.Format.RGBA);
	}
	
	/**
	 * Creates a NPOT texture with NEAREST filtering and a wrap mode of CLAMP_TO_EDGE (or CLAMP if GL < 1.2). This is
	 * for maximum compatibility with ATI drivers.
	 * 
	 * If NPOT is not supported, the resulting texture will be made to the next greatest power of two.
	 * 
	 * @param width
	 * @param height
	 * @return
	 */
	public static TextureImpl createEmptyTexture(int width, int height, ImageData.Format internalFormat) throws SlickException {
		SGL GL = Renderer.get();
		
		ByteBuffer buf = BufferUtils.createByteBuffer(width * height * 3);
		int fmt = SGL.GL_RGB;
		int id = InternalTextureLoader.createTextureID(); // increases texture count
		int target = SGL.GL_TEXTURE_2D;
		TextureImpl texture = new TextureImpl("generated:" + id + "," + width + "," + height, target, id);
		
		int texWidth = width;
		int texHeight = height;
		boolean usePOT = !isNPOTSupported();
		if (usePOT) {
			texWidth = toPowerOfTwo(width);
			texHeight = toPowerOfTwo(height);
		}
		
		texture.setTextureWidth(texWidth);
		texture.setTextureHeight(texHeight);
		texture.setWidth(width);
		texture.setHeight(height);
		texture.setImageFormat(internalFormat);
		
		GL.glBindTexture(target, id);
		
		int max = GL11.glGetInteger(SGL.GL_MAX_TEXTURE_SIZE);
		if (width > max || height > max)
			throw new SlickException("Attempt to allocate a texture to big for the current hardware");
		
		int filter = SGL.GL_LINEAR;
		GL.glTexParameteri(target, SGL.GL_TEXTURE_MIN_FILTER, filter);
		GL.glTexParameteri(target, SGL.GL_TEXTURE_MAG_FILTER, filter);
		
		int wrap = GLContext.getCapabilities().OpenGL12 ? GL12.GL_CLAMP_TO_EDGE : GL11.GL_CLAMP;
		GL11.glTexParameteri(target, GL11.GL_TEXTURE_WRAP_S, wrap);
		GL11.glTexParameteri(target, GL11.GL_TEXTURE_WRAP_T, wrap);
		
		// produce a texture from the byte buffer
		GL.glTexImage2D(target, 0, internalFormat.getOGLType(), texWidth, texHeight, 0, fmt, SGL.GL_UNSIGNED_BYTE, buf);
		return texture;
	}
}