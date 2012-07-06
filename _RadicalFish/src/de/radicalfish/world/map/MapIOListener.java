package de.radicalfish.world.map;
import de.radicalfish.context.Resources;

/**
 * A Listener to provide the read methods of {@link MapIO} with instances of the Map, Layer, TileSet and Tile
 * Interfaces. e.g. when loading a tile, the listener will call {@link MapIOListener#getTileInstance(String, String)}.
 * This methods should then return an new instance where the data can be set to.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 04.07.2012
 */
public interface MapIOListener {
	
	// READ
	// ŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ
	/**
	 * @param classname
	 *            the name of the class which was saved by {@link MapIO#writeMap(String, Map, boolean)} for the map.
	 * @return a {@link Map} Implementation to load the content into.
	 */
	public Map getMapInstance(String classname);
	/**
	 * @param classname
	 *            the name of the class which was saved by {@link MapIO#writeMap(String, Map, boolean)} for the layer.
	 * @return a {@link Layer} Implementation to load the content into.
	 */
	public Layer getLayerIntance(String classname);
	/**
	 * @param classname
	 *            the name of the class which was saved by {@link MapIO#writeMap(String, Map, boolean)} for the tileset.
	 * @param resourceName
	 *            the resource name which was saved and should be loaded in the {@link Resources} class. (Can be used to
	 *            load the TileSet graphics)
	 * @param resoureLocation
	 *            the resource location which was saved. (Can be used to load the TileSet graphics)
	 * @return a {@link TileSet} Implementation to load the content into.
	 */
	public TileSet getTileSetIntance(String classname, String resourceName, String resoureLocation);
	/**
	 * @param classname
	 *            the name of the class which was saved by {@link MapIO#writeMap(String, Map, boolean)} for the tile.
	 * @param type
	 *            the type of the tile as it was saved. (currently: animated or normal)
	 * @return a {@link Tile} Implementation to load the content into. (For animated tiles the returned tile must be
	 *         {@link AnimatedTile} to load the data correctly)
	 */
	public Tile getTileInstance(String classname, String type);
	
}