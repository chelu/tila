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

import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
	 * @param path uri request path
	 * @return a new Tile from request query string
	 */
	protected Tile parseTile(String path) {
		Tile tile = null;

		try {
			String[] parts = path.split("/");
			String type = parts[0];
			Map<String, String> params = getParameterMap(parts[1]);
			int x = Integer.parseInt(params.get("x"));
			int y = Integer.parseInt(params.get("y"));
			int zoom;
			
			if (params.containsKey("z"))
				zoom = Integer.parseInt(params.get("z"));
			else
				zoom = Integer.parseInt(params.get("zoom"));
			
			tile = new Tile(x, y, zoom);
			tile.setType(type);
			tile.setMimeType("image/png");
			tile.setLayer(params.get("lyrs"));
		}
		catch (Exception e) {
			log.error(e);
		}
		
		return tile;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getServerUrl(String query) {
		String path = StringUtils.substringBefore(query, "/");
		String server = path.equals("vt") ? "mt" : "kh";
		StringBuilder sb = new StringBuilder();
		sb.append("http://");
		sb.append(server);
		sb.append(this.serverIndex++);
		sb.append(".google.com");
		
		if (serverIndex > 2)
			serverIndex = 0;
		
		return sb.toString();
	}
}
	 
