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
package info.joseluismartin.gtc.mvc;

import info.joseluismartin.gtc.CacheManager;
import info.joseluismartin.gtc.GoogleCache;
import info.joseluismartin.gtc.Tile;
import info.joseluismartin.gtc.TileCache;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
@Controller
@SuppressWarnings("unused")
@RequestMapping("/**")
public class CacheController {

	private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2";
	static {
		System.setProperty("User-Agent", USER_AGENT);
	}


	private static final long serialVersionUID = 1L;

	private final static Log log = LogFactory.getLog(GoogleCache.class);

	private String diskCachePath;
	private boolean useProxy;
	private String proxyUser;
	private String proxyPassword;
	private String proxyHost;
	private String proxyPort;
	private Proxy proxy = Proxy.NO_PROXY;
	@Autowired
	private CacheManager cacheService;
	private Pattern URI_PATTERN = Pattern.compile("/([a-z-]+)/(.*)");


	/**
	 * Parse request and create a new Tile.
	 * Try to find tile in memory cache first, if not found, try to read from disk, if not found,
	 * download tile from server and write it to cache.
	 */
	@RequestMapping("/**")
	protected void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		String requestUri = req.getRequestURI();
		String contextPath = req.getContextPath();
		String requestPart = StringUtils.substringAfter(requestUri, contextPath);
		String queryPart = null;
		String cachePath = null;
		Matcher m  = URI_PATTERN.matcher(requestPart);

		if (m.find()) {
			cachePath = m.group(1);
			queryPart = m.group(2);
		}

		TileCache cache = getTileCache(cachePath);
		Tile tile = cache.getTile(queryPart);

		if (tile == null) {
			String redirectUrl = cache.getServerUrl() + "/" + queryPart;
			if (log.isDebugEnabled()) {
				log.debug("Can't parse tile url [" + requestUri +"], redirecting to:" + redirectUrl);
			}
			resp.sendRedirect(redirectUrl);
		}
		else {
			if (tile.isEmpty()) {

				// try three times...
				for (int i= 0; i < 3; i++) {
					try {
						downloadTile(tile, cache.getTileUrl(tile));
						break;
					}catch (IOException ioe) {
						log.error("Error downloading the tile, try: " + i, ioe);
					}
				}
				cache.storeTile(tile);   
			}
			resp.setContentType("image/png");
			resp.getOutputStream().write(tile.getImage());
		}
	}

	private TileCache getTileCache(String cachePath) {
		return cacheService.findCache(cachePath);
	}


	/**
	 * Download tile from google tile servers
	 * @param tile
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private void downloadTile(Tile tile, URL url) throws MalformedURLException, IOException {

		URLConnection conn = url.openConnection(proxy);
		conn.setRequestProperty("User-Agent", USER_AGENT);
		BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int b;
		while ((b = bis.read()) != -1) {
			baos.write(b);
		}
		tile.setImage(baos.toByteArray());
	}	


	//	public void init() throws ServletException {
	//		cachePath = getInitParameter("cachePath");
	//		google.setCachePath(cachePath + "/google");
	//		mapnik.setCachePath(cachePath + "/mapnik/tiles");
	//		
	//		String tileServer = getInitParameter("tileServer");
	//		if (tileServer != null) {
	//			mapnik.setTileServer(tileServer);
	//		}
	//		
	//		useProxy = "true".equalsIgnoreCase(getInitParameter("useProxy"));
	//
	//		if (useProxy) {
	//			proxyHost = getInitParameter("proxyHost");
	//			proxyPort = getInitParameter("proxyPort");
	//			proxyUser = getInitParameter("proxyUser");
	//			proxyPassword = getInitParameter("proxyPassword");
	//
	//			SocketAddress address = new InetSocketAddress(proxyHost, Integer.parseInt(proxyPort));
	//			proxy = new Proxy(Proxy.Type.HTTP, address);
	//			Authenticator.setDefault(new SimpleAuthenticator(proxyUser, proxyPassword));
	//
	//			log.info("Using proxy: " + proxyHost + ":" + proxyPort);
	//		}
	//		else {
	//			proxy = Proxy.NO_PROXY;
	//		}
	//	}
}

/**
 * Simple Authenticator for proxy
 * 
 * @author Jose Luis Martin
 */
class SimpleAuthenticator extends Authenticator {
	private String username, password;

	public SimpleAuthenticator(String username, String password) {
		this.username = username;
		this.password = password;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password.toCharArray());
	}
}


