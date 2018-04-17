package hecc.pay.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author xuhoujun
 * @description: MQ消息生产者
 * @date: Created In 下午9:27 on 2018/3/27.
 */
@Component
public class RabbitSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;
    public void send() {
        String context = "hello " + LocalDateTime.now();
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("hello", context);
    }
}
