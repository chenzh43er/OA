package com.web.oa.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.web.oa.pojo.ActiveUser;
import com.web.oa.pojo.Employee;
import com.web.oa.service.UserService;

public class ManagerTaskHandler implements TaskListener{

	public void notify(DelegateTask del) {
		//从Spring容器中获取UserService
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		UserService userService = (UserService) context.getBean("userService");
		
		//从SecurityUtil 中获取当前用户信息
		ActiveUser user = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		
		Employee employee = userService.findEmployeeByUserId(user.getManagerId());
		
		//通过监听器设置代办人
		del.setAssignee(employee.getName());
	}

}
