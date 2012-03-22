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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Vitual Earth Tile Cache
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class VeCache extends AbstractTileCache {
	
	private static final Log log = LogFactory.getLog(VeCache.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Tile parseTile(String uri) {
		Tile tile = null;
		try {
			if (!uri.startsWith("tiles/"))
				return null; // not a VE tile request
			
			uri = StringUtils.substringAfter(uri, "tiles/");
			String[] query =  uri.split("\\?");
			String quad = query[0].substring(1); // drop 'r'
			Map<String, String> params = getParameterMap(query[1]);
			int xyz[] = queadToXyz(quad);
			tile = new VeTile(xyz[0], xyz[1], xyz[2], params.get("g"), params.get("mkt"));
			tile.setMimeType("image/jpeg");
		} catch (Exception e) {
			log.error(e);
		}
		
		return tile;
	}
	
	
	/**
	 * @param quad
	 * @return
	 */
	private int[] queadToXyz(String quad) {
		int x = 0;
	    int y = 0;
        int zoom = quad.length();
        for (int i = zoom; i > 0; i--) {
            int mask = 1 << (i - 1);
            switch (quad.charAt(zoom - i)) {
                case '0':
                    break;

                case '1':
                    x |= mask;
                    break;

                case '2':
                    y |= mask;
                    break;

                case '3':
                    x |= mask;
                    y |= mask;
                    break;

                default:
                    throw new RuntimeException ("Invalid QuadKey digit sequence.");
            }
        }
        
        return new int[] {x, y, zoom};
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getCachePath(Tile tile) {
		VeTile t = (VeTile) tile;
		int x = t.getX();
		int y = t.getY();
		int zoom = t.getZoom() + 2;
		String type = t.getType();
		String layer = t.getLayer();
		String g = t.getG();
		String mkt = t.getMkt();

		String path = getCachePath() + File.separator + getPath();
		if (!StringUtils.isBlank(type)) {
			path += File.separator + type;
		}

		if (!StringUtils.isBlank(layer)) {
			path += File.separator + Integer.toHexString(layer.hashCode());
		}
		
		if (!StringUtils.isBlank(g))
			path += File.separator + g;
		
		if (!StringUtils.isBlank(mkt))
			path += File.separator + mkt;

		path += File.separator + zoom + File.separator + x / 1024 + 
				File.separator + x % 1024 + File.separator + y / 1024 + 
				File.separator + y % 1024 + ".png";

		return path;
	}

}
