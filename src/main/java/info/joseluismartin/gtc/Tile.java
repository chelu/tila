package info.joseluismartin.gtc;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  Tile holding raw bit data image and tile info: x, y and zoom level.
 *  
 * @author Jose Luis Martin
 */
public class Tile {

	private static final Log log = LogFactory.getLog(Tile.class);
	/** tile x */
	private int x;
	/** tile y */
	private int y;
	/** tile zomm level */
	private int zoom;
	/** tile image as byte array */
	private byte[] image;
	/**
	 *  server index to iterate when creating google url of this tile.
	 *  google have three servers mt0, mt1, and mt2, a call to getTile
	 */
	private static int serverIndex = 0;
	
	/**
	 * @param x
	 * @param y
	 * @param zoom
	 */
	public Tile(int x, int y, int zoom) {
		this.x = x;
		this.y = y;
		this.zoom = zoom;
	}

	public void load(String path) {
		try {
			image = FileUtils.readFileToByteArray(new File(path));
		} catch (IOException e) {
			log.error(e);
		}
	}
	
	/**
	 * @return
	 */
	public long getKey() {
		long key = x*10000000 + y*100 + zoom;
		return key;
	}
	
	public String getGoogleTileUrl(Tile tile) {
		int x = tile.getX();
		int y = tile.getY();
		int zoom = tile.getZoom();

		StringBuilder sb = new StringBuilder();
		sb.append("http://mt");
		sb.append(newIndex());
		sb.append(".google.com/vt/");
		sb.append("x=");
		sb.append(x);
		sb.append("&y=");
		sb.append(y);
		sb.append("&zoom=");
		sb.append(zoom);
		String url = sb.toString();

		return url;
	}

	private  int newIndex() {
		int index = serverIndex++;

		if (serverIndex > 2)
			serverIndex = 0;
	
		return index;
	}
	
	// Getters and Setters
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getZoom() {
		return zoom;
	}
	
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}
	
	public byte[] getImage() {
		return image;
	}
	
	public void setImage(byte[] image) {
		this.image = image;
	}

	public boolean isEmpty() {
		return image == null;
	}
}
