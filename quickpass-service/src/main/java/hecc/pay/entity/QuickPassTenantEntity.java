package hecc.pay.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @Auther xuhoujun
 * @Description: 闪付租户实体对象
 * @Date: Created In 下午11:55 on 2018/3/3.
 */
@Entity
@Table(name = "tenant")
public class QuickPassTenantEntity extends BaseEntity {
    /**
     * 对应基础租户ID
     */
    public Long tenantId;
    /**
     * 所属码
     */
    @ManyToOne
    public QuickPassCodeEntity code;
    /**
     * 是否已激活
     */
    public boolean active;
    /**
     * 默认码
     */
    @ManyToOne
    public QuickPassCodeEntity defaultCode;
}
