package com.app.lottery.bean;

import java.math.BigInteger;
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
@Table(name="jackpot")
public class Jackpot {

	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	
	@Column(name="value")
	private BigInteger value;
	
	@Column(name="gen_date")
	private Date gen_date;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BigInteger getValue() {
		return value;
	}

	public void setValue(BigInteger value) {
		this.value = value;
	}

	public Date getGen_date() {
		return gen_date;
	}

	public void setGen_date(Date gen_date) {
		this.gen_date = gen_date;
	}
	
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		try {
			json.put(ExtParamsKey.TIME, TimeUtils.getTimeString(this.gen_date.getTime(), true));
			json.put(ExtParamsKey.AMOUNT, value.longValue());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return json;
	}
	
}
