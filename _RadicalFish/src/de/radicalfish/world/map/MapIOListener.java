package de.radicalfish.world.map;
import java.io.Serializable;
import org.newdawn.slick.SpriteSheet;
import de.radicalfish.context.Resources;

/**
 * A listener for loading maps that must be provided when loading map via {@link MapIO}. Current use is for loading the
 * SpriteSheet in a TileSet. Those should not be saved because there are not {@link Serializable}. On load the callback will be invoked for each
 * TileSet (on each Layer). This should be used to set the SpriteSheet.
 * 
 * @author Stefan Lange
 * @version 0.0.0
 * @since 04.07.2012
 */
public interface MapIOListener {
	
	/**
	 * Callback for a SpriteSheet that should be loaded for a Tileset. The returned SpriteSheet cannot be null. the
	 * parameter <code>type</code> is equal to the class name of the TilSet to the time it was saved via {@link MapIO}.
	 * 
	 * @param resourceName
	 *            the name of the resource in the {@link Resources} class
	 * @param resourceLocation
	 *            the location of the resource on the file system
	 * @return a sprite sheet which should be used for the t
	 */
	public SpriteSheet readTileSet(Layer layer, String resourceName, String resourceLocation);
	
}