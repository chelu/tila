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

import java.io.IOException;
import java.io.InputStream;

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
	 * Sets the cache Config
	 * @param config config to set
	 */
	void setConfig(CacheConfig config);
	
	/**
	 * Gets the server url that it is caching
	 */
	String getServerUrl();

	/**
	 * Gets server url that it is caching for this query string
	 * @param query request part after the configured cachePath including query string
	 */
	String getServerUrl(String query);
	
	/**
	 * Let caches to modify response from remote server.
	 * @param serverStream remote server inputStream
	 * @param query query send to remote server
	 * @return response InputStream
	 * @throws IOException 
	 */
	InputStream parseResponse(InputStream serverStream, String remoteUri, String localUri) throws IOException;
}
