package com.web.oa.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.util.StringUtil;
import com.web.oa.mapper.BaoxiaoBillMapper;
import com.web.oa.pojo.BaoxiaoBill;
import com.web.oa.service.WorkFlowService;
import com.web.oa.utils.Constants;

@Service
public class WorkFlowServiceImpl implements WorkFlowService {
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private FormService formService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private BaoxiaoBillMapper baoxiaoBillMapper;

	/**
	 * 部署流程
	 */
	public void saveDeployProcess(InputStream in, String processName) {
		ZipInputStream zip = new ZipInputStream(in);

		// 部署流程信息
		this.repositoryService.createDeployment().name(processName).addZipInputStream(zip).deploy();
	}

	/**
	 * 查询流程部署信息
	 */
	public List<Deployment> listReActDeployment() {
		List<Deployment> deployments = this.repositoryService.createDeploymentQuery().orderByDeploymenTime().asc()
				.list();
		return deployments;
	}

	/**
	 * 查询流程定义信息
	 */
	public List<ProcessDefinition> listActReActProdef() {
		List<ProcessDefinition> definitions = this.repositoryService.createProcessDefinitionQuery()
				.orderByProcessDefinitionVersion().asc().list();
		return definitions;
	}

	/**
	 * 推进流程并获取 并使用监听器获取下一级代办人
	 * 
	 * @param id
	 * @param username
	 */
	public void saveStartProcess(Long id, String username) {
		String key = Constants.BAOXIAO_KEY;

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("inputUser", username);

		String objid = key + "." + id;

		map.put("objId", objid);

		this.runtimeService.startProcessInstanceByKey(key, objid, map);
	}

	/**
	 * 通过用户获取代办事务信息
	 */
	public List<Task> listRuTask(String username) {
		List<Task> tasks = taskService.createTaskQuery().taskAssignee(username).orderByTaskCreateTime().asc().list();
		return tasks;
	}

	/**
	 * 通过任务ID获取报销信息
	 * 
	 * @param taskId
	 * @return
	 */
	public BaoxiaoBill findBaoxiaoBillByTaskId(String taskId) {
		// 1.得到任务节点信息
		Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();

		// 2.通过任务节点信息 取出流程定义ID 得到流程定义对象
		ProcessInstance instance = this.runtimeService.createProcessInstanceQuery()
				.processInstanceId(task.getProcessInstanceId()).singleResult();

		String key = instance.getBusinessKey();

		String id = "";

		if (key != null && !"".equals(key)) {
			id = key.split("\\.")[1];
		}

		BaoxiaoBill baoxiaoBill = baoxiaoBillMapper.selectByPrimaryKey(Long.parseLong(id));

		return baoxiaoBill;
	}

	/**
	 * 通过任务ID 获取 批注信息
	 * @param taskId
	 * @return
	 */
	public List<Comment> findCommentsByTaskId(String taskId) {
		//1.得到任务节点信息
		Task task = this.taskService.createTaskQuery().taskId(taskId)
						.singleResult();
				
		//2.通过任务节点信息 取出流程定义ID 得到流程定义对象
		ProcessInstance instance = this.runtimeService.createProcessInstanceQuery()
				.processInstanceId(task.getProcessInstanceId()).singleResult();
		
		
		List<Comment> comments = taskService.getProcessInstanceComments(instance.getId());
		
		return comments;
	}

