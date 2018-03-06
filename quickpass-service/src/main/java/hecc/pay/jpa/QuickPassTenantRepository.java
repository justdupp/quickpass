package hecc.pay.jpa;

import hecc.pay.entity.QuickPassTenantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Auther xuhoujun
 * @Description: 闪付 -- 租户Jpa
 * @Date: Created In 上午12:18 on 2018/3/4.
 */
public interface QuickPassTenantRepository extends JpaRepository<QuickPassTenantEntity,Long> {

    QuickPassTenantEntity findOneByTenantIdAndDelIsFalse(Long tenantId);

    long countByCodeId(long codeId);
}
