package com.web.oa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.oa.mapper.EmployeeMapper;
import com.web.oa.mapper.SysPermissionMapperCustom;
import com.web.oa.pojo.Employee;
import com.web.oa.pojo.EmployeeCustom;
import com.web.oa.pojo.EmployeeExample;
import com.web.oa.pojo.MenuTree;
import com.web.oa.pojo.EmployeeExample.Criteria;
import com.web.oa.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private EmployeeMapper employeeMapper;
	
	@Autowired
	private SysPermissionMapperCustom sysPermissionMapperCustom;
	
	
	/**
	 * 通过用户名查询用户信息
	 * @param username
	 * @return
	 */
	public Employee findUserByUserName(String username) throws Exception{

		EmployeeExample example = new EmployeeExample();
		
		Criteria criteria = example.createCriteria();
		
		criteria.andNameEqualTo(username);
		
		List<Employee> employees = employeeMapper.selectByExample(example);
		
		if(employees!=null && employees.size()>0) {
			Employee employee = employees.get(0);
			return employee;
		}
		return null;
	}


	/**
	 * 获取所有的用户和角色信息
	 * @return
	 */
	public List<EmployeeCustom> findAllUsersAndRoles() {
		return sysPermissionMapperCustom.findUserAndRoleList();
	}


	public List<Employee> findEmployeeByLevel(int level) {
		
		EmployeeExample example = new EmployeeExample();
		
		Criteria criteria = example.createCriteria();
		
		criteria.andRoleEqualTo(level);
		
		return employeeMapper.selectByExample(example);
	}


	public Employee findEmployeeByUserId(Long managerId) {
		Employee employee = employeeMapper.selectByPrimaryKey(managerId);
		return employee;
	}
}
