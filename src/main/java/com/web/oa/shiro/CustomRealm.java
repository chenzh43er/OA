package com.web.oa.shiro;
import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.web.oa.pojo.ActiveUser;
import com.web.oa.pojo.Employee;
import com.web.oa.pojo.MenuTree;
import com.web.oa.pojo.SysPermission;
import com.web.oa.service.SysService;
import com.web.oa.service.UserService;

public class CustomRealm extends AuthorizingRealm{
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SysService sysService;
	
	public void setName(String name) {
		super.setName("自定义Realm");
	}
	
	//认证用户名以及密码
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("checking Authtication!!!");
		
		String username = (String) token.getPrincipal();
		System.out.println(username);
		
		
		String username_db = "";
		String password_db = "";
		String salt_db = "";
		
		Employee employee = null;
		
		try {
			employee = userService.findUserByUserName(username);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(employee == null) {
			return null;
		}
		
		List<MenuTree> menuTree = sysService.loadMenuTree();
		
		ActiveUser user = new ActiveUser();
		
		user.setUsername(employee.getName());
		user.setUserid(employee.getName());
		user.setUsercode(employee.getName());
		user.setManagerId(employee.getManagerId());
		user.setMenuTree(menuTree);
		user.setId(employee.getId());
		
		password_db = employee.getPassword();
		salt_db = employee.getSalt();
		
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password_db, ByteSource.Util.bytes(salt_db), this.getName());
		
		return info;
	}

	//授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
		
		//从principal中获取activeUser信息
		ActiveUser activeUser = (ActiveUser) principal.getPrimaryPrincipal();
		
		//通过当前登录用户获取当前用户权限信息
		List<SysPermission> permissions = null;
		
		try {
			 permissions = sysService.listPermissionsByUserId(activeUser.getUsername());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<String> pString = new ArrayList<String>();
		
		for (SysPermission sysPermission : permissions) {
			pString.add(sysPermission.getPercode());
		}
		
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		
		info.addStringPermissions(pString);
		
		return info;
	}
}
