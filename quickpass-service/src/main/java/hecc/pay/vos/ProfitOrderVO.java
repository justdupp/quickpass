package hecc.pay.vos;

import org.apache.commons.lang.StringUtils;

import static hecc.pay.util.MoneyUtil.toMoney;

/**
 * @author xuhoujun
 * @description: 订单利润VO
 * @date: Created In 下午9:43 on 2018/3/20.
 */
public class ProfitOrderVO {
    public String name;
    public String fee;

    public ProfitOrderVO(String name) {
        this.name = StringUtils.trimToEmpty(name);
    }

    public void setFee(Integer fee) {
        this.fee = toMoney(fee);
    }
}
