package hecc.pay.vos;

import hecc.pay.entity.QuickPassWithdrawEntity;
import hecc.pay.enumer.WithdrawStatusEnum;

import java.text.SimpleDateFormat;

import static hecc.pay.util.MoneyUtil.toMoney;

/**
 * @Auther xuhoujun
 * @Description: 提现实体VO
 * @Date: Created In 下午10:48 on 2018/3/22.
 */
public class WithdrawEntityVO {

    public Long id;
    public String fee;
    public String receiverUserName;
    public String receiverBankAccount;
    public String receiverBankMobile;
    public String receiverBankName;
    public String receiverIdCard;
    public WithdrawStatusEnum status;
    public String createDate;
    public String modifyDate;
    public String message;


    public WithdrawEntityVO(QuickPassWithdrawEntity withdraw) {

        this.id = withdraw.id;
        this.fee = toMoney(withdraw.fee);
        this.receiverUserName = withdraw.userName;
        this.receiverBankAccount = withdraw.bankAccount;
        this.receiverBankMobile = withdraw.bankReservedMobile;
        this.receiverBankName = withdraw.bankName;
        this.receiverIdCard = withdraw.idCard;
        this.status = withdraw.status;
        this.createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(withdraw.createDate);
        this.modifyDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(withdraw.modifyDate);
        this.message = withdraw.message;
    }
}
