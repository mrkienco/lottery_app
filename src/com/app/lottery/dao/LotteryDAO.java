package com.app.lottery.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.app.lottery.bean.Jackpot;
import com.app.lottery.bean.Loto;
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
		String from_date = from + " 00:00:00";
		String to_date = to + " 23:59:59";
		try {
			session.beginTransaction();
			String sql = "SELECT * FROM lottery WHERE cat_id = " + cat_id + " AND gen_date >= '" + from_date
					+ "' AND gen_date <= '" + to_date + "'";
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
		String from_date = from + " 00:00:00";
		String to_date = to + " 23:59:59";
		try {
			session.beginTransaction();
			String sql = "SELECT * FROM lottery WHERE gen_date >= '" + from_date + "' AND gen_date <= '" + to_date
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
		String from_date = from + " 00:00:00";
		String to_date = to + " 23:59:59";
		try {
			session.beginTransaction();
			String sql = "SELECT l.* FROM lottery l, category c " + "WHERE l.cat_id = c.cat_id AND c.type_id = " + type
					+ " AND gen_date >= '" + from_date + "' AND gen_date <= '" + to_date + "'";
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
		from_date = from_date + " 00:00:00";
		to_date = to_date + " 23:59:59";
		try {
			session.beginTransaction();
			String sql = "SELECT * FROM (SELECT value, DATEDIFF(now(), max(gen_date)) as diff, max(gen_date) FROM loto WHERE gen_date >= '"
					+ from_date + "' and gen_date <= '" + to_date + "' AND cat_id = " + cate_id
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
		from_date = from_date + " 00:00:00";
		to_date = to_date + " 23:59:59";
		try {
			session.beginTransaction();
			String sql = "SELECT value, max(diff) FROM (SELECT value, timestampdiff(day, coalesce(@prev,gen_date),gen_date) diff, @prev\\:=gen_date FROM loto , (select @prev\\:=null) v WHERE cat_id = "
					+ cate_id + " AND gen_date >= '" + from_date + "' AND gen_date <= '" + to_date
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

	public JSONArray soi_cau(String time_start, String time_end, int cat_id) {
		JSONArray json = new JSONArray();
		Session session = getSession();
		List<String> merge_string = new ArrayList<>();
		try {
			session.beginTransaction();
			String sql = "SELECT * FROM lottery WHERE cat_id = " + cat_id + " AND date(gen_date) >= '" + time_start
					+ "' AND date(gen_date) <= '" + time_end + "' ORDER BY gen_date";
			SQLQuery q = session.createSQLQuery(sql);
			List<Object[]> _list = q.list();
			String str = "";
			for (int i = 0; i < _list.size(); i++) {
				if (((Number) _list.get(i)[3]).intValue() == 0) {
					if (i != 0)
						merge_string.add(str);
					str = "";
				}
				str += (String) _list.get(i)[4];
			}
			merge_string.add(str);

			List<LotoTrongNgay> lttn_list = new ArrayList<>();
			List<Loto> list = new LotoDAO().getLoto(cat_id, time_start, time_end);
			String day = "";
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			LotoTrongNgay lotoTrongNgay = new LotoTrongNgay("");
			for (int i = 0; i < list.size(); i++) {
				String _day = df.parse(list.get(i).getDay()).toString();
				if (!_day.equals(day)) {
					if (i != 0) {
						lttn_list.add(lotoTrongNgay);
					}
					day = _day;
					lotoTrongNgay = new LotoTrongNgay(_day);
				}
				String value = (String) list.get(i).getValue();
				lotoTrongNgay.addLoto(value);
				if (i == list.size() - 1)
					lttn_list.add(lotoTrongNgay);
			}

			for (int code_int = 0; code_int < 100; code_int++) {
				JSONObject obj = new JSONObject();
				JSONArray arr = new JSONArray();
				List<Integer> first_index = new ArrayList<>();
				List<Integer> second_index = new ArrayList<>();
				String code = "";
				if (code_int < 10)
					code = "0" + code_int;
				else
					code = "" + code_int;
				for (int i = 0; i < merge_string.get(merge_string.size() - 1).length(); i++) {
					if (merge_string.get(merge_string.size() - 1).charAt(i) == (code.charAt(0)))
						first_index.add(i);
					if (merge_string.get(merge_string.size() - 1).charAt(i) == (code.charAt(1)))
						second_index.add(i);
				}

				for (int fi : first_index) {
					for (int si : second_index) {
						if (fi >= si)
							continue;
						boolean co_cau = true;
						for (int i = merge_string.size() - 2; i >= 0; i--) {
							String _code = merge_string.get(i).charAt(fi) + "" + merge_string.get(i).charAt(si);
							if (lttn_list.get(i + 1).contains(_code)) {
							} else {
								co_cau = false;
								break;
							}
						}
						if (co_cau) {
							 JSONObject _obj = new JSONObject();
							 _obj.put("i1", fi);
							 _obj.put("i2", si);
							 arr.put(_obj);
						}
					}
				}
				if (arr.length() == 0)
					continue;
				obj.put("l", code);
				obj.put("v", arr);
				json.put(obj);
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

	private class LotoTrongNgay {
		private String ngay = "";
		List<String> list = new ArrayList<>();

		LotoTrongNgay(String ngay) {
			this.ngay = ngay;
		}

		String getNgay() {
			return ngay;
		}

		void addLoto(String l) {
			list.add(l);
		}

		List<String> getLoto() {
			return list;
		}

		String getLotoToString() {
			String ret = "";
			for (String s : list) {
				ret += " " + s;
			}
			return ret;
		}

		boolean contains(String l) {
			String _l = l.charAt(1) + "" + l.charAt(0);
			for (String s : list)
				if (s.equals(l) || s.equals(_l))
					return true;
			return false;
		}
	}
}
