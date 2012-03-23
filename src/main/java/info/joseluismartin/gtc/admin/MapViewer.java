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

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Select;

/**
 * Map Viewer for cache testing.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
// TODO: Write a GWT Client Component instead of using executeJavaScript()
public class MapViewer extends CustomLayout implements ListPaneAware, ValueChangeListener {
	
	private static final Log log = LogFactory.getLog(MapViewer.class);
	// Map Types
	private static final String TMS = "TMS";
	private static final String GOOGLE = "Google Maps";
	// Map Scripts
	private static final String GOOGLE_MAP_SCRIPT = "VAADIN/js/googleMap.js";
	@Resource
	private PersistentService<CacheConfig, Integer> cacheService;
	private Select cacheSelect = new Select("Tile Caches");
	private ListSelect layers = new ListSelect("Layers");
	
	private String TMS_SCRIPT = "var layer = new OpenLayers.Layer.TMS($layerName'," +
				"$url', {layername: '$layer', type: 'png'});";
	
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
		cacheSelect.setItemCaptionPropertyId("name");
		cacheSelect.addListener(this);
		cacheSelect.setImmediate(true);

		addComponent(cacheSelect, "map-cache-select");
		addComponent(layers, "map-layer-select");
		
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
		valueChange(null);
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void hide() {

	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void valueChange(ValueChangeEvent event) {
		Object itemId = cacheSelect.getValue();
		
		if (itemId == null)  // nothing to do
			return;
		
		
		BeanItem<CacheConfig> item = (BeanItem<CacheConfig>) cacheSelect.getItem(itemId);
		if (item != null) {
			CacheConfig cache = item.getBean();
			String type =  cache.getType().getName();
			if (TMS.equals(type)) {
				addTMSLayer(cache);
			}
			else if (GOOGLE.equals(type)) {
				addGoogleMap(cache);
			}
		}
	}

	/**
	 * 
	 */
	private void addGoogleMap(CacheConfig cache) {
		ServletContext sc = ((WebApplicationContext) getApplication().getContext()).getHttpSession().getServletContext();
		File file = new File(sc.getRealPath(GOOGLE_MAP_SCRIPT));

		try {
			String script = FileUtils.readFileToString(file);
			String url = StringUtils.substringBefore(getApplication().getURL().toString(), "admin");
			url += cache.getPath();
			script = script.replace("$tilaGoogleCacheUrl", url);
			getWindow().executeJavaScript(script);
		} catch (IOException e) {
			log.error(e);
		}
	}

	/**
	 * @param cache
	 */
	private void addTMSLayer(CacheConfig cache) {
		// TODO Auto-generated method stub
		
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
