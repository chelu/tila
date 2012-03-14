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
import info.joseluismartin.gtc.model.CacheType;
import info.joseluismartin.service.PersistentService;

import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class CacheManager implements CacheService, ApplicationContextAware  {
	
	private static final Log log = LogFactory.getLog(CacheManager.class);
	private Hashtable<String, TileCache> cacheMap = new Hashtable<String, TileCache>();
	private ApplicationContext applicationContext;
	@Resource
	private PersistentService<CacheConfig, Integer> cacheService;
	
	public TileCache findCache(String key) {
		TileCache cache =  cacheMap.get(key);
		if (cache == null) {
			log.warn("TileCache not found for [" + key + "], using default");
			cache = cacheMap.get("google");
		}
		
		return cache;
	}
	
	public void init() {
		List<CacheConfig> configs = cacheService.getAll();
		for (CacheConfig c : configs) {
			if (c.isActive()) {
				if (log.isDebugEnabled())
					log.debug("Loading cache: [" + c.getName() +"]");
				loadCache(c);
			}
		}
	}
	
	public void loadCache(CacheConfig config) {
		CacheType type = config.getType();
		
		if (type != null) {
			TileCache cache = (TileCache) applicationContext.getBean(config.getType().getBeanName());
			cacheMap.put(config.getPath(), cache);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		
	}
}
