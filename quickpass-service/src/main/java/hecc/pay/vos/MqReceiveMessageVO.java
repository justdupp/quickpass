package hecc.pay.vos;

import hecc.pay.enumer.OrderStatusEnum;

import java.util.Date;

/**
 * @auther xuhoujun
 * @description: mq接受消息VO
 * @date: Created In 下午8:46 on 2018/3/25.
 */
public class MqReceiveMessageVO {

    public String orderId;
    public Date finishDate;
    public OrderStatusEnum status;
}
