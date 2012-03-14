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
package info.joseluismartin.gtc.admin;

import info.joseluismartin.gtc.model.CacheConfig;
import info.joseluismartin.vaadin.ui.ApplicationContextGuiFactory;
import info.joseluismartin.vaadin.ui.table.PageableTable;

/**
 * GuiFactory for Tila.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@SuppressWarnings("unchecked")
public class TilaGuiFactory extends ApplicationContextGuiFactory {
	
	private static final String CACHE_TABLE = "pageableTable";
	
	
	/**
	 * Gets a PageableTable for cache configurations
	 * @return a PageableTable instance of cache configurations
	 */
	public PageableTable<CacheConfig> getCacheTable() {
		return (PageableTable<CacheConfig>) this.applicationContext.getBean(CACHE_TABLE);
	}

}
