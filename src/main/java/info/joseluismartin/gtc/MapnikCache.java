package info.joseluismartin.gtc;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * WMS Tile Cache implementation.
 * 
 * @author Jose Luis Martin
 */
public class MapnikCache extends AbstractTileCache {
	
	private static String TILE_SERVER = "http://tile.openstreetmap.org";

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Tile parseTile(String uri) {
		String[] splited = uri.split("/");
		int length = splited.length;
		
		int x = Integer.parseInt(splited[length - 2]);
		int y = Integer.parseInt(splited[length - 1].substring(0, splited[length - 1].lastIndexOf('.')));
		int zoom = Integer.parseInt(splited[length - 3]);
		
		return new Tile(x, y, zoom);
	}

	/**
	 * {@inheritDoc}
	 */
	public URL getTileUrl(Tile tile) {
		String url = TILE_SERVER + "/" + tile.getZoom() + "/" + tile.getX() +"/" + tile.getY() + ".png";
		try {
			return new URL (url);
		} catch (MalformedURLException e) {
			return null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected String getCachePath(Tile tile) {
		int x = tile.getX();
		int y = tile.getY();
		int zoom = tile.getZoom();

		return getCachePath() + File.separator + zoom + File.separator + x + File.separator + y + ".png";
	}


}
