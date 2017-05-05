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

import com.app.lottery.bean.Category;
import com.app.lottery.bean.RequestStatus;
import com.app.lottery.bean.Type;
import com.app.lottery.dao.CategoryDAO;
import com.appbar.util.Constant;
import com.appbar.util.ExtParamsKey;
import com.appbar.util.json.JSONArray;
import com.appbar.util.json.JSONObject;

@Path("category")
public class CategoryResource {

	@Context 
    private ServletContext context; 

    @Context
    private HttpServletRequest request;
    
    @GET
    @Path("/type")
    @Produces(MediaType.TEXT_HTML)
	public String getAllType() {
    	JSONObject json = new JSONObject();
    	try {
    		CategoryDAO categoryDao = new CategoryDAO();
    		ArrayList<Type> types = categoryDao.getAllType();
    		JSONArray jsonArr = new JSONArray();
    		for (int i = 0; i < types.size(); i++) {
    			Type type = types.get(i);
    			jsonArr.put(type.toJson());
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
    @Path("/category")
    @Produces(MediaType.TEXT_HTML)
    public String getAllCategory(@DefaultValue("-1") @QueryParam(ExtParamsKey.TYPE) int type) {
    	JSONObject json = new JSONObject();
    	try {
    		CategoryDAO categoryDao = new CategoryDAO();
    		ArrayList<Category> categories = new ArrayList<Category>();
    		if (type >= 0) {
    			categories = categoryDao.getAllCategory(type);
    		} else if (type < 0) {
    			categories = categoryDao.getAllCategory();
    		}
    		JSONArray jsonArr = new JSONArray();
    		for (int i = 0; i < categories.size(); i++) {
    			Category category = categories.get(i);
    			jsonArr.put(category.toJson());
    		}
    		json = buildErrorRes(RequestStatus.SUCCES);
    		json.put(ExtParamsKey.CONTENT, jsonArr);
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
