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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vaadin.data.Buffered;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class ProxyForm extends Form implements ClickListener {

	private static final Log log = LogFactory.getLog(ProxyForm.class);
//	private static final String HOST = "host";
	private static final String PORT = "port";
	
	private PersistentService<ProxyConfig, Integer> proxyService;
	private Object[] visibleItemProperties = {
			"directConnection", "host", "port", "userName", "password"
	};
	private ProxyConfig proxy;
	private Button clear =  new Button("Clear");;
	private Button apply = new Button("Accept");
	
	Validator portValidator = new IntegerValidator("Port must be a number");
	
	
	public void init() {
		proxy = proxyService.get(1);
		BeanItem<ProxyConfig> item = new BeanItem<ProxyConfig>(proxy);
		setCaption("Proxy Configuration");
		setItemDataSource(item);
		setVisibleItemProperties(visibleItemProperties);
		clear.addListener((Button.ClickListener) this);
		apply.addListener((Button.ClickListener) this);
		HorizontalLayout hl = (HorizontalLayout) getFooter();
		hl.setSpacing(true);
		hl.addComponent(apply);
		hl.addComponent(clear);
		addValidators();
	}

	/**
	 * 
	 */
	private void addValidators() {
		getField(PORT).addValidator(new IntegerValidator("Port must be a number"));
	}

	/**
	 * {@inheritDoc}
	 */
	public void buttonClick(ClickEvent event) {
		if (apply.equals(event.getSource())) {
			if (StringUtils.isBlank((String) getField(PORT).getValue()))
				getField(PORT).setValue("0");
			try {
				commit();
			}
			catch (Buffered.SourceException e) {
				log.error(e);
			}
			proxyService.save(proxy);
		}
		else if (clear.equals(event.getSource())) {
			proxy.setHost("");
			proxy.setPassword("");
			proxy.setPort(0);
			proxy.setUserName("");
			setItemDataSource(new BeanItem<ProxyConfig>(proxy));
			setVisibleItemProperties(visibleItemProperties);
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
}
