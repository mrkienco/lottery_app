package com.appbar.util;

import java.util.ArrayList;
import java.util.Properties;

import com.app.lottery.bean.AppConfig;
import com.app.lottery.dao.CommonDAO;


public class Config {
	// =================================================
	// VARIABLES
	// =================================================

	public final static String PROPERTIES_PATH = "config.properties";

	private Properties properties;
	private static Config INSTANCE;
	
	private String secretKey = "8c24516c23b611420defccf253598412";
	private boolean secretEnable = true;

	// =================================================
	// CONTRUCTORS
	// =================================================

	public Config() {
		load();
	}

	public static Config getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Config();
		}
		return INSTANCE;
	}
	
	public void load() {
		CommonDAO commonDao = new CommonDAO();
		ArrayList<AppConfig> configs = commonDao.getAllAppConfig();
		for (int i = 0; i < configs.size(); i++) {
			AppConfig appConfig = configs.get(i);
			if (appConfig.getKey_s().equals("app_secret_key")) {
				secretKey = appConfig.getValue_s();
			} else if (appConfig.getKey_s().equals("app_secret_enable")) {
				secretEnable = appConfig.getValue_s().equals("true");
			}
		}
	}
	
	public String getSecretKey() {
		return secretKey;
	}
	
	public boolean getSecretEnable() {
		return secretEnable;
	}

}
