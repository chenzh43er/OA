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
	 * 部署流程信息
	 * @param processName
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "deployProcess")
	public String deployProcess(String processName,MultipartFile fileName) throws IOException{
		//部署流程
		workFlowService.saveDeployProcess(fileName.getInputStream(),processName);
		
		return "redriect:processDefinitionList";
	}
	
	/**
	 * 查看流程定义列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "processDefinitionList")
	public String processDefinitionList(Model model) {
		
		//1.查询流程部署信息
		List<Deployment> deployments = workFlowService.listReActDeployment();
		
	    //2.查询流程定义信息
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
		
		//1.保存报销单到数据库
		baoxiaoService.saveBaoxiao(baoxiaoBill);
		
		//2.推进流程前进
		workFlowService.saveStartProcess(baoxiaoBill.getId(), user.getUsername());
		
		return "redirect:/myTaskList";
	}
	
	/**
	 * 通过当前用户查询代办事务
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "myTaskList")
	public String myTaskList(Model model) {
		
		ActiveUser activeUser = (ActiveUser)SecurityUtils.getSubject().getPrincipal();
		
		//查看当前登录用户的代办事务
		List<Task> tasks = workFlowService.listRuTask(activeUser.getUsername());
		
		model.addAttribute("taskList", tasks);
		
		return "workflow_task";
	}
	
	@RequestMapping(value ="viewTaskForm")
	public String viewTaskForm(String taskId,Model model) {
		
		//通过任务ID获取报销信息
		BaoxiaoBill baoxiaoBill = workFlowService.findBaoxiaoBillByTaskId(taskId);
		
		//根据任务ID获取批注信息
		List<Comment> comments = workFlowService.findCommentsByTaskId(taskId);
		
		//通过当前任务节点ID获取当前任务的连接线名称
		List<String> lineList = workFlowService.findOutLineByTaskId(taskId);
		
		model.addAttribute("baoxiaoBill", baoxiaoBill);
		
		model.addAttribute("taskId", taskId);
		
		model.addAttribute("commentList", comments);
		
		model.addAttribute("outcomeList", lineList);
		
		return "approve_baoxiao";
	}
	
	/**
	 * 提交信息 流程向前推进
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
	 * 通过报销ID 查询历史批注信息
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
	 * 通过taskId 查看流程图
	 * @param taskId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "viewCurrentImage")
	public String viewCurrentImage(String taskId,Model model) {
		//1.获取流程定义对象
		ProcessDefinition pd = workFlowService.findProcessDefinitionByTaskId(taskId);
		
		//设置流程部署ID
		model.addAttribute("deploymentId", pd.getDeploymentId());
		
		//设置流程部署资源名称
		model.addAttribute("imageName", pd.getDiagramResourceName());
		
		//2.查看当前活动，获取x，y，width，heigth 将值存放在Map<String,Object>中
		Map<String,Object> map = workFlowService.findCoordingByTask(taskId);
		
		model.addAttribute("acs", map);
		
		return "viewimage";
	}
	
	/**
	 * 输出流程图
	 * @param deployementId
	 * @param imageName
	 * @param resp
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "viewImage")
	public String viewImage(String deploymentId,String imageName,
			HttpServletResponse resp) throws IOException {
		//1.获取资源文件表 act_ge_bytearray中资源图片输出流inputStream
		InputStream in = workFlowService.findImageInputStream(deploymentId,imageName);
		
		//2.从response对象获取输出流
		OutputStream out = resp.getOutputStream();
		
		//3.将输出流中数据读取，写入到输出流中
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
		
		//1：通过任务ID获取流程定义ID
		ProcessDefinition pd = workFlowService.findProcessDefinitionByTaskId(task.getId());

		model.addAttribute("deploymentId", pd.getDeploymentId());
		
		model.addAttribute("imageName", pd.getDiagramResourceName());
		
		Map<String, Object> map = workFlowService.findCoordingByTask(task.getId());
		
		model.addAttribute("acs", map);
		
		return "viewimage";
	}
}
