package com.app.lottery.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="app_config")
public class AppConfig {

	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	
	@Column(name="key_s")
	private String key_s;
	
	@Column(name="value_s")
	private String value_s;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKey_s() {
		return key_s;
	}

	public void setKey(String key) {
		this.key_s = key;
	}

	public String getValue_s() {
		return value_s;
	}

	public void setValue_s(String value_s) {
		this.value_s = value_s;
	}
	
}
