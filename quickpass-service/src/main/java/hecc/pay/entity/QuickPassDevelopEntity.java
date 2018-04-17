package hecc.pay.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author xuhoujun
 * @description: 发展租户实体
 * @date: Created In 下午4:29 on 2018/3/18.
 */
@Entity
@Table(name = "develop")
public class QuickPassDevelopEntity extends BaseEntity {

    @ManyToOne
    public QuickPassTenantEntity tenant;

    /**
     * 收益金额（分为单位）
     */
    public Integer profit;

    /**
     * 注册租户
     */
    @ManyToOne
    public QuickPassTenantEntity register;

    /**
     * 注册身份证号码
     */
    public String idCard;

    /**
     * 首刷订单
     */
    @ManyToOne
    public QuickPassOrderEntity order;
}
