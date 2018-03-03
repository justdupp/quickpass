package hecc.pay.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @Auther xuhoujun
 * @Description:  闪付码实体对象
 * @Date: Created In 下午11:54 on 2018/3/3.
 */
@Entity
public class QuickPassCodeEntity extends BaseEntity {
    /**
     * 关联租户
     */
    @ManyToOne
    public QuickPassTenantEntity tenant;
    /**
     * 码
     */
    public String code;
    /**
     * 默认码
     */
    public Boolean isDefault;

}
