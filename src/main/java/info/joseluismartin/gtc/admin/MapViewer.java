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
import info.joseluismartin.vaadin.ui.ListPane.ListPaneAware;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Select;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class MapViewer extends CustomLayout implements ListPaneAware, ValueChangeListener {
	
	private static final Log log = LogFactory.getLog(MapViewer.class);
	@Resource
	private PersistentService<CacheConfig, Integer> cacheService;
	private Select cacheSelect = new Select("Tile Caches");
	
	private String TMS_SCRIPT = "var layer = new OpenLayers.Layer.TMS('My Layer'," +
				"$url', {layername: 'basic', type: 'png'});";
	
	private String WMS_SCRIPT = "var layer = new OpenLayers.Layers.WMS('$layerName');";

	/**
	 * @param template
	 */
	public MapViewer(String template) {
		super(template);
	}
	
	public void init() {
		ContainerDataSource<CacheConfig> cds = new ContainerDataSource<CacheConfig>(CacheConfig.class, cacheService);
		cds.init();
		cacheSelect.setContainerDataSource(cds);
		addComponent(cacheSelect, "map-cache-select");
		cacheSelect.addListener(this);
		cacheSelect.setWidth("100px");
	}
	
	public void loadMap() {
		getWindow().executeJavaScript(createMapScript());
	}

	/**
	 * @return
	 */
	private String createMapScript() {
		String js = "var map = new OpenLayers.Map('map');" + 
				"var layer = new OpenLayers.Layer.TMS('My Layer'," +
				"'http://localhost:8080/tila/tms/', {layername: 'basic', type: 'png'});" +
				"map.addLayer(layer); map.zoomToMaxExtent();";   
		return js;
	}


	/**
	 * {@inheritDoc}
	 */
	public void show() {
		loadMap();
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void hide() {

	}

	/**
	 * {@inheritDoc}
	 */
	public void valueChange(ValueChangeEvent event) {
		CacheConfig config = (CacheConfig) cacheSelect.getValue();
		String js = getMapScript(config);
		getWindow().executeJavaScript(js);
	}

	/**
	 * @param config
	 * @return
	 */
	private String getMapScript(CacheConfig config) {
		if (log.isDebugEnabled())
			log.debug("Selected cache: [" + config.getName() + "]");
		
		return "";
	}
	
}
