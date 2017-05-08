package com.app.lottery.res;

import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.app.lottery.bean.Jackpot;
import com.app.lottery.bean.Loto;
import com.app.lottery.bean.Lottery;
import com.app.lottery.bean.RequestStatus;
import com.app.lottery.dao.LotoDAO;
import com.app.lottery.dao.LotteryDAO;
import com.appbar.util.CommonUtil;
import com.appbar.util.Constant;
import com.appbar.util.ExtParamsKey;
import com.appbar.util.TimeUtils;
import com.appbar.util.json.JSONArray;
import com.appbar.util.json.JSONException;
import com.appbar.util.json.JSONObject;

@Path("lottery")
public class LotteryResource {

	@Context
	private ServletContext context;

	@Context
	private HttpServletRequest request;

	@GET
	@Path("/result/query")
	@Produces(MediaType.TEXT_HTML)
	public String getResult(@QueryParam(ExtParamsKey.CATE_ID) int cate_id, @QueryParam(ExtParamsKey.TYPE) int type,
			@QueryParam(ExtParamsKey.TIME_START) String time_start, @QueryParam(ExtParamsKey.TIME_END) String time_end,
			@QueryParam(ExtParamsKey.COUNT) int count, @QueryParam(ExtParamsKey.SIGN) String sign,
			@DefaultValue("-1") @QueryParam(ExtParamsKey.RANK) int rank, @QueryParam(ExtParamsKey.FIELD) String field) {
		JSONObject json = new JSONObject();
		try {
			boolean validSign = CommonUtil.validSignature(sign, time_end);
			// if (!validSign) {
			// json = buildErrorRes(RequestStatus.WRONG_SIGNATURE);
			// return json.toString();
			// }
			LotteryDAO lotteryDao = new LotteryDAO();
			LotoDAO lotoDao = new LotoDAO();
			if (field == null) {
				field = Constant.LOTTERY + "," + Constant.LOTO;
			}
			if (time_start == null || time_end == null) {
				ArrayList<String> daysFromCount = lotteryDao.getDayQuayThuong(count, cate_id);
				time_start = daysFromCount.get(daysFromCount.size() - 1);
				time_end = daysFromCount.get(0);
			}
			ArrayList<String> days = TimeUtils.listDayBetween(time_start, time_end);

			ArrayList<Lottery> lotteries = new ArrayList<Lottery>();
			ArrayList<Loto> lotos = new ArrayList<Loto>();
			if (field.contains(Constant.LOTTERY)) {
				if (cate_id > 0) {
					lotteries = lotteryDao.getLottery(cate_id, time_start, time_end);
				} else {
					lotteries = lotteryDao.getLotteryByType(type, time_start, time_end);
				}
			}
			if (field.contains(Constant.LOTO)) {
				if (cate_id > 0) {
					lotos = lotoDao.getLoto(cate_id, time_start, time_end);
				} else {
					lotos = lotoDao.getLotoByType(type, time_start, time_end);
				}
			}
			// Neu request query co loc theo rank
			if (rank >= 0) {
				ArrayList<Lottery> tempLottery = new ArrayList<Lottery>();
				for (Lottery lottery : lotteries) {
					if (lottery.getRank() == rank) {
						tempLottery.add(lottery);
					}
				}
				lotteries = tempLottery;
			}
			JSONArray jsonLottery = new JSONArray();
			for (String day : days) {
				JSONObject dayObj = new JSONObject();
				dayObj.put(ExtParamsKey.TIME, day);//
				JSONArray lottInDay = new JSONArray();
				JSONArray lotoInDay = new JSONArray();
				for (int i = 0; i < lotteries.size(); i++) {
					Lottery lottery = lotteries.get(i);
					if (lottery.getDay().equalsIgnoreCase(day)) {
						lottInDay.put(lottery.toJsonShort());
					}
				}
				for (int i = 0; i < lotos.size(); i++) {
					Loto loto = lotos.get(i);
					if (loto.getDay().equalsIgnoreCase(day)) {
						lotoInDay.put(loto.toJsonShort());
					}
				}
				dayObj.put(ExtParamsKey.LOTTERY, lottInDay);//
				dayObj.put(ExtParamsKey.LOTO, lotoInDay);//
				jsonLottery.put(dayObj);
			}
			json = buildErrorRes(RequestStatus.SUCCES);
			json.put(ExtParamsKey.CONTENT, jsonLottery);
		} catch (Exception ex) {
			ex.printStackTrace();
			json = buildErrorRes(RequestStatus.FAIL);
		}
		return json.toString();
	}

