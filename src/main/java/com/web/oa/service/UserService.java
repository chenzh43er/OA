package com.web.oa.service;

import java.util.List;

import com.web.oa.pojo.Employee;
import com.web.oa.pojo.EmployeeCustom;
import com.web.oa.pojo.MenuTree;

public interface UserService {

	/**
	 * ͨ���û�����ѯ�û���Ϣ
	 * @param username
	 * @return
	 */
	Employee findUserByUserName(String username) throws Exception;

	/**
	 * ��ȡ���е��û��ͽ�ɫ��Ϣ
	 * @return
	 */
	List<EmployeeCustom> findAllUsersAndRoles();

	/**
	 * �鿴��ǰ�ȼ���Ա����Ϣ
	 * @param level
	 * @return
	 */
	List<Employee> findEmployeeByLevel(int level);

	/**
	 * ͨ��Id��ȡ�û���Ϣ
	 * @param managerId
	 * @return
	 */
	Employee findEmployeeByUserId(Long managerId);
	
}
