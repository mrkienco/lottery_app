package com.app.lottery.dao;

import org.hibernate.Session;

import com.appbar.util.HibernateUtil;

public class BaseDao extends HibernateUtil{

	public BaseDao() {
	}

	public Object insert(Object o) {
		Session session = getSession();
		session.beginTransaction();
		session.save(o);
		session.getTransaction().commit();
		session.close();
		return o;
	}

	public void delete(Object o) {
		Session session = getSession();
		session.beginTransaction();
		session.delete(o);
		session.getTransaction().commit();
		session.close();
	}

	public void update(Object o) {
		Session session = getSession();
		session.beginTransaction();
		session.update(o);
		session.getTransaction().commit();
		session.close();
	}
}
