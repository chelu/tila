package info.joseluismartin.gtc;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Abstract implementation of TileCache interface.
 * 
 * @author Jose Luis Martin
 */
@SuppressWarnings("unchecked")
public abstract class AbstractTileCache implements TileCache {
	
	private Map tileMap = Collections.synchronizedMap(new LRUMap(10000));
	private static final Log log = LogFactory.getLog(AbstractTileCache.class);
	private String cachePath;
	
	public Tile getTile(String uri) {
		
		Tile tile = parseTile(uri);
		
		if (tileMap.containsKey(tile.getKey())) {   // found in memory cache
			if (log.isDebugEnabled())
				log.debug("Tile found in memory cache: " + tile.getKey());
			tile = (Tile) tileMap.get(tile.getKey());
		}
		else {
			if (isTileCached(tile)) {  // found in disk
				if (log.isDebugEnabled())
					log.debug("Tile found in disk cache:" + tile.getKey());
				
				tile.load(getCachePath(tile));
				// refresh cache with last tile
				tileMap.put(tile.getKey(), tile);
			}
		}
	
		return tile;
	}
	
	/**
	 * Write tile to disk cache
	 * @param tile tile to store
	 * @throws IOException on write faliure
	 */
	public void storeTile(Tile tile) throws IOException {
		File file = new File(getCachePath(tile));
		FileUtils.writeByteArrayToFile(file, tile.getImage());
	}

	
	public boolean isTileCached(Tile tile) {
		File file = new File(getCachePath(tile));
		return file.exists();
	}
	
	/**
	 * @return File path of a tile in disk cache
	 */
	protected String getCachePath(Tile tile) {
		int x = tile.getX() / 100;
		int y = tile.getY() / 100;
		int zoom = tile.getZoom();

		return cachePath + File.separator + x + File.separator + y
		+ File.separator + "g_" + tile.getX() + "_" + tile.getY() + "_"
		+ zoom + ".png";
	}
	
	protected abstract Tile parseTile(String uri);

	/**
	 * @return the cachePath
	 */
	public String getCachePath() {
		return cachePath;
	}

	/**
	 * @param cachePath the cachePath to set
	 */
	public void setCachePath(String cachePath) {
		this.cachePath = cachePath;
	}

}
