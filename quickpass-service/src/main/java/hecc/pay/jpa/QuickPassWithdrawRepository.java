package hecc.pay.jpa;

import hecc.pay.entity.QuickPassWithdrawEntity;
import hecc.pay.enumer.WithdrawStatusEnum;
import hecc.pay.enumer.WithdrawTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @Auther xuhoujun
 * @Description: 提现JPA
 * @Date: Created In 下午3:57 on 2018/3/18.
 */
public interface QuickPassWithdrawRepository extends JpaRepository<QuickPassWithdrawEntity,Long> {

    Page<QuickPassWithdrawEntity> findByTenantTenantIdAndTypeAndDelIsFalse(Long tenantId, WithdrawTypeEnum type, Pageable page);

    List<QuickPassWithdrawEntity> findByTenantTenantIdAndTypeAndDelIsFalse(Long tenantId, WithdrawTypeEnum type);

    Long countByTenantTenantIdAndStatusNotAndDelIsFalse(Long tenantId, WithdrawStatusEnum status);

    @Query("select f from QuickPassWithdrawEntity f WHERE type = ?1 And f.createDate >= ?2 and f.createDate < ?3 and status = ?4")
    List<QuickPassWithdrawEntity> findByTypeAndCreateDateGreaterThanEqualAndCreateDateLessAndStatus(
            WithdrawTypeEnum type, Date startDate, Date endDate, WithdrawStatusEnum status);

    @Query("select f from QuickPassWithdrawEntity f WHERE type = ?1 And f.createDate >= ?2 and f.createDate < ?3")
    public List<QuickPassWithdrawEntity> findByTypeAndCreateDateGreaterThanEqualAndCreateDateLess(
            WithdrawTypeEnum type, Date startDate, Date endDate);

}
