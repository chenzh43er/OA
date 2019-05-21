package com.web.oa.service;

import java.util.List;

import com.web.oa.pojo.Employee;
import com.web.oa.pojo.EmployeeCustom;
import com.web.oa.pojo.MenuTree;

public interface UserService {

	/**
	 * 通过用户名查询用户信息
	 * @param username
	 * @return
	 */
	Employee findUserByUserName(String username) throws Exception;

	/**
	 * 获取所有的用户和角色信息
	 * @return
	 */
	List<EmployeeCustom> findAllUsersAndRoles();

	/**
	 * 查看当前等级的员工信息
	 * @param level
	 * @return
	 */
	List<Employee> findEmployeeByLevel(int level);

	/**
	 * 通过Id获取用户信息
	 * @param managerId
	 * @return
	 */
	Employee findEmployeeByUserId(Long managerId);
	
}
