package com.app.lottery.dao;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.app.lottery.bean.DreamBook;

public class DreamBookDAO extends BaseDao {

	public ArrayList<DreamBook> getDreamBook(String keyword, String code, 
			int limit, int offset) {
		Session session = getSession();
		ArrayList<DreamBook> list = new ArrayList<DreamBook>();
		try {
			session.beginTransaction();
			Criteria criteria = session.createCriteria(DreamBook.class);
			if (code != null) {
				criteria.add(Restrictions.like("code", "%" + code + "%"));
			}
			if (keyword != null) {
				criteria.add(Restrictions.or(
						Restrictions.like("dream", "%" + keyword + "%"),
						Restrictions.like("tag", "%" + keyword + "%")));
			}
			criteria.setFirstResult(offset);
			criteria.setMaxResults(limit);
			list = (ArrayList<DreamBook>) criteria.list();
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return list;
	}
	
	public int getNumberRecord() {
		Session session = getSession();
		int number = 0;
		try {
			session.beginTransaction();
			String sql = "SELECT count(id) FROM aia_lottery.dream_book";
			SQLQuery q = session.createSQLQuery(sql);
			Object obj = q.uniqueResult();
			number = ((Number) obj).intValue();
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return number;
	}
	
}
