package hecc.pay.vos;

import static hecc.pay.util.MoneyUtil.toMoney;

/**
 * @Auther xuhoujun
 * @Description: 订单统计VO
 * @Date: Created In 下午8:55 on 2018/3/22.
 */
public class OrderStatisticsVO {

    public String fee;
    public String amount;

    public OrderStatisticsVO(Long fee, Long amount) {
        this.fee = fee == null ? "0.00" : toMoney(fee);
        this.amount = (amount == null ? 0L : amount) + "";
    }
}
