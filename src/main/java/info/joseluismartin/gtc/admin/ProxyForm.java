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

import info.joseluismartin.gtc.model.ProxyConfig;
import info.joseluismartin.service.PersistentService;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class ProxyForm extends VerticalLayout implements ClickListener {

	private PersistentService<ProxyConfig, Integer> proxyService;
	private Form form = new Form();
	private Object[] visibleItemProperties = {
			"host", "port", "userName", "password"
	};
	private ProxyConfig proxy;
	private FormFieldFactory fieldFactory;
	private Button clear =  new Button("Clear");;
	private Button apply = new Button("Accept");
	
	public void init() {
		proxy = proxyService.get(1);
		BeanItem<ProxyConfig> item = new BeanItem<ProxyConfig>(proxy);
		form.setFormFieldFactory(fieldFactory);
		form.setItemDataSource(item);
		form.setVisibleItemProperties(visibleItemProperties);
		clear.addListener(this);
		apply.addListener(this);
		addComponent(form);
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		hl.addComponent(apply);
		hl.addComponent(clear);
		addComponent(hl);
	}

	/**
	 * {@inheritDoc}
	 */
	public void buttonClick(ClickEvent event) {
		if (apply.equals(event.getSource())) {
			form.commit();
			proxyService.save(proxy);
		}
		else if (clear.equals(event.getSource())) {
			proxy.setHost("");
			proxy.setPassword("");
			proxy.setPort(0);
			proxy.setUserName("");
			form.setItemDataSource(new BeanItem<ProxyConfig>(proxy));
			form.setVisibleItemProperties(visibleItemProperties);
			requestRepaint();
		}
	}

	/**
	 * @return the proxyService
	 */
	public PersistentService<ProxyConfig, Integer> getProxyService() {
		return proxyService;
	}

	/**
	 * @param proxyService the proxyService to set
	 */
	public void setProxyService(PersistentService<ProxyConfig, Integer> proxyService) {
		this.proxyService = proxyService;
	}

	/**
	 * @return the fieldFactory
	 */
	public FormFieldFactory getFieldFactory() {
		return fieldFactory;
	}

	/**
	 * @param fieldFactory the fieldFactory to set
	 */
	public void setFieldFactory(FormFieldFactory fieldFactory) {
		this.fieldFactory = fieldFactory;
	}
}
