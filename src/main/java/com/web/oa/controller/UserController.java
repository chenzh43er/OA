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
	
	//登录
	@RequestMapping(value="login")
	public String login(HttpServletRequest request,Model model) {
		
		String exceptionName = (String) request.getAttribute("shiroLoginFailure");
		
		if (exceptionName != null) {
			if (UnknownAccountException.class.getName().equals(exceptionName)) {
				
				model.addAttribute("errorMsg", "用户账号不存在");
			
			} else if (IncorrectCredentialsException.class.getName().equals(exceptionName)) {
				
				model.addAttribute("errorMsg", "密码不正确");
			
			} else if("randomcodeError".equals(exceptionName)) {
				
				model.addAttribute("errorMsg", "验证码不正确");
			
			}
			else {
				
				model.addAttribute("errorMsg", "未知错误");
			
			}
		}
		return "login";
	}
	
	//查看用户列表
	@RequestMapping(value="findUserList")
	public String findUserList(Model model) {
		
		List<SysRole> sysRoles = sysService.findAllRoles(); 
		
		List<EmployeeCustom> employeeCustoms = userService.findAllUsersAndRoles();
		
		model.addAttribute("allRoles", sysRoles);
		
		model.addAttribute("userList", employeeCustoms);
		
		return "userlist";
	}
	
	//通过用户名查询当前用户权限和角色信息
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
	 * 通过当前等级查询下一个等级的员工信息
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
	 * 跳转到添加角色页面
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
	 * 查询所有角色以及权限
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
