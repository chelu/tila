package info.joseluismartin.gtc;

import java.io.IOException;
import java.net.URL;

/**
 *  TileCache interface. Implement to create new tile caches. 
 *  
 * @author Jose Luis Martin
 */

public interface TileCache {
	/**
	 * Create a Tile from URI
	 * @param uri the URI to get the tile
	 * @return a new Tile
	 */
	Tile getTile(String uri);
	
	/**
	 * Cache the tile
	 * @param tile Tile to cache
	 * @throws IOException on store errors
	 */
	void storeTile(Tile tile) throws IOException;
	
	/**
	 * Gets URL from a Tile
	 * @param tile the Tile
	 * @return URL from tile.
	 */
	URL getTileUrl(Tile tile);
}
