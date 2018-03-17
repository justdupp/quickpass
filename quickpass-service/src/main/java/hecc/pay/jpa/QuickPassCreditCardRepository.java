package hecc.pay.jpa;

import hecc.pay.entity.QuickPassCreditCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Auther xuhoujun
 * @Description: 银行卡 -- jpa
 * @Date: Created In 下午9:33 on 2018/3/16.
 */
public interface QuickPassCreditCardRepository extends JpaRepository<QuickPassCreditCardEntity, Long> {

    List<QuickPassCreditCardEntity> findByTenantTenantIdAndDelIsFalse(Long tenantId);

}
