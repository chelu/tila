/*
 * Copyright 2009-2011 the original author or authors.
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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Tile Cache for TMS servers.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class TmsCache extends AbstractTileCache {

	private static final Log log = LogFactory.getLog(TmsCache.class);


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Tile parseTile(String uri) {
		Tile tile = null;
		try {
			String[] parts = uri.split("/");
			if (parts.length != 5)
				return null;
			String version = parts[0];
			String map = parts[1];
			int  zoom = Integer.parseInt(parts[2]);
			int x = Integer.parseInt(parts[3]);
			String[] file = parts[4].split("\\.");
			int y = Integer.parseInt(file[0]);
			String mimeType = "image/" + file[1];
			
			tile = new TmsTile(x, y , zoom, map, version);
			tile.setMimeType(mimeType);
		}
		catch (Exception e) {
			log.error(e);
		}
		
		return tile;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getCachePath(Tile tile) {
		  int x = tile.getX();
		  int y = tile.getY();
		  int zoom = tile.getZoom();
		  String type = tile.getType();
		    
		  String path = getCachePath() + File.separator + getPath();
		    if (!StringUtils.isBlank(type)) {
		    	path += File.separator + type;
		    }
		    path += File.separator + ((TmsTile) tile).getMap();
		    path += File.separator + zoom + File.separator + x / 1024 + 
		    		File.separator + x % 1024 + File.separator + y / 1024 + 
		    		File.separator + y % 1024 + ".png";
		    
		    return path;
	}

}
