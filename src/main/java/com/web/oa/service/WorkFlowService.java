package com.web.oa.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;

import com.web.oa.pojo.BaoxiaoBill;

public interface WorkFlowService {


	/**
	 * ��������
	 * @param bytes
	 * @param processName
	 */
	void saveDeployProcess(InputStream in, String processName);

	/**
	 * ��ѯ���̲�����Ϣ
	 * @return
	 */
	List<Deployment> listReActDeployment();

	/**
	 * ��ѯ���̶�����Ϣ
	 * @return
	 */
	List<ProcessDefinition> listActReActProdef();

	/**
	 * �ƽ����̲���ȡ ��ʹ�ü�������ȡ��һ��������
	 * @param id
	 * @param username
	 */
	void saveStartProcess(Long id, String username);

	/**
	 * ͨ�������˻�ȡ�����˵����д�������
	 * @param username
	 * @return
	 */
	List<Task> listRuTask(String username);

	/**
	 * ͨ������ID��ȡ������Ϣ
	 * @param taskId
	 * @return
	 */
	BaoxiaoBill findBaoxiaoBillByTaskId(String taskId);

	/**
	 * ͨ������ID ��ȡ ��ע��Ϣ
	 * @param taskId
	 * @return
	 */
	List<Comment> findCommentsByTaskId(String taskId);

	/**
	 * ͨ����ǰ����ID ��ȡ��ǰ����ڵ������������
	 * @param taskId
	 * @return
	 */
	List<String> findOutLineByTaskId(String taskId);

	/**
	 * �����ע ������ǰ�ƽ�
	 * @param id
	 * @param taskId
	 * @param comment
	 * @param outcome
	 */
	void saveSubmitTask(long id, String taskId, String comment,String outcome,String name);

	/**
	 * ͨ������ID��ȡ��ʷ��ע��Ϣ
	 * @param id
	 * @return
	 */
	List<Comment> findHistoryCommentByBaoxiaoId(String id);

	/**
	 * ͨ������ڵ��ȡ���̶������
	 * @param taskId
	 * @return
	 */
	ProcessDefinition findProcessDefinitionByTaskId(String taskId);

	/**
	 * ͨ������ID ��ȡ x,y,height,widthֵ�������map��
	 * @param taskId
	 * @return
	 */
	Map<String, Object> findCoordingByTask(String taskId);

	/**
	 * ͨ�����̲���ID��ͼ���ƻ�ȡͼƬ�����
	 * @param deployementId
	 * @param imageName
	 * @return
	 */
	InputStream findImageInputStream(String deployementId, String imageName);
}
