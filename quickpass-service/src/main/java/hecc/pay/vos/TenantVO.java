package hecc.pay.vos;

import hecc.pay.client.tenant.TenantEntityVO;
import hecc.pay.entity.QuickPassTenantEntity;

import static hecc.pay.util.MoneyUtil.toMoney;

/**
 * @Auther xuhoujun
 * @Description: 租户vo
 * @Date: Created In 下午6:04 on 2018/4/1.
 */
public class TenantVO {

    /**
     * id
     */
    public Long id;

    /**
     * 租户名称
     */
    public String name;

    /**
     * 银行名称
     */
    public String bankName;

    /**
     * 收款人卡号
     */
    public String receiverBankNumber;

    /**
     * 手机号
     */
    public String mobile;

    /**
     * 身份证号码
     */
    public String idCard;

    /**
     * 提现手续费
     */
    public String withdrawFee;

    public TenantVO(TenantEntityVO tenantEntityVO, QuickPassTenantEntity tenantEntity) {
        this.id = tenantEntityVO.id;
        this.name = tenantEntityVO.name;
        this.bankName = tenantEntityVO.receiverBankName;
        this.receiverBankNumber = tenantEntityVO.receiverBankAccount;
        this.mobile = tenantEntityVO.mobile;
        this.idCard = tenantEntityVO.idCard;
        if (tenantEntity != null) {
            this.withdrawFee = toMoney(tenantEntity.code.withdrawFee);
        } else {
            this.withdrawFee = "无";
        }
    }
}
