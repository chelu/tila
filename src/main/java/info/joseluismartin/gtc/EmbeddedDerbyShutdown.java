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
package info.joseluismartin.gtc;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.Lifecycle;

/**
 * Lifecycle Spring listener to close Derby database connectins.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class EmbeddedDerbyShutdown implements Lifecycle {

	private static final Log log = LogFactory.getLog(EmbeddedDerbyShutdown.class);
	private boolean running = true;
	private String databaseUrl;
	
	
	/**
	 * {@inheritDoc}
	 */
	public void start() 			{
		running = true;
	}

	/**
	 * {@inheritDoc}
	 */
	public void stop() {
		try {
			DriverManager.getConnection("jdbc:derby:;shutdown=true");
		} catch (SQLException e) {
			log.error(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * @return the databaseUrl
	 */
	public String getDatabaseUrl() {
		return databaseUrl;
	}

	/**
	 * @param databaseUrl the databaseUrl to set
	 */
	public void setDatabaseUrl(String databaseUrl) {
		this.databaseUrl = databaseUrl;
	}

}
