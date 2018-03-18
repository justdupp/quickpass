package hecc.pay.entity;

import hecc.pay.enumer.WithdrawStatusEnum;
import hecc.pay.enumer.WithdrawTypeEnum;

import javax.persistence.*;

/**
 * @Auther xuhoujun
 * @Description: 提现实体
 * @Date: Created In 下午3:49 on 2018/3/18.
 */
@Entity
@Table(name = "withdraw")
public class QuickPassWithdrawEntity extends BaseEntity {

    @ManyToOne
    public QuickPassTenantEntity tenant;

    /**
     * 提现金额（分为单位）
     */
    public Integer fee;

    /**
     * 提现卡号
     */
    public String bankAccount;

    /**
     * 提现卡银行名称
     */
    public String bankName;

    /**
     * 提现银行预留电话
     */
    public String bankReservedMobile;

    /**
     * 提现银行开户名
     */
    public String userName;

    /**
     * 身份证号码
     */
    public String idCard;

    /**
     * 提现状态描述
     */
    public String message;

    /**
     * 提现类型
     */
    @Enumerated(EnumType.STRING)
    public WithdrawTypeEnum type;

    /**
     * 提现状态
     */
    @Enumerated(EnumType.STRING)
    public WithdrawStatusEnum status;


}
