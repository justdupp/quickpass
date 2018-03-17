package hecc.pay.jpa;

import hecc.pay.entity.QuickPassCreditCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Auther xuhoujun
 * @Description:
 * @Date: Created In 下午9:33 on 2018/3/16.
 */
public interface QuickPassCreditCardRepo extends JpaRepository<QuickPassCreditCardEntity,Long> {

    List<QuickPassCreditCardEntity> findByTenantTenantUserIdAndDelIsFalse(Long userId);

}
