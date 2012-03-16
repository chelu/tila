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

import java.util.Map;

/**
 * Tile for WTMS Cache
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class WtmsTile extends Tile {
	
	private static final String LAYER = "Layer";
	private static final String STYLE = "Style";
	private static final String FORMAT = "Format";
	private static final String TILE_MATRIX_SET = "TileMatrixSet";
	private static final String TILE_MATRIX = "TILE_MATRIX";
	private static final String TILE_ROW = "TileRow";
	private static final String TILE_COL = "TileCol";
	
	private String style;
	private String tileMatrixSet;
	private String tileMatrix;
	
	/**
	 * @param fields
	 */
	public WtmsTile(Map<String, String> fields) {
		setLayer(fields.get(LAYER));
		setStyle(fields.get(STYLE));
		setTileMatrix(fields.get(TILE_MATRIX));
		setTileMatrixSet(fields.get(TILE_MATRIX_SET));
		setX(Integer.parseInt(fields.get(TILE_ROW)));
		setY(Integer.parseInt(fields.get(TILE_COL)));
		setMimeType(fields.get(FORMAT));
	}

	/**
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * @param style the style to set
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * @return the tileMatrixSet
	 */
	public String getTileMatrixSet() {
		return tileMatrixSet;
	}

	/**
	 * @param tileMatrixSet the tileMatrixSet to set
	 */
	public void setTileMatrixSet(String tileMatrixSet) {
		this.tileMatrixSet = tileMatrixSet;
	}

	/**
	 * @return the tileMatrix
	 */
	public String getTileMatrix() {
		return tileMatrix;
	}

	/**
	 * @param tileMatrix the tileMatrix to set
	 */
	public void setTileMatrix(String tileMatrix) {
		this.tileMatrix = tileMatrix;
	}

}
