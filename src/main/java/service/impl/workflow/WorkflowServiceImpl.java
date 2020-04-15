package service.impl.workflow;

import common.constant.WorkFlowConstant;
import common.utils.EmptyUtil;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import service.api.workflow.WorkflowService;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipInputStream;

/**
 * @ClassName: WorkflowServiceImpl
 * @Description: TODO
 * @author: yaozhenhua
 * @date: 2020/4/14 16:41
 */
@Service
public class WorkflowServiceImpl implements WorkflowService {
    private static Logger LOGGER = LoggerFactory.getLogger(WorkflowServiceImpl.class);
    @Resource
    private RepositoryService repositoryService;
    
    @Resource
    private RuntimeService runtimeService;
    
    @Resource
    private TaskService taskService;
    
    @Resource
    private HistoryService historyService;
    
    @Override
    public void deployProcess(InputStream inputStream) {
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(inputStream);
            repositoryService.createDeployment().addZipInputStream(zis).deploy();
        } finally {
            try {
                if (null != zis) {
                    zis.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (IOException e) {
            
            }
        }
        
    }
    
    @Override
    public void deployProcessByXml(String path) {
        repositoryService.createDeployment().addClasspathResource(path).deploy();
    
    }
    
    @Override
    public void startApplyVM(String businessId) {
        Map<String, Object> variables1 = new HashMap<String, Object>();
        String userid = "111";
        variables1.put("InputID", userid);
        runtimeService.startProcessInstanceByKey(WorkFlowConstant.APPLY_VM_TASK_DEF_KEY_APPLY_VM, businessId, variables1);
        List<Task> list = taskService.createTaskQuery()
                                  .taskAssignee(userid)
                                  .orderByTaskCreateTime().desc()
                                  .list();
        this.updateSubmitTask( list.get(0).getId(), businessId, WorkFlowConstant.LAUNCH_PROCESS, WorkFlowConstant.PASS);
        
    }
    
    @Override
    public void updateSubmitTask(String taskId, String businessId, String comment,
                                 String outcome) {
        String submitType = "2";
        Task task = taskService.createTaskQuery()
                            .taskId(taskId).singleResult();
        //获取当前任务的taskDefKey
        String taskDefKey = task.getTaskDefinitionKey();
        
        String currentUserId ="111";
        String assign = task.getAssignee();
        
        if(!EmptyUtil.isEmpty(assign)&&!Objects.equals(assign, currentUserId)) {
            LOGGER.error("用户不同异常");
      
        }
        
        task.setOwner(outcome);
        String processInstanceId = task.getProcessInstanceId();
        Authentication.setAuthenticatedUserId(currentUserId);
        Comment taskComment=taskService.addComment(taskId, processInstanceId, task.getName(), comment);
        
        
        /**
         * 如果连线的名称是“默认提交”，那么就不需要设置，如果不是，就需要设置流程变量 在完成任务之前，设置流程变量，按照连线的名称，去完成任务
         * 流程变量的名称：outcome 流程变量的值：连线的名称
         */
        Map<String, Object> variables = new HashMap<String, Object>();
        
        if (outcome.equals(WorkFlowConstant.PASS)) {
            outcome = WorkFlowConstant.PASS;
            variables.put("result", 1);
        } else if (outcome.equals(WorkFlowConstant.REFUSE) ) {
            variables.put("result", 2);
        } else {
            
            variables.put("result", 0);
        }
        
        variables.put(WorkFlowConstant.VARINST_NAME_IS_RESUBMITTED, 0);
        variables.put("outcome", outcome);
        
        taskService.saveTask(task);
        taskService.claim(taskId, "111");
        taskService.complete(taskId, variables);
        //当任务完成之后，需要指定下一个任务的办理人（使用类）-----已经开发完成
        
        WebApplicationContext ac = ContextLoader.getCurrentWebApplicationContext();
        
        if(Objects.equals(outcome, WorkFlowConstant.PASS)) {
            switch(taskDefKey) {
                case  WorkFlowConstant.INTERFACE_TASK_DEF_KEY_APPLY_INTERFACE :
                    //appItemRelDao.updateStateById(businessId, ESubscriState.PROJECT_MANAGER_CONFIRM.getValue());
                    break;
                //项目经理审批通过需要添加 项目号 和申请单编号
                case  WorkFlowConstant.INTERFACE_TASK_DEF_KEY_PROJECT_MANAGER_TASK :
                   // appItemRelDao.updateSubscriNoAndStateById(businessId, ESubscriState.BRANCH_MANAGER_CONFIRM.getValue(),subscriNo);
                    //appItemRelDao.updateItemno(businessId,itemno);
                    break;
            }
        } else if (Objects.equals(outcome,WorkFlowConstant.RETURN_OPTIMIZATION)) {
            switch(taskDefKey) {
                case  WorkFlowConstant.INTERFACE_TASK_DEF_KEY_PROJECT_MANAGER_TASK :
                    //appItemRelDao.updateStateById(businessId, ESubscriState.CORPORATE_APPLY_INTERFACE.getValue());
                   // appItemRelDao.updateReSubmittedById(businessId, AppItemConstant.RE_SUBMITTED_YES);
                    break;
                case  WorkFlowConstant.INTERFACE_TASK_DEF_KEY_BRANCH_MANAGER_TASK :
                    //appItemRelDao.updateStateById(businessId, ESubscriState.CORPORATE_APPLY_INTERFACE.getValue());
                   // appItemRelDao.updateReSubmittedById(businessId, AppItemConstant.RE_SUBMITTED_YES);
                    break;
            }
        }
        
        HistoricVariableInstance his = historyService.createHistoricVariableInstanceQuery()
                                               .processInstanceId(processInstanceId).variableName("result").singleResult();
        int result = Integer.parseInt(his.getValue().toString());
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                                     .processInstanceId(processInstanceId)
                                     .singleResult();
        
        if (pi == null) {
             if(submitType.equals(WorkFlowConstant.SUBMIT_TYPE_INTERFACE)){
                // 审核通过操作
                if (result == 1) {
                    LOGGER.info("审核通过操作");
                    //appItemRelDao.updateStateById(businessId, ESubscriState.AUDIT_PASS.getValue());
                    //拒绝
                }else if(result == 2){
                    LOGGER.info("审核拒绝操作");
                    //appItemRelDao.updateStateById(businessId, ESubscriState.AUDIT_FAIL.getValue());
                    /*TimerTask.newTimerTask().buildTrigger(Constant.TRIGGER_NAME_PREFIX_INTERFACE_APPLY_REFUSE+businessId,Constant.GROUP_INTERFACE_APPLY_REFUSE, DateUtils.getDate(3)).
                    buildJob(Constant.JOB_NAME_PREFIX+businessId, Constant.GROUP_INTERFACE_APPLY_REFUSE, businessId, InterfaceApplyRefuseTask.class).start();*/
                }
            }
            
        }
    }
}
