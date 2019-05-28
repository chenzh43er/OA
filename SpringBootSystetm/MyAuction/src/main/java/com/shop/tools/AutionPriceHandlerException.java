package com.shop.tools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;



// spring mvc 内置的异常管理器  

// 能够拦截到 spring mvc程序中所有的异常信息


// 不管我的spring  mvc程序中有多少个异常信息都能够被拦截到！！

// instanceof

@Component
public class AutionPriceHandlerException implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception exception) {
		
		CustomerException ex = null;
		
		// 判断  传进来的异常是不是之前设置的处理价格的异常！
		
		if(exception instanceof CustomerException)
		{
		   	ex = (CustomerException) exception; 
		}
		// 不是之前的价格异常，显示未知异常信息！
		else
		{
			exception.printStackTrace();
			ex = new CustomerException("未知异常！"); 
		}
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("errorMsg", ex.getMessage());
		mv.setViewName("error");  // 跳转错误页面
		
		return mv;
	}

}
