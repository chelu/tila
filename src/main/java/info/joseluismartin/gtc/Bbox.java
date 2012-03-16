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
public class Bbox {
	
	private float left;
	private float up;
	private float right;
	private float down;
	private String srs;
	
	/**
	 * @param left
	 * @param up
	 * @param right
	 * @param down
	 * @param srs
	 */
	public Bbox(float left, float up, float right, float down, String srs) {
		super();
		this.left = left;
		this.up = up;
		this.right = right;
		this.down = down;
		this.srs = srs;
	}
	/**
	 * @param string
	 */
	public Bbox(String string) {
		String[] box = string.split(",");
		left = Float.parseFloat(box[0]);
		up = Float.parseFloat(box[1]);
		right = Float.parseFloat(box[2]);
		down = Float.parseFloat(box[3]);
	}
	/**
	 * @return the left
	 */
	public float getLeft() {
		return left;
	}
	/**
	 * @param left the left to set
	 */
	public void setLeft(float left) {
		this.left = left;
	}
	/**
	 * @return the up
	 */
	public float getUp() {
		return up;
	}
	/**
	 * @param up the up to set
	 */
	public void setUp(float up) {
		this.up = up;
	}
	/**
	 * @return the right
	 */
	public float getRight() {
		return right;
	}
	/**
	 * @param right the right to set
	 */
	public void setRight(float right) {
		this.right = right;
	}
	/**
	 * @return the down
	 */
	public float getDown() {
		return down;
	}
	/**
	 * @param down the down to set
	 */
	public void setDown(float down) {
		this.down = down;
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
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(down);
		result = prime * result + Float.floatToIntBits(left);
		result = prime * result + Float.floatToIntBits(right);
		result = prime * result + ((srs == null) ? 0 : srs.hashCode());
		result = prime * result + Float.floatToIntBits(up);
		return result;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bbox other = (Bbox) obj;
		if (Float.floatToIntBits(down) != Float.floatToIntBits(other.down))
			return false;
		if (Float.floatToIntBits(left) != Float.floatToIntBits(other.left))
			return false;
		if (Float.floatToIntBits(right) != Float.floatToIntBits(other.right))
			return false;
		if (srs == null) {
			if (other.srs != null)
				return false;
		} else if (!srs.equals(other.srs))
			return false;
		if (Float.floatToIntBits(up) != Float.floatToIntBits(other.up))
			return false;
		return true;
	}

}
