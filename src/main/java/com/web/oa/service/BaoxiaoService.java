package com.web.oa.service;

import java.util.List;

import com.web.oa.pojo.BaoxiaoBill;

public interface BaoxiaoService {
	
	/**
	 * 保存报销单到数据库
	 * @param baoxiaoBill
	 */
	void saveBaoxiao(BaoxiaoBill baoxiaoBill);

	/**
	 * 通过用户ID 获取用户所有的报销信息
	 * @param id
	 * @return
	 */
	List<BaoxiaoBill> listBaoxiaoBillByUserId(long id);

	/**
	 * 通过ID获取报销信息
	 * @param id
	 * @return
	 */
	BaoxiaoBill findBaoxiaoBillById(String id);
}
