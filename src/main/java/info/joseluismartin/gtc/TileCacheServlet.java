/*
 * Copyright 20010-2012 the original author or authors.
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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Google Maps Tile Server Cache.
 * 
 * @author Jose Luis Martin
 */
public class TileCacheServlet extends HttpServlet {
	
	private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2";
	
	static {
		System.setProperty("User-Agent", USER_AGENT);
	}

	private static final long serialVersionUID = 1L;

	private final static Log log = LogFactory.getLog(GoogleCache.class);

	private String cachePath;
	private boolean useProxy;
	private String proxyUser;
	private String proxyPassword;
	private String proxyHost;
	private String proxyPort;
	private Proxy proxy;
	
	// FIXME: move to DI
	private static AbstractTileCache google = new GoogleCache();
	private static MapnikCache mapnik = new MapnikCache();


	/**
	 * Parse request and create a new Tile.
	 * Try to find tile in memory cache first, if not found, try to read from disk, if not found,
	 * download tile from google map server and write it to cache.
	 * 
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {

		TileCache cache = getTileCache(req);
		Tile tile = cache.getTile(req.getRequestURI());

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

	private TileCache getTileCache(HttpServletRequest req) {
		String queryString = req.getRequestURI();
		if (queryString != null && queryString.contains("x="))
			return google;
		
		return mapnik;
	}

	
	/**
	 * Download tile from google tile servers
	 * @param tile
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private void downloadTile(Tile tile, URL url)
	throws MalformedURLException, IOException {
		
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


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init() throws ServletException {
		cachePath = getInitParameter("cachePath");
		google.setCachePath(cachePath + "/google");
		mapnik.setCachePath(cachePath + "/mapnik/tiles");
		
		String tileServer = getInitParameter("tileServer");
		if (tileServer != null) {
			mapnik.setTileServer(tileServer);
		}
		
		useProxy = "true".equalsIgnoreCase(getInitParameter("useProxy"));

		if (useProxy) {
			proxyHost = getInitParameter("proxyHost");
			proxyPort = getInitParameter("proxyPort");
			proxyUser = getInitParameter("proxyUser");
			proxyPassword = getInitParameter("proxyPassword");

			SocketAddress address = new InetSocketAddress(proxyHost, Integer.parseInt(proxyPort));
			proxy = new Proxy(Proxy.Type.HTTP, address);
			Authenticator.setDefault(new SimpleAuthenticator(proxyUser, proxyPassword));

			log.info("Using proxy: " + proxyHost + ":" + proxyPort);
		}
		else {
			proxy = Proxy.NO_PROXY;
		}
	}
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
