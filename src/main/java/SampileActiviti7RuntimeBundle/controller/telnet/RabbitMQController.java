package SampileActiviti7RuntimeBundle.controller.telnet;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: RabbitMQController
 * @Description: TODO
 * @author: chenjy
 * @date: 2020/4/15 11:18
 */
@RestController
public class RabbitMQController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/mq-send")
    public String sendMessage(@RequestParam(value = "message") String message) {
        this.rabbitTemplate.convertAndSend("hello", message);
        return "Send Message Success:" +message;
    }
}
