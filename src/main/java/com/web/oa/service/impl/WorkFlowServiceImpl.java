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
	 * ��������
	 */
	public void saveDeployProcess(InputStream in, String processName) {
		ZipInputStream zip = new ZipInputStream(in);

		// ����������Ϣ
		this.repositoryService.createDeployment().name(processName).addZipInputStream(zip).deploy();
	}

	/**
	 * ��ѯ���̲�����Ϣ
	 */
	public List<Deployment> listReActDeployment() {
		List<Deployment> deployments = this.repositoryService.createDeploymentQuery().orderByDeploymenTime().asc()
				.list();
		return deployments;
	}

	/**
	 * ��ѯ���̶�����Ϣ
	 */
	public List<ProcessDefinition> listActReActProdef() {
		List<ProcessDefinition> definitions = this.repositoryService.createProcessDefinitionQuery()
				.orderByProcessDefinitionVersion().asc().list();
		return definitions;
	}

	/**
	 * �ƽ����̲���ȡ ��ʹ�ü�������ȡ��һ��������
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
	 * ͨ���û���ȡ����������Ϣ
	 */
	public List<Task> listRuTask(String username) {
		List<Task> tasks = taskService.createTaskQuery().taskAssignee(username).orderByTaskCreateTime().asc().list();
		return tasks;
	}

	/**
	 * ͨ������ID��ȡ������Ϣ
	 * 
	 * @param taskId
	 * @return
	 */
	public BaoxiaoBill findBaoxiaoBillByTaskId(String taskId) {
		// 1.�õ�����ڵ���Ϣ
		Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();

		// 2.ͨ������ڵ���Ϣ ȡ�����̶���ID �õ����̶������
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
	 * ͨ������ID ��ȡ ��ע��Ϣ
	 * @param taskId
	 * @return
	 */
	public List<Comment> findCommentsByTaskId(String taskId) {
		//1.�õ�����ڵ���Ϣ
		Task task = this.taskService.createTaskQuery().taskId(taskId)
						.singleResult();
				
		//2.ͨ������ڵ���Ϣ ȡ�����̶���ID �õ����̶������
		ProcessInstance instance = this.runtimeService.createProcessInstanceQuery()
				.processInstanceId(task.getProcessInstanceId()).singleResult();
		
		
		List<Comment> comments = taskService.getProcessInstanceComments(instance.getId());
		
		return comments;
	}

	/**
	 * ͨ����ǰ����ID ��ȡ��ǰ����ڵ������������
	 * @param taskId
	 * @return
	 */
	public List<String> findOutLineByTaskId(String taskId) {
		//1.�������߶��󼯺�
		List<String> names = new ArrayList<String>();
		
		//2.ʹ������ID ��ѯ�������
		Task task = taskService.createTaskQuery().taskId(taskId)
				.singleResult();
		
		//3.��ѯ���̶���ID
		String processDefinitionId = task.getProcessDefinitionId();
		
		//4.��ѯ���̶���ʵ����� ProcessDefinitionEntity
		ProcessDefinitionEntity entity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
		
		//5.ʹ������Task��ȡ����ʵ��ID
		String processInstanceId = task.getProcessInstanceId();
		
		//6.��ȡ����ʵ�� ��ѯ��ǰ����ִ�еĶ��� ��������ʵ������
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		
		//7.��ȡ��ǰ����ID
		String activityId = processInstance.getActivityId();
		
		//8.��ȡ��ǰ�Ļ����
		ActivityImpl activityImpl = entity.findActivity(activityId);
		
		//9.��ȡ��ǰ�
		List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();
		
		if(pvmTransitions!= null && pvmTransitions.size()>0 ) {
			for (PvmTransition pvmTransition : pvmTransitions) {
				String name = (String) pvmTransition.getProperty("name");
				
				if(StringUtils.isNotBlank(name)) {
					names.add(name);
				}else {
					names.add("Ĭ���ύ");
				}
			}
		}
			
		return names;
	}

	
	/**
	 * �����ע ������ǰ�ƽ�
	 * @param id
	 * @param taskId
	 * @param comment
	 * @param outcome
	 */
	public void saveSubmitTask(long id, String taskId, String comment, String outcome,String name) {
		
		//1.ͨ������ID ��ȡ����ڵ���Ϣ
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		
		//2.��ȡ����ʵ��ID 
		String processInstanceId = task.getProcessInstanceId();
		
		//3.�������������
		Authentication.setAuthenticatedUserId(name);
		
		//4.�����ע��Ϣ
		taskService.addComment(taskId, processInstanceId, comment);
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		//5.�����ťoutcome ΪĬ���ύ ����Ҫ��ӱ�����Ϣ
		// ���򽫱�����Ϣ����Ϊ outcome
		if(outcome != null && !outcome.equals("Ĭ���ύ")) {
			map.put("message", outcome);
			taskService.complete(taskId, map);
		}else {
			taskService.complete(taskId);
		}
		
		ProcessInstance instance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		
		//6.������̽����ˣ����baoxiaobill״̬����Ϊ2
		if(instance != null) {
			BaoxiaoBill baoxiaoBill = baoxiaoBillMapper.selectByPrimaryKey(id);
			baoxiaoBill.setState(2);
			baoxiaoBillMapper.updateByPrimaryKey(baoxiaoBill);
		}
	}

	/**
	 * ͨ������ID��ȡ��ʷ��ע��Ϣ
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
	 * ͨ������ڵ��ȡ���̶������
	 * @param taskId
	 * @return
	 */
	public ProcessDefinition findProcessDefinitionByTaskId(String taskId) {
		
		//1.ͨ������ID��ȡ����ڵ���Ϣ
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		
		//2.��ȡ���̶���ID 
		String processDefinitionId = task.getProcessDefinitionId();
		
		//3.ͨ�����̶���ID��ȡ���̶������
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		
		return processDefinition;
	}

	/**
	 * ͨ������ID ��ȡ x,y,height,widthֵ�������map��
	 * @param taskId
	 * @return
	 */
	public Map<String, Object> findCoordingByTask(String taskId) {
		//�������
		Map<String,Object> map = new HashMap<String,Object>();
		
		//1.ʹ������ID����ȡ�������
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		
		//2.��ȡ���̶���ID
		String processDefinitionId = task.getProcessDefinitionId();
		
		//3.��ȡʵ��ID
		String processInstanceId = task.getProcessInstanceId();
		
		//4.��ȡ�Ե�ǰ�������е�����ʵ������
		ProcessInstance instance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		
		//5.��ȡ��ǰ�ID
		String activityId = instance.getActivityId();
		
		//6.��ȡ���̶���ʵ�����
		ProcessDefinitionEntity entity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
		
		//7.��ȡ��ǰ�����
		ActivityImpl activityImpl = entity.findActivity(activityId);
		
		//8.��ȡ��ǰ�������
		map.put("x", activityImpl.getX());
		map.put("y", activityImpl.getY());
		map.put("width", activityImpl.getWidth());
		map.put("height", activityImpl.getHeight());
		
		return map;
	}

	/**
	 * ͨ�����̲���ID��ͼ���ƻ�ȡͼƬ�����
	 * @param deployementId
	 * @param imageName
	 * @return
	 */
	public InputStream findImageInputStream(String deployementId, String imageName) {
		return repositoryService.getResourceAsStream(deployementId, imageName);
	}

	/**
	 * ͨ��bussiness_key ��ѯtask��Ϣ
	 * @param bUSSINESS_KEY
	 * @return
	 */
	public Task findTaskByBussinessKey(String bUSSINESS_KEY) {
		return taskService.createTaskQuery().processInstanceBusinessKey(bUSSINESS_KEY).singleResult();
	}
}
