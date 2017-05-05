package com.app.lottery.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.appbar.util.ExtParamsKey;
import com.appbar.util.json.JSONObject;

@Entity
@Table(name="category")
public class Category {

	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	
	@Column(name="cat_id")
	private int cat_id;
	
	@Column(name="cat_name")
	private String cat_name;
	
	@Column(name="type_id")
	private int type_id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCat_id() {
		return cat_id;
	}

	public void setCat_id(int cat_id) {
		this.cat_id = cat_id;
	}

	public String getCat_name() {
		return cat_name;
	}

	public void setCat_name(String cat_name) {
		this.cat_name = cat_name;
	}

	public int getType_id() {
		return type_id;
	}

	public void setType_id(int type_id) {
		this.type_id = type_id;
	}
	
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		try {
			json.put(ExtParamsKey.ID, getCat_id());
			json.put(ExtParamsKey.TYPE, getType_id());
			json.put(ExtParamsKey.TITLE, getCat_name());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return json;
	}
	
}
