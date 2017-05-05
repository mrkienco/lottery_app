/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.appbar.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author thinhnv
 */
public class TimeUtils {

	public final static long MILLIS_ONE_DAY = 24 * 60 *60 * 1000;
    public final static SimpleDateFormat normalTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    public final static SimpleDateFormat onlyDayFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    public static String currentDay() {
    	return getTimeString(System.currentTimeMillis(), true);
    }
    
    /**
     * Trả giá trị 1 nếu time nhỏ hơn hiện tại 1 ngày
     * Trả giá trị 0 nếu ngược lại
     * @param time
     * @return
     */
    public static int compareTimeStampAndNow(Timestamp time) {
        Calendar currentCal = Calendar.getInstance();
        Calendar lastLoginCal = Calendar.getInstance();
        lastLoginCal.setTimeInMillis(time.getTime());
        if (currentCal.get(Calendar.YEAR) > lastLoginCal.get(Calendar.YEAR)) {
            return 1;
        }
        if (currentCal.get(Calendar.MONTH) > lastLoginCal.get(Calendar.MONTH)) {
            return 1;
        }
        if (currentCal.get(Calendar.DATE) > lastLoginCal.get(Calendar.DATE)) {
            return 1;
        }
        return 0;
    }

    /**
     * Trả ra giá trị millis time của giờ. Định dạng giờ: hh:mm:ss
     * @param strHour
     * @return
     */
    public static long getTimeByHour(String strHour) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String data[] = strHour.split(":");
        int hour = Integer.parseInt(data[0]);
        int minute = Integer.parseInt(data[1]);
        int second = Integer.parseInt(data[2]);
        calendar.clear();
        calendar.set(year, month, day, hour, minute, second);
        return calendar.getTimeInMillis();
    }

    public static long getDayBetween(Calendar c1, Calendar c2) {
        return (c1.getTimeInMillis() - c2.getTimeInMillis()) / 86400000;
    }

    /**
     * Dua ra chuoi thoi gian dua vao thoi gian truyen vao dang millisecond
     * Chuoi dua ra co the bao gom day du ngay gio phut giay, co the chi bao gom
     * ngay thang nam (bien onlyDay)
     * @param timeMillis
     * @return
     */
    public static String getTimeString(long timeMillis, boolean onlyDay) {
        String format = "";
        if (onlyDay) {
            Date date = new Date(timeMillis);
            format = onlyDayFormat.format(date);
            return format;
        } else {
            Date date = new Date(timeMillis);
            format = normalTimeFormat.format(date);
            return format;
        }
    }

    /**
     * Dua ra thoi gian duoi dang giua hai khoang thoi gian
     * @param startTimeMillis
     * @param endTimeMillis
     * @param onlyDay
     * @return
     */
    public static String getBetweenTimeString(long startTimeMillis, long endTimeMillis, boolean onlyDay) {
        String format = "";
        if (onlyDay) {
            Date dateStart = new Date(startTimeMillis);
            Date dateEnd = new Date(endTimeMillis);
            format = onlyDayFormat.format(dateStart) + " đến " + onlyDayFormat.format(dateEnd);
        } else {
            Date dateStart = new Date(startTimeMillis);
            Date dateEnd = new Date(endTimeMillis);
            format = normalTimeFormat.format(dateStart) + " -> " + normalTimeFormat.format(dateEnd);
        }
        return format;
    }
    
    public static ArrayList<String> listDayBetween(String start, String end) {
    	ArrayList<String> days = new ArrayList<String>();
    	try {
    		long dayMillis = 24 * 60 * 60 * 1000;
	    	Date dateStart = onlyDayFormat.parse(start);
	    	Date dateEnd = onlyDayFormat.parse(end);
	    	Date tempDate = new Date(dateStart.getTime());
	    	days.add(start);
	    	while(tempDate.getTime() < dateEnd.getTime()) {
	    		tempDate.setTime(tempDate.getTime() + dayMillis);
	    		String tempDayStr = onlyDayFormat.format(tempDate);
	    		days.add(tempDayStr);
	    	}
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	return days;
    }
    
    public static String getYesterday(String day) {
    	String yesterday = day;
    	try {
    		Date date = onlyDayFormat.parse(day);
    		Date yesterdayDate = new Date(date.getTime() - MILLIS_ONE_DAY);
    		yesterday = onlyDayFormat.format(yesterdayDate);
    	} catch (Exception ex) {
    		
    	}
    	return yesterday;
    }
    
    public static String getTomorrow(String day) {
    	String tomorrow = day;
    	try {
    		Date date = onlyDayFormat.parse(day);
    		Date tomorrowDate = new Date(date.getTime() + MILLIS_ONE_DAY);
    		tomorrow = onlyDayFormat.format(tomorrowDate);
    	} catch (Exception ex) {
    		
    	}
    	return tomorrow;
    }
    
    public static String getFirstDayOfMonth() {
    	Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 1);
		Date firstDayOfMonth = cal.getTime();
		return getTimeString(firstDayOfMonth.getTime(), true);
    }
    
    public static String getLastDayOfMonth() {
    	Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
		Date lastDayOfMonth = cal.getTime();
		return getTimeString(lastDayOfMonth.getTime(), true);
    }
    
    public static String getFirstDayOfYear() {
    	Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		Date firstDayOfYear = cal.getTime();
		return getTimeString(firstDayOfYear.getTime(), true);
    }
    
    public static String getLastDayOfYear() {
    	Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 31);
		cal.set(Calendar.MONTH, Calendar.DECEMBER);
		Date lastDayOfYear = cal.getTime();
		return getTimeString(lastDayOfYear.getTime(), true);
    }
}
