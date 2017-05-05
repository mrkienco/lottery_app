package com.app.lottery.res;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.appbar.util.Config;
import com.appbar.util.Constant;
import com.appbar.util.ExtParamsKey;
import com.appbar.util.json.JSONObject;

@Path("app")
public class AppResource {
	@Context 
    private ServletContext context; 

    @Context
    private HttpServletRequest request;
    
    @GET
    @Path("/reloadconfig")
    @Produces(MediaType.TEXT_HTML)
    public String reloadConfig() {
    	JSONObject json = new JSONObject();
    	try {
    		Config.getInstance().load();
    		json.put(ExtParamsKey.RESULT, Constant.SUCCESS);
    		json.put("secret_key", Config.getInstance().getSecretKey());
    		json.put("sercret_enable", Config.getInstance().getSecretEnable());
    	} catch (Exception ex) {
    		ex.printStackTrace();
    		try {
    		json.put(ExtParamsKey.RESULT, Constant.FAILED);
    		json.put(ExtParamsKey.MESSAGE, ex.getMessage());
    		} catch (Exception e) {}
    	}
    	return json.toString();
    }
}
