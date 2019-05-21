package com.web.oa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.oa.mapper.BaoxiaoBillMapper;
import com.web.oa.pojo.BaoxiaoBill;
import com.web.oa.pojo.BaoxiaoBillExample;
import com.web.oa.pojo.BaoxiaoBillExample.Criteria;
import com.web.oa.service.BaoxiaoService;

@Service
public class BaoxiaoServiceImpl implements BaoxiaoService{
	
	@Autowired
	private BaoxiaoBillMapper baoxiaoBillMapper;

	public void saveBaoxiao(BaoxiaoBill baoxiaoBill) {
		baoxiaoBillMapper.insert(baoxiaoBill);
	}
	
	/**
	 * 通过用户ID 获取用户所有的报销信息
	 * @param id
	 * @return
	 */
	public List<BaoxiaoBill> listBaoxiaoBillByUserId(long id) {
		
		BaoxiaoBillExample example = new BaoxiaoBillExample();
		
		Criteria criteria = example.createCriteria();
		
		criteria.andUserIdEqualTo(id);
		
		List<BaoxiaoBill> list = baoxiaoBillMapper.selectByExample(example);
		
		return list;
	}

	/**
	 * 通过ID获取报销信息
	 * @param id
	 * @return
	 */
	public BaoxiaoBill findBaoxiaoBillById(String id) {
		BaoxiaoBill baoxiaoBill = baoxiaoBillMapper.selectByPrimaryKey(Long.parseLong(id));
		return baoxiaoBill;
	}
}
