package hecc.pay.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @Auther xuhoujun
 * @Description: 银行卡实体
 * @Date: Created In 下午9:18 on 2018/3/16.
 */
@Entity
@Table(name = "card")
public class QuickPassCreditCardEntity extends BaseEntity {

    @ManyToOne
    public QuickPassTenantEntity tenant;
    /**
     * 付款卡号
     */
    public String bankAccount;
    /**
     * 付款卡银行名称
     */
    public String bankName;
    /**
     * 柜台预留电话
     */
    public String mobile;
    /**
     * 银行开户名
     */
    public String userName;
    /**
     * 开户人身份证
     */
    public String idCard;
    /**
     * 是否激活
     */
    public boolean active;
}
