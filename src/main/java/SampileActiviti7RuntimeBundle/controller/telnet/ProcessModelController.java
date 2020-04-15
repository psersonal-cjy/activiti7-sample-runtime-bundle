package SampileActiviti7RuntimeBundle.controller.telnet;

import org.activiti.engine.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @ClassName: ProcessModelController
 * @Description: TODO
 * @author: chenjy
 * @date: 2020/4/15 11:18
 */
@RestController
public class ProcessModelController {
    private Logger logger = LoggerFactory.getLogger(ProcessModelController.class);

    @Autowired
    private RepositoryService repositoryService;
    @RequestMapping("/getModelByProcessId")
    public String getModelByProcessId(@RequestParam(value = "processId") String processId) {
       // get model
        return "Get Model Success:" +processId;
    }
}

