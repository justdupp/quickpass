package hecc.pay.jpa;

import hecc.pay.entity.QuickPassWithdrawEntity;
import hecc.pay.enumer.WithdrawStatusEnum;
import hecc.pay.enumer.WithdrawTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author xuhoujun
 * @description: 提现JPA
 * @date: Created In 下午3:57 on 2018/3/18.
 */
public interface QuickPassWithdrawRepository extends JpaRepository<QuickPassWithdrawEntity, Long> {

    /**
     * 根据租户id、提现类型、分页信息获取提现分页列表
     *
     * @param tenantId 租户id
     * @param type     提现类型
     * @param page     分页
     * @return 提现列表
     */
    Page<QuickPassWithdrawEntity> findByTenantTenantIdAndTypeAndDelIsFalse(Long tenantId, WithdrawTypeEnum type, Pageable page);

    /**
     * 根据租户id、提现类型获取提现列表
     *
     * @param tenantId 租户id
     * @param type     提现类型
     * @return 提现列表
     */
    List<QuickPassWithdrawEntity> findByTenantTenantIdAndTypeAndDelIsFalse(Long tenantId, WithdrawTypeEnum type);

    /**
     * 根据租户id、提现类型统计提现条数
     *
     * @param tenantId 租户id
     * @param status   提现类型
     * @return 提现条数
     */
    Long countByTenantTenantIdAndStatusNotAndDelIsFalse(Long tenantId, WithdrawStatusEnum status);

    /**
     * 根据提现类型、开始时间、结束时间、提现状态获取提现列表
     *
     * @param type      提现类型
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param status    提现状态
     * @return 提现列表
     */
    @Query("select f from QuickPassWithdrawEntity f WHERE type = ?1 And f.createDate >= ?2 and f.createDate < ?3 and status = ?4")
    List<QuickPassWithdrawEntity> findByTypeAndCreateDateGreaterThanEqualAndCreateDateLessAndStatus(
            WithdrawTypeEnum type, Date startDate, Date endDate, WithdrawStatusEnum status);

    /**
     * 根据提现类型、开始时间、结束时间获取提现列表
     *
     * @param type      提现类型
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 提现列表
     */
    @Query("select f from QuickPassWithdrawEntity f WHERE type = ?1 And f.createDate >= ?2 and f.createDate < ?3")
    List<QuickPassWithdrawEntity> findByTypeAndCreateDateGreaterThanEqualAndCreateDateLess(
            WithdrawTypeEnum type, Date startDate, Date endDate);

    /**
     * 根据id修改提现状态、提现状态描述
     *
     * @param status  提现状态
     * @param message 提现状态描述
     * @param id      提现id
     * @return 数量
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update QuickPassWithdrawEntity f set f.status = ?1,f.message = ?2 where f.id = ?3")
    int modifyByQuickPassWithdrawEntityId(WithdrawStatusEnum status, String message, Long id);

}
