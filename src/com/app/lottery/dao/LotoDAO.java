package com.app.lottery.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.app.lottery.bean.Loto;
import com.appbar.util.AnalyticsUtils;
import com.appbar.util.TimeUtils;
import com.appbar.util.json.JSONArray;
import com.appbar.util.json.JSONException;
import com.appbar.util.json.JSONObject;

import sun.util.locale.provider.TimeZoneNameUtility;

public class LotoDAO extends BaseDao {

	public ArrayList<Loto> getLoto(int cat_id, String day) {
		Session session = getSession();
		ArrayList<Loto> list = new ArrayList<Loto>();
		try {
			session.beginTransaction();
			String sql = "SELECT * FROM loto WHERE cat_id = " + cat_id + " AND DATE(gen_date) = '" + day + "'";
			SQLQuery q = session.createSQLQuery(sql).addEntity(Loto.class);
			list = (ArrayList<Loto>) q.list();
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return list;
	}

	public ArrayList<Loto> getLoto(int cat_id, String from, String to) {
		Session session = getSession();
		ArrayList<Loto> list = new ArrayList<Loto>();
		try {
			session.beginTransaction();
			String sql = "SELECT * FROM loto WHERE cat_id = " + cat_id + " AND DATE(gen_date) >= '" + from
					+ "' AND DATE(gen_date) <= '" + to + "'";
			SQLQuery q = session.createSQLQuery(sql).addEntity(Loto.class);
			list = (ArrayList<Loto>) q.list();
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return list;
	}

	public ArrayList<Loto> getLotoByType(int type, String from, String to) {
		Session session = getSession();
		ArrayList<Loto> list = new ArrayList<Loto>();
		try {
			session.beginTransaction();
			String sql = "SELECT l.* FROM loto l, category c " + "WHERE l.cat_id = c.cat_id AND c.type_id = " + type
					+ " AND DATE(gen_date) >= '" + from + "' AND DATE(gen_date) <= '" + to + "'";
			SQLQuery q = session.createSQLQuery(sql).addEntity(Loto.class);
			list = (ArrayList<Loto>) q.list();
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return list;
	}

	public JSONArray getLotoCount(int cat_id, String from, String to) {
		Session session = getSession();
		JSONArray jsonArr = new JSONArray();
		try {
			session.beginTransaction();
			String sql = "SELECT value, count(id) FROM loto WHERE date(gen_date) >= '" + from
					+ "' AND date(gen_date) <= '" + to + "' AND cat_id = " + cat_id + " GROUP BY value";
			SQLQuery q = session.createSQLQuery(sql);
			List<Object[]> list = q.list();
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = list.get(i);
				String code = (String) objs[0];
				int count = ((Number) objs[1]).intValue();
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("l", code);
				jsonObj.put("n", count);
				jsonArr.put(jsonObj);
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

	public String lastestDayResult() {
		Session session = getSession();
		String day = null;
		try {
			session.beginTransaction();
			String sql = "SELECT MAX(gen_date) FROM loto";
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
		return null;
	}

	public JSONArray getLotoByDay(int weeks, int day, int cat_id) throws JSONException {
		long k = (long) (weeks * 7) * 86400000;
		DateTimeFormatter pattern = DateTimeFormat.forPattern("yyyy-MM-dd");
		DateTime startDate = pattern.parseDateTime(TimeUtils.getTimeString(System.currentTimeMillis() - k, true));
		DateTime endDate = pattern.parseDateTime(TimeUtils.getTimeString(System.currentTimeMillis(), true));

		List<String> days = new ArrayList<>();
		while (startDate.isBefore(endDate)) {
			if (startDate.getDayOfWeek() == day) {
				days.add(startDate.toString().substring(0, startDate.toString().indexOf("T")));
			}
			startDate = startDate.plusDays(1);
		}

		Session session = getSession();
		session.beginTransaction();
		JSONArray json = new JSONArray();
		List<Object[]> list = new ArrayList<>();
		String sql = "SELECT value, count(id) FROM loto WHERE date(gen_date) IN (";
		for (int i = 0; i < days.size(); i++) {
			if (i > 0)
				sql += " , ";
			sql += "'" + days.get(i) + "'";
		}
		sql += ") GROUP BY value ORDER BY count(id) DESC";
		SQLQuery q = session.createSQLQuery(sql);
		list = q.list();
		for (int i = 0; i < list.size(); i++) {
			String code = (String) list.get(i)[0];
			int count = ((Number) list.get(i)[1]).intValue();
			JSONObject js = new JSONObject();
			js.put("n", count);
			js.put("l", code);
			json.put(js);
		}
		return json;
	}

	public JSONArray quick_analytics(String date, String value, int cat_id) throws Exception {
		Session session = getSession();
		session.beginTransaction();
		JSONArray array = new JSONArray();
		String[] vals = {};
		if (value != null)
			vals = value.split(",");

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date day = null;
		try {
			day = df.parse(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String day1 = TimeUtils.getTimeString(day.getTime(), true);
		String sql = "SELECT value, count(id), max(gen_date) FROM loto WHERE date(gen_date) >= '" + day1
				+ "' AND cat_id = " + cat_id + " AND date(gen_date) <= '"
				+ TimeUtils.getTimeString(System.currentTimeMillis(), true)
				+ "' GROUP BY value ORDER BY count(id) DESC";
		if (vals.length > 0) {
			sql = "SELECT value, count(id), max(gen_date) FROM loto WHERE date(gen_date) >= '" + day1
					+ "' AND cat_id = " + cat_id + " AND date(gen_date) <= '"
					+ TimeUtils.getTimeString(System.currentTimeMillis(), true) + "' AND ";
			for (int i = 0; i < vals.length; i++) {
				if (i > 0)
					sql += " OR ";
				sql += " value = '" + vals[i] + "'";
			}
			sql += " GROUP BY value ORDER BY count(id) DESC";
		}
		SQLQuery q = session.createSQLQuery(sql);
		List<Object[]> list = q.list();
		q = session.createSQLQuery(sql);
		list = q.list();
		for (int i = 0; i < list.size(); i++) {
			JSONObject value_json = new JSONObject();
			String code = (String) list.get(i)[0];
			int count = ((Number) list.get(i)[1]).intValue();
			String time = df.format(list.get(i)[2]);
			value_json.put("n", count);
			value_json.put("t", time);
			value_json.put("l", code);
			array.put(value_json);
		}

		return array;
	}

	public JSONArray get_tong(String time_start, String time_end, String tong, int cat_id) throws JSONException {
		List<String> _list = AnalyticsUtils.cac_so_co_tong_la(Integer.parseInt(tong));
		String sql = "SELECT count(id), value, max(gen_date) FROM loto WHERE cat_id = " + cat_id
				+ " AND date(gen_date) > '" + time_start + "' AND date(gen_date) < '" + time_end + "' AND (";
		for (int i = 0; i < _list.size(); i++) {
			if (i > 0)
				sql += " OR ";
			sql += " value = '" + _list.get(i) + "'";
		}
		sql += ") group by value order by value";
		Session session = getSession();
		session.beginTransaction();
		JSONArray array = new JSONArray();
		SQLQuery q = session.createSQLQuery(sql);
		List<Object[]> list = q.list();
		q = session.createSQLQuery(sql);
		list = q.list();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < list.size(); i++) {
			int count = ((Number) list.get(i)[0]).intValue();
			String code = (String) list.get(i)[1];
			String time = df.format(list.get(i)[2]);
			JSONObject js = new JSONObject();
			js.put("n", count);
			js.put("t", time);
			js.put("l", code);
			array.put(js);
		}
		return array;
	}
}