package SampileActiviti7RuntimeBundle.service.impl.workflow;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: MyService
 * @Description: service Test
 * @author: chenjy
 * @date: 2020/4/15 11:18
 */
@Service (value = "MyService")
@Transactional
public class MyService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    public String startProcess(String message, String assignee) {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("message", message);
        variables.put("person", assignee);

        runtimeService.startProcessInstanceByMessage(message, variables);

        return "Process started";
    }

    public List<Task> getTasks(String assignee) {
        return taskService.createTaskQuery().taskAssignee(assignee).list();
    }

    public void completeTask(String taskId) {
        taskService.complete(taskId);
    }
}
