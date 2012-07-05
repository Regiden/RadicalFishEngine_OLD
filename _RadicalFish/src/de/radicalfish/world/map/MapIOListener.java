package de.radicalfish.world.map;
import de.radicalfish.context.Resources;

/**
 * A listener for loading maps that mus be provided when loading map via {@link MapIO}.
 * 
 * @author Stefan Lange
 * @version 0.0.0
 * @since 04.07.2012
 */
public interface MapIOListener {
	
	/**
	 * Callback for a Map that should be loaded. The map returned cannot be null. It should be an empty object. The map
	 * will be filled with data via the setters by the {@link MapIO} class.
	 * 
	 * @return a Map that will be used to load all the content into it.
	 */
	public Map getLoadableMap();
	
	/**
	 * Callback for a Layer that should be loaded. The returned Layer cannot be null. the parameter <code>type</code> is
	 * equal to the class name of the Layer to the time it was saved via {@link MapIO}.
	 * 
	 * @param type
	 *            the type of the Layer (class name)
	 * @return a Layer which equals the class of the <code>type</code> string.
	 */
	public Layer loadLayer(String type);
	/**
	 * Callback for a collision Layer that should be loaded. The returned collision Layer cannot be null. the parameter <code>type</code> is
	 * equal to the class name of the collision Layer to the time it was saved via {@link MapIO}.
	 * 
	 * @param type
	 *            the type of the collision Layer (class name)
	 * @return a collision Layer which equals the class of the <code>type</code> string.
	 */
	public Layer loadCollisionLayer(String type);
	
	/**
	 * Callback for a TilSet that should be loaded. The returned TilSet cannot be null. the parameter <code>type</code>
	 * is equal to the class name of the TilSet to the time it was saved via {@link MapIO}.
	 * 
	 * @param resourceName
	 *            the name of the resource in the {@link Resources} class
	 * @param resourceLocation
	 *            the location of the resource on the file system
	 * @param type
	 *            the type of the Layer (class name)
	 * @return a TilSet which equals the class of the <code>type</code> String.
	 */
	public TileSet loadTileSet(String resourceName, String resourceLocation, String type);
	
	/**
	 * Callback for a Tile that should be loaded. The returned Tile cannot be null. the parameter <code>type</code> is
	 * equal to the class name of the Tile to the time it was saved via {@link MapIO}.
	 * 
	 * @param id
	 *            the id of the Tile
	 * @param type
	 *            the type of the tile (class name)
	 * @return a Tile which equals the class of the <code>type</code> string
	 */
	public Tile loadTile(int id, String type);
	
}