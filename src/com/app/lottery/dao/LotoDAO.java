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
		String from_date = from + " 00:00:00";
		String to_date = to + " 23:59:59";
		try {
			session.beginTransaction();
			String sql = "SELECT * FROM loto WHERE cat_id = " + cat_id + " AND gen_date >= '" + from_date
					+ "' AND gen_date <= '" + to_date + "'";
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
		String from_date = from + " 00:00:00";
		String to_date = to + " 23:59:59";
		try {
			session.beginTransaction();
			String sql = "SELECT l.* FROM loto l, category c " + "WHERE l.cat_id = c.cat_id AND c.type_id = " + type
					+ " AND gen_date >= '" + from_date + "' AND gen_date <= '" + to_date + "'";
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
		String from_date = from + " 00:00:00";
		String to_date = to + " 23:59:59";
		try {
			session.beginTransaction();
			String sql = "SELECT value, count(id) FROM loto WHERE gen_date >= '" + from_date + "' AND gen_date <= '"
					+ to_date + "' AND cat_id = " + cat_id + " GROUP BY value";
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

	public JSONArray getLotoByDay(int weeks, int day, int cat_id) {
		JSONArray json = new JSONArray();
		Session session = getSession();
		try {
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

			session.beginTransaction();
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
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return json;
	}

	public JSONArray quick_analytics(String start_date, String end_date, String value, int cat_id) {
		Session session = getSession();
		JSONArray array = new JSONArray();
		try {
			String[] vals = {};
			if (value != null)
				vals = value.split(",");

			String sql = "SELECT value, count(id), max(gen_date) FROM loto WHERE date(gen_date) >= '" + start_date
					+ "' AND cat_id = " + cat_id + " AND date(gen_date) <= '" + end_date
					+ "' GROUP BY value ORDER BY count(id) DESC";
			if (vals.length > 0) {
				sql = "SELECT value, count(id), max(gen_date) FROM loto WHERE date(gen_date) >= '" + start_date
						+ "' AND cat_id = " + cat_id + " AND date(gen_date) <= '" + end_date + "' AND (";
				for (int i = 0; i < vals.length; i++) {
					if (i > 0)
						sql += " OR ";
					sql += " value = '" + vals[i] + "'";
				}
				sql += ") GROUP BY value ORDER BY count(id) DESC";
			}
			session.beginTransaction();
			SQLQuery q = session.createSQLQuery(sql);
			List<Object[]> list = q.list();
			list = q.list();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
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
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}

		return array;
	}

	public JSONArray get_tong(String time_start, String time_end, String tong, int cat_id) {
		Session session = getSession();
		JSONArray array = new JSONArray();
		try {
			String from_date = time_start + " 00:00:00";
			String to_date = time_end + " 23:59:59";
			List<String> _list = AnalyticsUtils.cac_so_co_tong_la(Integer.parseInt(tong));
			String sql = "SELECT count(id), value, max(gen_date) FROM loto WHERE cat_id = " + cat_id
					+ " AND gen_date >= '" + from_date + "' AND gen_date <= '" + to_date + "' AND (";
			for (int i = 0; i < _list.size(); i++) {
				if (i > 0)
					sql += " OR ";
				sql += " value = '" + _list.get(i) + "'";
			}
			sql += ") group by value order by value";
			session.beginTransaction();
			SQLQuery q = session.createSQLQuery(sql);
			List<Object[]> list = q.list();
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
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return array;
	}

	public JSONArray thong_ke_ngay_mai(String time_start, String time_end, int cate_id, String code)
			throws JSONException {
		Session session = getSession();
		JSONArray array = new JSONArray();
		try {
			session.beginTransaction();
			String sql = "select gen_date, value from aia_lottery.lottery where gen_date >= '" + time_start
					+ " 00:00:01' and gen_date <='" + time_end + " 23:59:59' and cat_id = " + cate_id
					+ " and rank = 0 and date(gen_date) IN (select date(DATE_ADD(gen_date,INTERVAL 1 DAY) ) from aia_lottery.lottery where value like '%"
					+ code + "' and gen_date >= '" + time_start + " 00:00:01' and gen_date <='" + time_end
					+ " 23:59:59' and cat_id = " + cate_id + " and rank = 0)";
			SQLQuery q = session.createSQLQuery(sql);
			List<Object[]> list = q.list();
			list = q.list();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			for (int i = 0; i < list.size(); i++) {
				String date = df.format(list.get(i)[0]);
				String _code = (String) list.get(i)[1];
				JSONObject js = new JSONObject();
				js.put("l", _code);
				js.put("t", date);
				array.put(js);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return array;
	}
}
