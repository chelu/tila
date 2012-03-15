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
package info.joseluismartin.gtc.model;

import info.joseluismartin.service.PersistentService;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * Configuration Properties
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class SystemConfig implements Serializable {
	
	public static final String CACHE_PATH = "cachePath";
	
	
	private Map<String, ConfigProperty> configMap = new HashMap<String, ConfigProperty>();
	@Resource
	private PersistentService<ConfigProperty, Integer> configService;
	
	public void init() {
		List<ConfigProperty> properties = configService.getAll();
		for (ConfigProperty cp : properties) {
			configMap.put(cp.getName(), cp);
		}
	}
	
	public String getCachePath() {
		return getValue(CACHE_PATH);
	}
	
	public void setCachePath(String path) {
		ConfigProperty cp = configMap.get(CACHE_PATH);
		if (cp != null)
			cp.setValue(path);
	}

	/**
	 * @param cachePath
	 */
	private String getValue(String property) {
		ConfigProperty cp = configMap.get(property);
		return cp != null ? cp.getValue() : "";
	}
	
	private  Collection<ConfigProperty> getProperties() {
		return configMap.values();
	}
	
	public void save () {
		configService.save(getProperties());
	}
}
