package com.web.oa.service;

import java.util.List;

import com.web.oa.pojo.BaoxiaoBill;

public interface BaoxiaoService {
	
	/**
	 * ���汨���������ݿ�
	 * @param baoxiaoBill
	 */
	void saveBaoxiao(BaoxiaoBill baoxiaoBill);

	/**
	 * ͨ���û�ID ��ȡ�û����еı�����Ϣ
	 * @param id
	 * @return
	 */
	List<BaoxiaoBill> listBaoxiaoBillByUserId(long id);

	/**
	 * ͨ��ID��ȡ������Ϣ
	 * @param id
	 * @return
	 */
	BaoxiaoBill findBaoxiaoBillById(String id);
}
