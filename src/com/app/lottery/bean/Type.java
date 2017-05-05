package com.app.lottery.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.appbar.util.ExtParamsKey;
import com.appbar.util.json.JSONObject;

@Entity
@Table(name="type")
public class Type {

	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	
	@Column(name="type_id")
	private int type_id;
	
	@Column(name="type_name")
	private String type_name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType_id() {
		return type_id;
	}

	public void setType_id(int type_id) {
		this.type_id = type_id;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		try {
			json.put(ExtParamsKey.ID, getType_id());
			json.put(ExtParamsKey.TITLE, getType_name());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return json;
	}
}
