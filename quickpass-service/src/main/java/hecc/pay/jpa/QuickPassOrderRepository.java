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
 * @author xuhoujun
 * @description: 订单jpa
 * @date: Created In 下午8:15 on 2018/3/5.
 */
public interface QuickPassOrderRepository extends JpaRepository<QuickPassOrderEntity, Long> {

    /**
     * 根据租户id、时间、分页获取订单信息
     *
     * @param tenantId  租户id
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param page      页数
     * @return 订单对象分页信息
     */
    Page<QuickPassOrderEntity> findByTenantTenantIdAndCreateDateGreaterThanEqualAndCreateDateLessThanEqualAndDelIsFalse(
            Long tenantId, Date startDate, Date endDate, Pageable page);

    /**
     * 根据租户id、开始时间、结束时间获取订单统计信息
     *
     * @param tenantId  租户id
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 订单统计信息
     */
    @Query("select new hecc.pay.vos.OrderStatisticsVO(sum(o.fee),count(o.id)) from QuickPassOrderEntity o where o.tenant.tenantId = ?1 and o.del = false and o.createDate >= ?2 and o.createDate <= ?3")
    OrderStatisticsVO calculateByTenantTenantIdAndCreateDateGreaterThanEqualAndCreateDateLessThanEqualAndDelIsFalse(
            Long tenantId, Date startDate, Date endDate);

    /**
     * 根据租户id、开始时间、结束时间，订单状态、页数获取订单列表
     *
     * @param tenantId  租户id
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param status    订单状态
     * @param page      页数
     * @return 订单列表
     */
    Page<QuickPassOrderEntity> findByTenantTenantIdAndCreateDateGreaterThanEqualAndCreateDateLessThanEqualAndStatusAndDelIsFalse(
            Long tenantId, Date startDate, Date endDate, OrderStatusEnum status, Pageable page);

    /**
     * 根据租户id、开始时间、结束时间、订单状态获取订单统计信息
     *
     * @param tenantId  租户id
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param status    订单状态
     * @return 订单统计信息
     */
    @Query("select new hecc.pay.vos.OrderStatisticsVO(sum(o.fee),count(o.id)) from QuickPassOrderEntity o where o.tenant.tenantId = ?1 and o.del = false and o.createDate >= ?2 and o.createDate <= ?3 and o.status = ?4")
    OrderStatisticsVO calculateByTenantTenantIdAndCreateDateGreaterThanEqualAndCreateDateLessThanEqualAndStatusAndDelIsFalse(
            Long tenantId, Date startDate, Date endDate, OrderStatusEnum status);

    /**
     * 根据租户id、订单状态获取订单数量
     *
     * @param tenantId 租户id
     * @param status   订单状态
     * @return 订单数量
     */
    Long countByTenantTenantIdAndStatusAndDelIsFalse(Long tenantId, OrderStatusEnum status);

    /**
     * 根据开始时间、结束时间、订单状态获取订单列表
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param status    订单状态
     * @return 订单列表
     */
    @Query("select f from QuickPassOrderEntity f where f.createDate >= ?1 and f.createDate <= ?2 and  f.status = ?3")
    List<QuickPassOrderEntity> findByCreateDateGreaterThanEqualAndCreateDateLessThanEqualAndStatusAndDelIsFalse(
            Date startDate, Date endDate, OrderStatusEnum status);

    /**
     * 根据开始时间，结束时间、订单状态获取订单列表
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param status1   订单状态
     * @param status2   订单状态
     * @return 订单列表
     */
    @Query("select f from QuickPassOrderEntity f where f.createDate >= ?1 and f.createDate < ?2 and  (f.status = ?3 or f.status = ?4)")
    List<QuickPassOrderEntity> findByCreateDateGreaterThanEqualAndCreateDateLessAndStatusAndStatusAndDelIsFalse(
            Date startDate, Date endDate, OrderStatusEnum status1, OrderStatusEnum status2);
}

