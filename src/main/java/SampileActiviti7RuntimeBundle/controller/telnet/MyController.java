package SampileActiviti7RuntimeBundle.controller.telnet;

import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import SampileActiviti7RuntimeBundle.service.impl.workflow.MyService;

import java.util.List;

/**
 * @ClassName: MyController
 * @Description: TODO
 * @author: chenjy
 * @date: 2020/4/15 11:18
 */
@RestController
public class MyController {

    @Autowired
    private MyService myService;

    @RequestMapping(value = "startProcess/{message}")
    public String startProcessInstace(@PathVariable("message") String message, @RequestParam String assignee) {
        return myService.startProcess(message, assignee);
    }

    @RequestMapping(value = "/task/{assignee}")
    public String getTasks(@PathVariable("assignee") String assignee) {
        List<Task> tasks= myService.getTasks(assignee);
        return  tasks.toString();
    }

    @RequestMapping(value = "/completetask")
    public String completeTask(@RequestParam String id) {
        myService.completeTask(id);
        return "Task with id " + id + " has been completed!";
    }
}
