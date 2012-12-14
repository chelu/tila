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
import info.joseluismartin.gtc.model.ProxyConfig;
import info.joseluismartin.service.PersistentService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UrlPathHelper;

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

	private final static Log log = LogFactory.getLog(CacheController.class);

	private String diskCachePath;
	private boolean useProxy;
	private String proxyUser;
	private String proxyPassword;
	private String proxyHost;
	private int proxyPort;
	private Proxy proxy = Proxy.NO_PROXY;
	@Autowired
	private CacheManager cacheService;
	@Resource 
	private PersistentService<ProxyConfig, Integer> proxyService;
	
	private Pattern URI_PATTERN = Pattern.compile("/([a-z-]+)/?(.*)");


	/**
	 * Parse request and create a new Tile.
	 * Try to find tile in memory cache first, if not found, try to read from disk, if not found,
	 * download tile from server and write it to cache.
	 * @throws ServletException 
	 */
	@RequestMapping("/**")
	protected void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		UrlPathHelper pathHelper = new UrlPathHelper();
		String requestString = pathHelper.getPathWithinApplication(req);
		
		if (requestString == null)
			throw new ServletException("The request must include a cache path");
			
		
		if (req.getQueryString() != null) 
			requestString += "?" + req.getQueryString();
		
		String cachePath = null;
		String query = null;
		Matcher m  = URI_PATTERN.matcher(requestString);
		
		if (m.find()) {
			cachePath = m.group(1);
			query = pathHelper.decodeRequestString(req, m.group(2));
			if (log.isDebugEnabled()) {
				log.debug("received request: cachePath [" + cachePath + "] query [" + query + "]");
			}
		}

		TileCache cache = cacheService.findCache(cachePath);

		if (cache == null) {
			throw new ServletException("There is not tile cache configured for path [" + cachePath + "]");
		}
		
		// Have a cache to handle request, now test the tile
		Tile tile = cache.getTile(query);
		
		String remoteUrlString = cache.getServerUrl(query) + 
				(query.startsWith("?") ? query :  "/" + query);
		URL remoteUrl = new URL(remoteUrlString);
		
		if (tile == null) { 
			proxyConnection(req, resp, requestString, cache, remoteUrlString, remoteUrl);
		}
		else {  
			if (tile.isEmpty()) {  
				// try three times...
				for (int i= 0; i < 3; i++) {
					try {
						downloadTile(tile, remoteUrl);
						break;
					}catch (IOException ioe) {
						log.error("Error downloading the tile, try: " + i, ioe);
					}
				}
				cache.storeTile(tile);   
			}
			resp.setContentType(tile.getMimeType());
			resp.getOutputStream().write(tile.getImage());
		}
	}

	private void proxyConnection(HttpServletRequest req, HttpServletResponse resp, String requestString,
			TileCache cache, String remoteUrlString, URL remoteUrl) throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("Can't parse tile url [" + requestString +"], proxy to: " + remoteUrlString);
		}
		
		URLConnection conn = getConnection(remoteUrl);
		resp.setContentType(conn.getContentType());
		InputStream is = cache.parseResponse(conn.getInputStream(), remoteUrlString, getContextUrl(req));
		IOUtils.copy(is, resp.getOutputStream());
	
	}

	/**
	 * @param req
	 * @return
	 */
	private String getContextUrl(HttpServletRequest req) {
	
	    StringBuffer url = new StringBuffer ();
	    String scheme = req.getScheme ();
		int port = req.getServerPort ();
		String servletPath = req.getServletPath ();
		String contextPaht = req.getContextPath();
		url.append (scheme);	
		url.append ("://");
		url.append (req.getServerName ());
			
		if ((scheme.equals ("http") && port != 80)
				|| (scheme.equals ("https") && port != 443)) {
			    url.append (':');
			    url.append (req.getServerPort ());
			}
		
		if (contextPaht != null)
			url.append(contextPaht);
		
		if (servletPath != null)
			url.append (servletPath);
		
		return url.toString();
	}



	private TileCache getTileCache(String cachePath) {
		return cacheService.findCache(cachePath);
	}


	/**
	 * Download tile from tile server
	 * @param tile
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private void downloadTile(Tile tile, URL url) throws MalformedURLException, IOException {
		InputStream is = getServerStream(url);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		IOUtils.copy(is, baos);
		tile.setImage(baos.toByteArray());
	}

	/**
	 * Connect to remote server and get connection input stream
	 * @param url url to connect
	 * @return connection InputStream
	 * @throws IOException
	 */
	private InputStream getServerStream(URL url) throws IOException {
		URLConnection conn = getConnection(url);
		InputStream is = conn.getInputStream();
		return is;
	}

	private URLConnection getConnection(URL url) throws IOException {
		URLConnection conn = url.openConnection(proxy);
		conn.setRequestProperty("User-Agent", USER_AGENT);
		return conn;
	}	


	public void init() throws ServletException {
		List<ProxyConfig> list = proxyService.getAll();
		
		if (!list.isEmpty()) {
			ProxyConfig config = list.get(0);
			if (!config.isDirectConnection()) {
				proxyHost = config.getHost();
				proxyPort = config.getPort();
				proxyUser = config.getUserName();
				proxyPassword = config.getPassword();
				SocketAddress address = new InetSocketAddress(proxyHost,proxyPort);
				proxy = new Proxy(Proxy.Type.HTTP, address);
				Authenticator.setDefault(new SimpleAuthenticator(proxyUser, proxyPassword));
				log.info("Using proxy: " + proxyHost + ":" + proxyPort);
			}
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


