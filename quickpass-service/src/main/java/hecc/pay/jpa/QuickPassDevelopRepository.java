package hecc.pay.jpa;

import hecc.pay.entity.QuickPassDevelopEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Auther xuhoujun
 * @Description: 拉新 -- jpa
 * @Date: Created In 下午4:34 on 2018/3/18.
 */
public interface QuickPassDevelopRepository extends JpaRepository<QuickPassDevelopEntity,Long> {

    List<QuickPassDevelopEntity> findByTenantTenantIdAndDelIsFalse(Long tenantId);
}
