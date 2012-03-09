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

import info.joseluismartin.vaadin.ui.Box;

import com.vaadin.Application;
import com.vaadin.terminal.ClassResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;


/**
 * Tila Cache Server administration app.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class AdminApplication  extends Application {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init() {
		Window mainWindow = new Window("Tila Administration");
		VerticalLayout layout = new VerticalLayout();
        layout.addComponent(createLogo());
		mainWindow.setContent(layout);
		setMainWindow(mainWindow);
	}
	
	private HorizontalLayout createLogo() {
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSizeFull();
		Embedded logo = new Embedded("", new ClassResource("/images/tila.png",this));
		hl.addComponent(logo);
		Box.addHorizontalGlue(hl);
		Embedded jlm = new Embedded("", new ClassResource("/images/jlm-logo.png", this));
		hl.addComponent(jlm);
		return hl;
	}

}
