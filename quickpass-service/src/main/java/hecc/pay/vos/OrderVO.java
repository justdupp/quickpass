package hecc.pay.vos;

import hecc.pay.entity.QuickPassOrderEntity;
import org.apache.commons.lang.time.DateFormatUtils;

import static hecc.pay.util.MoneyUtil.toMoney;

/**
 * @author xuhoujun
 * @description: 订单VO
 * @date: Created In 下午9:05 on 2018/3/22.
 */
public class OrderVO {

    public static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public String fee;
    public String status;
    public String tradeDate;
    public String createDate;
    public String bankName;
    public String bankAccountNumber;
    public String payBankName;
    public String payBankAccountNumber;
    public String tradeNumber;
    public String thirdNumber;
    public String idCard;
    public String mobile;

    public OrderVO(QuickPassOrderEntity orderEntity) {
        this.fee = toMoney(orderEntity.fee);
        this.status = orderEntity.status.name();
        this.tradeDate = orderEntity.finishDate == null ? null : DateFormatUtils
                .format(orderEntity.finishDate, DEFAULT_TIME_FORMAT);
        this.bankName = orderEntity.receiverBankName;
        this.bankAccountNumber = orderEntity.receiverBankAccount;
        this.payBankName = orderEntity.payBankName;
        this.payBankAccountNumber = orderEntity.payBankAccount;
        this.tradeNumber = orderEntity.id + "";
        this.thirdNumber = orderEntity.thirdNo;
        this.createDate = DateFormatUtils.format(orderEntity.createDate, DEFAULT_TIME_FORMAT);
    }

}
