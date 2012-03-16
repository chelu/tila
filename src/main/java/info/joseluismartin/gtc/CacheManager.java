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
import info.joseluismartin.gtc.model.SystemConfig;
import info.joseluismartin.service.PersistentService;

import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
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
	@Resource
	private SystemConfig systemConfig;
	
	public TileCache findCache(String key) {
		TileCache cache =  cacheMap.get(key);
		if (cache == null) {
			for (CacheConfig c : cacheService.getAll()) {
				if (c.isActive() && c.getPath().equals(key)) {
					if (log.isDebugEnabled())
						log.debug("Loading cache: [" + c.getName() +"]");
					loadCache(c);
				}
			}
		}
		return cache;
	}
	
	public void init() {
		cacheMap.clear();
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
		try {
			if (type != null) {
				TileCache cache = (TileCache) applicationContext.getBean(config.getType().getBeanName());
				config.setDiskCachePath(systemConfig.getCachePath());
				cache.setConfig(config);
				cacheMap.put(config.getPath(), cache);
			}
		}
		catch (NoSuchBeanDefinitionException e) {
			log.error("Failed to load cache bean name [" + e.getBeanName() + "]");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		
	}

	/**
	 * @return the systemConfig
	 */
	public SystemConfig getSystemConfig() {
		return systemConfig;
	}

	/**
	 * @param systemConfig the systemConfig to set
	 */
	public void setSystemConfig(SystemConfig systemConfig) {
		this.systemConfig = systemConfig;
	}
}
