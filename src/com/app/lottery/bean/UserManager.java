package com.app.lottery.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user_manager")
public class UserManager {

	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="title")
	private String title;
	
	@Column(name="avatar")
	private String avatar;
	
	@Column(name="password")
	private String password;
	
	@Column(name="money")
	private int money;
	
	@Column(name="platform")
	private String platform;
	
	@Column(name="reg_type")
	private int reg_type;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="version")
	private String version;
	
	@Column(name="gen_date")
	private Date gen_date;
	
	@Column(name="last_login")
	private Date last_login;
	
	@Column(name="device_name")
	private String device_name;
	
	@Column(name="device_id")
	private String device_id;
	
	@Column(name="last_device")
	private String last_device;
	
	@Column(name="account_state")
	private int account_state;
	
	@Column(name="ip")
	private String ip;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public int getReg_type() {
		return reg_type;
	}

	public void setReg_type(int reg_type) {
		this.reg_type = reg_type;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Date getGen_date() {
		return gen_date;
	}

	public void setGen_date(Date gen_date) {
		this.gen_date = gen_date;
	}

	public Date getLast_login() {
		return last_login;
	}

	public void setLast_login(Date last_login) {
		this.last_login = last_login;
	}

	public String getDevice_name() {
		return device_name;
	}

	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getLast_device() {
		return last_device;
	}

	public void setLast_device(String last_device) {
		this.last_device = last_device;
	}

	public int getAccount_state() {
		return account_state;
	}

	public void setAccount_state(int account_state) {
		this.account_state = account_state;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
}
