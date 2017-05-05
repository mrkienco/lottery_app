package com.appbar.util;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsUtils {
	public static List<String> cac_so_co_tong_la(int tong) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				int k = i + j;
				if ((k + "").endsWith((tong + "")))
					list.add(i + "" + j);
			}
		}
		return list;
	}
}
