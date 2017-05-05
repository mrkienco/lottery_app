package com.app.lottery.dao;

import java.util.ArrayList;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.app.lottery.bean.AppConfig;

public class CommonDAO extends BaseDao{

	public String getAppConfig(String key) {
		Session session = getSession();
		String value = "";
		try {
			session.beginTransaction();
			String sql = "SELECT * FROM app_config WHERE key_s = '" + key + "'";
			SQLQuery q = session.createSQLQuery(sql).addEntity(AppConfig.class);
			Object obj = q.uniqueResult();
			if (obj != null) {
				value = ((AppConfig) obj).getValue_s();
			}
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return value;
	}
	
	public ArrayList<AppConfig> getAllAppConfig() {
		Session session = getSession();
		ArrayList<AppConfig> configs = new ArrayList<AppConfig>();
		try {
			session.beginTransaction();
			String sql = "SELECT * FROM app_config";
			SQLQuery q = session.createSQLQuery(sql).addEntity(AppConfig.class);
			configs = (ArrayList<AppConfig>) q.list();
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return configs;
	}
	
}