	/**
	 * 通过当前任务ID 获取当前任务节点的连接线名称
	 * @param taskId
	 * @return
	 */
	public List<String> findOutLineByTaskId(String taskId) {
		//1.创建连线对象集合
		List<String> names = new ArrayList<String>();
		
		//2.使用任务ID 查询任务对象
		Task task = taskService.createTaskQuery().taskId(taskId)
				.singleResult();
		
		//3.查询流程定义ID
		String processDefinitionId = task.getProcessDefinitionId();
		
		//4.查询流程定义实体对象 ProcessDefinitionEntity
		ProcessDefinitionEntity entity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
		
		//5.使用任务Task获取流程实例ID
		String processInstanceId = task.getProcessInstanceId();
		
		//6.获取流程实例 查询当前正在执行的对象 返回流程实例对象
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		
		//7.获取当前任务ID
		String activityId = processInstance.getActivityId();
		
		//8.获取当前的活动任务
		ActivityImpl activityImpl = entity.findActivity(activityId);
		
		//9.获取当前活动
		List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();
		
		if(pvmTransitions!= null && pvmTransitions.size()>0 ) {
			for (PvmTransition pvmTransition : pvmTransitions) {
				String name = (String) pvmTransition.getProperty("name");
				
				if(StringUtils.isNotBlank(name)) {
					names.add(name);
				}else {
					names.add("默认提交");
				}
			}
		}
			
		return names;
	}

	
	/**
	 * 添加批注 流程向前推进
	 * @param id
	 * @param taskId
	 * @param comment
	 * @param outcome
	 */
	public void saveSubmitTask(long id, String taskId, String comment, String outcome,String name) {
		
		//1.通过任务ID 获取任务节点信息
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		
		//2.获取流程实例ID 
		String processInstanceId = task.getProcessInstanceId();
		
		//3.设置任务审核人
		Authentication.setAuthenticatedUserId(name);
		
		//4.添加批注信息
		taskService.addComment(taskId, processInstanceId, comment);
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		//5.如果按钮outcome 为默认提交 则不需要添加变量信息
		// 否则将变量信息设置为 outcome
		if(outcome != null && !outcome.equals("默认提交")) {
			map.put("message", outcome);
			taskService.complete(taskId, map);
		}else {
			taskService.complete(taskId);
		}
		
		ProcessInstance instance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		
		//6.如果流程结束了，则把baoxiaobill状态设置为2
		if(instance != null) {
			BaoxiaoBill baoxiaoBill = baoxiaoBillMapper.selectByPrimaryKey(id);
			baoxiaoBill.setState(2);
			baoxiaoBillMapper.updateByPrimaryKey(baoxiaoBill);
		}
	}

	/**
	 * 通过报销ID获取历史批注信息
	 * @param id
	 * @return
	 */
	public List<Comment> findHistoryCommentByBaoxiaoId(String id) {
		
		String key = Constants.BAOXIAO_KEY + "." +id;
		
		HistoricProcessInstance his = historyService.createHistoricProcessInstanceQuery()
				.processInstanceBusinessKey(key).singleResult();
		
		List<Comment> comments = taskService.getProcessInstanceComments(his.getId());
		
		return comments;
	}

	/**
	 * 通过任务节点获取流程定义对象
	 * @param taskId
	 * @return
	 */
	public ProcessDefinition findProcessDefinitionByTaskId(String taskId) {
		
		//1.通过任务ID获取任务节点信息
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		
		//2.获取流程定义ID 
		String processDefinitionId = task.getProcessDefinitionId();
		
		//3.通过流程定义ID获取流程定义对象
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		
		return processDefinition;
	}

	/**
	 * 通过任务ID 获取 x,y,height,width值并存放在map里
	 * @param taskId
	 * @return
	 */
	public Map<String, Object> findCoordingByTask(String taskId) {
		//存放坐标
		Map<String,Object> map = new HashMap<String,Object>();
		
		//1.使用任务ID，获取任务对象
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		
		//2.获取流程定义ID
		String processDefinitionId = task.getProcessDefinitionId();
		
		//3.获取实例ID
		String processInstanceId = task.getProcessInstanceId();
		
		//4.获取对当前正在运行的流程实例对象
		ProcessInstance instance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		
		//5.获取当前活动ID
		String activityId = instance.getActivityId();
		
		//6.获取流程定义实体对象
		ProcessDefinitionEntity entity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
		
		//7.获取当前活动对象
		ActivityImpl activityImpl = entity.findActivity(activityId);
		
		//8.获取当前活动的坐标
		map.put("x", activityImpl.getX());
		map.put("y", activityImpl.getY());
		map.put("width", activityImpl.getWidth());
		map.put("height", activityImpl.getHeight());
		
		return map;
	}

	/**
	 * 通过流程部署ID和图名称获取图片输出流
	 * @param deployementId
	 * @param imageName
	 * @return
	 */
	public InputStream findImageInputStream(String deployementId, String imageName) {
		return repositoryService.getResourceAsStream(deployementId, imageName);
	}

	/**
	 * 通过bussiness_key 查询task信息
	 * @param bUSSINESS_KEY
	 * @return
	 */
	public Task findTaskByBussinessKey(String bUSSINESS_KEY) {
		return taskService.createTaskQuery().processInstanceBusinessKey(bUSSINESS_KEY).singleResult();
	}
}
