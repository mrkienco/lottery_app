package com.app.lottery.res;

import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.app.lottery.bean.DreamBook;
import com.app.lottery.bean.RequestStatus;
import com.app.lottery.dao.DreamBookDAO;
import com.appbar.util.Config;
import com.appbar.util.Constant;
import com.appbar.util.ExtParamsKey;
import com.appbar.util.json.JSONArray;
import com.appbar.util.json.JSONObject;

@Path("dreambook")
public class DreamBookResource {

	@Context 
    private ServletContext context; 

    @Context
    private HttpServletRequest request;
    
    @GET
    @Path("/query")
    @Produces(MediaType.TEXT_HTML)
    public String query(@QueryParam(ExtParamsKey.KEYWORD) String keyword,
    		@QueryParam(ExtParamsKey.CODE) String code,
    		@QueryParam(ExtParamsKey.RANGE) String range) {
    	JSONObject json = new JSONObject();
    	try {
    		DreamBookDAO dreamBookDAO = new DreamBookDAO();
    		//
			int limit = 80;
    		int offset = 0;
    		if (range != null) {
    			String splits[] = range.split(Constant.SPLIT_NGANG);
    			offset = Integer.parseInt(splits[0]);
    			limit = Integer.parseInt(splits[1]) - Integer.parseInt(splits[0]);
    		}
    		ArrayList<DreamBook> list = dreamBookDAO.getDreamBook(keyword, code, limit, offset);
    		json = buildErrorRes(RequestStatus.SUCCES);
    		JSONObject metaObj = new JSONObject();
    		JSONArray jsonArr = new JSONArray();
    		for (int i = 0; i < list.size(); i++) {
    			jsonArr.put(list.get(i).toJson());
    		}
    		int totalRecords = dreamBookDAO.getNumberRecord();
    		metaObj.put(ExtParamsKey.AMOUNT, totalRecords);
    		json.put(ExtParamsKey.CONTENT, jsonArr);
    		json.put(ExtParamsKey.META, metaObj);
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
