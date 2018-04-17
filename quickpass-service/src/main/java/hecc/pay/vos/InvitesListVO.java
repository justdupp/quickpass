package hecc.pay.vos;

import hecc.pay.entity.QuickPassDevelopEntity;
import org.apache.commons.lang.time.DateFormatUtils;

import static hecc.pay.util.MoneyUtil.toMoney;

/**
 * @author xuhoujun
 * @description: 拉新VO
 * @date: Created In 下午10:51 on 2018/3/19.
 */
public class InvitesListVO {

    public static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public String name;
    public String mobile;
    public String firstPayTime;
    public String profit;

    public InvitesListVO(QuickPassDevelopEntity developEntity) {
        this.name = developEntity.order.payUserName;
        this.mobile = developEntity.order.payBankMobile;
        this.firstPayTime = DateFormatUtils
                .format(developEntity.order.createDate, DEFAULT_TIME_FORMAT);
        this.profit = toMoney(developEntity.profit);
    }
}
