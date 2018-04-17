package hecc.pay.vos;

import hecc.pay.entity.QuickPassWithdrawEntity;
import hecc.pay.enumer.WithdrawStatusEnum;
import org.apache.commons.lang.time.DateFormatUtils;

import static hecc.pay.util.MoneyUtil.toMoney;


/**
 * @author xuhoujun
 * @description: 提现VO
 * @date: Created In 下午9:40 on 2018/3/19.
 */
public class WithdrawVO {

    public static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public String fee;
    public String withdrawTime;
    public Long withdrawNumber;
    public String bankName;
    public String bankAccountNumber;
    public String mobile;
    public String userName;
    public WithdrawStatusEnum status;

    public WithdrawVO(QuickPassWithdrawEntity withdraw, String type) {
        this.fee = toMoney(withdraw.fee);
        this.withdrawTime = DateFormatUtils.format(withdraw.createDate, DEFAULT_TIME_FORMAT);
        this.withdrawNumber = withdraw.id;
        this.bankName = withdraw.bankName;
        this.bankAccountNumber = withdraw.bankAccount;
        this.status = withdraw.status;
    }

    public WithdrawVO(QuickPassWithdrawEntity withdraw) {
        this.fee = toMoney(withdraw.fee);
        this.withdrawTime = DateFormatUtils.format(withdraw.createDate, DEFAULT_TIME_FORMAT);
        this.withdrawNumber = withdraw.id;
        this.bankName = withdraw.bankName;
        this.bankAccountNumber = withdraw.bankAccount;
        this.mobile = withdraw.bankReservedMobile;
        this.userName = withdraw.userName;
        this.status = withdraw.status;
    }

}
