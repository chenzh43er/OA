package com.web.oa.service;

import java.util.List;

import com.web.oa.pojo.MenuTree;
import com.web.oa.pojo.SysPermission;
import com.web.oa.pojo.SysRole;

public interface SysService {
	/**
	 * ���ص�ǰϵͳ���˵�
	 * @return
	 */
	List<MenuTree> loadMenuTree();

	/**
	 * ͨ���û�ID��ȡ�û�Ȩ����Ϣ
	 * @param id
	 * @return
	 */
	List<SysPermission> listPermissionsByUserId(String id) throws Exception;

	/**
	 * ��ȡϵͳ���н�ɫ
	 * @return
	 */
	List<SysRole> findAllRoles();

	/**
	 * ͨ���û������ҽ�ɫ��Ȩ��
	 * @param userName
	 * @return
	 */
	SysRole findRolesAndPermissionsByUserId(String userName);

	/**
	 * ��ѯ���еĽ�ɫ�Լ���Ȩ��
	 * @return
	 */
	List<SysRole> findRolesAndPermissions();

	List<SysPermission> findAllMenus();

	/**
	 * ͨ����ɫID��ѯ Ȩ��
	 * @param roleId
	 * @return
	 */
	List<SysPermission> findPermissionsByRoleId(String roleId);
}
