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

import info.joseluismartin.gtc.model.SystemConfig;
import info.joseluismartin.vaadin.ui.form.FormEditor;
import info.joseluismartin.vaadin.ui.form.SaveAction;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button.ClickEvent;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class ConfigSaveAction extends SaveAction {

	@SuppressWarnings("unchecked")
	@Override
	public void buttonClick(ClickEvent event) {
		FormEditor editor = (FormEditor) getForm();
		editor.commit();
		BeanItem<SystemConfig> item = (BeanItem<SystemConfig>) editor.getItemDataSource();
		item.getBean().save();
	}
}
