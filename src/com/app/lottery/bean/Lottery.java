package com.app.lottery.bean;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.appbar.util.ExtParamsKey;
import com.appbar.util.TimeUtils;
import com.appbar.util.json.JSONObject;

@Entity
@Table(name="lottery")
public class Lottery {

	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	
	@Column(name="cat_id")
	private int cat_id;
	
	@Column(name="gen_date")
	private Date gen_date;
	
	@Column(name="rank")
	private int rank;
	
	@Column(name="value")
	private String value;

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

	public Date getGen_date() {
		return gen_date;
	}

	public void setGen_date(Date gen_date) {
		this.gen_date = gen_date;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getDay() {
		return TimeUtils.getTimeString(this.getGen_date().getTime(), true);
	}
	
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		try {
			json.put(ExtParamsKey.CATE_ID, getCat_id());
			json.put(ExtParamsKey.TIME, TimeUtils.getTimeString(getGen_date().getTime(), true));
			json.put(ExtParamsKey.CODE, getValue());
			json.put(ExtParamsKey.RANK, getRank());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return json;
	}
	
	public JSONObject toJsonShort() {
		JSONObject json = new JSONObject();
		try {
			json.put(ExtParamsKey.CATE_ID, getCat_id());
			json.put(ExtParamsKey.RANK, getRank());
			json.put(ExtParamsKey.CODE, getValue());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return json;
	}
	
}
