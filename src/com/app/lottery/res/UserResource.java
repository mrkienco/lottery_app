package com.app.lottery.res;

import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.app.lottery.bean.RequestStatus;
import com.app.lottery.bean.UserManager;
import com.app.lottery.dao.UserManagerDAO;
import com.appbar.util.CommonUtil;
import com.appbar.util.Constant;
import com.appbar.util.ExtParamsKey;
import com.appbar.util.json.JSONObject;
import com.sun.org.apache.bcel.internal.generic.GETFIELD;

@Path("user")
public class UserResource {
	@Context 
    private ServletContext context; 

    @Context
    private HttpServletRequest request;
    
    @POST
    @Path("/login")
    @Produces(MediaType.TEXT_HTML)
    public String login(@FormParam(ExtParamsKey.PLATFORM) String platform,
    		@FormParam(ExtParamsKey.VERSION) String version,
    		@FormParam(ExtParamsKey.DEVICE_NAME) String device_name,
    		@FormParam(ExtParamsKey.DEVICE_ID) String device_id,
    		@FormParam(ExtParamsKey.SIGN) String sign) {
    	JSONObject json = new JSONObject();
    	try {
    		if (platform == null || version == null || device_name == null 
    				|| device_id == null || sign == null) {
    			json = buildErrorRes(RequestStatus.PARAMS_INVALID);
    			return json.toString();
    		}
    		boolean validSign = CommonUtil.validSignature(sign, device_name + device_id);
    		if (!validSign) {
    			json = buildErrorRes(RequestStatus.WRONG_SIGNATURE);
    			return json.toString();
    		}
    		UserManagerDAO userDAO = new UserManagerDAO();
    		UserManager userCheck = userDAO.getUserManager(device_id);
    		if (userCheck != null) {
    			//User da co nick roi
    			json = buildErrorRes(RequestStatus.SUCCES);
    			json.put(ExtParamsKey.ID, userCheck.getId());
    			json.put(ExtParamsKey.USER_NAME, userCheck);
    			json.put(ExtParamsKey.TITLE, userCheck);
    			return json.toString();
    		}
    		//Dang ky user moi
    		UserManager userManager = new UserManager();
    		userManager.setPlatform(platform);
    		userManager.setReg_type(Constant.RegType.NORMAL);
    		userManager.setPhone("");
    		userManager.setVersion(version);
    		userManager.setGen_date(new Date(System.currentTimeMillis()));
    		userManager.setLast_login(new Date(System.currentTimeMillis()));
    		userManager.setDevice_name(device_name);
    		userManager.setDevice_id(device_id);
    		userManager.setLast_device(device_name + " | " + device_id);
    		userManager.setAccount_state(Constant.AccountState.ACTIVE);
    		userManager.setIp(CommonUtil.getClientIpAddr(request));
    		userDAO.insertUserManager(userManager);
    		//
    		UserManager userInserted = userDAO.getUserManager(device_id);
    		if (userInserted == null) {
    			json = buildErrorRes(RequestStatus.FAIL);
    		} else {
    			json = buildErrorRes(RequestStatus.SUCCES);
    			String username = "lott_" + userInserted.getId();
    			String usertitle = username;
    			userDAO.updateUserById(userInserted.getId(), "name", username);
    			userDAO.updateUserById(userInserted.getId(), "title", usertitle);
    			json.put(ExtParamsKey.ID, userInserted.getId());
    			json.put(ExtParamsKey.USER_NAME, username);
    			json.put(ExtParamsKey.TITLE, usertitle);
    		}
    	} catch (Exception ex) {
    		ex.printStackTrace();
    		json = buildErrorRes(RequestStatus.FAIL);
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
