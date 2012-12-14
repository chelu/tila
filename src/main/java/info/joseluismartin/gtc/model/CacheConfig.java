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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
@Entity
@Table(name="caches")
public class CacheConfig implements Serializable {
	
	
	@Id
	@GeneratedValue
	private Long id;
	private String name ="";
	private String path = "";
	private String url = "";
	private Integer size = 10000;
	private boolean active = Boolean.TRUE;
	@ManyToOne
	@JoinColumn(name="type")
	private CacheType type;
	@Transient
	private String diskCachePath = "";
	/** age of cache on days */
	private int age;
	
	public String toString() {
		return name;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * @return the remote url
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * @param url the remoteUrl to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the size
	 */
	public Integer getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(Integer size) {
		this.size = size;
	}

	/**
	 * @return the type
	 */
	public CacheType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(CacheType type) {
		this.type = type;
	}

	/**
	 * @return the diskCachePath
	 */
	public String getDiskCachePath() {
		return diskCachePath;
	}

	/**
	 * @param diskCachePath the diskCachePath to set
	 */
	public void setDiskCachePath(String diskCachePath) {
		this.diskCachePath = diskCachePath;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}
}
