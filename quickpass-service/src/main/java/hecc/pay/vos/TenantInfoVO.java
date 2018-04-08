package hecc.pay.vos;

import hecc.pay.client.tenant.TenantEntityVO;
import hecc.pay.entity.QuickPassCreditCardEntity;
import hecc.pay.entity.QuickPassTenantEntity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther xuhoujun
 * @Description: 租户信息VO
 * @Date: Created In 下午9:16 on 2018/3/16.
 */
public class TenantInfoVO {

    public List<CreditCardVO> creditCardList;
    public Boolean bankCardHasPassed;
    public String name;
    public String bankName;
    public String receiverBankNumber;
    public String mobile;
    public String idCard;
    public String parentName;
    public Boolean isOpen;

    public TenantInfoVO(TenantEntityVO parentTenant, TenantEntityVO tenantEntity,
                        QuickPassTenantEntity tenant, List<QuickPassCreditCardEntity> creditCardList) {

        this.parentName = parentTenant.name != null ? parentTenant.name : null;
        this.name = tenantEntity.name;
        this.bankCardHasPassed = tenantEntity.bankCardHasPassed;
        this.bankName = tenantEntity.receiverBankName;
        this.receiverBankNumber = tenantEntity.receiverBankAccount;
        this.mobile = tenantEntity.mobile;
        this.idCard = tenantEntity.idCard;
        this.isOpen = tenant.active;
        this.creditCardList = creditCardList.stream()
                .map(creditCard -> new CreditCardVO(creditCard))
                .collect(Collectors.toList());
    }
}
