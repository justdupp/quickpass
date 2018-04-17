package hecc.pay.jpa;

import hecc.pay.entity.QuickPassDevelopEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author xuhoujun
 * @description: 拉新 -- jpa
 * @date: Created In 下午4:34 on 2018/3/18.
 */
public interface QuickPassDevelopRepository extends JpaRepository<QuickPassDevelopEntity, Long> {

    /**
     * 根据租户id获取拉新列表
     *
     * @param tenantId 租户id
     * @return 拉新列表
     */
    List<QuickPassDevelopEntity> findByTenantTenantIdAndDelIsFalse(Long tenantId);

    /**
     * 根据身份证号码获取条数
     *
     * @param idCard 身份证号码
     * @return 条数
     */
    Long countByIdCardAndDelIsFalse(String idCard);
}