	@GET
	@Path("/analystics/loto")
	@Produces(MediaType.TEXT_HTML)
	public String analysticsLoto(@QueryParam(ExtParamsKey.CATE_ID) int cate_id,
			@QueryParam(ExtParamsKey.TIME_START) String time_start, @QueryParam(ExtParamsKey.TIME_END) String time_end,
			@QueryParam(ExtParamsKey.COUNT) int count) {
		JSONObject json = new JSONObject();
		try {
			LotteryDAO lotteryDao = new LotteryDAO();
			LotoDAO lotoDao = new LotoDAO();
			if (time_start == null || time_end == null) {
				ArrayList<String> daysFromCount = lotteryDao.getDayQuayThuong(count, cate_id);
				time_start = daysFromCount.get(daysFromCount.size() - 1);
				time_end = daysFromCount.get(0);
			}
			ArrayList<String> days = TimeUtils.listDayBetween(time_start, time_end);
			JSONArray jsonArr = lotoDao.getLotoCount(cate_id, time_start, time_end);
			json = buildErrorRes(RequestStatus.SUCCES);
			json.put(ExtParamsKey.CONTENT, jsonArr);
		} catch (Exception ex) {
			ex.printStackTrace();
			json = buildErrorRes(RequestStatus.FAIL);
		}
		return json.toString();
	}

	@GET
	@Path("/lo_gan")
	@Produces(MediaType.TEXT_HTML)
	public String loGan(@QueryParam(ExtParamsKey.CATE_ID) int cate_id,
			@QueryParam(ExtParamsKey.TIME_START) String time_start, @QueryParam(ExtParamsKey.TIME_END) String time_end,
			@QueryParam(ExtParamsKey.AMOUNT) int amount) {
		JSONObject json = new JSONObject();
		try {
			if (time_end == null) {
				time_end = TimeUtils.currentDay();
			}
			LotteryDAO lotteryDao = new LotteryDAO();
			JSONArray queryDataArr = lotteryDao.getLoGan(cate_id, time_start, time_end, amount);
			String currentDay = TimeUtils.currentDay();
			String lastestResultDay = lotteryDao.lastestDayResultCate(cate_id);
			if (!lastestResultDay.equals(currentDay)) {
				for (int i = 0; i < queryDataArr.length(); i++) {
					JSONObject oneObj = queryDataArr.getJSONObject(i);
					int t = oneObj.getInt(ExtParamsKey.AMOUNT);
					oneObj.put(ExtParamsKey.AMOUNT, t - 1);
				}
			}
			json = buildErrorRes(RequestStatus.SUCCES);
			json.put(ExtParamsKey.CONTENT, queryDataArr);
		} catch (Exception ex) {
			ex.printStackTrace();
			json = buildErrorRes(RequestStatus.FAIL);
		}
		return json.toString();
	}

	@GET
	@Path("/lo_max_gan")
	@Produces(MediaType.TEXT_HTML)
	public String loMaxGan(@QueryParam(ExtParamsKey.CATE_ID) int cate_id,
			@QueryParam(ExtParamsKey.TIME_START) String time_start,
			@QueryParam(ExtParamsKey.TIME_END) String time_end) {
		JSONObject json = new JSONObject();
		try {
			if (time_end == null) {
				time_end = TimeUtils.currentDay();
			}
			LotteryDAO lotteryDao = new LotteryDAO();
			JSONArray queryDataArr = lotteryDao.getLoMaxGan(cate_id, time_start, time_end);
			json = buildErrorRes(RequestStatus.SUCCES);
			json.put(ExtParamsKey.CONTENT, queryDataArr);
		} catch (Exception ex) {
			ex.printStackTrace();
			json = buildErrorRes(RequestStatus.FAIL);
		}
		return json.toString();
	}

