package com.web.oa.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.web.oa.pojo.ActiveUser;
import com.web.oa.pojo.BaoxiaoBill;
import com.web.oa.service.BaoxiaoService;
import com.web.oa.service.WorkFlowService;
import com.web.oa.utils.Constants;

@Controller
public class WorkFlowController {
	@Autowired
	private WorkFlowService workFlowService;
	
	@Autowired
	private BaoxiaoService baoxiaoService; 
	
	/**
	 * ����������Ϣ
	 * @param processName
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "deployProcess")
	public String deployProcess(String processName,MultipartFile fileName) throws IOException{
		//��������
		workFlowService.saveDeployProcess(fileName.getInputStream(),processName);
		
		return "redriect:processDefinitionList";
	}
	
	/**
	 * �鿴���̶����б�
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "processDefinitionList")
	public String processDefinitionList(Model model) {
		
		//1.��ѯ���̲�����Ϣ
		List<Deployment> deployments = workFlowService.listReActDeployment();
		
	    //2.��ѯ���̶�����Ϣ
		List<ProcessDefinition> definitions = workFlowService.listActReActProdef();
		
		model.addAttribute("depList", deployments);
		
		model.addAttribute("pdList", definitions);
		
		return "workflow_list";
	}
	
	@RequestMapping(value = "saveStartBaoxiao")
	public String saveStartBaoxiao(BaoxiaoBill baoxiaoBill) {
		ActiveUser user = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		
		baoxiaoBill.setCreatdate(new Date());
		baoxiaoBill.setState(1);
		baoxiaoBill.setUserId(user.getId());
		
		//1.���汨���������ݿ�
		baoxiaoService.saveBaoxiao(baoxiaoBill);
		
		//2.�ƽ�����ǰ��
		workFlowService.saveStartProcess(baoxiaoBill.getId(), user.getUsername());
		
		return "redirect:/myTaskList";
	}
	
	/**
	 * ͨ����ǰ�û���ѯ��������
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "myTaskList")
	public String myTaskList(Model model) {
		
		ActiveUser activeUser = (ActiveUser)SecurityUtils.getSubject().getPrincipal();
		
		//�鿴��ǰ��¼�û��Ĵ�������
		List<Task> tasks = workFlowService.listRuTask(activeUser.getUsername());
		
		model.addAttribute("taskList", tasks);
		
		return "workflow_task";
	}
	
	@RequestMapping(value ="viewTaskForm")
	public String viewTaskForm(String taskId,Model model) {
		
		//ͨ������ID��ȡ������Ϣ
		BaoxiaoBill baoxiaoBill = workFlowService.findBaoxiaoBillByTaskId(taskId);
		
		//��������ID��ȡ��ע��Ϣ
		List<Comment> comments = workFlowService.findCommentsByTaskId(taskId);
		
		//ͨ����ǰ����ڵ�ID��ȡ��ǰ���������������
		List<String> lineList = workFlowService.findOutLineByTaskId(taskId);
		
		model.addAttribute("baoxiaoBill", baoxiaoBill);
		
		model.addAttribute("taskId", taskId);
		
		model.addAttribute("commentList", comments);
		
		model.addAttribute("outcomeList", lineList);
		
		return "approve_baoxiao";
	}
	
	/**
	 * �ύ��Ϣ ������ǰ�ƽ�
	 * @param id
	 * @param taskId
	 * @param comment
	 * @param outcome
	 * @return
	 */
	@RequestMapping(value = "submitTask")
	public String submitTask(long id,String taskId,String comment,String outcome) {
		
		ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		
		this.workFlowService.saveSubmitTask(id,taskId,comment,outcome,activeUser.getUsername());
		
		return "redirect:/myTaskList";
	}
	
	/**
	 * ͨ������ID ��ѯ��ʷ��ע��Ϣ
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "viewHisComment")
	public String viewHisComment(String id,Model model) {
		
		BaoxiaoBill baoxiaoBill = baoxiaoService.findBaoxiaoBillById(id);
		
		List<Comment> comments = workFlowService.findHistoryCommentByBaoxiaoId(id);
		
		model.addAttribute("baoxiaoBill", baoxiaoBill);
		
		model.addAttribute("commentList", comments);
		
		return "workflow_commentlist";
	}
	
	/**
	 * ͨ��taskId �鿴����ͼ
	 * @param taskId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "viewCurrentImage")
	public String viewCurrentImage(String taskId,Model model) {
		//1.��ȡ���̶������
		ProcessDefinition pd = workFlowService.findProcessDefinitionByTaskId(taskId);
		
		//�������̲���ID
		model.addAttribute("deploymentId", pd.getDeploymentId());
		
		//�������̲�����Դ����
		model.addAttribute("imageName", pd.getDiagramResourceName());
		
		//2.�鿴��ǰ�����ȡx��y��width��heigth ��ֵ�����Map<String,Object>��
		Map<String,Object> map = workFlowService.findCoordingByTask(taskId);
		
		model.addAttribute("acs", map);
		
		return "viewimage";
	}
	
	/**
	 * �������ͼ
	 * @param deployementId
	 * @param imageName
	 * @param resp
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "viewImage")
	public String viewImage(String deploymentId,String imageName,
			HttpServletResponse resp) throws IOException {
		//1.��ȡ��Դ�ļ��� act_ge_bytearray����ԴͼƬ�����inputStream
		InputStream in = workFlowService.findImageInputStream(deploymentId,imageName);
		
		//2.��response�����ȡ�����
		OutputStream out = resp.getOutputStream();
		
		//3.������������ݶ�ȡ��д�뵽�������
		for(int b=-1;(b=in.read())!=-1;) {
			out.write(b);
		}
		
		out.close();
		
		in.close();
		
		return null;
	}
	
	//test Git version Control
	
	@RequestMapping("/viewCurrentImageByBill")
	public String viewCurrentImageByBill(long billId,ModelMap model) {
		
		String BUSSINESS_KEY = Constants.BAOXIAO_KEY + "." + billId;
		
		Task task = this.workFlowService.findTaskByBussinessKey(BUSSINESS_KEY);
		
		//1��ͨ������ID��ȡ���̶���ID
		ProcessDefinition pd = workFlowService.findProcessDefinitionByTaskId(task.getId());

		model.addAttribute("deploymentId", pd.getDeploymentId());
		
		model.addAttribute("imageName", pd.getDiagramResourceName());
		
		Map<String, Object> map = workFlowService.findCoordingByTask(task.getId());
		
		model.addAttribute("acs", map);
		
		return "viewimage";
	}
}
