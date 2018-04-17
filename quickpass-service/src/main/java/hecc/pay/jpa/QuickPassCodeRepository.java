package hecc.pay.jpa;

import hecc.pay.entity.QuickPassCodeEntity;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author xuhoujun
 * @description: 闪付 -- 码jpa
 * @date: Created In 上午12:17 on 2018/3/4.
 */
@CacheConfig(cacheNames = "code")
public interface QuickPassCodeRepository extends JpaRepository<QuickPassCodeEntity, Long> {

    /**
     * 根据租户id获取码列表
     *
     * @param tenantId 租户id
     * @return 码列表
     */
    List<QuickPassCodeEntity> findByTenantTenantIdAndDelIsFalse(Long tenantId);

    /**
     * 根据码查询码对象
     *
     * @param code 码
     * @return 码实体
     */
    @Cacheable(key = "#p0")
    QuickPassCodeEntity findOneByCodeAndDelIsFalse(String code);

    /**
     * 根据平台单号查询码对象
     *
     * @param platform 平台单号
     * @return 码对象
     */
    QuickPassCodeEntity findFirstByPlatformAndIsDefaultIsTrueAndDelIsFalse(String platform);

    /**
     * 根据是否默认码查询码列表
     *
     * @return 码对象列表
     */
    List<QuickPassCodeEntity> findByIsDefaultIsTrueAndDelIsFalse();

}
