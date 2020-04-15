package service.impl.workflow;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: ServiceTask
 * @Description: TODO
 * @author: yaozhenhua
 * @date: 2020/4/15 11:18
 */
public class ServiceTask implements JavaDelegate {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTask.class);
    private Expression text1;
    @Override
    public void execute(DelegateExecution execution) {
        LOGGER.info("ServiceTask已经执行");
        String value1 = (String) text1.getValue(execution);
        LOGGER.info(value1);
        execution.setVariable("var1", new StringBuffer(value1).reverse().toString());
    }
}
