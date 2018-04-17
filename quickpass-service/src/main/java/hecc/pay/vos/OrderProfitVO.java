package hecc.pay.vos;

import hecc.pay.entity.QuickPassOrderEntity;
import hecc.pay.entity.QuickPassProfitEntity;
import org.apache.commons.lang.time.DateFormatUtils;

import static hecc.pay.util.MoneyUtil.toMoney;

/**
 * @author xuhoujun
 * @description: 订单分润VO
 * @date: Created In 下午9:51 on 2018/3/22.
 */
public class OrderProfitVO {

    public static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public String orderUserName;
    public String orderFee;
    public String orderTradeDate;

    public String profit;

    public String withdrawFee;

    public OrderProfitVO(QuickPassOrderEntity order, QuickPassProfitEntity profit) {
        this.orderUserName = order.payUserName;
        this.orderFee = toMoney(order.fee);
        this.orderTradeDate = order.finishDate == null ? null : DateFormatUtils
                .format(order.finishDate, DEFAULT_TIME_FORMAT);
        this.profit = toMoney(profit.profit);
        this.withdrawFee = toMoney(profit.code.withdrawFee);
    }
}
