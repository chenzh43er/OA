package com.web.oa.controller;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.oa.mapper.BaoxiaoBillMapper;
import com.web.oa.pojo.ActiveUser;
import com.web.oa.pojo.BaoxiaoBill;
import com.web.oa.service.BaoxiaoService;

@Controller
public class BaoxiaoBillController {
	
	@Autowired
	private BaoxiaoService baoxiaoService;
	
	@RequestMapping("/main")
	public String main(ModelMap model) {
		
		//从SecurtyUtils中获取activeUser对象
		ActiveUser activeUser =  (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		
		model.addAttribute("activeUser", activeUser);
		
		return "index";
	}
	
	@RequestMapping(value = "myBaoxiaoBill")
	public String myBaoxiaoBill(Model model) {
		
		ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		
		List<BaoxiaoBill> list = baoxiaoService.listBaoxiaoBillByUserId(activeUser.getId());
		
		model.addAttribute("baoxiaoList", list);
		
		return "baoxiaobill";
	}
}
