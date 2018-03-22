package hecc.pay.jpa;

import hecc.pay.entity.QuickPassProfitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Auther xuhoujun
 * @Description:
 * @Date: Created In 下午9:16 on 2018/3/20.
 */
public interface QuickPassProfitRepository extends JpaRepository<QuickPassProfitEntity,Long> {

    List<QuickPassProfitEntity> findByTenantTenantIdAndDelIsFalse(Long tenantId);

    Long countByOrderIdAndDelIsFalse(Long orderId);
}
