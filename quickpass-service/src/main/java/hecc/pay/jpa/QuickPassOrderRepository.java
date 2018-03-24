package hecc.pay.jpa;

import hecc.pay.entity.QuickPassOrderEntity;
import hecc.pay.enumer.OrderStatusEnum;
import hecc.pay.vos.OrderStatisticsVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @Auther xuhoujun
 * @Description: 订单jpa
 * @Date: Created In 下午8:15 on 2018/3/5.
 */
public interface QuickPassOrderRepository extends JpaRepository<QuickPassOrderEntity, Long> {

    Page<QuickPassOrderEntity> findByTenantTenantIdAndCreateDateGreaterThanEqualAndCreateDateLessThanEqualAndDelIsFalse(
            Long tenantId, Date startDate, Date endDate, Pageable page);

    @Query("select new hecc.pay.vos.OrderStatisticsVO(sum(o.fee),count(o.id)) from QuickPassOrderEntity o where o.tenant.tenantId = ?1 and o.del = false and o.createDate >= ?2 and o.createDate <= ?3")
    OrderStatisticsVO calculateByTenantTenantIdAndCreateDateGreaterThanEqualAndCreateDateLessThanEqualAndDelIsFalse(
            Long tenantId, Date startDate, Date endDate);

    Page<QuickPassOrderEntity> findByTenantTenantIdAndCreateDateGreaterThanEqualAndCreateDateLessThanEqualAndStatusAndDelIsFalse(
            Long tenantId, Date startDate, Date endDate, OrderStatusEnum status, Pageable page);

    @Query("select new hecc.pay.vos.OrderStatisticsVO(sum(o.fee),count(o.id)) from QuickPassOrderEntity o where o.tenant.tenantId = ?1 and o.del = false and o.createDate >= ?2 and o.createDate <= ?3 and o.status = ?4")
    OrderStatisticsVO calculateByTenantTenantIdAndCreateDateGreaterThanEqualAndCreateDateLessThanEqualAndStatusAndDelIsFalse(
            Long tenantId, Date startDate, Date endDate, OrderStatusEnum status);

    Long countByTenantTenantIdAndStatusAndDelIsFalse(Long tenantId, OrderStatusEnum status);

    @Query("select f from QuickPassOrderEntity f where f.createDate >= ?1 and f.createDate <= ?2 and  f.status = ?3")
    List<QuickPassOrderEntity> findByCreateDateGreaterThanEqualAndCreateDateLessThanEqualAndStatusAndDelIsFalse(
            Date startDate, Date endDate, OrderStatusEnum status);

    @Query("select f from QuickPassOrderEntity f where f.createDate >= ?1 and f.createDate < ?2 and  (f.status = ?3 or f.status = ?4)")
    List<QuickPassOrderEntity> findByCreateDateGreaterThanEqualAndCreateDateLessAndStatusAndStatusAndDelIsFalse(
            Date startDate, Date endDate, OrderStatusEnum status1, OrderStatusEnum status2);
}

