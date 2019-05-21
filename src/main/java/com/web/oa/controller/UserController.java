package com.web.oa.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.util.json.JSONArray;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.web.oa.pojo.Employee;
import com.web.oa.pojo.EmployeeCustom;
import com.web.oa.pojo.MenuTree;
import com.web.oa.pojo.SysPermission;
import com.web.oa.pojo.SysRole;
import com.web.oa.service.SysService;
import com.web.oa.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private SysService sysService;
	
	//��¼
	@RequestMapping(value="login")
	public String login(HttpServletRequest request,Model model) {
		
		String exceptionName = (String) request.getAttribute("shiroLoginFailure");
		
		if (exceptionName != null) {
			if (UnknownAccountException.class.getName().equals(exceptionName)) {
				
				model.addAttribute("errorMsg", "�û��˺Ų�����");
			
			} else if (IncorrectCredentialsException.class.getName().equals(exceptionName)) {
				
				model.addAttribute("errorMsg", "���벻��ȷ");
			
			} else if("randomcodeError".equals(exceptionName)) {
				
				model.addAttribute("errorMsg", "��֤�벻��ȷ");
			
			}
			else {
				
				model.addAttribute("errorMsg", "δ֪����");
			
			}
		}
		return "login";
	}
	
	//�鿴�û��б�
	@RequestMapping(value="findUserList")
	public String findUserList(Model model) {
		
		List<SysRole> sysRoles = sysService.findAllRoles(); 
		
		List<EmployeeCustom> employeeCustoms = userService.findAllUsersAndRoles();
		
		model.addAttribute("allRoles", sysRoles);
		
		model.addAttribute("userList", employeeCustoms);
		
		return "userlist";
	}
	
	//ͨ���û�����ѯ��ǰ�û�Ȩ�޺ͽ�ɫ��Ϣ
	@RequestMapping(value = "viewPermissionByUser")
	@ResponseBody
	public void viewPermissionByUser(HttpServletResponse resp,String userName) throws IOException {
		
		SysRole role = sysService.findRolesAndPermissionsByUserId(userName);
		
		String roleJSON = JSON.toJSONString(role);
		
		resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
		resp.getWriter().print(roleJSON);
	}
	
	/**
	 * ͨ����ǰ�ȼ���ѯ��һ���ȼ���Ա����Ϣ
	 * @param level
	 * @param resp
	 * @throws IOException 
	 */
	@RequestMapping(value = "findNextManager")
	@ResponseBody
	public void findNextManager(int level,HttpServletResponse resp) throws IOException {
		level = level + 1;
		List<Employee> employees = userService.findEmployeeByLevel(level);
		
		String empJSONS = JSON.toJSONString(employees);
		
		resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
		resp.getWriter().print(empJSONS);
	}
	
	/**
	 * ��ת����ӽ�ɫҳ��
	 * @return
	 */
	@RequestMapping(value = "toAddRole")
	public String toAddRole(Model model) {
		
		List<MenuTree> menuTrees = sysService.loadMenuTree();
		
		List<SysRole> sysRoles = sysService.findRolesAndPermissions();
		
		List<SysPermission> menus = sysService.findAllMenus();
		
		model.addAttribute("menuTypes", menus);
		
		model.addAttribute("roleAndPermissionsList", sysRoles);
		
		model.addAttribute("allPermissions", menuTrees);
		
		return "rolelist";
	}
	
	/**
	 * ��ѯ���н�ɫ�Լ�Ȩ��
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "findRoles")
	public String findRoles(Model model) {
		
		List<MenuTree> menuTrees = sysService.loadMenuTree();
		
		List<SysRole> sysRoles = sysService.findRolesAndPermissions();
		
		model.addAttribute("allRoles", sysRoles);
		
		model.addAttribute("allMenuAndPermissions", menuTrees);
		
		return "permissionlist";
	}
	
	@RequestMapping(value = "loadMyPermissions")
	@ResponseBody
	public void loadMyPermissions(HttpServletResponse resp,String roleId) throws IOException {
		List<SysPermission> permissions = sysService.findPermissionsByRoleId(roleId);
		
		String listStr = JSON.toJSONString(permissions);
		
		resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().print(listStr);
	}
}
