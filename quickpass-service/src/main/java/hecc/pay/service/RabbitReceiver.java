package hecc.pay.service;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Auther xuhoujun
 * @Description:  mq消息消费者
 * @Date: Created In 下午9:28 on 2018/3/27.
 */
@Component
@RabbitListener(queues = "hello")
public class RabbitReceiver {

    @RabbitHandler
    public void process(String hello) {
        System.out.println("Receiver : " + hello);
    }
}
