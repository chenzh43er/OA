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
		//��Spring�����л�ȡUserService
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		UserService userService = (UserService) context.getBean("userService");
		
		//��SecurityUtil �л�ȡ��ǰ�û���Ϣ
		ActiveUser user = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		
		Employee employee = userService.findEmployeeByUserId(user.getManagerId());
		
		//ͨ�����������ô�����
		del.setAssignee(employee.getName());
	}

}
