package SampileActiviti7RuntimeBundle.common.connector;

import org.activiti.api.process.model.IntegrationContext;
import org.activiti.api.process.runtime.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @ClassName: ServiceTask1Connector
 * @Description: TODO
 * @author: chenjy
 * @date: 2020/4/15 11:18
 */
@Service(value = "serviceTask1Impl")
public class ServiceTask1Connector implements Connector {
    private Logger logger = LoggerFactory.getLogger(ServiceTask1Connector.class);

    public IntegrationContext execute(IntegrationContext integrationContext) {
        logger.info("Some service task logic... [processInstanceId=" + integrationContext.getProcessInstanceId() + "]");
        return integrationContext;
    }
}

