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

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vaadin.Application;
import com.vaadin.Application.WindowAttachListener;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Window;


/**
 * Tila Cache Server administration app.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class AdminApplication  extends Application  {
	
	private ApplicationContext ctx; 
	private TilaGuiFactory guiFactory;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init() {
		ctx = WebApplicationContextUtils.getWebApplicationContext(((WebApplicationContext) getContext())
				.getHttpSession().getServletContext());
		guiFactory = (TilaGuiFactory) ctx.getBean("guiFactory");
		
		setTheme("tila");
		Window mainWindow = new Window("Tila Administration");
		CustomLayout cl = new CustomLayout("main");
		Component main = guiFactory.getComponent("main");
		cl.addComponent(main, "main");
		cl.setSizeFull();
		mainWindow.setContent(cl);
		setMainWindow(mainWindow);
	}

	
}
