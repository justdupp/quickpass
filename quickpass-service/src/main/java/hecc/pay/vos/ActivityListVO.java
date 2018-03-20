package hecc.pay.vos;

import java.util.List;

import static hecc.pay.util.MoneyUtil.toMoney;

/**
 * @Auther xuhoujun
 * @Description:
 * @Date: Created In 下午10:55 on 2018/3/19.
 */
public class ActivityListVO {

    public String totalProfit;
    public String availableProfit;
    public List<InvitesListVO> invites;

    public ActivityListVO(long totalProfit, long availableProfit, List list) {
        this.totalProfit = toMoney(totalProfit);
        this.availableProfit = toMoney(availableProfit);
        this.invites = list;
    }
}
