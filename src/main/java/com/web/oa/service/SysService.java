package com.web.oa.service;

import java.util.List;

import com.web.oa.pojo.MenuTree;
import com.web.oa.pojo.SysPermission;
import com.web.oa.pojo.SysRole;

public interface SysService {
	/**
	 * 加载当前系统主菜单
	 * @return
	 */
	List<MenuTree> loadMenuTree();

	/**
	 * 通过用户ID获取用户权限信息
	 * @param id
	 * @return
	 */
	List<SysPermission> listPermissionsByUserId(String id) throws Exception;

	/**
	 * 获取系统所有角色
	 * @return
	 */
	List<SysRole> findAllRoles();

	/**
	 * 通过用户名查找角色和权限
	 * @param userName
	 * @return
	 */
	SysRole findRolesAndPermissionsByUserId(String userName);

	/**
	 * 查询所有的角色以及其权限
	 * @return
	 */
	List<SysRole> findRolesAndPermissions();

	List<SysPermission> findAllMenus();

	/**
	 * 通过角色ID查询 权限
	 * @param roleId
	 * @return
	 */
	List<SysPermission> findPermissionsByRoleId(String roleId);
}
