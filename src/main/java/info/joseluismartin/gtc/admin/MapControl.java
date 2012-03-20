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
import info.joseluismartin.service.PersistentService;
import info.joseluismartin.vaadin.data.ContainerDataSource;

import java.io.IOException;

import javax.annotation.Resource;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vaadin.vol.Layer;
import org.vaadin.vol.MapTilerLayer;
import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.OpenStreetMapLayer;
import org.xml.sax.SAXException;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Select;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class MapControl extends HorizontalLayout implements ValueChangeListener {
	
	private static final Log log = LogFactory.getLog(MapControl.class);
	private static final String TMS = "TMS";
	private static final String WMS = "WMS";
	private static final String WMWS = "WMTS";
	
	@Resource
	private PersistentService<CacheConfig, Integer> cacheService;
	private Select cacheSelect = new Select("Tile Caches");
	private OpenLayersMap map;
	private Layer currentLayer;
	
	public void init() {
		ContainerDataSource<CacheConfig> cds = new ContainerDataSource<CacheConfig>(CacheConfig.class, cacheService);
		cds.init();
		cacheSelect.setContainerDataSource(cds);
		cacheSelect.setItemCaptionPropertyId("name");
		cacheSelect.addListener(this);
		cacheSelect.setImmediate(true);
		setSpacing(true);
		addComponent(cacheSelect);
		buildOsmMap();
		addComponent(map);
		this.setExpandRatio(map, 1.0f);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void valueChange(ValueChangeEvent event) {
		BeanItem<CacheConfig> item = (BeanItem<CacheConfig>) cacheSelect.getItem(cacheSelect.getValue());
		if (item != null) {
			CacheConfig cache = item.getBean();
			String type =  cache.getType().getName();
			if (TMS.equals(type)) {
				addTMSLayer(cache);
			}
		}
		
	}
	
	/**
	 * @param cache
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	private void addTMSLayer(CacheConfig cache)  {
		try {
			String url = getWindow().getApplication().getURL().toString();
			url = StringUtils.substringBefore(url, "admin");
			url += cache.getPath() + "/";
			MapTilerLayer tms = new MapTilerLayer(url);
			map.removeLayer(currentLayer);
			map.addLayer(tms);
		}
		catch (Exception e) {
			log.error(e);
		}
	}

	private void buildOsmMap() {
		map = new OpenLayersMap();
		map.setImmediate(true);
		OpenStreetMapLayer osmLayer = new OpenStreetMapLayer();

		map.addLayer(osmLayer);
		currentLayer = osmLayer;
	}
}
