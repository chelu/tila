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
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * TileCache implentation for WMTS
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class WtmsCache extends WmsCache {
	
	protected static final String GET_TILE = "GetTile";

	private static final Log log = LogFactory.getLog(WtmsCache.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Tile parseTile(String uri) {
		Tile tile = null;
		if (uri.startsWith("?")) 
			uri = uri.substring(1);
		
		try {
			Map<String, String> map = getParameterMap(uri);
			
			if (GET_TILE.equalsIgnoreCase(map.get(REQUEST)))
				tile = new WtmsTile(getParameterMap(uri));
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
		WtmsTile t = (WtmsTile) tile;
		StringBuilder sb = new StringBuilder();
		sb.append(getPath());
		sb.append(File.separator);
		sb.append(t.getLayer());
		sb.append(File.separator);
		sb.append(t.getTileMatrix());
		sb.append(File.separator);
		sb.append(t.getTileMatrixSet());
		sb.append(File.separator);
		sb.append(t.getX() % 1024);
		sb.append(File.separator);
		sb.append(t.getY() % 1024);
		sb.append(File.separator);
		sb.append(t.getX() + "_" + t.getY());
		sb.append(t.getFileExension());
		
		return sb.toString();
	}

}
