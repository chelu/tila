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
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class WmsTile extends Tile {

	// ?REQUEST=GetMap&SERVICE=WMS&VERSION=1.1.1&LAYERS=Catastro&SRS=EPSG:4326&BBOX=-18.409639,32.716930593531956,-1.2383291010173174,44.85536&WIDTH=904&HEIGHT=640&FORMAT=image/png&STYLES=Default&TRANSPARENT=TRUE
	private static final String LAYERS = "LAYERS";
	private static final String HEIGHT = "HEIGHT";
	private static final String WIDTH ="WIDTH";
	private static final String FORMAT = "FORMAT";
	private static final String STYLES = "STYLES";
	private static final String TRANSPARENT = "TRANSPARENT";
	private static final String BBOX = "BBOX";
	private static final String SRS = "SRS";
	
	private String layers;
	private String srs;
	private String styles;
	private boolean transparent;
	private Bbox bbox;
	private int height;
	private int width;
	
	public WmsTile() {
		
	}
	
	public WmsTile(Map<String, String> fields) {
		layers = fields.get(LAYERS);
		srs = fields.get(SRS);
		styles = fields.get(STYLES);
		setMimeType(fields.get(FORMAT));
		transparent = "TRUE".equalsIgnoreCase(fields.get(TRANSPARENT));
		height = Integer.parseInt(fields.get(HEIGHT));
		width = Integer.parseInt(fields.get(WIDTH));
		bbox = new Bbox(fields.get(BBOX));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getKey() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bbox == null) ? 0 : bbox.hashCode());
		result = prime * result + height;
		result = prime * result + ((layers == null) ? 0 : layers.hashCode());
		result = prime * result + ((srs == null) ? 0 : srs.hashCode());
		result = prime * result + ((styles == null) ? 0 : styles.hashCode());
		result = prime * result + (transparent ? 1231 : 1237);
		result = prime * result + width;
		
		return result;
	}
	
	/**
	 * @return the layers
	 */
	public String getLayers() {
		return layers;
	}
	
	/**
	 * @param layers the layers to set
	 */
	public void setLayers(String layers) {
		this.layers = layers;
	}
	
	/**
	 * @return the srs
	 */
	public String getSrs() {
		return srs;
	}
	
	/**
	 * @param srs the srs to set
	 */
	public void setSrs(String srs) {
		this.srs = srs;
	}
	
	/**
	 * @return the styles
	 */
	public String getStyles() {
		return styles;
	}
	
	/**
	 * @param styles the styles to set
	 */
	public void setStyles(String styles) {
		this.styles = styles;
	}
	
	/**
	 * @return the transparent
	 */
	public boolean isTransparent() {
		return transparent;
	}
	
	/**
	 * @param transparent the transparent to set
	 */
	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}

	/**
	 * @return the bbox
	 */
	public Bbox getBbox() {
		return bbox;
	}

	/**
	 * @param bbox the bbox to set
	 */
	public void setBbox(Bbox bbox) {
		this.bbox = bbox;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

}
