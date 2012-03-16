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

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;


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
	 * @param path uri request path
	 * @return a new Tile from request query string
	 */
	protected Tile parseTile(String path) {
		Tile tile = null;
		try {
			String[] parts = path.split("/");
			String type = parts[0];
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
			builder.query(parts[1]);
			UriComponents components = builder.build();
			MultiValueMap<String, String> params = components.getQueryParams();
			int x = Integer.parseInt(params.getFirst("x"));
			int y = Integer.parseInt(params.getFirst("y"));
			int zoom = Integer.parseInt(params.getFirst("z"));
			tile = new Tile(x, y, zoom);
			tile.setType(type);
			tile.setMimeType("image/png");
		}
		catch (Exception e) {
			log.error(e);
		}
		
		return tile;
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
		String type = tile.getType();
		
		StringBuilder sb = new StringBuilder();
		sb.append(getServerUrl());
		sb.append("/");
		sb.append(type);
		sb.append("/x=");
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
	
	
	/**
	 * {@inheritDoc}
	 */
	public String getServerUrl() {
		StringBuilder sb = new StringBuilder();
		sb.append("http://mt");
		sb.append(this.serverIndex++);
		sb.append(".google.com");
		
		if (serverIndex > 2)
			serverIndex = 0;
		
		return sb.toString();
	}
}
	 
