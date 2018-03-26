package hecc.pay.service;

import com.alibaba.fastjson.JSON;
import hecc.pay.entity.QuickPassOrderEntity;
import hecc.pay.enumer.OrderStatusEnum;
import hecc.pay.jpa.QuickPassOrderRepository;
import hecc.pay.vos.MqReceiveMessageVO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther xuhoujun
 * @Description: MQ消息处理
 * @Date: Created In 下午8:32 on 2018/3/25.
 */
@Component
public class RabbitMQListener {

    @Autowired
    private AsyncTask asyncTask;
    @Autowired
    private QuickPassOrderRepository orderRepository;

    @RabbitListener(queues = "orderStatus")
    public void orderListener(String jsonStr) {

        MqReceiveMessageVO receiveMessageVO = JSON.parseObject(jsonStr, MqReceiveMessageVO.class);
        QuickPassOrderEntity order = orderRepository.findOne(Long.parseLong(receiveMessageVO.orderId));
        order.status = receiveMessageVO.status;
        order.finishDate = receiveMessageVO.finishDate;
        order.thirdNo = receiveMessageVO.orderId;
        try {
            orderRepository.save(order);
        } catch (Exception e) {
        }
        if (OrderStatusEnum.交易成功.equals(order.status)) {
            asyncTask.calculateProfit(order.id);
            asyncTask.saveDevelop(order.id);
        }
    }

}
