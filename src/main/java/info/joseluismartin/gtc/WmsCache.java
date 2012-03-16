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

import info.joseluismartin.gtc.model.CacheConfig;
import info.joseluismartin.service.PersistentService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * TileCache for WMS servers
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class WmsCache  extends AbstractTileCache {

	static final Log log = LogFactory.getLog(WmsCache.class);
	protected static final String GET_CAPABILITIES = "GetCapabilities";
	protected static final String GET_MAP = "GetMap";
	protected static final String REQUEST = "REQUEST";
	@Resource
	private PersistentService<CacheConfig, Integer> cacheService;
	
	
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
			
			if (GET_MAP.equalsIgnoreCase(map.get(REQUEST)))
				tile = new WmsTile(map);
		}
		catch (Exception e) {
			log.error(e);
		}
		
		return tile;
	}

	/**
	 * {@inheritDoc}
	 * @throws IOException 
	 */
	@Override
	public InputStream parseResponse(InputStream serverStream, String remoteUri, String localUri) throws IOException {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(remoteUri);
		UriComponents remoteComponents = builder.build();
		
		InputStream is = serverStream;
		MultiValueMap<String, String> params = remoteComponents.getQueryParams();
		
		if (GET_CAPABILITIES.equals(params.getFirst(REQUEST))) {
			String response = IOUtils.toString(serverStream);
			response = response.replaceAll(getServerUrl(), localUri + "/" + getPath());
			is = IOUtils.toInputStream(response);
		}
		
		return is;
	}

	/**
	 * @return the cacheService
	 */
	public PersistentService<CacheConfig, Integer> getCacheService() {
		return cacheService;
	}

	/**
	 * @param cacheService the cacheService to set
	 */
	public void setCacheService(PersistentService<CacheConfig, Integer> cacheService) {
		this.cacheService = cacheService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getCachePath(Tile tile) {
		WmsTile t = (WmsTile) tile;
		Bbox b = t.getBbox();
		int left =  180 + (int) Math.floor(b.getLeft());
		int up =  180 + (int) Math.floor(b.getUp());
		int right = 180 +  (int)  Math.floor(b.getRight());
		int down = 180 + (int)  Math.floor(b.getDown());
		
		String path = getCachePath() + File.separator + getPath();

		path += File.separator + t.getSrs() + File.separator + 
				File.separator + t.getStyles() + File.separator + t.getLayers() +
				File.separator + left + File.separator + up +
				File.separator + right + File.separator + down +
				File.separator + Integer.toHexString((t.getBbox().hashCode())) + "_" + (t.isTransparent() ? "t" : "o") +
				"." + t.getFileExension();
				

		return path;
	}
	
	/**
	 * Create a parameter map from a query string
	 * @param uri the query string
	 * @return a map with parameters
	 */
	protected Map<String, String> getParameterMap(String uri) {
		UriComponentsBuilder b = UriComponentsBuilder.newInstance();
		b.query(uri);
		UriComponents c = b.build();
		return c.getQueryParams().toSingleValueMap();
	}
	
}
