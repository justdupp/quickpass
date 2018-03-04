package hecc.pay.entity;

import hecc.pay.enumer.OrderStatusEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * @Auther xuhoujun
 * @Description:  闪付 -- 订单实体对象
 * @Date: Created In 下午4:50 on 2018/3/4.
 */
public class QuickPassOrderEntity extends BaseEntity {

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
     * 实际交易金额
     */
    public String realFee;
}
