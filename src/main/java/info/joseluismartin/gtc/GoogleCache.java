/*
 * Copyright 20010-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.joseluismartin.gtc;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * TileCache implementation for Google Maps. 
 * 
 * The file system cache is compatible with gmapcacher.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class GoogleCache extends AbstractTileCache {

	private static final Log log = LogFactory.getLog(GoogleCache.class);
	private int serverIndex = 0;


	/**
	 * Parse request and create a new tile with x, y, and zoom level
	 * @param req http request
	 * @return a new Tile from request query string
	 */
	protected Tile parseTile(String uri) {
		String[] splited = uri.split("&");
		int x = Integer.parseInt((splited[0].substring(splited[0]
		                                                       .lastIndexOf('=') + 1)));
		int y = Integer.parseInt((splited[1].substring(splited[1]
		                                                       .lastIndexOf('=') + 1)));
		int zoom = Integer.parseInt((splited[2].substring(splited[2]
		                                                          .lastIndexOf('=') + 1)));
		return new Tile(x, y, zoom);

	}

	/**
	 * Get de Tile url on google map tile server
	 * @param tile
	 * @return a String with url of tile in google map servers
	 */
	public URL getTileUrl(Tile tile) {
		int x = tile.getX();
		int y = tile.getY();
		int zoom = tile.getZoom();

		StringBuilder sb = new StringBuilder();
		sb.append("http://mt");
		sb.append(this.serverIndex++);
		if (serverIndex > 2)
			serverIndex = 0;
		sb.append(".google.com/vt/");
		sb.append("x=");
		sb.append(x);
		sb.append("&y=");
		sb.append(y);
		sb.append("&zoom=");
		sb.append(zoom);
		String url = sb.toString();

		if (log.isDebugEnabled())
			log.debug("Parsed url to: " + url);

		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			log.error(e);
			return null;
		}
	}
	
	 protected String getCachePath(Tile tile)
	  {
	    int x = tile.getX();
	    int y = tile.getY();
	    int zoom = tile.getZoom();
	    
	    return getCachePath() + File.separator + zoom + File.separator + x / 1024 + File.separator + x % 1024 + File.separator + y / 1024 + File.separator + y % 1024 + ".png";
	 } 
} 

