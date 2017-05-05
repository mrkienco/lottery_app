package com.appbar.util;

public class Constant {

	public final static String SUCCESS = "success";
	public final static String FAILED = "failed";
	public final static String ACCESS_KEY = "access_key";
	
	public final static String SPLIT_PHAY = ",";
	public final static String SPLIT_NGANG = "-";
	
	public final static String LOTTERY = "lottery";
	public final static String LOTO = "loto";
	
	public final static String ASC = "asc";
	public final static String DESC = "desc";
	
	public class AccountState {
		public final static int ACTIVE = 1;
		public final static int INACTIVE = 0;
		public final static int BLOCKED = -1;
		public final static int EVER_LOGIN = 2;
	}
	
	public class UserMessage {
		public final static int TYPE_USER_SEND = 0;
		public final static int TYPE_ADMIN_SEND = 1;
		public final static int TYPE_PAYMENT = 2;
		public final static int TYPE_GIFT = 3;
		
		public final static int READ = 1;
		public final static int UNREAD = 0;
	}
	
	public class RegType {
		public final static int NORMAL = 0;
		public final static int FACEBOOK = 1;
		public final static int GOOGLEPLUS = 2;
	}
	
	public class UserManagerField {
		public final static String id = "id";
		public final static String name = "name";
		public final static String title = "title";
		public final static String avatar = "avatar";
		public final static String password = "password";
		public final static String money = "money";
		public final static String platform = "platform";
		public final static String reg_type = "reg_type";
		public final static String phone = "phone";
		public final static String version = "version";
		public final static String gen_date = "gen_date";
		public final static String last_login = "last_login";
		public final static String device_name = "device_name";
		public final static String device_id = "device_id";
		public final static String last_device = "last_device";
		public final static String account_state = "account_state";
	}
}
