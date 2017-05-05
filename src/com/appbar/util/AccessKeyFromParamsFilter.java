package com.appbar.util;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

public class AccessKeyFromParamsFilter implements Filter{
	
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) 
			throws IOException, ServletException {
		// Use HTTP specific requests.
	    doHttpFilter((HttpServletRequest)req, (HttpServletResponse)res, chain);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}
	
	private void doHttpFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) 
		     throws IOException, ServletException {	
		HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(req);
		String paramsAccessKey = null;
//		System.out.println("URL : " + req.getRequestURL().toString() + "Params : " + CommonUtil.getAllParametersServlet(req));
		if (req.getParameter(Constant.ACCESS_KEY) != null) {
			paramsAccessKey = req.getParameter(Constant.ACCESS_KEY);
		}
		String headerAccessKey = req.getHeader(Constant.ACCESS_KEY);
		if (headerAccessKey == null && paramsAccessKey != null) {
			requestWrapper.addHeader(Constant.ACCESS_KEY, paramsAccessKey);
		}
		chain.doFilter(requestWrapper, res);
	}
	
	public class HeaderMapRequestWrapper extends HttpServletRequestWrapper {
        /**
         * construct a wrapper for this request
         * 
         * @param request
         */
        public HeaderMapRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        private Map<String, String> headerMap = new HashMap<String, String>();

        /**
         * add a header with given name and value
         * 
         * @param name
         * @param value
         */
        public void addHeader(String name, String value) {
            headerMap.put(name, value);
        }

        @Override
        public String getHeader(String name) {
            String headerValue = super.getHeader(name);
            if (headerMap.containsKey(name)) {
                headerValue = headerMap.get(name);
            }
            return headerValue;
        }

        /**
         * get the Header names
         */
        @Override
        public Enumeration<String> getHeaderNames() {
            List<String> names = Collections.list(super.getHeaderNames());
            for (String name : headerMap.keySet()) {
                names.add(name);
            }
            return Collections.enumeration(names);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            List<String> values = Collections.list(super.getHeaders(name));
            if (headerMap.containsKey(name)) {
                values.add(headerMap.get(name));
            }
            return Collections.enumeration(values);
        }

    }

}
