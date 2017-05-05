package com.app.lottery.dao;

import java.util.ArrayList;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.app.lottery.bean.Category;
import com.app.lottery.bean.Type;

public class CategoryDAO extends BaseDao{

	public ArrayList<Type> getAllType() {
		Session session = getSession();
		ArrayList<Type> list = new ArrayList<Type>();
		try {
			session.beginTransaction();
			String sql = "SELECT * FROM type";
			SQLQuery q = session.createSQLQuery(sql).addEntity(Type.class);
			list = (ArrayList<Type>) q.list();
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return list;
	}
	
	public ArrayList<Category> getAllCategory() {
		Session session = getSession();
		ArrayList<Category> list = new ArrayList<Category>();
		try {
			session.beginTransaction();
			String sql = "SELECT * FROM category";
			SQLQuery q = session.createSQLQuery(sql).addEntity(Category.class);
			list = (ArrayList<Category>) q.list();
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return list;
	}
	
	public ArrayList<Category> getAllCategory(int type) {
		Session session = getSession();
		ArrayList<Category> list = new ArrayList<Category>();
		try {
			session.beginTransaction();
			String sql = "SELECT * FROM category WHERE type_id = " + type;
			SQLQuery q = session.createSQLQuery(sql).addEntity(Category.class);
			list = (ArrayList<Category>) q.list();
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return list;
	}
	
}
