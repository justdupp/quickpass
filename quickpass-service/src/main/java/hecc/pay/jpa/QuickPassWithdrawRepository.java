package hecc.pay.jpa;

import hecc.pay.entity.QuickPassWithdrawEntity;
import hecc.pay.enumer.WithdrawTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Auther xuhoujun
 * @Description: 提现JPA
 * @Date: Created In 下午3:57 on 2018/3/18.
 */
public interface QuickPassWithdrawRepository extends JpaRepository<QuickPassWithdrawEntity,Long> {

    Page<QuickPassWithdrawEntity> findByTenantTenantIdAndTypeAndDelIsFalse(Long tenantId, WithdrawTypeEnum type, Pageable page);

    List<QuickPassWithdrawEntity> findByTenantTenantIdAndTypeAndDelIsFalse(Long tenantId, WithdrawTypeEnum type);

}
