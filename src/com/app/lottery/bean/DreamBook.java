package com.app.lottery.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.appbar.util.ExtParamsKey;
import com.appbar.util.json.JSONObject;

@Entity
@Table(name="dream_book")
public class DreamBook {

	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	
	@Column(name="dream")
	private String dream;
	
	@Column(name="code")
	private String code;
	
	@Column(name="tag")
	private String tag;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDream() {
		return dream;
	}

	public void setDream(String dream) {
		this.dream = dream;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		try {
			json.put(ExtParamsKey.ID, getId());
			json.put(ExtParamsKey.CONTENT, getDream());
			json.put(ExtParamsKey.CODE, getCode());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return json;
	}
}
