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
	 * 部署流程
	 * @param bytes
	 * @param processName
	 */
	void saveDeployProcess(InputStream in, String processName);

	/**
	 * 查询流程部署信息
	 * @return
	 */
	List<Deployment> listReActDeployment();

	/**
	 * 查询流程定义信息
	 * @return
	 */
	List<ProcessDefinition> listActReActProdef();

	/**
	 * 推进流程并获取 并使用监听器获取下一级代办人
	 * @param id
	 * @param username
	 */
	void saveStartProcess(Long id, String username);

	/**
	 * 通过代办人获取代办人的所有代办事务
	 * @param username
	 * @return
	 */
	List<Task> listRuTask(String username);

	/**
	 * 通过任务ID获取报销信息
	 * @param taskId
	 * @return
	 */
	BaoxiaoBill findBaoxiaoBillByTaskId(String taskId);

	/**
	 * 通过任务ID 获取 批注信息
	 * @param taskId
	 * @return
	 */
	List<Comment> findCommentsByTaskId(String taskId);

	/**
	 * 通过当前任务ID 获取当前任务节点的连接线名称
	 * @param taskId
	 * @return
	 */
	List<String> findOutLineByTaskId(String taskId);

	/**
	 * 添加批注 流程向前推进
	 * @param id
	 * @param taskId
	 * @param comment
	 * @param outcome
	 */
	void saveSubmitTask(long id, String taskId, String comment,String outcome,String name);

	/**
	 * 通过报销ID获取历史批注信息
	 * @param id
	 * @return
	 */
	List<Comment> findHistoryCommentByBaoxiaoId(String id);

	/**
	 * 通过任务节点获取流程定义对象
	 * @param taskId
	 * @return
	 */
	ProcessDefinition findProcessDefinitionByTaskId(String taskId);

	/**
	 * 通过任务ID 获取 x,y,height,width值并存放在map里
	 * @param taskId
	 * @return
	 */
	Map<String, Object> findCoordingByTask(String taskId);

	/**
	 * 通过流程部署ID和图名称获取图片输出流
	 * @param deployementId
	 * @param imageName
	 * @return
	 */
	InputStream findImageInputStream(String deployementId, String imageName);
}
