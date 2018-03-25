package hecc.pay.vos;

import java.util.LinkedList;
import java.util.List;

import static hecc.pay.util.MoneyUtil.toMoney;

/**
 * @Auther xuhoujun
 * @Description: 利润VO
 * @Date: Created In 下午9:49 on 2018/3/20.
 */
public class ProfitVO {

    public List<ProfitListVO> codes;
    public String totalFee;
    public String totalProfit;
    public String availableProfit;

    public ProfitVO(long totalFee, long totalProfit, long availableProfit) {
        this.totalFee = toMoney(totalFee);
        this.totalProfit = toMoney(totalProfit);
        this.availableProfit = toMoney(availableProfit);
        this.codes = new LinkedList<>();
    }

}
