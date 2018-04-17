package hecc.pay.entity;


import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author xuhoujun
 * @Description:
 * @Date: Created In 下午9:14 on 2018/3/20.
 */
@Entity
@Table(name = "profit")
public class QuickPassProfitEntity extends BaseEntity {

    /**
     * 分润所属码
     */
    @ManyToOne
    public QuickPassCodeEntity code;

    /**
     * 分润所属租户
     */
    @ManyToOne
    public QuickPassTenantEntity tenant;

    /**
     * 原订单
     */
    @ManyToOne
    public QuickPassOrderEntity order;

    /**
     * 收益（单位：分）
     */
    public Integer profit;

}
