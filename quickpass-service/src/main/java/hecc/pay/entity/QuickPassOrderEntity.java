package hecc.pay.entity;

import hecc.pay.enumer.OrderStatusEnum;
import org.apache.commons.lang.time.DateFormatUtils;

import javax.persistence.*;
import java.util.Date;

/**
 * @Auther xuhoujun
 * @Description: 闪付 -- 订单实体对象
 * @Date: Created In 下午4:50 on 2018/3/4.
 */
@Entity
@Table(name = "`order`")
public class QuickPassOrderEntity extends BaseEntity {

    private static final String ORDER_FORMAT = "CNY%s%012d";

    @ManyToOne
    public QuickPassTenantEntity tenant;

    /**
     * 第三方单号
     */
    public String thirdNo;

    /**
     * 交易金额：单位为分
     */
    public Integer fee;

    /**
     * 交易完成时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    public Date finishDate;

    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    public OrderStatusEnum status;

    /**
     * 付款卡号
     */
    public String payBankAccount;

    /**
     * 付款卡银行名称
     */
    public String payBankName;

    /**
     * 付款银行预留电话
     */
    public String payBankMobile;

    /**
     * 付款银行开户名
     */
    public String payUserName;

    /**
     * 付款银行开户身份证
     */
    public String payIdCard;

    /**
     * 收款卡号
     */
    public String receiverBankAccount;
    /**
     * 收款卡银行名称
     */
    public String receiverBankName;
    /**
     * 收款银行预留电话
     */
    public String receiverBankMobile;
    /**
     * 收款银行开户名
     */
    public String receiverUserName;
    /**
     * 收款银行开户身份证
     */
    public String receiverIdCard;

    /**
     * 实际交易金额
     */
    public Integer realFee;

    /**
     * 交易提现费（分）
     */
    public Integer withdrawFee;

    /**
     * 异步通知地址
     */
    public String notifyUrl;

    public String generateOrderId() {
        return String.format(ORDER_FORMAT, DateFormatUtils.format(createDate, "yyyyMMdd"), id);
    }
}
