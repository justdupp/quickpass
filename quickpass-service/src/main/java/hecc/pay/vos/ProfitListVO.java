package hecc.pay.vos;

import hecc.pay.entity.QuickPassCodeEntity;

import java.util.LinkedList;
import java.util.List;

import static hecc.pay.util.MoneyUtil.toMoney;

/**
 * @Auther xuhoujun
 * @Description:
 * @Date: Created In 下午9:41 on 2018/3/20.
 */
public class ProfitListVO {

    public String code;
    public String withdrawFee;
    public String fee;
    public List<ProfitOrderVO> list;

    public ProfitListVO(QuickPassCodeEntity code) {
        this.code = code.code;
        this.withdrawFee = toMoney(code.withdrawFee);
        this.list = new LinkedList<>();
    }

    public void addTenantOrder(ProfitOrderVO order) {
        this.list.add(order);
    }

    public void setFee(Long fee) {
        this.fee = toMoney(fee);
    }
}
