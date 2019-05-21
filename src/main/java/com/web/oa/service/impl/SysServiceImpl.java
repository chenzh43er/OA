package com.web.oa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.oa.mapper.SysPermissionCustomMapper;
import com.web.oa.mapper.SysPermissionMapper;
import com.web.oa.mapper.SysPermissionMapperCustom;
import com.web.oa.mapper.SysRoleMapper;
import com.web.oa.pojo.MenuTree;
import com.web.oa.pojo.SysPermission;
import com.web.oa.pojo.SysPermissionExample;
import com.web.oa.pojo.SysRole;
import com.web.oa.service.SysService;

@Service
public class SysServiceImpl implements SysService{

	@Autowired
	private SysPermissionMapperCustom sysPermissionMapperCustom;
	
	@Autowired
	private SysRoleMapper sysRoleMapper;
	
	@Autowired
	private SysPermissionMapper sysPermissionMapper;
	
	/**
	 * 加载当前系统主菜单
	 * @return
	 */
	public List<MenuTree> loadMenuTree() {
		return sysPermissionMapperCustom.getMenuTree();
	}

	/**
	 * 通过用户ID获取用户权限信息
	 * @param id
	 * @return
	 */
	public List<SysPermission> listPermissionsByUserId(String id) throws Exception {
		return sysPermissionMapperCustom.findPermissionListByUserId(id);
	}

	/**
	 * 获取系统所有角色
	 * @return
	 */
	public List<SysRole> findAllRoles() {
		return sysRoleMapper.selectByExample(null);
	}

	/**
	 * 通过用户名查找角色和权限
	 * @param userName
	 * @return
	 */
	public SysRole findRolesAndPermissionsByUserId(String userName) {
		return sysPermissionMapperCustom.findRoleAndPermissionListByUserId(userName);
	}

	public List<SysRole> findRolesAndPermissions() {
		return sysPermissionMapperCustom.findRoleAndPermissionList();
	}

	/**
	 * 查询所有的菜单
	 */
	public List<SysPermission> findAllMenus() {
		
		SysPermissionExample example = new SysPermissionExample();
		
		SysPermissionExample.Criteria criteria = example.createCriteria();
		
		criteria.andTypeEqualTo("menu");
		
		return sysPermissionMapper.selectByExample(example);
	}

	/**
	 * 通过角色ID查询权限
	 */
	public List<SysPermission> findPermissionsByRoleId(String roleId) {
		return sysPermissionMapperCustom.findPermissionsByRoleId(roleId);
	}

}
