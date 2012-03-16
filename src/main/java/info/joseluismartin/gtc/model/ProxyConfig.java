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
package info.joseluismartin.gtc.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */

@Entity
@Table(name="proxy")
public class ProxyConfig implements Serializable {
	
	@Id
	private Integer id = 1;
	private String host;
	private Integer port;
	private String userName;
	private String password;
	private boolean directConnection;
	
	public boolean isValid() {
		return port != 0;
	}
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}
	
	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}
	
	/**
	 * @return the port
	 */
	public Integer getPort() {
		return port;
	}
	
	/**
	 * @param port the port to set
	 */
	public void setPort(Integer port) {
		this.port = port;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the user to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the directConnection
	 */
	public boolean isDirectConnection() {
		return directConnection;
	}

	/**
	 * @param directConnection the directConnection to set
	 */
	public void setDirectConnection(boolean directConnection) {
		this.directConnection = directConnection;
	}
}
