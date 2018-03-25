package hecc.pay.entity;

import hecc.pay.enumer.RemittanceStatusEnum;

import javax.persistence.*;

/**
 * @Auther xuhoujun
 * @Description: 打款实体
 * @Date: Created In 下午3:59 on 2018/3/18.
 */
@Entity
@Table(name = "remittance")
public class QuickPassRemittanceEntity extends BaseEntity {

    @ManyToOne
    public QuickPassOrderEntity order;

    /**
     * 订单金额
     */
    public Integer orderFee;

    /**
     * 打款状态描述
     */
    public String message;

    /**
     * 打款标识
     */
    @Enumerated(EnumType.STRING)
    public RemittanceStatusEnum status = RemittanceStatusEnum.未打款;

}
