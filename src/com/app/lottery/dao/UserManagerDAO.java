package com.app.lottery.dao;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.app.lottery.bean.UserManager;

public class UserManagerDAO extends BaseDao {

	public UserManager getUserManager(String device_id) {
		Session session = getSession();
		UserManager userManager = null;
		try {
			session.beginTransaction();
			String sql = "SELECT * FROM user_manager WHERE device_id = '" + device_id + "' LIMIT 1";
			SQLQuery q = session.createSQLQuery(sql).addEntity(UserManager.class);
			Object obj = q.uniqueResult();
			if (obj != null) {
				userManager = (UserManager) obj;
			}
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return userManager;
	}
	
	public void insertUserManager(UserManager userManager) {
		this.insert(userManager);
	}
	
	public void updateUserById(int id, String field, String value) {
		Session session = getSession();
		try {
			session.beginTransaction();
			String sql = "UPDATE user_manager SET " + field + " = '" + value + "' WHERE id = " + id;
			SQLQuery q = session.createSQLQuery(sql);
			q.executeUpdate();
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
	}
	
	public void updateUserById(int id, String field, int value) {
		Session session = getSession();
		try {
			session.beginTransaction();
			String sql = "UPDATE user_manager SET " + field + " = " + value + " WHERE id = " + id;
			SQLQuery q = session.createSQLQuery(sql);
			q.executeUpdate();
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
	}
	
}
