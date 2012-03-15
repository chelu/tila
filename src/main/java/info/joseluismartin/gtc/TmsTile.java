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

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class TmsTile extends Tile {
	
	private String version;
	private String map;
	
	/**
	 * @param x tile x
	 * @param y tile y 
	 * @param zoom zoom level
	 * @param map map 
	 * @param version 
	 */
	public TmsTile(int x, int y, int zoom, String map, String version) {
		super(x, y, zoom);
		this.map = map;
		this.version = version;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getKey() {
		final int prime = 31;
		int result = super.getKey();
		result = prime * result + ((map == null) ? 0 : map.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}
	
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	/**
	 * @return the map
	 */
	public String getMap() {
		return map;
	}
	
	/**
	 * @param map the map to set
	 */
	public void setMap(String map) {
		this.map = map;
	}
}
