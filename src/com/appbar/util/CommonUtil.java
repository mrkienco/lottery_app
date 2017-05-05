package com.appbar.util;

import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class CommonUtil {

	private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-Z]).{6,20})";
	private static final String USERNAME_PATTERN = "^[A-Za-z0-9_-]{6,20}$";
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public static String getSrcUrl(String baseUrl, String srcUrl) {
		String url = srcUrl;
		if (srcUrl != null && !srcUrl.startsWith("http")) {
			url = baseUrl + srcUrl;
		}
		return url;
	}

	public static String createAccessKey(String username) {
		String clearAcessKey = username + System.currentTimeMillis();
		return MD5Good.hash(clearAcessKey);
	}

	public static boolean validSignature(String clientSign, String clearSign) {
		if (clientSign == null) {
			return false;
		}
		if (Config.getInstance().getSecretEnable()) {
			String serverSign = MD5Good.hash(clearSign
					+ Config.getInstance().getSecretKey());
			return clientSign.equals(serverSign);
		} else {
			return true;
		}
	}

	public static boolean validUserName(String username) {
		Pattern pattern = Pattern.compile(USERNAME_PATTERN);
		Matcher matcher = pattern.matcher(username);
		return matcher.matches();
	}

	public static boolean validPassword(String clearPass) {
		Pattern pattern = Pattern.compile(USERNAME_PATTERN);
		Matcher matcher = pattern.matcher(clearPass);
		return matcher.matches();
	}

	public static boolean validEmail(String email) {
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
	public static String getAllParametersServlet(HttpServletRequest req) {
		StringBuffer strBuffer = new StringBuffer();
		Enumeration<String> parameterNames = req.getParameterNames();

		while (parameterNames.hasMoreElements()) {

			String paramName = parameterNames.nextElement();
			strBuffer.append(paramName + "=");

			String paramValues = req.getParameter(paramName);
			strBuffer.append(paramValues);
			if (parameterNames.hasMoreElements()) {
				strBuffer.append("&");
			}
		}
		return strBuffer.toString();
	}
	
	public static String getClientIpAddr(HttpServletRequest request) {  
        String ip = request.getHeader("X-Forwarded-For");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        return ip;  
    }  
}
