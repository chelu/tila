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
import info.joseluismartin.xml.XMLUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.WebMapServer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
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
	private static final String OSM = "Mapnik (OSM)";
	private static final String WMS = "WMS";
	private static final String WMTS = "WMTS";
	private static final String VE = "Virtual Earth";
	// Map Scripts
	private static final String GOOGLE_MAP_SCRIPT = "VAADIN/js/googleMap.js";
	private static final String TMS_MAP_SCRIPT = "VAADIN/js/tmsMap.js";
	private static final String WMS_MAP_SCRIPT = "VAADIN/js/wmsMap.js";
	private static final String WMTS_MAP_SCRIPT = "VAADIN/js/wmtsMap.js";
	private static final String VE_MAP_SCRIPT = "VAADIN/js/veMap.js";
	private static final String OSM_MAP_SCRIPT = "VAADIN/js/osmMap.js";
	
	@Resource
	private PersistentService<CacheConfig, Integer> cacheService;
	private Select cacheSelect = new Select("Tile Caches");
	private ListSelect layers = new ListSelect("Layers");
	private ListSelect srs = new ListSelect("srs");
	

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
		cacheSelect.setWidth("150px");
		layers.setWidth("150px");
		layers.setHeight("450px");
		layers.setImmediate(true);
		layers.addListener(new LayerListener());

		addComponent(cacheSelect, "map-cache-select");
		addComponent(layers, "map-layer-select");
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void show() {
		if (cacheSelect.getItemIds().size() > 0)
			cacheSelect.select(cacheSelect.getItemIds().iterator().next());
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
		layers.removeAllItems();
		
		Object itemId = cacheSelect.getValue();
		
		if (itemId == null)  // nothing to do
			return;
		
		BeanItem<CacheConfig> item = (BeanItem<CacheConfig>) cacheSelect.getItem(itemId);
		
		if (item != null) {
			CacheConfig cache = item.getBean();
			String type =  cache.getType().getName();
			if (TMS.equals(type)) {
				addTMSMap(cache);
			}
			else if (GOOGLE.equals(type)) {
				addGoogleMap(cache);
			}
			else if (OSM.equals(type)) {
				addOsmMap(cache);
			}
			else if (WMS.equals(type)) {
				addWmsMap(cache);
			}
		}
	}

	/**
	 * Execute JS on client to add a WMS layer to OpenLayers Map
	 * @param cache cache to add as WMS Layer 
	 */
	private void addWmsMap(CacheConfig cache) {
		String script = getScript(WMS_MAP_SCRIPT);
		if (script != null) {
			try {
//			// Get Capabilities
//			URL url = new URL(getCacheUrl(cache) + "?REQUEST=GetCapabilities");
//			Document cap  = XMLUtils.getDocumentBuilder().parse(url.openStream());
//			NodeList nl = cap.getElementsByTagName("LAYER");
				
				WebMapServer wms = new WebMapServer(new URL(getCacheUrl(cache)));
				WMSCapabilities c = wms.getCapabilities();
				
			}
			catch (Exception e) {
				log.error(e);
			}
		}
		
	}

	/**
	 * Execute JS on client to add a OSM Layer to OpenLayer map.
	 * @param cache cache to add as OSM layer
	 */
	private void addOsmMap(CacheConfig cache) {
		String script = getScript(OSM_MAP_SCRIPT);
		if (script != null) {
			String url = getCacheUrl(cache);
			script = script.replace("$cacheUrl", url);
			getWindow().executeJavaScript(script);
		}
	}

	/**
	 * Execute a JS on client to create a google map 
	 * @param cache cache to show in google map.
	 */
	private void addGoogleMap(CacheConfig cache) {
		String script = getScript(GOOGLE_MAP_SCRIPT);

		if (script != null) {
			String url = getCacheUrl(cache);
			script = script.replace("$tilaGoogleCacheUrl", url);
			getWindow().executeJavaScript(script);
		} 
	}

	/**
	 * Gets the cache url for a cache.
	 * @param cache cache to create the cache url
	 * @return Url to access de cache.
	 */
	private String getCacheUrl(CacheConfig cache) {
		String url = StringUtils.substringBefore(getApplication().getURL().toString(), "admin");
		url += cache.getPath();
		return url;
	}
	
	/**
	 * Parse TMS service response and fill layers select.
	 * @param cache TMS cache to use.
	 */
	private void addTMSMap(CacheConfig cache) {
		URL tmsService;
		try {
			tmsService = new URL(getCacheUrl(cache));
			Document service = XMLUtils.getDocumentBuilder().parse(tmsService.openStream());
			NodeList nl = service.getElementsByTagName("TileMapService");
			if (nl.getLength() > 0) {
				// First service only 
				Element map = (Element) nl.item(0);
				URL mapService = new URL(map.getAttribute("href"));
				Document tileMapService = XMLUtils.getDocumentBuilder().parse(mapService.openStream());
				NodeList tileMapNodes = tileMapService.getElementsByTagName("TileMap");
				for (int i = 0; i < tileMapNodes.getLength(); i++) {
					TileMap tileMap = new TileMap((Element) tileMapNodes.item(i), cache);
					layers.addItem(tileMap);
				}
				
			}
		layers.requestRepaint();
		} catch (Exception e) {
			log.error(e);
		}
	}
	
	
	/**
	 * Get a Script from filesystem
	 * @param path relative path to context
	 * @return String with script context or null on IOExceptions
	 */
	private String getScript(String path) {
		String script = null;
		
		ServletContext sc = ((WebApplicationContext) getApplication().getContext()).getHttpSession().getServletContext();
		File file = new File(sc.getRealPath(path));
		try {
			script = FileUtils.readFileToString(file);
		} catch (IOException e) {
			log.error(e);
		}
		
		return script;
	}
	
	/**
	 * Hold TileMap info from TMS Service.
	 * @author Jose Luis Martin - (jlm@joseluismartin.info)
	 */
	class TileMap implements MapBuilder {
		
		private String href;
		private String 	srs;
		private String title;
		private CacheConfig cache;
		
		public TileMap(Element element, CacheConfig cache) {
			this.href = element.getAttribute("href");
			this.srs = element.getAttribute("srs");
			this.title = element.getAttribute("title");
			this.cache = cache;
		}
		
		/**
		 * @return the href
		 */
		public String getHref() {
			return href;
		}
		
		/**
		 * @param href the href to set
		 */
		public void setHref(String href) {
			this.href = href;
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
		 * @return the title
		 */
		public String getTitle() {
			return title;
		}
		
		/**
		 * @param title the title to set
		 */
		public void setTitle(String title) {
			this.title = title;
		}
		
		public String toString() {
			return title;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void buildMap() {
			String script = getScript(TMS_MAP_SCRIPT);
			script = script.replace("$cacheUrl", getCacheUrl(cache));
			script = script.replace("$layerName", title);
			script = script.replace("$srs", srs);
			getWindow().executeJavaScript(script);
		}
	}
	
	/**
	 * Litener for Layer changes. Build a new map client with selected layer 
	 * on layer changes. 
	 * @author Jose Luis Martin - (jlm@joseluismartin.info)
	 */
	class LayerListener implements ValueChangeListener {

		/**
		 * {@inheritDoc}
		 */
		public void valueChange(ValueChangeEvent event) {
			MapBuilder mb = (MapBuilder) layers.getValue();
			if (mb != null)
				mb.buildMap();
		}
		
	}
}

interface MapBuilder {
	/**
	 * Build a Map on Client
	 */
	void buildMap();
}
