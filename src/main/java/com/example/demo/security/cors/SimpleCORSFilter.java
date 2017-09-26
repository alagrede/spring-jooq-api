package com.example.demo.security.cors;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SimpleCORSFilter extends GenericFilterBean {

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT, HEAD");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "X-CSRF-Token, Access-Control-Allow-Origin, x-requested-with, Content-Type, If-Modified-Since, X-AUTH-TOKEN, Pragma, Cache-Control, tenant");
		response.setHeader("Access-Control-Expose-Headers", "X-AUTH-TOKEN, X-CSRF-Token");
		
		if (!((HttpServletRequest)req).getMethod().equals("OPTIONS")) 	{
			chain.doFilter(req, res);
		}
	}

}