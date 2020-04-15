package SampileActiviti7RuntimeBundle.controller.mng;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import SampileActiviti7RuntimeBundle.service.api.workflow.WorkflowService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @ClassName: WorkFlowController
 * @Description: TODO
 * @author: yaozhenhua
 * @date: 2020/4/14 16:33
 */
@Controller
@RequestMapping("/workflow")
public class WorkFlowController {
    @Resource
    private WorkflowService workflowService;
    
    private static Logger LOGGER = LoggerFactory.getLogger(WorkFlowController.class);
    @RequestMapping(value = "/deploy", method = RequestMethod.POST)
    public void deploy(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        if (!file.isEmpty()) {
            try {
                workflowService.deployProcess(file.getInputStream());
                responseMessage(false, "流程发布成功", response);
            } catch (Exception e) {
                LOGGER.error("流程发布异常", e);
                responseMessage(true, String.format("流程发布失败，原因：%s", e.getMessage()), response);
            }
        } else {
            responseMessage(true, "文件不存在或为空", response);
        }
    }
    @RequestMapping(value = "/deployXml", method = RequestMethod.POST)
    public void deployXml(HttpServletResponse response) {
            try {
                workflowService.deployProcessByXml("processes/ApplyVM.xml");
                responseMessage(false, "流程发布成功", response);
            } catch (Exception e) {
                LOGGER.error("流程发布异常", e);
                responseMessage(true, String.format("流程发布失败，原因：%s", e.getMessage()), response);
            }
        
    }
    
    @RequestMapping(value = "/startApplyVM", method = RequestMethod.GET)
    public void startApplyVM(HttpServletResponse response) {
        try {
            workflowService.startApplyVM("业务id");
            responseMessage(false, "流程开启成功", response);
        } catch (Exception e) {
            LOGGER.error("流程开启异常", e);
            responseMessage(true, String.format("流程开启失败，原因：%s", e.getMessage()), response);
        }
        
    }
    
    public void responseMessage(boolean hasException, String message, HttpServletResponse response) {
        String rspMessage = "";
        if (hasException) {
            rspMessage = "{success:false, msg:'" + message + "'}";
        } else {
            rspMessage = "{success:true, msg:'" + message + "'}";
        }
        response.setContentType("text/html; charset=utf-8");
        try {
            PrintWriter writer = response.getWriter();
            writer.write(rspMessage);
            writer.flush();
        } catch (IOException e) {
            LOGGER.error("", e.getMessage());
        }
    }
    
    
}
