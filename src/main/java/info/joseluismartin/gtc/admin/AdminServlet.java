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

import java.io.BufferedWriter;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.vaadin.terminal.gwt.server.ApplicationServlet;

/**
 * Tila Application Servlet. Override writeAjaxPageHtmlHeader() to add 
 * OpenLayer and GoogleMap API.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class AdminServlet extends ApplicationServlet {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeAjaxPageHtmlHeader(BufferedWriter page, String title, String themeUri,
			HttpServletRequest request) throws IOException {
		super.writeAjaxPageHtmlHeader(page, title, themeUri, request);
		page.append("<script src='http://openlayers.org/api/OpenLayers.js'></script>");
		page.append("<script type=\"text/javascript\" src=\"https://maps.google.com/maps/api/js?sensor=false\"></script>");
	}

}
