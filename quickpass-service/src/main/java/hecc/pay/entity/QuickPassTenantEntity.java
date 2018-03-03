package hecc.pay.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @Auther xuhoujun
 * @Description:
 * @Date: Created In 下午11:55 on 2018/3/3.
 */
@Entity
public class QuickPassTenantEntity extends BaseEntity {
    /**
     * 对应基础用户ID
     */
    public Long userId;
    /**
     * 所属码
     */
    @ManyToOne
    public QuickPassCodeEntity code;
    /**
     * 是否已激活
     */
    public boolean isActive;
    /**
     * 默认码
     */
    @ManyToOne
    public QuickPassCodeEntity defaultCode;
}
