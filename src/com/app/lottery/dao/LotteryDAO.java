package com.app.lottery.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.app.lottery.bean.Jackpot;
import com.app.lottery.bean.Lottery;
import com.appbar.util.ExtParamsKey;
import com.appbar.util.TimeUtils;
import com.appbar.util.json.JSONArray;
import com.appbar.util.json.JSONObject;

public class LotteryDAO extends BaseDao {

	public ArrayList<Lottery> getLottery(int cat_id, String day) {
		Session session = getSession();
		ArrayList<Lottery> list = new ArrayList<Lottery>();
		try {
			session.beginTransaction();
			String sql = "SELECT * FROM lottery WHERE cat_id = " + cat_id + " AND DATE(gen_date) = '" + day + "'";
			SQLQuery q = session.createSQLQuery(sql).addEntity(Lottery.class);
			list = (ArrayList<Lottery>) q.list();
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return list;
	}

	public ArrayList<Lottery> getLottery(int cat_id, String from, String to) {
		Session session = getSession();
		ArrayList<Lottery> list = new ArrayList<Lottery>();
		try {
			session.beginTransaction();
			String sql = "SELECT * FROM lottery WHERE cat_id = " + cat_id + " AND DATE(gen_date) >= '" + from
					+ "' AND DATE(gen_date) <= '" + to + "'";
			SQLQuery q = session.createSQLQuery(sql).addEntity(Lottery.class);
			list = (ArrayList<Lottery>) q.list();
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return list;
	}

	public ArrayList<Lottery> getLottery(String from, String to) {
		Session session = getSession();
		ArrayList<Lottery> list = new ArrayList<Lottery>();
		try {
			session.beginTransaction();
			String sql = "SELECT * FROM lottery WHERE DATE(gen_date) >= '" + from + "' AND DATE(gen_date) <= '" + to
					+ "'";
			SQLQuery q = session.createSQLQuery(sql).addEntity(Lottery.class);
			list = (ArrayList<Lottery>) q.list();
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return list;
	}

	public ArrayList<Lottery> getLotteryByType(int type, String from, String to) {
		Session session = getSession();
		ArrayList<Lottery> list = new ArrayList<Lottery>();
		try {
			session.beginTransaction();
			String sql = "SELECT l.* FROM lottery l, category c " + "WHERE l.cat_id = c.cat_id AND c.type_id = " + type
					+ " AND DATE(gen_date) >= '" + from + "' AND DATE(gen_date) <= '" + to + "'";
			SQLQuery q = session.createSQLQuery(sql).addEntity(Lottery.class);
			list = (ArrayList<Lottery>) q.list();
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return list;
	}

	public String lastestDayResultCate(int cate_id) {
		Session session = getSession();
		String day = null;
		try {
			session.beginTransaction();
			String sql = "SELECT MAX(gen_date) FROM lottery WHERE cat_id = " + cate_id;
			SQLQuery q = session.createSQLQuery(sql);
			List<Object> list = q.list();
			if (list.size() > 0) {
				Date maxDate = (Date) list.get(0);
				day = TimeUtils.getTimeString(maxDate.getTime(), true);
			}
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return day;
	}

	public String lastestDayResultType(int type) {
		Session session = getSession();
		String day = null;
		try {
			session.beginTransaction();
			String sql = "SELECT MAX(l.gen_date) FROM lottery l, category c WHERE l.cat_id = c.cat_id AND c.type_id = "
					+ type;
			SQLQuery q = session.createSQLQuery(sql);
			List<Object[]> list = q.list();
			if (list.size() > 0) {
				Date maxDate = (Date) list.get(0)[0];
				day = TimeUtils.getTimeString(maxDate.getTime(), true);
			}
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return day;
	}

	public ArrayList<String> getDayQuayThuong(int limit, int cate_id) {
		Session session = getSession();
		ArrayList<String> list = new ArrayList<String>();
		try {
			session.beginTransaction();
			String sql = "SELECT DISTINCT DATE(gen_date) FROM lottery " + "WHERE cat_id = " + cate_id
					+ " ORDER BY DATE(gen_date) DESC LIMIT " + limit;
			SQLQuery q = session.createSQLQuery(sql);
			List<Object> results = q.list();
			for (int i = 0; i < results.size(); i++) {
				Object objs = results.get(i);
				Date date = (Date) objs;
				String day = TimeUtils.getTimeString(date.getTime(), true);
				list.add(day);
			}
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return list;
	}

	public JSONArray getLoGan(int cate_id, String from_date, String to_date, int bien_do_gan) {
		Session session = getSession();
		JSONArray jsonArr = new JSONArray();
		try {
			session.beginTransaction();
			String sql = "SELECT * FROM (SELECT value, DATEDIFF(now(), max(gen_date)) as diff, max(gen_date) FROM loto WHERE date(gen_date) >= '"
					+ from_date + "' and date(gen_date) <= '" + to_date + "' AND cat_id = " + cate_id
					+ " GROUP BY value) c WHERE c.diff > 10 ORDER BY c.diff DESC";
			SQLQuery q = session.createSQLQuery(sql);
			List<Object[]> list = q.list();
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = list.get(i);
				String code = (String) objs[0];
				int amount = (Integer) objs[1];// So ngay lo gan
				Date lastestDate = (Date) objs[2];
				JSONObject json = new JSONObject();
				json.put(ExtParamsKey.CODE, code);
				json.put(ExtParamsKey.AMOUNT, amount);
				json.put(ExtParamsKey.TIME, TimeUtils.getTimeString(lastestDate.getTime(), true));
				jsonArr.put(json);
			}
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return jsonArr;
	}

	public JSONArray getLoMaxGan(int cate_id, String from_date, String to_date) {
		Session session = getSession();
		JSONArray jsonArr = new JSONArray();
		try {
			session.beginTransaction();
			String sql = "SELECT value, max(diff) FROM (SELECT value, timestampdiff(day, coalesce(@prev,gen_date),gen_date) diff, @prev\\:=gen_date FROM loto , (select @prev\\:=null) v WHERE cat_id = "
					+ cate_id + " AND DATE(gen_date) >= '" + from_date + "' AND DATE(gen_date) <= '" + to_date
					+ "' order by value, gen_date ) sq group by value";
			SQLQuery q = session.createSQLQuery(sql);
			List<Object[]> list = q.list();
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = list.get(i);
				String code = (String) objs[0];
				int amount = ((Number) objs[1]).intValue();
				JSONObject json = new JSONObject();
				json.put(ExtParamsKey.CODE, code);
				json.put(ExtParamsKey.AMOUNT, amount - 1);
				jsonArr.put(json);
			}
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return jsonArr;
	}

	public ArrayList<Jackpot> getJackpots(String from, String to) {
		Session session = getSession();
		ArrayList<Jackpot> jackpots = new ArrayList<Jackpot>();
		try {
			session.beginTransaction();
			String sql = "SELECT * FROM jackpot WHERE DATE(gen_date) >= '" + from + "' AND DATE(gen_date) <= '" + to
					+ "'";
			SQLQuery q = session.createSQLQuery(sql).addEntity(Jackpot.class);
			jackpots = (ArrayList<Jackpot>) q.list();
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return jackpots;
	}
}
