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

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  Tile holding raw bit data image and tile info: x, y and zoom level.
 *  
 * @author Jose Luis Martin
 */
public class Tile {

	private static final Log log = LogFactory.getLog(Tile.class);
	/** tile x */
	private int x;
	/** tile y */
	private int y;
	/** tile zomm level */
	private int zoom;
	/** tile image as byte array */
	private byte[] image;
	/** used from some caches (like google maps) */
	private String type ="";
	/** layer */
	private String layer;
	/** mime type */
	private String mimeType;
	
	public Tile() {
	}
	
	/**
	 * @param x
	 * @param y
	 * @param zoom
	 */
	public Tile(int x, int y, int zoom) {
		this.x = x;
		this.y = y;
		this.zoom = zoom;
	}

	public void load(String path) {
		try {
			image = FileUtils.readFileToByteArray(new File(path));
		} catch (IOException e) {
			log.error(e);
		}
	}
	
	/**
	 * @return key from tile data
	 */
	public int getKey() {
		return new HashCodeBuilder(31, 99)
			.append(layer)
			.append(type)
			.append(x)
			.append(y)
			.append(zoom)
			.toHashCode();
	}
	
	public String getFileExension() {
		return StringUtils.substringAfter(mimeType, "/"); 
	}
	
	// Getters and Setters
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getZoom() {
		return zoom;
	}
	
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}
	
	public byte[] getImage() {
		return image;
	}
	
	public void setImage(byte[] image) {
		this.image = image;
	}

	public boolean isEmpty() {
		return image == null;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the layer
	 */
	public String getLayer() {
		return layer;
	}

	/**
	 * @param layer the layer to set
	 */
	public void setLayer(String layer) {
		this.layer = layer;
	}

	/**
	 * @return the mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * @param mimeType the mimeType to set
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

}