	@GET
	@Path("/nearest_day")
	@Produces(MediaType.TEXT_HTML)
	public String nearest_day(@QueryParam(ExtParamsKey.CATE_ID) int cate_id, @QueryParam(ExtParamsKey.TYPE) int type) {
		JSONObject json = new JSONObject();
		try {
			String nearestDay = TimeUtils.getYesterday(TimeUtils.currentDay());
			LotteryDAO lotteryDAO = new LotteryDAO();
			if (cate_id > 0) {
				nearestDay = lotteryDAO.lastestDayResultCate(cate_id);
			} else if (type > 0) {
				nearestDay = lotteryDAO.lastestDayResultType(type);
			}
			json = buildErrorRes(RequestStatus.SUCCES);
			json.put(ExtParamsKey.TIME, nearestDay);
		} catch (Exception ex) {
			ex.printStackTrace();
			json = buildErrorRes(RequestStatus.FAIL);
		}
		return json.toString();
	}

	@GET
	@Path("/vietlott/jackpot")
	@Produces(MediaType.TEXT_HTML)
	public String vietlottJackpot(@QueryParam(ExtParamsKey.TIME_START) String time_start,
			@QueryParam(ExtParamsKey.TIME_END) String time_end) {
		JSONObject json = new JSONObject();
		try {
			LotteryDAO lotteryDao = new LotteryDAO();
			ArrayList<Jackpot> jackpots = lotteryDao.getJackpots(time_start, time_end);
			JSONArray jsonArr = new JSONArray();
			for (int i = 0; i < jackpots.size(); i++) {
				Jackpot jackpot = jackpots.get(i);
				jsonArr.put(jackpot.toJson());
			}
			json = buildErrorRes(RequestStatus.SUCCES);
			json.put(ExtParamsKey.CONTENT, jsonArr);
		} catch (Exception ex) {
			ex.printStackTrace();
			json = buildErrorRes(RequestStatus.FAIL);
		}
		return json.toString();
	}

	@GET
	@Path("/analytics/loto_by_day")
	@Produces(MediaType.TEXT_HTML)
	public String lotoByDay(@QueryParam("weeks") int weeks, @QueryParam("day_of_week") int day,
			@QueryParam(ExtParamsKey.CATE_ID) int cat_id) {
		JSONObject json = new JSONObject();
		if (cat_id == 0)
			cat_id = 1;
		try {
			JSONArray arr = new LotoDAO().getLotoByDay(weeks, day, cat_id);
			json.put("content", arr);
			json.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				json.put("status", 0);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		return json.toString();
	}

	@GET
	@Path("/analytics/quick_analytics")
	@Produces(MediaType.TEXT_HTML)
	public String quickAnalytics(@QueryParam("start_date") String date, @QueryParam("value") String value,
			@QueryParam(ExtParamsKey.CATE_ID) int cat_id) {
		JSONObject json = new JSONObject();
		try {
			if (cat_id == 0)
				cat_id = 1;
			LotoDAO dao = new LotoDAO();
			json.put("content", dao.quick_analytics(date, value, cat_id));
			json.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				json.put("status", 0);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		return json.toString();
	}

	@GET
	@Path("/analytics/thong_ke_tong")
	@Produces(MediaType.TEXT_HTML)
	public String thong_ke_theo_tong(@QueryParam(ExtParamsKey.TIME_START) String time_start,
			@QueryParam(ExtParamsKey.TIME_END) String time_end, @QueryParam("tong") String tong,
			@QueryParam(ExtParamsKey.CATE_ID) int cate_id) {
		JSONObject json = new JSONObject();
		try {
			if (cate_id == 0)
				cate_id = 1;
			json.put("content", new LotoDAO().get_tong(time_start, time_end, tong, cate_id));
			json.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				json.put("status", 0);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		return json.toString();
	}

	// ========================================================
	// PRIVATE METHOD
	// ========================================================

	private JSONObject buildErrorRes(RequestStatus status) {
		JSONObject json = new JSONObject();
		try {
			json.put(ExtParamsKey.STATUS, status.getCode());
			json.put(ExtParamsKey.MESSAGE, status.getMessage());
		} catch (Exception ex) {
		}
		return json;
	}
}
