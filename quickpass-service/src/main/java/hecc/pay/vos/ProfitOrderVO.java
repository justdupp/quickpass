package hecc.pay.vos;

import org.apache.commons.lang.StringUtils;

import static hecc.pay.util.MoneyUtil.toMoney;

/**
 * @Auther xuhoujun
 * @Description: 订单利润VO
 * @Date: Created In 下午9:43 on 2018/3/20.
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
