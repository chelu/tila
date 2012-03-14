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

import info.joseluismartin.gtc.model.CacheConfig;

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
	
	private Map<Integer, Tile> tileMap = Collections.synchronizedMap(new LRUMap(10000));
	private static final Log log = LogFactory.getLog(AbstractTileCache.class);
	private String cachePath = "/tmp";
	private String name;
	private String serverUrl;
	
	public Tile getTile(String uri) {
		
		Tile tile = parseTile(uri);
		
		if (tile == null)
			return null;
		
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
	
	public void setConfig(CacheConfig config) {
		this.cachePath = config.getPath();
		this.tileMap = Collections.synchronizedMap(new LRUMap(config.getSize()));
		this.name = config.getName();
		this.serverUrl = config.getUrl();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the serverUrl
	 */
	public String getServerUrl() {
		return serverUrl;
	}

	/**
	 * @param serverUrl the serverUrl to set
	 */
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

}
