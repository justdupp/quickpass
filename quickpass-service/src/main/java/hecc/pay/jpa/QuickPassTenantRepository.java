package hecc.pay.jpa;

import hecc.pay.entity.QuickPassTenantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author xuhoujun
 * @description: 闪付 -- 租户Jpa
 * @date: Created In 上午12:18 on 2018/3/4.
 */
public interface QuickPassTenantRepository extends JpaRepository<QuickPassTenantEntity, Long> {

    /**
     * 根据租户id获取租户信息
     *
     * @param tenantId 租户id
     * @return 租户实体对象
     */
    QuickPassTenantEntity findOneByTenantIdAndDelIsFalse(Long tenantId);

    /**
     * 根据码id统计条数
     *
     * @param codeId 码id
     * @return 租户条数
     */
    long countByCodeId(long codeId);
}
