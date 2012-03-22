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
 * Tile for VirtualEarth map service.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class VeTile extends Tile {

	private String g;
	private String mkt;

	public VeTile() {
		super();
	}

	/**
	 * @param x
	 * @param y
	 * @param zoom
	 */
	public VeTile(int x, int y, int zoom) {
		super(x, y, zoom);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param g
	 * @param mkt
	 */
	public VeTile(int x, int y, int z, String g, String mkt) {
		super(x, y, z);
		this.g = g;
		this.mkt = mkt;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getKey() {
		final int prime = 31;
		int result = super.getKey();
		result = prime * result + ((g == null) ? 0 : g.hashCode());
		result = prime * result + ((mkt == null) ? 0 : mkt.hashCode());
		return result;
	}

	/**
	 * @return the g
	 */
	public String getG() {
		return g;
	}

	/**
	 * @param g the g to set
	 */
	public void setG(String g) {
		this.g = g;
	}

	/**
	 * @return the mkt
	 */
	public String getMkt() {
		return mkt;
	}

	/**
	 * @param mkt the mkt to set
	 */
	public void setMkt(String mkt) {
		this.mkt = mkt;
	}
}
