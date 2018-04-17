package hecc.pay.vos;

import hecc.pay.enumer.ResultOrderStatusEnum;

import java.util.Date;

/**
 * @author xuhoujun
 * @description: RabbitMQVO
 * @date: Created In 上午11:33 on 2018/3/24.
 */
public class RabbitMqMessageVO {

    public String orderId;
    public Date finishTime;
    public ResultOrderStatusEnum status;

    public RabbitMqMessageVO(String orderId, ResultOrderStatusEnum status, Date finishTime) {
        this.orderId = orderId;
        this.finishTime = finishTime;
        this.status = status;
    }

    @Override
    public String toString() {
        return "RabbitMqMessageVO{" +
                "orderId='" + orderId + '\'' +
                ", finishTime=" + finishTime +
                ", status=" + status +
                '}';
    }
}
