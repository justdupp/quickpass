package hecc.pay.jpa;

import hecc.pay.entity.QuickPassCreditCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author xuhoujun
 * @description: 银行卡 -- jpa
 * @date: Created In 下午9:33 on 2018/3/16.
 */
public interface QuickPassCreditCardRepository extends JpaRepository<QuickPassCreditCardEntity, Long> {

    /**
     * 根据租户id获取银行卡列表
     *
     * @param tenantId 租户id
     * @return 银行卡对象列表
     */
    List<QuickPassCreditCardEntity> findByTenantTenantIdAndDelIsFalse(Long tenantId);

    /**
     * 根据银行账号获取银行卡信息
     *
     * @param bankAccount 银行账号
     * @return 银行卡信息
     */
    QuickPassCreditCardEntity findFirstByBankAccountAndDelIsFalse(String bankAccount);

}
