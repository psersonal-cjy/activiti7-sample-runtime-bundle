package service.api.workflow;

import java.io.InputStream;

/**
 * @ClassName: WorkflowService
 * @Description: TODO
 * @author: yaozhenhua
 * @date: 2020/4/14 16:40
 */
public interface WorkflowService {
    void deployProcess(InputStream inputStream);
    void deployProcessByXml(String path);
    void startApplyVM(String businessId);
    void updateSubmitTask(String taskId, String businessId, String comment, String outcome);
}
