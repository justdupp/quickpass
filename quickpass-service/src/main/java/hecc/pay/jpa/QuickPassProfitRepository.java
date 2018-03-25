package hecc.pay.jpa;

import hecc.pay.entity.QuickPassProfitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Auther xuhoujun
 * @Description:
 * @Date: Created In 下午9:16 on 2018/3/20.
 */
public interface QuickPassProfitRepository extends JpaRepository<QuickPassProfitEntity, Long> {

    /**
     * 根据租户id获取分润实体列表
     *
     * @param tenantId 租户id
     * @return 分润对象列表
     */
    List<QuickPassProfitEntity> findByTenantTenantIdAndDelIsFalse(Long tenantId);

    /**
     * 根据订单id获取分润数量
     *
     * @param orderId 订单id
     * @return 分润条数
     */
    Long countByOrderIdAndDelIsFalse(Long orderId);
}
